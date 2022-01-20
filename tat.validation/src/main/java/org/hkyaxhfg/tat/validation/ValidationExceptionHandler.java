package org.hkyaxhfg.tat.validation;

import org.hkyaxhfg.tat.lang.res.Failure;
import org.hkyaxhfg.tat.lang.res.HttpServerState;
import org.hkyaxhfg.tat.lang.res.Result;
import org.hkyaxhfg.tat.lang.util.LoggerGenerator;
import org.hkyaxhfg.tat.lang.util.Unaware;
import org.slf4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

/**
 * 校验相关异常的处理器.
 *
 * @author: wjf
 * @date: 2022/1/20
 */
@ControllerAdvice
public class ValidationExceptionHandler {

    private static final Logger logger = LoggerGenerator.logger(ValidationExceptionHandler.class);

    @ExceptionHandler({
            /**
             * spring在controller中绑定BindingResult参数时，出现的绑定异常.
             */
            BindException.class,
            /**
             * javax.validation的Java校验框架异常, 异常表示报告违反约束的结果.
             */
            ConstraintViolationException.class,
    })
    @ResponseBody
    public Result<?, ?> process(Exception e) {
        String customizeMessage = null;
        String originate = null;

        if (e instanceof BindException) {
            BindException be = Unaware.castUnaware(e);
            logger.error("Bean参数校验错误: {}", be.getMessage());
            BindingResult bindingResult = be.getBindingResult();
            List<FieldError> errors = bindingResult.getFieldErrors();

            StringBuilder builder = new StringBuilder();
            for (FieldError error : errors) {
                builder.append("'")
                        .append(error.getField())
                        .append("': 非法值 [")
                        .append(error.getRejectedValue())
                        .append("], ")
                        .append(error.getDefaultMessage())
                        .append(";");
            }
            customizeMessage = builder.toString();
            originate = errors.get(0).getDefaultMessage();

        } else if (e instanceof ConstraintViolationException) {
            ConstraintViolationException ce = (ConstraintViolationException) e;
            logger.error("方法参数校验错误: {}", ce.getMessage());
            Set<ConstraintViolation<?>> violations = ce.getConstraintViolations();

            StringBuilder builder = new StringBuilder();
            boolean hasValue = false;

            for (ConstraintViolation<?> violation : violations) {
                if (!hasValue) {
                    originate = violation.getMessage();
                    hasValue = true;
                }

                builder.append("'")
                        .append(violation.getPropertyPath())
                        .append("': 非法值 [")
                        .append(violation.getInvalidValue())
                        .append("], ")
                        .append(violation.getMessage())
                        .append(";");
            }
            customizeMessage = builder.toString();
        }

        return new Failure<>(HttpServerState.Bad_Request.getState(), HttpServerState.Bad_Request.getCnMessage(), e, customizeMessage, originate);
    }

}
