package org.hkyaxhfg.tat.boot.download;

import java.io.OutputStream;

/**
 * 可下载的, 此处为将数据流放入输出流的逻辑.
 *
 * @author: wjf
 * @date: 2022/1/26
 */
@FunctionalInterface
public interface Downloadable {

    /**
     * 将数据流放入输出流的逻辑.
     * @param os 输出流.
     */
    void downloadable(OutputStream os);

}
