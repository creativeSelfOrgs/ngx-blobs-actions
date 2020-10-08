package com.phuocnguyen.app.sivaosactions.Configurer.AutomationSIVAOS.ProfilesScannerBSOS;

import com.sivaos.Utils.*;
import com.sivaos.Variables.PatternEpochVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Objects;

@Component
public class ProfilesScannerBSOS {

    private static String DESTINATION_ACTIVE_PROFILES = "logs/active-profiles".concat(Objects.requireNonNull(PrefixUtils.snagPrefix("-", PropertiesUtils.readPropertiesByAttributed("application-params.yml", "app"), ".log", true)));
    private final Logger LOGGER = LoggerFactory.getLogger(com.sivaos.Configurer.AutomationSIVAOS.ProfilesScannerBSOS.ProfilesScannerBSOS.class);
    private Environment environment;

    ProfilesScannerBSOS(Environment environment) {
        this.environment = environment;
        FileUtils.createNewFile(DESTINATION_ACTIVE_PROFILES);
    }

    @PostConstruct
    void logsProfilesOnSIVAOS() {
        String[] activeProfiles = environment.getActiveProfiles();
        LOGGER.info("Active profiles: {}", Arrays.toString(activeProfiles));
        WriterUtils.exportStringToFileUsingBufferedOutputStream(DESTINATION_ACTIVE_PROFILES, DateUtils.snagPatternStageDefault(PatternEpochVariable.BIBLIOGRAPHY_EPOCH_PATTERN), true);
        WriterUtils.exportStringToFileUsingBufferedOutputStream(DESTINATION_ACTIVE_PROFILES, Arrays.toString(activeProfiles), true);
        WriterUtils.exportStringToFileUsingBufferedOutputStream(DESTINATION_ACTIVE_PROFILES, "\n", true);
    }
}
