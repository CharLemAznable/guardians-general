package com.github.charlemaznable.guardians.general.requestbodyraw;

import com.github.charlemaznable.guardians.PostGuardian;
import com.github.charlemaznable.guardians.PreGuardian;
import com.github.charlemaznable.guardians.general.RequestBodyRawValidate;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.github.charlemaznable.core.net.Http.dealRequestBodyStream;
import static com.github.charlemaznable.core.net.Http.responseText;
import static java.nio.charset.StandardCharsets.UTF_8;

@Controller
@RequestMapping("/requestBodyRaw")
@PreGuardian(RequestBodyRawGuardian.class)
@PostGuardian(RequestBodyRawGuardian.class)
public class RequestBodyRawController {

    @RequestMapping("/error")
    public void error(HttpServletRequest request, HttpServletResponse response) {
        responseText(response, dealRequestBodyStream(request, UTF_8.name()));
    }

    @RequestBodyRawValidate
    @RequestMapping("/index")
    public void index(HttpServletRequest request, HttpServletResponse response) {
        responseText(response, dealRequestBodyStream(request, UTF_8.name()));
    }
}
