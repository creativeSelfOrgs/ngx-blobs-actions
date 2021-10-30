package com.phuocnguyen.app.sivaosactions.config.appConfig;

import com.ngxsivaos.services.NgxSOrgHelperBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@SuppressWarnings({"FieldCanBeLocal"})
@Component
@Order(-1)
public class AppConfig implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    private final NgxSOrgHelperBaseService ngxSOrgHelperBaseService;

    private final Environment environment;
    private final ApplicationContext applicationContext;


    @Value("${spring.profiles.active}")
    private String profileActive;

    @Autowired
    public AppConfig(
            NgxSOrgHelperBaseService ngxSOrgHelperBaseService,
            Environment environment,
            ApplicationContext applicationContext) {
        this.ngxSOrgHelperBaseService = ngxSOrgHelperBaseService;
        this.environment = environment;
        this.applicationContext = applicationContext;
    }


    @PostConstruct
    private void init() {
        ngxSOrgHelperBaseService.onScannerContext(applicationContext);
        ngxSOrgHelperBaseService.onScannerProfilesContext(environment);
        ngxSOrgHelperBaseService.onScriptsContext(profileActive);
    }

    @Override
    public void run(String... args) {
        logger.info("SIVAOS starting...auto generate....");
    }
}
