package org.CodeForPizza.spirit;

import org.CodeForPizza.cam.Params;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class Player extends Sprite {

    private int width;

    public Player() {
        initPlayer();
    }

    private void initPlayer() {

        //gets the image of the player
        var playerImg = "/images/player.png";
        var img = new ImageIcon(getClass().getResource(playerImg));

        //gets the width of the image
        width = img.getImage().getWidth(null);
        setImage(img.getImage());


        //starting position of the player
        int START_X = 270;
        setX(START_X);

        //starting position of the player
        int START_Y = 280;
        setY(START_Y);
    }

    //moves the player
    public void act() {

        x+=dx;

        if(x<= 2) {

            x=2;
        }

        if( x >= Params.BOARD_WIDTH - 2*width) {

            x= Params.BOARD_WIDTH - 2*width;
        }
    }


    //when the key is pressed, the player moves
    public void keyPressed(KeyEvent e) {

        //stores the current key being pressed
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {

            dx = -2;
        }

        if (key == KeyEvent.VK_RIGHT) {

            dx = 2;
        }
    }

    //when the key is released, the player stops moving
    public void KeyReleased(KeyEvent e) {


        int key = e.getKeyCode();

        if(key == KeyEvent.VK_LEFT) {

            dx = 0;
        }

        if(key == KeyEvent.VK_RIGHT) {

            dx = 0;
        }
    }
}
