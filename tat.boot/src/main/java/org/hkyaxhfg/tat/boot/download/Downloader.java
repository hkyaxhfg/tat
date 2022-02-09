package org.hkyaxhfg.tat.boot.download;

import java.util.function.Supplier;

/**
 * 下载器接口, 要配合{@link Downloadable} 接口一起使用.
 * @param <Request> 抽象的请求, 必须是标识当前请求的主体描述对象.
 * @param <Response>> 抽象的响应, 必须是标识当前响应的主体描述对象.
 *
 * @author: wjf
 * @date: 2022/1/26
 */
@FunctionalInterface
public interface Downloader<Request, Response> {

    /**
     * 下载.
     * @param requestSupplier 当前请求提供者.
     * @param responseSupplier 当前响应提供者.
     * @param filename 文件名称.
     * @param downloadable 可下载的逻辑.
     */
    void download(Supplier<Request> requestSupplier, Supplier<Response> responseSupplier, String filename, Downloadable downloadable);

}
