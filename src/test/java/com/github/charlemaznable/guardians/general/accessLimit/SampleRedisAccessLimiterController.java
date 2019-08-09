package com.github.charlemaznable.guardians.general.accessLimit;

import com.github.charlemaznable.guardians.PostGuardian;
import com.github.charlemaznable.guardians.PreGuardian;
import com.github.charlemaznable.guardians.general.AccessLimit;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.codec.Json.jsonOf;
import static com.github.charlemaznable.net.Http.responseJson;

@Controller
@RequestMapping("/sampleRedis")
@PreGuardian(SampleRedisAccessLimiterGuardian.class)
@PostGuardian(SampleRedisAccessLimiterGuardian.class)
@AccessLimit(SampleRedisAccessLimiter.class)
public class SampleRedisAccessLimiterController {

    @RequestMapping("/unlimit")
    public void unlimit(HttpServletResponse response) {
        responseJson(response, jsonOf("result", "SUCCESS"));
    }

    @RequestMapping("/index")
    public void index(HttpServletResponse response) {
        responseJson(response, jsonOf("result", "SUCCESS"));
    }

    @RequestMapping("/tenSeconds")
    public void tenSeconds(HttpServletResponse response) {
        responseJson(response, jsonOf("result", "SUCCESS"));
    }
}
