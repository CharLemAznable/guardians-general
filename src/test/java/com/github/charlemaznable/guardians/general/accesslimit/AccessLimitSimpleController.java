package com.github.charlemaznable.guardians.general.accesslimit;

import com.github.charlemaznable.guardians.PostGuardian;
import com.github.charlemaznable.guardians.PreGuardian;
import com.github.charlemaznable.guardians.general.AccessLimit;
import com.github.charlemaznable.guardians.general.AccessLimit.AccessLimiter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.github.charlemaznable.core.codec.Json.json;
import static com.github.charlemaznable.core.net.Http.fetchParameterMap;
import static com.github.charlemaznable.core.net.Http.responseJson;

@Controller
@RequestMapping("/accessLimit")
@PreGuardian(AccessLimitSimpleGuardian.class)
@PostGuardian(AccessLimitSimpleGuardian.class)
public class AccessLimitSimpleController {

    @RequestMapping("/simple")
    public void simple(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @AccessLimit
    @RequestMapping("/default")
    public void def(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @AccessLimit(AccessLimiterSimpleRefuse.class)
    @RequestMapping("/refuse")
    public void refuse(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @AccessLimit(AccessLimiterSimpleException.class)
    @RequestMapping("/exception")
    public void exception(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @Component
    public static class AccessLimiterSimpleRefuse implements AccessLimiter {

        @Override
        public boolean tryAcquire(HttpServletRequest request) {
            return false;
        }
    }

    @Component
    public static class AccessLimiterSimpleException implements AccessLimiter {

        @Override
        public boolean tryAcquire(HttpServletRequest request) {
            throw new RuntimeException("RuntimeException");
        }
    }
}
