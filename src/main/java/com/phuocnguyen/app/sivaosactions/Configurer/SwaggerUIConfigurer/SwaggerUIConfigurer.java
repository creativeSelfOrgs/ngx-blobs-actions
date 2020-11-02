package com.phuocnguyen.app.sivaosactions.Configurer.SwaggerUIConfigurer;

import com.sivaos.Service.SIVAOSServiceImplement.SIVAOSSIVAOSSwaggerUIConfigurerServiceImplement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @http://host:port/swagger-ui.html
 */
@Configuration
@EnableSwagger2
public class SwaggerUIConfigurer {

    SIVAOSSIVAOSSwaggerUIConfigurerServiceImplement swaggerService;

    @Bean
    public Docket swaggerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfoProvider())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfoProvider() {
        swaggerService = new SIVAOSSIVAOSSwaggerUIConfigurerServiceImplement();
        return swaggerService.apiInfoProvider();
    }
}
