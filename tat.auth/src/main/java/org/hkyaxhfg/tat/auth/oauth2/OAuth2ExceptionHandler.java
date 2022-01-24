package org.hkyaxhfg.tat.auth.oauth2;

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
 * OAuth2鉴权专用异常的异常处理器.
 *
 * @author: wjf
 * @date: 2022/1/24
 */
@ControllerAdvice
public class OAuth2ExceptionHandler {

    private static final Logger logger = LoggerGenerator.logger(OAuth2ExceptionHandler.class);

    @ExceptionHandler(OAuth2Exception.class)
    @ResponseBody
    public Result<OAuth2Exception, Failure<OAuth2Exception>> processEx(OAuth2Exception e) {
        logger.error(HttpServerState.OAuth2_Unauthorized.getCnMessage(), e);
        return ResultUtils.failure(HttpServerState.OAuth2_Unauthorized.getState(), HttpServerState.OAuth2_Unauthorized.getCnMessage(), e, null, null);
    }

}
