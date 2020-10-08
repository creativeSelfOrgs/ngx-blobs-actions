package com.phuocnguyen.app.sivaosactions.AutomationSIVAOS.ControlCenterBSOS;


import com.sivaos.Utils.FileUtils;
import com.sivaos.Utils.PrefixUtils;
import com.sivaos.Utils.PropertiesUtils;
import com.sivaos.Utils.WriterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Objects;

@Component
@Order(999)
public class ControlCenterBSOS {

    private static final Logger LOGGER = LoggerFactory.getLogger(com.sivaos.Configurer.AutomationSIVAOS.ControlCenterBSOS.ControlCenterBSOS.class);

    private static final String BUILD_SIVAOS = Objects.requireNonNull(PrefixUtils.snagPrefix("-", PropertiesUtils.readPropertiesByAttributed("application-params.yml", "app-build"), ".bat", false));
    private static final String CURRENT_WORKING_DIR = "user.dir";
    private static final String RUNNING_SIVAOS = Objects.requireNonNull(PrefixUtils.snagPrefix("-", PropertiesUtils.readPropertiesByAttributed("application-params.yml", "app-run"), ".bat", false));
    private static final String COMMAND_RUN_SIVAOS = "@echo off".concat(System.getProperty("line.separator")).concat("start java -Xmx1024m -jar -Dspring.profiles.active=");
    private static final String COMMAND_RUN_BUILD = "@echo off ".concat(System.getProperty("line.separator")).concat("start cmd /k .\\gradlew bootJar");
    private static final String FOLDER_JAR_BUILD = " build\\libs\\" + Objects.requireNonNull(PrefixUtils.snagPrefix("-", PropertiesUtils.readPropertiesByAttributed("application-params.yml", "app-jar"), ".jar", false)) + " &";
    @Value("${spring.profiles.active}")
    private String profilesActive;

    ControlCenterBSOS() {
    }

    @PostConstruct
    void logsControlCenterBSOS() {
        buildSIVAOSReturnFileBatExtension();
        buildRunSIVAOSReturnFileBatExtension();
    }

    void buildSIVAOSReturnFileBatExtension() {
        String root = System.getProperty(CURRENT_WORKING_DIR);
        String pathDestination = root.concat("/").concat(BUILD_SIVAOS);
        boolean isTrue = FileUtils.createNewFile(pathDestination);
        if (isTrue) {
            WriterUtils.exportStringToFileUsingBufferedOutputStream(pathDestination, COMMAND_RUN_BUILD, false);
            /* synchronizeBuildAppSIVAOS(pathDestination); */
        }
    }

    void buildRunSIVAOSReturnFileBatExtension() {
        String root = System.getProperty(CURRENT_WORKING_DIR);
        String pathDestination = root.concat("/").concat(RUNNING_SIVAOS);
        boolean isTrue = FileUtils.createNewFile(pathDestination);
        if (isTrue) {
            WriterUtils.exportStringToFileUsingBufferedOutputStream(pathDestination, COMMAND_RUN_SIVAOS + profilesActive + FOLDER_JAR_BUILD, false);
        }
    }

    void synchronizeBuildAppSIVAOS(String path) {
        if (FileUtils.isFileExist(path)) {
            try {
                Runtime.getRuntime().exec(new String[]{"cmd", "/k", "start ".concat(path)});
            } catch (IOException message) {
                LOGGER.error(message.getMessage());
            }
        }
    }
}
