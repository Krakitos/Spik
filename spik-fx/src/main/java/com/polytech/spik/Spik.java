package com.funtowiczmo.spik;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Created by momo- on 26/10/2015.
 */
public class Spik extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Spik.class);

    private static final String I18N_BASE_NAME = "i18n/strings";

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        LOGGER.info("Started Spik");
        final Rectangle2D screen = Screen.getPrimary().getBounds();
        final Parent parent = FXMLLoader.load(
                getClass().getClassLoader().getResource("views/main.fxml"),
                getResourceBundle()
        );
        final Scene scene = new Scene(parent, screen.getWidth() * 0.7, screen.getHeight() * 0.7);

        Font.loadFont(getClass().getClassLoader().getResourceAsStream("fonts/Roboto-Black.ttf"), 12);

        scene.getStylesheets().add("styles/styles.css");
        primaryStage.setTitle("Spik :)");
        primaryStage.setScene(scene);

        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.show();
    }

    private ResourceBundle getResourceBundle(){
        try{
            return ResourceBundle.getBundle(I18N_BASE_NAME, Locale.getDefault());
        }catch (MissingResourceException e){
            return ResourceBundle.getBundle(I18N_BASE_NAME, Locale.ENGLISH);
        }
    }
}
