package com.github.charlemaznable.guardians.general.uniquenonsense;

import com.github.charlemaznable.guardians.PostGuardian;
import com.github.charlemaznable.guardians.PreGuardian;
import com.github.charlemaznable.guardians.general.UniqueNonsense;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.core.codec.Json.jsonOf;
import static com.github.charlemaznable.core.net.Http.responseJson;

@Controller
@RequestMapping("/unique-nonsense")
@PreGuardian(SampleRedisUniqueCheckerGuardian.class)
@PostGuardian(SampleRedisUniqueCheckerGuardian.class)
public class SampleRedisUniqueCheckerController {

    @UniqueNonsense
    @RequestMapping("/unlimit")
    public void unlimit(HttpServletResponse response) {
        responseJson(response, jsonOf("result", "SUCCESS"));
    }

    @UniqueNonsense(checker = SampleRedisUniqueChecker.class)
    @RequestMapping("/index")
    public void index(HttpServletResponse response) {
        responseJson(response, jsonOf("result", "SUCCESS"));
    }
}
