package com.github.charlemaznable.guardians.general.requestFields;

import com.github.charlemaznable.guardians.spring.GuardiansImport;
import com.github.charlemaznable.core.spring.FastJsonHttpMessageConverterBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import static com.google.common.base.Charsets.UTF_8;

@EnableWebMvc
@Configuration
@ComponentScan
@GuardiansImport
public class RequestFieldsConfiguration implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new FastJsonHttpMessageConverterBuilder().charset(UTF_8).build());
    }
}
