package com.github.charlemaznable.guardians.general.visitorcounting;

import com.github.charlemaznable.guardians.PostGuardian;
import com.github.charlemaznable.guardians.PreGuardian;
import com.github.charlemaznable.guardians.general.Counter;
import com.github.charlemaznable.guardians.general.CountingPageView;
import com.github.charlemaznable.guardians.general.CountingUniqueVisitor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.core.codec.Json.jsonOf;
import static com.github.charlemaznable.core.net.Http.responseJson;

@Controller
@RequestMapping("/default-counter")
@PreGuardian(DefaultRedisCounterGuardian.class)
@PostGuardian(DefaultRedisCounterGuardian.class)
public class DefaultRedisCounterController {

    @RequestMapping("/missing")
    public void missing(HttpServletResponse response) {
        responseJson(response, jsonOf("result", "SUCCESS"));
    }

    @CountingPageView(AbstractPageViewCounter.class)
    @RequestMapping("/abstract-page-view")
    public void abstractPageView(HttpServletResponse response) {
        responseJson(response, jsonOf("result", "SUCCESS"));
    }

    @CountingPageView(ExceptionPageViewCounter.class)
    @RequestMapping("/exception-page-view")
    public void exceptionPageView(HttpServletResponse response) {
        responseJson(response, jsonOf("result", "SUCCESS"));
    }

    @CountingUniqueVisitor(AbstractUniqueVisitorCounter.class)
    @RequestMapping("/abstract-unique-visitor")
    public void abstractUniqueVisitor(HttpServletResponse response) {
        responseJson(response, jsonOf("result", "SUCCESS"));
    }

    @CountingUniqueVisitor(ExceptionUniqueVisitorCounter.class)
    @RequestMapping("/exception-unique-visitor")
    public void exceptionUniqueVisitor(HttpServletResponse response) {
        responseJson(response, jsonOf("result", "SUCCESS"));
    }

    @CountingPageView
    @CountingUniqueVisitor
    @RequestMapping("/index")
    public void index(HttpServletResponse response) {
        responseJson(response, jsonOf("result", "SUCCESS"));
    }

    public static abstract class AbstractPageViewCounter implements Counter {}

    public static class ExceptionPageViewCounter implements Counter {

        @Override
        public void count(HttpServletRequest request) {
            throw new RuntimeException("PageViewCountingException");
        }
    }

    public static abstract class AbstractUniqueVisitorCounter implements Counter {}

    public static class ExceptionUniqueVisitorCounter implements Counter {

        @Override
        public void count(HttpServletRequest request) {
            throw new RuntimeException("UniqueVisitorCountingException");
        }
    }
}
