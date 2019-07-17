package com.example.demo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource("classpath:/export/echarts.properties")
@Component
public class EChartsConfig {
    @Value("${echarts.path}")
    @Getter
    @Setter
    private String JSPath;

    @Value("${phantomjs.path}")
    @Getter
    @Setter
    private String ExecPath;

    @Value("${output.path}")
    @Getter
    @Setter
    private String OutputPath;
}
