package com.example.javafx.Stages;

import Util.CreateScrollPane;
import Util.DrawButton;
import com.example.javafx.HelloApplication;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.sql.SQLException;

public class MyCanvas extends Stage {


    private Canvas thecanvas;
    private GraphicsContext context;

    private String operType;

    private WritableImage snap;
    private double width;
    private double height;

    private CheckBox fillcheckBox;
    private ColorPicker colorPicker;
    private Slider linewidthslider;
    private TextField textinput;
    private Slider textsizeslider;

    private Image chooseimage;

    private void addConfigPane(BorderPane mainPane) {
        VBox mainvbox = new VBox();

        mainvbox.setMinWidth(width / 4);
        Label textinputtext = new Label("Input Text:");
        textinputtext.setFont(new Font(18));
        textinput = new TextField();
        textsizeslider = new Slider();
        textsizeslider.setMin(5);
        textsizeslider.setMax(100);
        textsizeslider.setShowTickLabels(true);
        Tooltip textsizetp = new Tooltip("change the font of the input text");
        textsizetp.setFont(new Font(18));
        textsizeslider.setTooltip(textsizetp);


        Label sliderlabel = new Label("Line size");
        sliderlabel.setFont(new Font(18));
        linewidthslider = new Slider();
        linewidthslider.setMax(90);
        linewidthslider.setMin(5);
        Tooltip linewidthtp = new Tooltip("change the width of the input line");
        linewidthtp.setFont(new Font(18));
        linewidthslider.setTooltip(linewidthtp);


        Label colorlabel = new Label("Choose the color");
        colorlabel.setFont(new Font(18));
        colorPicker = new ColorPicker();//颜色选择器
        colorPicker.setValue(Color.CORAL);
        colorPicker.setTooltip(new Tooltip("Pick the color"));

        fillcheckBox = new CheckBox("Fill");
        fillcheckBox.setFont(new Font(18));

        fillcheckBox.setTooltip(new Tooltip("Fill the shape"));


        mainvbox.getChildren().addAll(textinputtext, textinput, textsizeslider, sliderlabel, linewidthslider);
        mainvbox.getChildren().addAll(fillcheckBox, colorlabel, colorPicker);
        mainvbox.setSpacing(10);
        mainvbox.setAlignment(Pos.CENTER);

        mainPane.setRight(mainvbox);

    }

    /**
     * the user's profile
     */
    public static Image userimage = null;
    public static boolean usepicchange = false;


