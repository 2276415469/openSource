package com.ning.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.trace.ActiveSpan;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ApmHttpInfo extends HttpFilter {
    //被忽略的头部信息
    private static final Set<String> IGNORED_HEADERS;
    static {
        Set<String> ignoredHeaders = new HashSet<>();
        ignoredHeaders.addAll(
                java.util.Arrays.asList(
                        "Content-Type",
                        "User-Agent",
                        "Accept",
                        "Cache-Control",
                        "Postman-Token",
                        "Host",
                        "Accept-Encoding",
                        "Connection",
                        "Content-Length"
                ).stream()
                        .map(String::toUpperCase)
                        .collect(Collectors.toList())
        );
        IGNORED_HEADERS = ignoredHeaders;
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            try {
                //构造请求信息: 比如 curl -X GET http://localhost:18080/getPerson?id=1 -H 'token: me-token' -d '{ "name": "hello" }'
                //构造请求的方法&URL&参数
                StringBuilder sb = new StringBuilder("curl")
                        .append(" -X ").append(request.getMethod())
                        .append(" ").append(request.getRequestURL().toString());
                if (StringUtils.hasLength(request.getQueryString())) {
                    sb.append("?").append(request.getQueryString());
                }

                //构造header
                Enumeration<String> headerNames = request.getHeaderNames();
                while (headerNames.hasMoreElements()) {
                    String headerName = headerNames.nextElement();
                    if (!IGNORED_HEADERS.contains(headerName.toUpperCase())) {
                        sb.append(" -H '").append(headerName).append(": ").append(request.getHeader(headerName)).append("'");
                    }
                }

                //获取body
                String body = new String(requestWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
                if (StringUtils.hasLength(body)) {
                    sb.append(" -d '").append(body).append("'");
                }
                //输出到input
                ActiveSpan.tag("input", sb.toString());

                //获取返回值body
                String responseBody = new String(responseWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
                //输出到output
                ActiveSpan.tag("output", responseBody);
            } catch (Exception e) {
                log.warn("fail to build http log", e);
            } finally {
                //这一行必须添加，否则就一直不返回
                responseWrapper.copyBodyToResponse();
            }
        }
    }
}

