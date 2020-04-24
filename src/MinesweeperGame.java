import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MinesweeperGame extends MouseAdapter implements ActionListener {
    GameField gameField;                                             //Declaring game field
    Icon rs2 = new ImageIcon("icons/reset1.png");            //Another reset icon
    Icon openTile = new ImageIcon("icons/emptyTile.png");    //Empty tile icon
    Icon bomb = new ImageIcon("icons/bomb.png");             //Bomb icon
    Icon flag = new ImageIcon("icons/flag.png");             //Flag icon

    public MinesweeperGame() {
        gameField = new GameField(250, 250, 10, 10, 12);   //Creating new game field
        for (GameObject button : gameField.buttons) {         //Adding Action Listener to all buttons
            button.addActionListener(this);
            button.addMouseListener(this);
        }
        gameField.reset.addActionListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            for (GameObject button : gameField.buttons) {
                if (e.getSource() == button) {
                    button.setIcon(flag);
                    //button.setBackground(Color.red);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == gameField.reset)
            gameField.reset.setIcon(rs2);
        for (GameObject button : gameField.buttons) {
            if (e.getSource() == button) {
                //button.setIcon(openTile);
                //button.setFocusPainted(false);
                //button.setBorderPainted(false);
                //button.setContentAreaFilled(false);
                button.setEnabled(false);
                //button.setBackground(Color.red);
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
