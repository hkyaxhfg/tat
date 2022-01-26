package org.hkyaxhfg.tat.auth.user;

import org.hkyaxhfg.tat.lang.res.Failure;
import org.hkyaxhfg.tat.lang.res.HttpServerState;
import org.hkyaxhfg.tat.lang.res.Result;
import org.hkyaxhfg.tat.lang.res.ResultUtils;
import org.hkyaxhfg.tat.lang.util.LoggerGenerator;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户鉴权专用异常的异常处理器.
 *
 * @author: wjf
 * @date: 2022/1/24
 */
@ControllerAdvice
public class UserAuthExceptionHandler {

    private static final Logger logger = LoggerGenerator.logger(UserAuthExceptionHandler.class);

    @ExceptionHandler(UserAuthException.class)
    @ResponseBody
    public Result<UserAuthException, Failure<UserAuthException>> processEx(UserAuthException e) {
        logger.error(HttpServerState.Unauthorized.getCnMessage(), e);
        return ResultUtils.failure(HttpServerState.Unauthorized.getState(), HttpServerState.Unauthorized.getCnMessage(), e, e.toString(), HttpServerState.Unauthorized.getCnMessage());
    }

}
