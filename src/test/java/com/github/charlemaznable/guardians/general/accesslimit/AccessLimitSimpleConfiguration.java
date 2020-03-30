package com.github.charlemaznable.guardians.general.accesslimit;

import com.github.charlemaznable.guardians.spring.GuardiansImport;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Configuration
@ComponentScan(resourcePattern = "**/AccessLimitSimple*.class")
@GuardiansImport
public class AccessLimitSimpleConfiguration {
}
