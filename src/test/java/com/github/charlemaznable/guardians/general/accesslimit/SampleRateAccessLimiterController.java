package com.github.charlemaznable.guardians.general.accesslimit;

import com.github.charlemaznable.guardians.PostGuardian;
import com.github.charlemaznable.guardians.PreGuardian;
import com.github.charlemaznable.guardians.general.AccessLimit;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.github.charlemaznable.core.codec.Json.jsonOf;
import static com.github.charlemaznable.core.net.Http.responseJson;

@Controller
@RequestMapping("/sampleRate")
@PreGuardian(SampleRateAccessLimiterGuardian.class)
@PostGuardian(SampleRateAccessLimiterGuardian.class)
@AccessLimit(SampleRateAccessLimiter.class)
public class SampleRateAccessLimiterController {

    @RequestMapping("/index")
    public void index(HttpServletResponse response) {
        responseJson(response, jsonOf("result", "SUCCESS"));
    }
}