    public MyCanvas(Stage stage) {
        Rectangle2D screenRectangle = Screen.getPrimary().getBounds();
        width = screenRectangle.getWidth() / 2;
        height = screenRectangle.getHeight() / 2;

        operType = "";
        chooseimage = null;

        BorderPane mainPane = new BorderPane();
        addCanvas(mainPane);
        addButtonPane(mainPane);
        addConfigPane(mainPane);
        addMouseEvent();
        snap = thecanvas.snapshot(null, null);

        Scene scene = new Scene(mainPane, width, height);
        MenuBar menuBar = new MenuBar();
        menuBar.prefWidthProperty().bind(stage.widthProperty());


        HBox bottomHbox = new HBox();


        HBox profileHbox = new HBox();

        ImageView userpic = new ImageView();
        userpic.setFitHeight(50);
        userpic.setFitWidth(50);
        userpic.setImage(userimage);
        Label username = new Label(HelloApplication.username);
        username.setFont(new Font(20));

        Button changeProfile = new Button("Profile");
        changeProfile.setFont(new Font(20));


        changeProfile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                UserProfile userProfile = null;
                try {
                    userProfile = new UserProfile(stage);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                userProfile.show();
            }
        });

        Button logout = new Button("Log out");
        logout.setFont(new Font(20));

        profileHbox.getChildren().addAll(userpic, username, changeProfile, logout);
        profileHbox.setAlignment(Pos.CENTER);
        profileHbox.setSpacing(15);
        bottomHbox.getChildren().add(profileHbox);

        mainPane.setBottom(bottomHbox);
        mainPane.setTop(menuBar);

        // File menu - new, save, exit
        Menu fileMenu = new Menu("File");
        MenuItem newMenuItem = new MenuItem("New Canvas");
        MenuItem saveMenuItem = new MenuItem("Save Canvas");
        MenuItem exitMenuItem = new MenuItem("Exit");

        newMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                context.setFill(Color.WHITE);
                context.fillRect(0, 0, width, height);
                snap = thecanvas.snapshot(null, null);
            }
        });

        saveMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

            }
        });


        exitMenuItem.setOnAction(actionEvent -> Platform.exit());

        fileMenu.getItems().addAll(newMenuItem,
                new SeparatorMenuItem(), exitMenuItem);
        menuBar.getMenus().addAll(fileMenu);
        setScene(scene);
    }

    public static ImageView getImageView(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(55);
        imageView.setFitWidth(55);
        return imageView;
    }


    private void addnewButton(VBox mainVbox, String name, String bname, String des) {
        Image image = new Image(name + ".png");
        DrawButton drawButton = new DrawButton(name);
        drawButton.setFont(new Font(18));
        drawButton.setGraphic(getImageView(image));
        VBox vBox = new VBox();
        Label label = new Label(bname);
        label.setFont(getThefont());
        Tooltip tooltip = new Tooltip(des);
        tooltip.setFont(getThefont());
        drawButton.setTooltip(tooltip);
        vBox.getChildren().addAll(drawButton, label);
        label.setTextAlignment(TextAlignment.CENTER);
        vBox.setAlignment(Pos.CENTER);
        mainVbox.getChildren().add(vBox);

        Stage tempstage = this;
        if (name.equals("image")) {
            drawButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    operType = name;
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Get Resource File");
                    fileChooser.getExtensionFilters().addAll(
                            new FileChooser.ExtensionFilter("png", "*.png"),
                            new FileChooser.ExtensionFilter("jpeg", "*.jpeg")
                    );
                    File file = fileChooser.showOpenDialog(tempstage);
                    System.out.println("file:" + file.getAbsolutePath());
                    chooseimage = new Image("file:" + file.getAbsolutePath());
                }
            });
        } else {
            drawButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    operType = name;
                }
            });

        }

    }

    private double beginX, beginY;
    private double endX, endY;


    private void addMouseEvent() {


        thecanvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                beginX = mouseEvent.getX();
                beginY = mouseEvent.getY();
            }
        });
        thecanvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                endX = mouseEvent.getX();
                endY = mouseEvent.getY();
                context.setFill(colorPicker.getValue());
                context.setStroke(colorPicker.getValue());
                context.setLineWidth(linewidthslider.getValue());
                if (operType.equals("line")) {
//                    System.out.println(endX);
                    context.clearRect(0, 0, width, height);
                    context.drawImage(snap, 0, 0, width, height);
                    context.strokeLine(beginX, beginY, endX, endY);
                } else if (operType.equals("oval")) {
                    context.clearRect(0, 0, width, height);
                    context.drawImage(snap, 0, 0, width, height);

                    if (fillcheckBox.isSelected()) {
                        context.fillOval(Math.min(beginX, endX), Math.min(beginY, endY), Math.abs(endX - beginX), Math.abs(endY - beginY));
                    } else {
                        context.strokeOval(Math.min(beginX, endX), Math.min(beginY, endY), Math.abs(endX - beginX), Math.abs(endY - beginY));
                    }
                } else if (operType.equals("square")) {
                    context.clearRect(0, 0, width, height);
                    context.drawImage(snap, 0, 0, width, height);
                    if (fillcheckBox.isSelected()) {
                        context.fillRect(Math.min(beginX, endX), Math.min(beginY, endY), Math.abs(endX - beginX), Math.abs(endY - beginY));
                    } else {
                        context.strokeRect(Math.min(beginX, endX), Math.min(beginY, endY), Math.abs(endX - beginX), Math.abs(endY - beginY));
                    }
                }
            }

        });
        thecanvas.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (operType.equals("text")) {
                    context.setLineWidth(1);
                    context.setFill(colorPicker.getValue());
                    context.setStroke(colorPicker.getValue());
                    context.setFont(new Font(textsizeslider.getValue()));
                    if (fillcheckBox.isSelected()) {
                        context.fillText(textinput.getText(), mouseEvent.getX(), mouseEvent.getY());
                    } else {
                        context.strokeText(textinput.getText(), mouseEvent.getX(), mouseEvent.getY());
                    }
                } else if (operType.equals("image")) {
                    if (chooseimage != null) {
                        context.drawImage(chooseimage, mouseEvent.getX(), mouseEvent.getY());
                    }
                }
                snap = thecanvas.snapshot(null, null);
            }
        });
    }

    private void addButtonPane(BorderPane mainPane) {
        VBox buttonVbox = new VBox();
        addnewButton(buttonVbox, "square", "Square", "Draw a square");
        addnewButton(buttonVbox, "line", "Line", "Draw a line");
        addnewButton(buttonVbox, "oval", "Oval", "Draw a oval");
        addnewButton(buttonVbox, "text", "Text", "Input text");
        addnewButton(buttonVbox, "image", "Image", "Input image");
        buttonVbox.setSpacing(15);
        mainPane.setLeft(buttonVbox);
    }

    private void addCanvas(BorderPane mainPane) {

        thecanvas = new Canvas(width, height);
        context = thecanvas.getGraphicsContext2D();

        context.setFill(Color.WHITE);
        context.fillRect(0, 0, width, height);


        Group group = new Group();
        group.getChildren().add(thecanvas);
        ScrollPane scrollPane = CreateScrollPane.createZoomPane(group);
        mainPane.setCenter(scrollPane);
    }

    private Font getThefont() {
        return Font.font("TimesRomes", FontWeight.EXTRA_BOLD, 15);
    }

    /**
     * set the wid
     *
     * @param stage
     */
    private void setScreenProperty(Stage stage) {


        Rectangle2D screenRectangle = Screen.getPrimary().getBounds();
        double width = screenRectangle.getWidth();
        double height = screenRectangle.getHeight();
        stage.setHeight(height / 2);
        stage.setWidth(width / 2);
    }
}
