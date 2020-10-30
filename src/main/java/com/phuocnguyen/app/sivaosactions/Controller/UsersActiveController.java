package com.phuocnguyen.app.sivaosactions.Controller;

import com.phuocnguyen.app.sivaosactions.Configurer.SIVAJDBCConnectAutomation.SIVAJDBCConnectConfigurer;
import com.sivaos.Controller.BaseSIVAOSController;
import com.sivaos.Model.Response.SIVAResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;

import static com.sivaos.Variables.EndPointVariable.ENDPOINT_USER_CURRENT;
import static com.sivaos.Variables.EndPointVariable.ENDPOINT_USER_URL;

@RestController
@RequestMapping(value = ENDPOINT_USER_URL)
public class UsersActiveController extends BaseSIVAOSController {

    private static final Logger logger = LoggerFactory.getLogger(UsersActiveController.class);

    private final SIVAJDBCConnectConfigurer sivajdbcConnectConfigurer;

    @Autowired
    public UsersActiveController(SIVAJDBCConnectConfigurer sivajdbcConnectConfigurer) {
        this.sivajdbcConnectConfigurer = sivajdbcConnectConfigurer;
    }

    @GetMapping(ENDPOINT_USER_CURRENT)
    @Async("threadPoolExecutor")
    public @ResponseBody
    ResponseEntity<?> snagUsers() {
        Connection connection = sivajdbcConnectConfigurer.getConnection();
        logger.info("current username: {}", currentUsername);
        snagUserAsQuery(currentUsername, connection);
        snagRoleIdsAsQuery(currentUsername, connection);
        return new ResponseEntity<>(SIVAResponseDTO.buildSIVAResponse(user), HttpStatus.OK);
    }


}
