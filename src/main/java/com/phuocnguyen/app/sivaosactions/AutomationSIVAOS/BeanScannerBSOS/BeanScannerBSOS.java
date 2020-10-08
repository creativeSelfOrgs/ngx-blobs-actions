package com.phuocnguyen.app.sivaosactions.AutomationSIVAOS.BeanScannerBSOS;


import com.sivaos.Utils.*;
import com.sivaos.Variables.PatternEpochVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Objects;

@Component
public class BeanScannerBSOS {

    private static String DESTINATION_BEANS = "logs/beans".concat(Objects.requireNonNull(PrefixUtils.snagPrefix("-", PropertiesUtils.readPropertiesByAttributed("application-params.yml", "app"), ".log", true)));
    /**
     * -sivaos-actions.log
     */
    private final Logger LOGGER = LoggerFactory.getLogger(com.sivaos.Configurer.AutomationSIVAOS.BeanScannerBSOS.BeanScannerBSOS.class);
    private final ApplicationContext applicationContext;

    public BeanScannerBSOS(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        FileUtils.createNewFile(DESTINATION_BEANS);
    }

    @PostConstruct
    void logsBeansOnSIVAOS() {
        String[] beans = applicationContext.getBeanDefinitionNames();
        int countBeans = applicationContext.getBeanDefinitionCount();
        Arrays.sort(beans);
        WriterUtils.exportStringToFileUsingBufferedOutputStream(DESTINATION_BEANS, DateUtils.snagPatternStageDefault(PatternEpochVariable.BIBLIOGRAPHY_EPOCH_PATTERN), true);
        WriterUtils.exportStringToFileUsingBufferedOutputStream(DESTINATION_BEANS, "Total: " + ExchangeUtils.exchangeIntegerToStringUsingIntegerToString(countBeans), true);
        WriterUtils.exportStringToFileUsingBufferedOutputStream(DESTINATION_BEANS, "-------------START-----------", true);
        for (String bean : beans) {
            WriterUtils.exportStringToFileUsingBufferedOutputStream(DESTINATION_BEANS, bean, true);
        }
        WriterUtils.exportStringToFileUsingBufferedOutputStream(DESTINATION_BEANS, "-------------END-------------", true);
        WriterUtils.exportStringToFileUsingBufferedOutputStream(DESTINATION_BEANS, "\n", true);
        LOGGER.info("success BeanScannerBSOS");
    }
}
