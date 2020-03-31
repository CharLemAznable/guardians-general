package com.github.charlemaznable.guardians.general.utils;

import lombok.val;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static com.github.charlemaznable.core.lang.Condition.blankThen;
import static com.github.charlemaznable.core.lang.Condition.nullThen;
import static com.github.charlemaznable.core.lang.Mapp.newHashMap;
import static com.github.charlemaznable.core.net.Http.dealRequestBodyStream;
import static com.github.charlemaznable.core.net.Http.fetchCookieMap;
import static com.github.charlemaznable.core.net.Http.fetchHeaderMap;
import static com.github.charlemaznable.core.net.Http.fetchParameterMap;
import static com.github.charlemaznable.core.net.Http.fetchPathVariableMap;
import static com.github.charlemaznable.guardians.general.utils.RequestBodyFormat.FORM;
import static java.nio.charset.StandardCharsets.UTF_8;

public enum RequestValueExtractor {

    PARAMETER {
        @Override
        public Map<String, Object> extract(HttpServletRequest request,
                                           RequestBodyFormat format, String charsetName) {
            return newHashMap(fetchParameterMap(request));
        }
    },
    PATH {
        @Override
        public Map<String, Object> extract(HttpServletRequest request,
                                           RequestBodyFormat format, String charsetName) {
            return newHashMap(fetchPathVariableMap(request));
        }
    },
    HEADER {
        @Override
        public Map<String, Object> extract(HttpServletRequest request,
                                           RequestBodyFormat format, String charsetName) {
            return newHashMap(fetchHeaderMap(request));
        }
    },
    COOKIE {
        @Override
        public Map<String, Object> extract(HttpServletRequest request,
                                           RequestBodyFormat format, String charsetName) {
            return newHashMap(fetchCookieMap(request));
        }
    },
    BODY {
        @Override
        public Map<String, Object> extract(HttpServletRequest request,
                                           RequestBodyFormat format, String charsetName) {
            format = nullThen(format, () -> FORM);
            charsetName = blankThen(charsetName, UTF_8::name);
            val requestBody = dealRequestBodyStream(request, charsetName);
            return format.parse(requestBody, charsetName);
        }
    };

    public abstract Map<String, Object> extract(HttpServletRequest request,
                                                RequestBodyFormat format,
                                                String charsetName);
}
