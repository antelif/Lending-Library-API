package com.antelif.library.configuration;

import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SpringFoxConfig implements WebMvcConfigurer {
  @Override
  public void addViewControllers(final ViewControllerRegistry registry) {
    registry.addRedirectViewController("/", "/swagger-ui/index.html");
    registry.addRedirectViewController("/swagger-ui", "/swagger-ui/index.html");
  }

  /**
   * Bean for Swagger documentation. The select() method returns an instance of ApiSelectorBuilder
   * which provides a way to control the endpoints exposed by Swagger.
   *
   * @return the primary interface into the swagger documentation.
   */
  @Bean
  public Docket api() {
    return new Docket(SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.antelif.library.application.controller"))
        .paths(PathSelectors.any())
        .build();
  }
}
