package org.mitallast.finance;

import org.mitallast.finance.config.AsyncConfig;
import org.mitallast.finance.config.DataSourceConfig;
import org.mitallast.finance.config.ViewConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan
@EnableAutoConfiguration(exclude = {ThymeleafAutoConfiguration.class})
@Import({AsyncConfig.class, DataSourceConfig.class, ViewConfig.class})
public class Application {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
}
