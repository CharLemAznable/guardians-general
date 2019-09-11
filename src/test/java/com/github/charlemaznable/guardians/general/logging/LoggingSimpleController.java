package com.github.charlemaznable.guardians.general.logging;

import com.github.charlemaznable.guardians.PostGuardian;
import com.github.charlemaznable.guardians.PreGuardian;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.core.codec.Json.json;
import static com.github.charlemaznable.core.net.Http.fetchParameterMap;
import static com.github.charlemaznable.core.net.Http.responseJson;

@Controller
@RequestMapping("/logging")
public class LoggingSimpleController {

    @PreGuardian(LoggingSimpleGuardian.class)
    @PostGuardian(LoggingSimpleGuardian.class)
    @RequestMapping("/simple")
    public void simple(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @PreGuardian(LoggingSimpleExceptionGuardian.class)
    @PostGuardian(LoggingSimpleExceptionGuardian.class)
    @RequestMapping("/exception")
    public void exception(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @PreGuardian(LoggingSimpleAsyncGuardian.class)
    @PostGuardian(LoggingSimpleAsyncGuardian.class)
    @RequestMapping("/async/simple")
    public void asyncSimple(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @PreGuardian(LoggingSimpleAsyncExceptionGuardian.class)
    @PostGuardian(LoggingSimpleAsyncExceptionGuardian.class)
    @RequestMapping("/async/exception")
    public void asyncException(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }
}
