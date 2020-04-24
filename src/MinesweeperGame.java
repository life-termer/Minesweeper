import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MinesweeperGame extends MouseAdapter {
    GameField gameField;                                             //Declaring game field
    Icon rs2 = new ImageIcon("icons/reset1.png");            //Another reset icon
    Icon open = new ImageIcon("icons/open.png");
    Icon dead = new ImageIcon("icons/dead.png");
    Icon openTile = new ImageIcon("icons/emptyTile.png");    //Empty tile icon
    Icon bomb = new ImageIcon("icons/bomb.png");             //Bomb icon
    Icon flag = new ImageIcon("icons/flag.png");             //Flag icon

    public MinesweeperGame() {
        gameField = new GameField(250, 250, 10, 10, 12);   //Creating new game field
        for (GameObject button : gameField.buttons) {         //Adding Action Listener to all buttons
            button.addMouseListener(this);
        }
        gameField.reset.addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {  //Switch
        if (e.getButton() == MouseEvent.BUTTON3) {
            for (GameObject button : gameField.buttons) {
                if (e.getSource() == button) {
                    button.setIcon(flag);
                    //button.setBackground(Color.red);
                }
            }
        }
        if (e.getButton() == MouseEvent.BUTTON1) {
            for (GameObject button : gameField.buttons) {
                if (e.getSource() == button) {
                    //button.setIcon(openTile);
                    //button.setBackground(Color.red);
                    button.setIcon(null);
                    button.setBackground(Color.white);
                    //button.setEnabled(false);
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getSource() == gameField.reset && SwingUtilities.isLeftMouseButton(e))
            gameField.reset.setIcon(rs2);

        for (GameObject button : gameField.buttons) {
            if (e.getSource() == button && SwingUtilities.isLeftMouseButton(e)) {
                gameField.reset.setIcon(dead);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getSource() == gameField.reset) {
            gameField.reset.setIcon(gameField.rs);
        }

        for (GameObject button : gameField.buttons) {
            if (e.getSource() == button) {
                gameField.reset.setIcon(gameField.rs);
            }
        }
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
