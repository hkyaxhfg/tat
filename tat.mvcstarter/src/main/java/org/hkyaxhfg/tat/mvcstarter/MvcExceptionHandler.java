package org.hkyaxhfg.tat.mvcstarter;

import org.hkyaxhfg.tat.lang.res.Failure;
import org.hkyaxhfg.tat.lang.res.HttpServerState;
import org.hkyaxhfg.tat.lang.res.Result;
import org.hkyaxhfg.tat.lang.res.ResultUtils;
import org.hkyaxhfg.tat.lang.util.LoggerGenerator;
import org.slf4j.Logger;
import org.springframework.beans.TypeMismatchException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 各类异常的异常处理器.
 *
 * @author: wjf
 * @date: 2022/1/21
 */
@ControllerAdvice
public class MvcExceptionHandler {

    private static final Logger logger = LoggerGenerator.logger(MvcExceptionHandler.class);

    @ExceptionHandler(BizException.class)
    @ResponseBody
    public Result<BizException, Failure<BizException>> process(BizException e) {
        logger.error(HttpServerState.Internal_Server_Error.getCnMessage(), e);

        String message = HttpServerState.Internal_Server_Error.getCnMessage();
        int state = HttpServerState.Internal_Server_Error.getState();
        if (e != null) {
            message = e.getMessage();
            state = e.getState();
        }
        return ResultUtils.failure(state, message, e, "", "");
    }

    @ExceptionHandler({
            /**
             * 非法参数异常
             */
            IllegalArgumentException.class,
            /**
             * 参数类型不匹配异常
             * getPropertyName()获取数据类型不匹配参数名称
             * getRequiredType()实际要求客户端传递的数据类型
             */
            TypeMismatchException.class,
            /**
             * 缺少参数异常
             * getParameterName() 缺少的参数名称
             */
            MissingServletRequestParameterException.class,
            /**
             * Http 请求方法不支持异常
             */
            HttpRequestMethodNotSupportedException.class
    })
    @ResponseBody
    public Result<Exception, Failure<Exception>> process(Exception e) {
        logger.error(HttpServerState.Internal_Server_Error.getCnMessage(), e);

        String message = HttpServerState.Internal_Server_Error.getCnMessage();
        int state = HttpServerState.Internal_Server_Error.getState();
        if (e != null) {
            message = e.getMessage();

            if (e instanceof IllegalArgumentException) {
                state = HttpServerState.Bad_Request.getState();
            } else if (e instanceof TypeMismatchException) {
                TypeMismatchException te = (TypeMismatchException) e;
                message = String.format("参数类型不匹配, 参数 '%s' 类型应该为: %s", te.getPropertyName(), te.getRequiredType());
                logger.error(message);
                state = HttpServerState.Bad_Request.getState();
            } else if (e instanceof MissingServletRequestParameterException) {
                MissingServletRequestParameterException msrpe = (MissingServletRequestParameterException) e;
                message = String.format("缺少必要参数, 参数名称为: %s", msrpe.getParameterName());
                logger.error(message);
                state = HttpServerState.Bad_Request.getState();
            } else if (e instanceof HttpRequestMethodNotSupportedException) {
                HttpRequestMethodNotSupportedException hrmnse = (HttpRequestMethodNotSupportedException) e;
                message = HttpServerState.Method_Not_Allowed.getCnMessage() + ": " + hrmnse.getMethod();
                logger.error(message);
                logger.error("StackTrace[0]:{}", hrmnse.getStackTrace()[0]);
                state = HttpServerState.Bad_Request.getState();
            }
        }
        return ResultUtils.failure(state, message, e, null, null);
    }

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public Result<Exception, Failure<Exception>> processEx(Exception e) {
        logger.error(HttpServerState.Internal_Server_Error.getCnMessage(), e);
        return ResultUtils.failure(HttpServerState.Internal_Server_Error.getState(), HttpServerState.Internal_Server_Error.getCnMessage(), e, null, null);
    }

}
