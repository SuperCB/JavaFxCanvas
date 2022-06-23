package com.example.javafx.Stages;

import com.example.javafx.models.SqliteModel;
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
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class CreateNewUser extends Stage {

    private static boolean choosepic = false;

    public CreateNewUser(Stage stage) {
        setTitle("Create a new user");
        ImageView imageView = new ImageView();

        imageView.setFitHeight(200);
        imageView.setFitWidth(200);
        Image image = new Image("login.jpeg");

        imageView.setImage(image);


        Label selectpic = new Label("Click here to select your new profile picture");
        selectpic.setTextFill(Color.GRAY);
        selectpic.setFont(Font.font("TimesRomes", FontWeight.BOLD, FontPosture.ITALIC, 14));

        selectpic.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Get Resource File");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("png", "*.png")
                );
                File file = fileChooser.showOpenDialog(stage);
                System.out.println("file:" + file.getAbsolutePath());
                Image image = new Image("file:" + file.getAbsolutePath());
                choosepic = true;
                imageView.setImage(image);
            }
        });
        selectpic.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                selectpic.setUnderline(true);
            }
        });
        selectpic.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                selectpic.setUnderline(false);
            }
        });


        VBox profileVBox = new VBox();
        profileVBox.setAlignment(Pos.CENTER);
        profileVBox.getChildren().add(imageView);
        profileVBox.getChildren().add(selectpic);


        VBox mainbox = new VBox();
        Label username = new Label("User Name");
        TextField usernamet = new TextField();

        Label firstname = new Label("First Name");
        TextField firstnamet = new TextField();
        Label lastname = new Label("Last Name");
        TextField lastnamet = new TextField();


        Label password = new Label("Password");
        PasswordField passwordt = new PasswordField();

        Label repassword = new Label("Repeat your password");
        PasswordField repasst = new PasswordField();


        mainbox.getChildren().add(profileVBox);
        mainbox.getChildren().add(username);
        mainbox.getChildren().add(usernamet);
        mainbox.getChildren().add(firstname);
        mainbox.getChildren().add(firstnamet);
        mainbox.getChildren().add(lastname);
        mainbox.getChildren().add(lastnamet);

        mainbox.getChildren().add(password);
        mainbox.getChildren().add(passwordt);
        mainbox.getChildren().add(repassword);
        mainbox.getChildren().add(repasst);
        mainbox.setSpacing(10);


        HBox buttonBox = new HBox();

        Button adduser = new Button("Add User");
        adduser.setFont(Font.font(17));
        Button cancer = new Button("Cancer ");
        cancer.setFont(Font.font(17));


        adduser.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                boolean usernameflag = usernamet.getText().isEmpty();
                boolean passwordflag = passwordt.getText().isEmpty();
                boolean repasswordflag = repasst.getText().isEmpty();
                System.out.println(usernameflag);
                System.out.println(passwordflag);
                if (usernameflag || passwordflag || repasswordflag) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setContentText("You missed important imformation");
                    alert.show();
                    return;
                }

                if (!passwordt.getText().equals(repasst.getText())) {

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setContentText("The passwords entered twice are different");
                    alert.show();
                    passwordt.setText("");
                    repasst.setText("");
                    return;
                }
                if (choosepic) {
                    System.out.println(imageView.getImage().getUrl());
                    File picfile = new File(imageView.getImage().getUrl().substring(5));
                    try {
                        SqliteModel.insertNewUser(usernamet.getText(),
                                firstname.getText(), lastnamet.getText(), passwordt.getText()
                                , picfile
                        );
                    } catch (SQLException | IOException e) {
                        throw new RuntimeException(e);
                    } finally {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setContentText("Add a new user successfully");
                        alert.show();
                        usernamet.setText("");
                        firstnamet.setText("");
                        lastnamet.setText("");
                        passwordt.setText("");
                        repasst.setText("");
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("You forgot to choose your own profile picture");
                    alert.show();

                }
            }
        });


        buttonBox.getChildren().add(adduser);
        buttonBox.getChildren().add(cancer);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(15);

        mainbox.getChildren().add(buttonBox);

        Rectangle2D screenRectangle = Screen.getPrimary().getBounds();
        double width = screenRectangle.getWidth();
        double height = screenRectangle.getHeight();
        this.setHeight(height / 2);
        this.setWidth(width / 3);

        HBox hBox = new HBox();

        hBox.getChildren().add(mainbox);
        hBox.setSpacing(20);
        hBox.setAlignment(Pos.CENTER);

        VBox finbox = new VBox();
        finbox.getChildren().add(hBox);
        finbox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(finbox);

        setScene(scene);
    }


}
