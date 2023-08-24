package org.CodeForPizza.cam;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    public Main() {
        //creates the board
        initUI();
    }

    private void initUI() {
        //adds the board to the window
        add(new Board());
        setTitle("Space Invaders");
        setSize(Params.BOARD_WIDTH, Params.BOARD_HEIGHT);

        //closes the window when the x button is pressed
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //centers the window
        setLocationRelativeTo(null);
        //makes the window not resizable
        setResizable(false);
    }


    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            var ex = new Main();
            ex.setVisible(true);
        });
    }
}
