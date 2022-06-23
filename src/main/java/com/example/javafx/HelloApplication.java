package com.example.javafx;

import com.example.javafx.Stages.CreateNewUser;
import com.example.javafx.Stages.MyCanvas;
import com.example.javafx.models.SqliteModel;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class HelloApplication extends Application {


    public static String username = null;

    @Override
    public void start(Stage stage) throws IOException {


        VBox mainBox = new VBox();


        Text name = new Text("Name       ");
        name.setFont(Font.font(18));
        TextField nameInput = new TextField();
        HBox nameBox = new HBox();
        nameBox.getChildren().add(name);
        nameBox.getChildren().add(nameInput);
        nameBox.setAlignment(Pos.BASELINE_CENTER);

        Text passWord = new Text("PassWord ");
        passWord.setFont(Font.font(18));
        PasswordField passwordInput = new PasswordField();
        HBox passwordBox = new HBox();
        passwordBox.getChildren().add(passWord);
        passwordBox.getChildren().add(passwordInput);
        passwordBox.setAlignment(Pos.BASELINE_CENTER);

        Button loginbutton = new Button("Login");
        loginbutton.setFont(Font.font(17));
        Button exitbutton = new Button("Cancel");
        exitbutton.setFont(Font.font(17));

        HBox buttonBox = new HBox();
        buttonBox.getChildren().add(loginbutton);
        buttonBox.getChildren().add(exitbutton);
        buttonBox.setAlignment(Pos.BASELINE_CENTER);
        buttonBox.setSpacing(20);

        loginbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                MyCanvas myCanvas = new MyCanvas(stage);
                myCanvas.show();
            }
        });


        mainBox.setSpacing(10);


        exitbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                stage.close();
            }
        });


        Image image = new Image("login.jpeg");
        ImageView imageView = new ImageView(image);

        mainBox.getChildren().add(imageView);
        mainBox.getChildren().add(nameBox);
        mainBox.getChildren().add(passwordBox);
        mainBox.getChildren().add(buttonBox);


        Rectangle2D screenRectangle = Screen.getPrimary().getBounds();
        double width = screenRectangle.getWidth();
        double height = screenRectangle.getHeight();
        stage.setHeight(height / 2);
        stage.setWidth(width / 2);
        Scene theScene = new Scene(mainBox);
        mainBox.setAlignment(Pos.CENTER);

        Label adduser = new Label("or create a newuser");
        adduser.setTextFill(Color.BLUE);
        adduser.setFont(Font.font("TimesRomes", FontWeight.BOLD, FontPosture.ITALIC, 15));
        adduser.setUnderline(true);

        adduser.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                CreateNewUser createNewUser = new CreateNewUser(stage);
                createNewUser.show();
            }
        });

        mainBox.getChildren().add(adduser);
        stage.setTitle("Welcome to my canvas");
        stage.setScene(theScene);
        stage.initStyle(StageStyle.DECORATED);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}