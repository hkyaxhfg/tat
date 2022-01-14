package org.hkyaxhfg.tat.monitoring;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.hkyaxhfg.tat.lang.util.LoggerGenerator;
import org.slf4j.Logger;

/**
 * {@link Monitoring}的解析器.
 *
 * @see Monitoring
 *
 * @author: wjf
 * @date: 2022/1/6
 */
@Aspect
public class MonitoringAnnotatedResolver {

    private final Monitoring.MonitoringLogic monitoringLogic;

    public MonitoringAnnotatedResolver(Monitoring.MonitoringLogic monitoringLogic) {
        this.monitoringLogic = monitoringLogic == null ? new Monitoring.DefaultMonitoringLogic() : monitoringLogic;
    }

    @Pointcut("@annotation(org.hkyaxhfg.tat.monitoring.Monitoring)")
    public void pointcut() {}

    @Around(value = "pointcut() && @annotation(monitoring)")
    public Object monitoring(ProceedingJoinPoint point, Monitoring monitoring) throws Throwable {
        Logger logger = LoggerGenerator.logger(monitoring.logger());
        return this.monitoringLogic.logic(logger, point, monitoring);
    }

}
