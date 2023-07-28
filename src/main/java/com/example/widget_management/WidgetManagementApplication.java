package com.example.widget_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class WidgetManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(WidgetManagementApplication.class, args);
	}
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.example.widget_management"))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(apiInfo());
	}
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Widget Management API")
				.description("APIs for managing widgets")
				.version("1.0.0")
				.build();
	}

}
