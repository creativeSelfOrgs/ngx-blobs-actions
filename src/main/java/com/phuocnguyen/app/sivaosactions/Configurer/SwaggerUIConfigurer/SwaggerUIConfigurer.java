package com.phuocnguyen.app.sivaosactions.Configurer.SwaggerUIConfigurer;

import com.sivaos.Utils.PropertiesUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @http://host:port/swagger-ui.html
 */
@Configuration
@EnableSwagger2
public class SwaggerUIConfigurer {

    private void loadConfigProperties() {
        PropertiesUtils.setApplicationSource("application-params.yml");
        PropertiesUtils.loadProperties();
    }

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
        loadConfigProperties();
        return new ApiInfoBuilder().title(PropertiesUtils.readPropertiesByAttributed("app"))
                .description(PropertiesUtils.readPropertiesByAttributed("description"))
                .contact(new Contact(PropertiesUtils.readPropertiesByAttributed("contact-name"), PropertiesUtils.readPropertiesByAttributed("contact-url"), PropertiesUtils.readPropertiesByAttributed("contact-email")))
                .license(PropertiesUtils.readPropertiesByAttributed("license"))
                .licenseUrl(PropertiesUtils.readPropertiesByAttributed("license-url"))
                .version(PropertiesUtils.readPropertiesByAttributed("version")).build();
    }
}
