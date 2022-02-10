package org.hkyaxhfg.tat.boot.download;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.hkyaxhfg.tat.lang.res.HttpServerState;
import org.hkyaxhfg.tat.lang.util.LoggerGenerator;
import org.hkyaxhfg.tat.lang.util.TatException;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Web下载器, 此下载器是线程不安全的.
 *
 * @author: wjf
 * @date: 2022/1/26
 */
@Component
public class WebDownloader implements Downloader<HttpServletRequest, HttpServletResponse> {

    private static final Logger logger = LoggerGenerator.logger(WebDownloader.class);

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private static final String DEFAULT_CONTENT_DISPOSITION = "attachment";

    @Override
    public void download(Supplier<HttpServletRequest> httpServletRequestSupplier, Supplier<HttpServletResponse> httpServletResponseSupplier, String filename, Downloadable downloadable) {
        HttpServletRequest request = Objects.requireNonNull(httpServletRequestSupplier.get(), "HttpServletRequest 不能为空");
        HttpServletResponse response = Objects.requireNonNull(httpServletResponseSupplier.get(), "HttpServletResponse 不能为空");
        Objects.requireNonNull(downloadable, "Downloadable 不能为空");
        if (StringUtils.isBlank(filename)) {
            TatException.throwEx("filename 不能为空");
        }

        OutputStream os = null;

        try {
            String method = request.getMethod();
            if (!StringUtils.equalsIgnoreCase("POST", method)) {
                TatException.throwEx("必须是POST, {}", HttpServerState.Method_Not_Allowed.getCnMessage());
            }

            filename = URLEncoder.encode(filename, DEFAULT_CHARSET.name());
            response.reset();
            response.setHeader("Content-Disposition", String.format("%s;filename=%s", DEFAULT_CONTENT_DISPOSITION, filename));
            response.setHeader("Access-Control-Expose-Headers", "filename");
            response.setContentType(String.format("application/octet-stream; charset=%s", DEFAULT_CHARSET.name()));
            os = response.getOutputStream();
            downloadable.downloadable(os);
            os.flush();
        } catch (IOException e) {
            logger.error("WebDownloader: 文件下载失败, {}", e.getMessage());

            try {
                response.setContentType(String.format("text/html; charset=%s", DEFAULT_CHARSET.name()));
                if (os != null) {
                    os.write(e.getMessage().getBytes(DEFAULT_CHARSET));
                    os.flush();
                }
            } catch (IOException ioe) {
                logger.error("WebDownloader: 文件下载失败, {}", ioe.getMessage());
            }

        } finally {
            IOUtils.closeQuietly(os);
        }

    }

}
