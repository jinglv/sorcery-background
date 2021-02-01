package com.sorcery.api.common.config;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author jingLv
 * @date 2021/01/13
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket docket() {
        ParameterBuilder builder = new ParameterBuilder();
        // 在Swagger里显示header
        builder.parameterType("header").name("token")
                .description("token值")
                .required(true)
                .modelRef(new ModelRef("string"));
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Api Demo")
                .apiInfo(apiInfo())
                .globalOperationParameters(Lists.newArrayList(builder.build()))
                .select().paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Sorcery系统")
                .description("Sorcery系统系统接口文档")
                .contact(new Contact("jinglv", "https://github.com/jinglv/sorcery-background", "lvjing0705@126.com"))
                .version("1.0")
                .build();
    }
}
