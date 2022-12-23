package com.github.charlemaznable.guardians.general.riven;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.github.charlemaznable.core.net.Http.dealRequestBodyStream;
import static com.github.charlemaznable.core.net.Http.responseText;

@Controller
@RequestMapping("/guard")
@RivenGuardian
public class RivenTestController {

    @RequestMapping("/test")
    public void test(HttpServletRequest request, HttpServletResponse response) {
        responseText(response, dealRequestBodyStream(request, "UTF-8"));
    }
}
