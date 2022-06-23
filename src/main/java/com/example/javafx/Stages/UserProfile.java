package com.example.javafx.Stages;

import com.example.javafx.HelloApplication;
import com.example.javafx.models.SqliteModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class UserProfile extends Stage {

    public UserProfile(Stage stage) throws SQLException {

        setTitle("Change your Profile");

        HBox mainhbox = new HBox();
        ImageView imageView = new ImageView();
        imageView.setImage(MyCanvas.userimage);

        imageView.setFitWidth(150);
        imageView.setFitHeight(150);


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
        Label username = new Label("UserName ");
        username.setFont(new Font(20));


        Label namecontent = new Label(HelloApplication.username);
        namecontent.setFont(new Font(20));

        Label finame = new Label("First Name");
        finame.setFont(new Font(20));
        TextField firstfield = new TextField(SqliteModel.getFirstName(HelloApplication.username));


        Label lastname = new Label("Last Name");
        lastname.setFont(new Font(20));

        TextField lastfield = new TextField(SqliteModel.getLastName(HelloApplication.username));

        VBox leftbox = new VBox();
        VBox rightbox = new VBox();

        leftbox.getChildren().addAll(imageView, selectpic);
        rightbox.getChildren().addAll(username, namecontent, finame, firstfield, lastname, lastfield);


        Button ok = new Button("OK");

        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                File picfile = new File(imageView.getImage().getUrl().substring(5));
                try {
                    SqliteModel.changeUserProfile(HelloApplication.username, firstfield.getText(), lastfield.getText(), picfile);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Change Profile successfully");
                alert.show();
            }
        });

        Button cancel = new Button("Cancel");

        HBox buttonbox = new HBox();
        buttonbox.getChildren().addAll(ok, cancel);

        buttonbox.setAlignment(Pos.CENTER);
        leftbox.setAlignment(Pos.CENTER);
        rightbox.setAlignment(Pos.CENTER);

        rightbox.getChildren().add(buttonbox);
        mainhbox.getChildren().addAll(leftbox, rightbox);
        rightbox.setSpacing(15);
        Scene scene = new Scene(mainhbox);
        setScene(scene);
    }
}
