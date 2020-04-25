import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class MinesweeperGame extends MouseAdapter {
    private static int side = 10;
    private static int mines = 10;
    private final GameField gameField;                                             //Declaring game field
    private int countMinesOnField;
    private int countFlags;
    private boolean isGameStopped;
    private int countClosedTiles = side * side;
    private int score;
    private static final Icon rs2 = new ImageIcon("icons/reset1.png");            //Another reset icons
    private static final Icon open = new ImageIcon("icons/open.png");
    private static final Icon dead = new ImageIcon("icons/dead.png");
    private static final Icon openTile = new ImageIcon("icons/emptyTile.png");    //Empty tile icon
    private static final Icon bomb = new ImageIcon("icons/bomb.png");             //Bomb icon
    private static final Icon flag = new ImageIcon("icons/flag.png");             //Flag icon
    private static final Icon i1 = new ImageIcon("icons/2.png");
    private Random random;

    public MinesweeperGame() {
        gameField = new GameField(250, 250, side, side);   //Creating new game field
        createGame();
    }

    @Override
    public void mouseClicked(MouseEvent e) {                    //Switch
        switch (e.getButton()) {
            case MouseEvent.BUTTON1: {                          //Getting a mouse button
                for (GameObject button : gameField.buttons) {
                    if (e.getSource() == button && !button.isMine && !button.isOpen) {
                        button.setIcon(i1);
                        button.isOpen = true;
                        button.setBackground(Color.white);
                        button.setFocusPainted(false);
                        //button.setBorderPainted(false);
                        //button.setContentAreaFilled(false);
                        button.setEnabled(false);
                    }
                    if (e.getSource() == button && button.isMine && !button.isOpen) {
                        button.setIcon(bomb);
                        button.isOpen = true;
                        button.setBackground(Color.white);
                        countMinesOnField--;
                        gameField.leftMines.setText(String.valueOf(countMinesOnField));
                    }
                }
            }
            break;
            case MouseEvent.BUTTON3: {
                for (GameObject button : gameField.buttons) {
                    if (e.getSource() == button  && !button.isOpen) {
                        button.setIcon(flag);
                        //button.setBackground(Color.red);
                    }
                }
            }
            break;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getSource() == gameField.reset && SwingUtilities.isLeftMouseButton(e))
            gameField.reset.setIcon(rs2);

        for (GameObject button : gameField.buttons) {
            if (e.getSource() == button && SwingUtilities.isLeftMouseButton(e) && !button.isOpen) {
                gameField.reset.setIcon(open);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getSource() == gameField.reset) {
            gameField.reset.setIcon(gameField.rs);
        }

        for (GameObject button : gameField.buttons) {
            if (e.getSource() == button && !button.isOpen) {
                gameField.reset.setIcon(gameField.rs);
            }
        }
    }

    public void createGame() {
        random = new Random();
        for (int i = 0; i < mines; i++) {
            while (true) {
                int r = random.nextInt(side * side);
                if (!gameField.buttons[r].isMine) {
                    gameField.buttons[r].isMine = true;
                    break;
                }
            }
        }
        for (GameObject button : gameField.buttons) {                           //Adding Mouse Listener to all buttons
            if (button.isMine) countMinesOnField++;
            button.addMouseListener(this);
        }
        gameField.reset.addMouseListener(this);                              //Adding Mouse Listener to the reset button
        countMinesOnField = mines;

    }
}




/*
panel.addMouseListener(new MouseAdapter() {

@Override
public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1) {
        // whatever
        }
        }
        });*/

/*
    final JLabel lblRightClickMe = new JLabel("Right click me");        //create a label
lblRightClickMe.setBounds(152, 119, 94, 14);
final JPopupMenu jpm = new JPopupMenu("Hello");                     //create a JPopupMenu
        jpm.add("Right");jpm.add("Clicked");jpm.add("On");                  //add some elements
        jpm.add("This");jpm.add("Label");
        lblRightClickMe.setComponentPopupMenu(jpm);                         //set jpm as this label's popup menu

        lblRightClickMe.addMouseListener(new MouseAdapter() {
@Override
public void mouseClicked(MouseEvent arg0) {
        if (arg0.getButton() == MouseEvent.BUTTON3){                //get the mouse button
        jpm.show(arg0.getComponent(), arg0.getX(), arg0.getY());//set the position and show the popup menu
        }
        }
        });*/
