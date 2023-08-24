package org.CodeForPizza.spirit;

import javax.swing.*;

public class Shot extends Sprite {

    public Shot() {

    }

    public Shot(int x, int y) {
        initShot(x, y);

    }

    private void initShot(int x, int y) {
        var shotImg = "/images/shot.png";
        var img = new ImageIcon(getClass().getResource(shotImg));
        setImage(img.getImage());

        int X_Space = 1;
        int Y_SPACE = 6;
        this.setX(x + X_Space);
        this.setY(y - Y_SPACE);


    }
}
