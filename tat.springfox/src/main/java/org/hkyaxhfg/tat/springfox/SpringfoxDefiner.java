package org.hkyaxhfg.tat.springfox;

import springfox.documentation.spring.web.plugins.Docket;

/**
 * springfox定义器.
 *
 * @author: wjf
 * @date: 2022/1/12
 */
@FunctionalInterface
public interface SpringfoxDefiner {

    public static final String NET_SCHEMA = "http";

    /**
     * 定义接口文档.
     * @return 接口文档的描述对象.
     */
    Docket definition();

}
