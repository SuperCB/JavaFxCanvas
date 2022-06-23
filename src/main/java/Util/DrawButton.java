package Util;

import javafx.scene.control.Button;

public class DrawButton extends Button {
    String name;

    public DrawButton(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
