package com.polytech.spik.controllers;

import com.polytech.spik.services.sms.LanSmsService;
import com.polytech.spik.services.sms.SmsRemoteContext;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by momo- on 15/12/2015.
 */
public class MainController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);


    /** User Interface Bindings **/
    public TextField message_input;

    /** Model **/
    private ResourceBundle resources;

    /** Services **/
    private LanSmsService smsService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.trace("Initialized Spik with Bundle {}", resources.getLocale());

        this.resources = resources;

        launchSpik();
    }

    private void launchSpik() {
        try {
            smsService = new LanSmsService(SmsRemoteContext::new);
            smsService.run();
        } catch (InterruptedException e) {
            LOGGER.error("Unable to launch Sms Service", e);
        }
    }

    public void sendMessage(ActionEvent actionEvent) {
        LOGGER.trace("Click on send button (message.size = {})", message_input.getText().length());
    }
}
