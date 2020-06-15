/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.ui;

import com.zafirodesktop.controller.WelcomeController;
import com.zafirodesktop.entity.Settings;
import com.zafirodesktop.model.AbstractFacade;
import com.zafirodesktop.model.SessionBD;
import com.zafirodesktop.util.LogActions;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 *
 * @author Digitall
 */
public class ZafiroDesktop extends Application {

    private Pane splashLayout;
    private ProgressBar loadProgress;
    private Label progressText;
    private Stage mainStage;
    private static final int SPLASH_WIDTH = 400;
    private static final int SPLASH_HEIGHT = 300;

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void init() {
        ImageView splash = new ImageView(new Image("/com/zafirodesktop/ui/img/logo.png"));
        loadProgress = new ProgressBar();
        loadProgress.setPrefWidth(SPLASH_WIDTH - 20);
        progressText = new Label("Cargando . . .");
        splashLayout = new VBox();
        splashLayout.getChildren().addAll(splash, loadProgress, progressText);
        progressText.setAlignment(Pos.CENTER);
        splashLayout.setStyle("-fx-padding: 5; -fx-background-color: white; -fx-border-width:0;");
        splashLayout.setEffect(new DropShadow());
    }

    @Override
    public void start(final Stage initStage) throws IOException {
        showSplash(initStage);
        /*String[] cmd = {"cscript", "start.vbs", "\\bin\\"};
        Runtime.getRuntime().exec(cmd);
        Runtime.getRuntime().exec("cscript start.vbs");*/
        initStage.toFront();
        FadeTransition fadeSplash = new FadeTransition(Duration.seconds(3), splashLayout);
        fadeSplash.setFromValue(0.0);
        fadeSplash.setToValue(1.0);
        fadeSplash.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    showMainStage();
                } catch (IOException ex) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    ex.printStackTrace(pw);
                    LogActions log = new LogActions();
                    log.createEntry(String.valueOf(ex.hashCode()), "Iniciar la aplicación", ex.getMessage(), sw.toString(), "");
                }
                initStage.close();
            }
        });
        fadeSplash.play();
    }

    private void showMainStage() throws IOException {
        mainStage = new Stage(StageStyle.UNDECORATED);
        Parent root;
        AbstractFacade abs;
        String title;
        Locale es = new Locale("es", "ES");
        try {
            SessionBD.persistenceCreate();
            abs = new AbstractFacade();
            Settings settings = (Settings) abs.findByIdInt("Settings", 1);
            if (settings.getPrintSize() != null) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
                fxmlLoader.setResources(ResourceBundle.getBundle("com.zafirodesktop.util.Bundle", es));
                root = (Parent) fxmlLoader.load();
                title = "Login";
            } else {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("welcome.fxml"));
                fxmlLoader.setResources(ResourceBundle.getBundle("com.zafirodesktop.util.Bundle", es));
                root = (Parent) fxmlLoader.load();
                WelcomeController welcomeController = fxmlLoader.<WelcomeController>getController();
                welcomeController.startInicialConfig();
                welcomeController.setIsInitialConfig(true);
                title = "Bienvenido";
            }
            Scene scene = new Scene(root);
            mainStage.getIcons().add(new Image("/com/zafirodesktop/ui/img/ico/scirebox.bmp"));
            mainStage.setScene(scene);
            mainStage.setTitle(title);
            mainStage.show();
        } catch (Exception e) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("welcome.fxml"));
            fxmlLoader.setResources(ResourceBundle.getBundle("com.zafirodesktop.util.Bundle", es));
            root = (Parent) fxmlLoader.load();
            WelcomeController welcomeController = fxmlLoader.<WelcomeController>getController();
            welcomeController.displayConectionError();

            Scene scene = new Scene(root);
            mainStage.getIcons().add(new Image("/com/zafirodesktop/ui/img/ico/scirebox.bmp"));
            mainStage.setScene(scene);
            mainStage.setTitle("Error");
            mainStage.show();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Iniciar la base de datos para acceder a la App", e.getMessage(), sw.toString(), "");
        }
    }

    private void showSplash(Stage initStage) {
        Scene splashScene = new Scene(splashLayout);
        final Rectangle2D bounds = Screen.getPrimary().getBounds();
        initStage.getIcons().add(new Image("/com/zafirodesktop/ui/img/ico/scirebox.bmp"));
        initStage.setScene(splashScene);
        initStage.initStyle(StageStyle.UNDECORATED);
        initStage.setX(bounds.getMinX() + bounds.getWidth() / 2 - SPLASH_WIDTH / 2);
        initStage.setY(bounds.getMinY() + bounds.getHeight() / 2 - SPLASH_HEIGHT / 2);
        initStage.show();
    }

}
