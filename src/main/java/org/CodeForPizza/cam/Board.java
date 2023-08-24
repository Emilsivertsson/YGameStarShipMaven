package org.CodeForPizza.cam;

import org.CodeForPizza.spirit.Alien;
import org.CodeForPizza.spirit.Player;
import org.CodeForPizza.spirit.Shot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.swing.Timer;



import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Board extends JPanel {

    private Dimension d;
    private List<Alien> aliens;
    private Player player;
    private Shot shot;

    private int direction = -1;
    private int deaths = 0;

    private boolean inGame = true;
    private String explImg = "/images/explosion.png";
    private String message = "Game Over";

    private Timer timer;

    public Board() {

        initBoard();
        gameInit();
    }

    private void initBoard() {

        //adds the key listener to the board so that the player can move
        addKeyListener(new TAdapter());
        //allows the board to be focused on so that the key listener can work properly
        setFocusable(true);
        //sets the size of the board to the specified width and height
        d = new Dimension(Params.BOARD_WIDTH, Params.BOARD_HEIGHT);
        setBackground(Color.black);

        //creates the timer that will be used to update the game every DELAY milliseconds
        timer = new Timer(Params.DELAY, new GameCycle());
        timer.start();

        gameInit();
    }

    private void gameInit() {

        aliens = new ArrayList<>();
        //initializes the aliens
        for(int i =0; i < 4; i++) {
            for(int j = 0; j < 6; j++) {

                var alien = new Alien(Params.ALIEN_INIT_X + 18 * j,
                        Params.ALIEN_INIT_Y + 18*i);
                aliens.add(alien);
            }
        }

        //initializes the player and the shot
        player = new Player();
        shot = new Shot();
    }


    private void drawAliens(Graphics g) {

        //draws the aliens on the board
        for (Alien alien : aliens) {

            //if the alien is visible, draw it
            if (alien.isVisible()) {
                g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
            }

            if (alien.isDying()) {

                alien.die();
            }
        }
    }

    private void drawPlayer(Graphics g) {

        if (player.isVisible()) {

            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
        }

        if (player.isDying()) {

            player.die();
            inGame = false;
        }
    }

    private void drawShot(Graphics g) {

        if (shot.isVisible()) {

            g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
        }
    }

    private void drawBombing(Graphics g) {

        for (Alien a : aliens) {

            Alien.Bomb b = a.getBomb();

            if (!b.isDestroyed()) {

                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);

    }


    private void doDrawing(Graphics g) {

        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);
        g.setColor(Color.green);

        if(inGame) {

            g.drawLine(0, Params.GROUND, Params.BOARD_WIDTH, Params.GROUND);

            drawAliens(g);
            drawPlayer(g);
            drawShot(g);
            drawBombing(g);

        } else {

            if (timer.isRunning()) {
                timer.stop();
            }

            gameOver(g);

        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void gameOver(Graphics g) {

        g.setColor(Color.black);
        g.fillRect(0, 0, Params.BOARD_WIDTH, Params.BOARD_HEIGHT);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, Params.BOARD_WIDTH/ 2 - 30, Params.BOARD_WIDTH - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, Params.BOARD_WIDTH/ 2 - 30, Params.BOARD_WIDTH - 100, 50);

        var small = new Font("Helvetica", Font.BOLD, 14);
        var fontMetrics = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (Params.BOARD_WIDTH - fontMetrics.stringWidth(message)) / 2,
                Params.BOARD_WIDTH / 2);
    }


    private void update() {

        if(deaths == Params.NUMBER_OF_ALIENS_TO_DISPLAY) {

            inGame = false;
            timer.stop();
            message = "You won!";
        }



        player.act();


        //checks if the shot is visible
        //if it is visible, it checks if it is colliding with an alien
        //if it is colliding with an alien, it kills the alien and the shot
        //if it is not colliding with an alien, it moves the shot up
        if (shot.isVisible()) {

            int shotX = shot.getX();
            int shotY = shot.getY();

            for (Alien alien : aliens) {

                int alienX = alien.getX();
                int alienY = alien.getY();

                if(alien.isVisible() && shot.isVisible()) {
                    if (shotX >= (alienX)
                            && shotX <= (alienX + Params.ALIEN_WIDTH)
                            && shotY >= (alienY)
                            && shotY <= (alienY + Params.ALIEN_HEIGHT)) {

                        var img = new ImageIcon(explImg);
                        alien.setImage(img.getImage());
                        alien.setDying(true);
                        deaths++;
                        shot.die();
                    }
                }
            }

            int y = shot.getY();
            y-=4;

            if (y<0) {
                shot.die();
            } else {
                shot.setY(y);
            }
        }

        //iterates through the aliens and checks if they are visible
        //if they are visible, it checks if they are at the edge of the board

        for (Alien alien : aliens) {

            int x = alien.getX();

            if (x >= Params.BOARD_WIDTH - Params.BORDER_RIGHT && direction != -1) {

                direction = -1;

                Iterator<Alien> i1 = aliens.iterator();

                while (i1.hasNext()) {

                    Alien a2 = i1.next();
                    a2.setY(a2.getY() + Params.GO_DOWN);
                }
            }

            //if they are at the edge of the board, it changes the direction of the aliens
            //and moves them down
            if (x <= Params.BORDER_LEFT && direction != 1) {

                direction = 1;

                Iterator<Alien> i2 = aliens.iterator();

                while (i2.hasNext()) {

                    Alien a = i2.next();
                    a.setY(a.getY() + Params.GO_DOWN);
                }
            }
        }


        Iterator<Alien> it = aliens.iterator();

        while (it.hasNext()) {

            Alien alien = it.next();

            if (alien.isVisible()) {

                int y = alien.getY();

                if (y > Params.GROUND - Params.ALIEN_HEIGHT) {

                    inGame = false;
                    message = "Invasion!";
                }

                alien.act(direction);
            }
        }

        //checks if the bombs are destroyed
        //if they are destroyed, it sets them to the same position as the alien that dropped them
        //if they are not destroyed, it moves them down
        var generator = new Random();

        for(Alien alien : aliens) {

            int shot = generator.nextInt(15);
            Alien.Bomb bomb = alien.getBomb();

            if (shot == Params.CHANCE && alien.isVisible() && bomb.isDestroyed()) {

                bomb.setDestroyed(false);
                bomb.setX(alien.getX());
                bomb.setY(alien.getY());

            }

            //checks if the bomb is colliding with the player
            //if it is colliding with the player, it kills the player and the bomb
            int bombX = bomb.getX();
            int bombY = bomb.getY();
            int playerX = player.getX();
            int playerY = player.getY();

            if (player.isVisible() && !bomb.isDestroyed()) {

                if(bombX >=(playerX)
                        && bombX <= (playerX + Params.PLAYER_WIDTH)
                        && bombY >= (playerY)
                        && bombY <= (playerY + Params.PLAYER_HEIGHT)) {

                    var img = new ImageIcon(explImg);
                    player.setImage(img.getImage());
                    player.setDying(true);
                    bomb.setDestroyed(true);

                }
            }
            //moves the bomb down
            if (!bomb.isDestroyed()) {

                bomb.setY(bomb.getY() + 1);

                if (bomb.getY() >= Params.GROUND - Params.BOMB_HEIGHT) {

                    bomb.setDestroyed(true);
                }
            }


        }
    }

    private void doGameCycle() {

        update();
        repaint();
    }

    private class GameCycle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            doGameCycle();
        }
    }


    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {

            player.KeyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {

            player.keyPressed(e);

            int x = player.getX();
            int y = player.getY();

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_SPACE) {

                if(inGame) {

                    if (!shot.isVisible()) {

                        shot = new Shot(x, y);
                    }
                }
            }
        }
    }

}

