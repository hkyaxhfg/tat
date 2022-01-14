package org.hkyaxhfg.tat.lang.util;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 大文本的处理类.
 *
 * @author: wjf
 * @date: 2022/1/8
 */
public class Longtext implements Iterator<String> {

    /**
     * 缓冲阅读器.
     */
    private final BufferedReader bufferedReader;

    /**
     * 当前正在读取的文本行.
     */
    private String cachedLine;

    /**
     * 是否读取完毕的标志.
     */
    private boolean finished = false;

    /**
     * 读取文本的构造器.
     *
     * @param reader 读者.
     */
    public Longtext(final Reader reader) {
        if (reader == null) {
            throw new IllegalArgumentException("Reader must not be null");
        }
        if (reader instanceof BufferedReader) {
            bufferedReader = Unaware.castUnaware(reader);
        } else {
            bufferedReader = new BufferedReader(reader);
        }
    }

    /**
     * 读取文本的构造器.
     *
     * @param is 输入流.
     */
    public Longtext(final InputStream is) {
        this(new InputStreamReader(is));
    }

    /**
     * 是否有下一行文本.
     *
     * @return 是否有下一行文本.
     */
    @Override
    public boolean hasNext() {
        if (cachedLine != null) {
            return true;
        } else if (finished) {
            return false;
        } else {
            try {
                while (true) {
                    final String line = bufferedReader.readLine();
                    if (line == null) {
                        finished = true;
                        return false;
                    } else if (isValidLine(line)) {
                        cachedLine = line;
                        return true;
                    }
                }
            } catch (final IOException ioe) {
                close();
                throw new IllegalStateException(ioe);
            }
        }
    }

    /**
     * 是否是有效行, 默认是true.
     *
     * @param line 文本行.
     * @return 是否是有效行.
     */
    protected boolean isValidLine(final String line) {
        return true;
    }

    /**
     * 读取文本行.
     *
     * @return 文本行.
     */
    @Override
    public String next() {
        return nextLine();
    }

    /**
     * 安静的关闭底层的读者reader.
     */
    public void close() {
        finished = true;
        IOUtils.closeQuietly(bufferedReader);
        cachedLine = null;
    }

    /**
     * 移除文本行, 不支持此操作.
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("不可以删除文本内容");
    }

    /**
     * 读取文本行.
     *
     * @return 文本行.
     */
    private String nextLine() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more lines");
        }
        final String currentLine = cachedLine;
        cachedLine = null;
        return currentLine;
    }

}
