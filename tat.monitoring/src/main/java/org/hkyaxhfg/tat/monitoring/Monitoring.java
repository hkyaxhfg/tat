package org.hkyaxhfg.tat.monitoring;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.hkyaxhfg.tat.lang.json.JSON;
import org.hkyaxhfg.tat.lang.json.JSONProcessor;
import org.hkyaxhfg.tat.lang.reflect.FieldReflector;
import org.hkyaxhfg.tat.lang.reflect.MethodReflector;
import org.hkyaxhfg.tat.lang.util.StopWatch;
import org.hkyaxhfg.tat.lang.util.Unaware;
import org.slf4j.Logger;

import java.lang.annotation.*;
import java.lang.reflect.Parameter;
import java.util.List;

/**
 * 监控, 添加此注解则会记录日志, 日志大概格式: method --> request --> response.
 *
 * @see MonitoringAnnotatedResolver
 *
 * @author: wjf
 * @date: 2022/1/6
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Monitoring {

    /**
     * 日志记录者的名称.
     * @return 日志记录者的名称.
     */
    String logger() default "monitoring";

    /**
     * 监控逻辑的实现.
     * @return MonitoringLogic
     */
    Class<? extends MonitoringLogic> monitoringLogic() default DefaultMonitoringLogic.class;

    /**
     * 此接口主要是{@link Monitoring}注解所需要的记录日志逻辑, 此接口的实现必须拥有无参构造方法.
     */
    @FunctionalInterface
    @SuppressWarnings("all")
    interface MonitoringLogic {

        /**
         * 日志记录的具体实现.
         * @param logger 日志记录者.
         * @param point 切点.
         * @param monitoring 注解.
         * @return 运行结果为 {@link ProceedingJoinPoint#proceed(Object[])}的返回值.
         * @throws Throwable Throwable
         */
        Object logic(Logger logger, ProceedingJoinPoint point, Monitoring monitoring) throws Throwable;

    }

    /**
     * 默认的日志记录实现.
     */
    class DefaultMonitoringLogic implements MonitoringLogic {

        @Override
        @SuppressWarnings("all")
        public Object logic(Logger logger, ProceedingJoinPoint point, Monitoring monitoring) throws Throwable {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            Object[] methodArgs = point.getArgs();

            // 解析request
            JSON json = new JSON();
            MethodReflector methodReflector = new MethodReflector(Unaware.<MethodSignature>castUnaware(point.getSignature()).getMethod());
            List<Parameter> parameters = methodReflector.getParameters();
            for (Parameter parameter : parameters) {
                FieldReflector fieldReflector = new FieldReflector("index", Parameter.class);
                json.addJsonObject(parameter.getName(), methodArgs[fieldReflector.<Integer>read(parameter)]);
            }
            String request = json.toString();

            String response = null;
            Throwable throwable = null;
            try {
                Object result = point.proceed(methodArgs);
                response = new JSONProcessor().toJsonString(result);
                return result;
            } catch (Throwable t) {
                throwable = t;
                throw t;
            } finally {
                stopWatch.stop();
                logger.info(
                        String.format("==================================================" +
                                        "\nmethod: {}," +
                                        "\ntime: {}ms," +
                                        "\nrequest: {}," +
                                        "\nresponse: {}," +
                                        "\n%s",
                                throwable == null ? "" : "error: {}"),
                        Unaware.<MethodSignature>castUnaware(point.getSignature()).getMethod().toGenericString(),
                        stopWatch.timeDifferenceMillisecond(),
                        request,
                        response,
                        throwable == null ? "" : throwable.toString()
                );
            }
        }

    }

}
