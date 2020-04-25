import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MinesweeperGame extends MouseAdapter {
    private static int side = 10;
    private static int mines = 12;
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
        gameField = new GameField(250, 250, side);   //Creating new game field
        createGame();
    }

    @Override
    public void mouseClicked(MouseEvent e) {                    //Switch
        switch (e.getButton()) {
            case MouseEvent.BUTTON1: {                          //Getting a mouse button
                for (int i = 0; i < side; i++) {
                    for (int j = 0; j < side; j++) {
                        if (e.getSource() == gameField.buttons[i][j] && !gameField.buttons[i][j].isFlag
                                && !gameField.buttons[i][j].isOpen && !isGameStopped) {
                            if (gameField.buttons[i][j].isMine) {
                                gameField.buttons[i][j].setIcon(bomb);
                                gameField.buttons[i][j].isOpen = true;
                                gameField.buttons[i][j].setBackground(Color.white);
                                countMinesOnField--;
                                gameField.leftMines.setText(String.valueOf(countMinesOnField));
                            }else if(gameField.buttons[i][j].countMineNeighbors == 0){
                                gameField.buttons[i][j].isOpen = true;
                                gameField.buttons[i][j].setIcon(null);
                                gameField.buttons[i][j].setEnabled(false);
                            }else {
                                gameField.buttons[i][j].isOpen = true;
                                gameField.buttons[i][j].setIcon(i1);
                                gameField.buttons[i][j].setEnabled(false);
                            }
                        }
                    }
                }
            }
            break;
            case MouseEvent.BUTTON3: {
                for (int i = 0; i < side; i++) {
                    for (int j = 0; j < side; j++) {
                        if (e.getSource() == gameField.buttons[i][j] && !gameField.buttons[i][j].isOpen
                                && !gameField.buttons[i][j].isFlag && !isGameStopped) {
                            gameField.buttons[i][j].isFlag = true;
                            countFlags--;
                            gameField.buttons[i][j].setIcon(flag);
                        } else if (e.getSource() == gameField.buttons[i][j] && !gameField.buttons[i][j].isOpen
                                && gameField.buttons[i][j].isFlag && !isGameStopped) {
                            gameField.buttons[i][j].isFlag = false;
                            countFlags++;
                            gameField.buttons[i][j].setIcon(gameField.tile);
                        }
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

        for (int i = 0; i < side; i++) {
            for (int j = 0; j < side; j++) {
                if (e.getSource() == gameField.buttons[i][j] && SwingUtilities.isLeftMouseButton(e)
                        && !gameField.buttons[i][j].isOpen) {
                    gameField.reset.setIcon(open);
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getSource() == gameField.reset) {
            gameField.reset.setIcon(gameField.rs);
        }

        for (int i = 0; i < side; i++) {
            for (int j = 0; j < side; j++) {
                if (e.getSource() == gameField.buttons[i][j] && !gameField.buttons[i][j].isOpen) {
                    gameField.reset.setIcon(gameField.rs);
                }
            }
        }
    }

    public void createGame() {
        random = new Random();
        for (int i = 0; i < mines; i++) {
            while (true) {
                int x = random.nextInt(side);
                int y = random.nextInt(side);
                if (!gameField.buttons[x][y].isMine) {
                    gameField.buttons[x][y].isMine = true;
                    break;
                }
            }
        }
        for (int i = 0; i < side; i++) {
            for (int j = 0; j < side; j++) {                                //Adding Mouse Listener to all buttons
                if (gameField.buttons[i][j].isMine) countMinesOnField++;
                gameField.buttons[i][j].addMouseListener(this);
            }
        }
        gameField.reset.addMouseListener(this);                              //Adding Mouse Listener to the reset button
        countMineNeighbors();
        countMinesOnField = mines;
        countFlags = countMinesOnField;

    }

    public void countMineNeighbors() {
        for (int i = 0; i < side; i++) {
            for (int j = 0; j < side; j++) {
                if (gameField.buttons[i][j].isMine) continue;
                List<GameObject> result = getNeighbors(gameField.buttons[i][j],i,j);
                int cnt = 0;
                for(GameObject gameObject : result){
                    if(gameObject.isMine) cnt++;
                }
                gameField.buttons[i][j].countMineNeighbors = cnt;
            }
        }
    }

    public List<GameObject> getNeighbors(GameObject gameObject, int x, int y) {
        List<GameObject> result = new ArrayList<>();
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i < 0 || i >= side) {
                    continue;
                }
                if (j < 0 || j >= side) {
                    continue;
                }
                if (gameField.buttons[i][j] == gameObject) {
                    continue;
                }
                result.add(gameField.buttons[i][j]);
            }
        }
        return result;
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
