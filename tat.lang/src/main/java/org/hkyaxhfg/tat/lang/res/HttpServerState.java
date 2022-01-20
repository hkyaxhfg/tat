package org.hkyaxhfg.tat.lang.res;

/**
 * Http 服务器状态.
 *
 * @author: wjf
 * @date: 2022/1/20
 */
public enum HttpServerState {

    Ok(200, "OK", "操作成功"),
    Bad_Request(400, "Bad Request", "请求语法错误"),
    Unauthorized(401, "Unauthorized", "用户未进行身份认证"),
    Forbidden(403, "Forbidden", "服务器拒绝执行此请求"),
    Not_Found(404, "Not Found", "服务器找不到资源"),
    Method_Not_Allowed(405, "Method Not Allowed", "请求中的方法被禁止"),
    Request_Timeout(408, "Request Timeout", "请求时间过长"),
    Gone(410, "Gone", "请求的资源已经不存在"),
    Unsupported_Media_Type(415, "Unsupported Media Type", "无法处理请求附带的媒体格式(Content-Type)"),
    Internal_Server_Error(500, "Internal Server Error", "服务异常"),
    Service_Unavailable(503, "Service Unavailable", "服务不可用"),
    ;

    private final int state;

    private final String enMessage;

    private final String cnMessage;

    HttpServerState(int state, String enMessage, String cnMessage) {
        this.state = state;
        this.enMessage = enMessage;
        this.cnMessage = cnMessage;
    }

    public int getState() {
        return state;
    }

    public String getEnMessage() {
        return enMessage;
    }

    public String getCnMessage() {
        return cnMessage;
    }
}
