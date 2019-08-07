package com.github.charlemaznable.guardians.general.accessLimit;

import com.github.charlemaznable.guardians.PostGuardian;
import com.github.charlemaznable.guardians.PreGuardian;
import com.github.charlemaznable.guardians.general.AccessLimit;
import com.github.charlemaznable.guardians.general.AccessLimit.AccessLimiter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.codec.Json.json;
import static com.github.charlemaznable.net.Http.fetchParameterMap;
import static com.github.charlemaznable.net.Http.responseJson;

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
