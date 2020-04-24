import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MinesweeperGame implements ActionListener {


    @Override
    public void actionPerformed(ActionEvent e) {

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
