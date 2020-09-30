package com.github.charlemaznable.guardians.general.visitorcounting;

import com.github.charlemaznable.guardians.PostGuardian;
import com.github.charlemaznable.guardians.PreGuardian;
import com.github.charlemaznable.guardians.general.CountingPageView;
import com.github.charlemaznable.guardians.general.CountingUniqueVisitor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.core.codec.Json.jsonOf;
import static com.github.charlemaznable.core.net.Http.responseJson;

@Controller
@RequestMapping("/custom-counter")
@PreGuardian(CustomUniqueVisitorCounterGuardian.class)
@PostGuardian(CustomUniqueVisitorCounterGuardian.class)
public class CustomUniqueVisitorCounterController {

    @CountingPageView
    @CountingUniqueVisitor(CustomUniqueVisitorCounter.class)
    @RequestMapping("/index")
    public void index(HttpServletResponse response) {
        responseJson(response, jsonOf("result", "SUCCESS"));
    }
}
