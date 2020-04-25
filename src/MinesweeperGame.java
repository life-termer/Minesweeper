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
            case MouseEvent.BUTTON1: {
                for (int i = 0; i < side; i++) {
                    for (int j = 0; j < side; j++) {
                        if (e.getSource() == gameField.buttons[i][j]){
                            openTile(gameField.buttons[i][j]);
                        }
                    }
                }
            }
            break;
            case MouseEvent.BUTTON3: {
                for (int i = 0; i < side; i++) {
                    for (int j = 0; j < side; j++) {
                        if (e.getSource() == gameField.buttons[i][j]){
                            markTile(gameField.buttons[i][j]);
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

    public void openTile(GameObject object){
        if(!object.isFlag && !object.isOpen && !isGameStopped){
            if(object.isMine){
                object.setIcon(bomb);
                object.isOpen = true;
                object.setBackground(Color.white);
                countMinesOnField--;
                gameField.leftMines.setText(String.valueOf(countMinesOnField));
            }else if (object.countMineNeighbors == 0){
                object.isOpen = true;
                object.setIcon(null);
                object.setEnabled(false);
                List<GameObject> objects = getNeighbors(object);
                for (GameObject gameObject : objects) {
                    if (!gameObject.isOpen) openTile(gameObject);
                }
            }else {
                object.isOpen = true;
                object.setIcon(i1);
                object.setEnabled(false);
            }
        }
    }

    public void markTile(GameObject object){
        if(!object.isFlag && !object.isOpen && !isGameStopped){
            object.isFlag = true;
            countFlags--;
            object.setIcon(flag);
        }else if (!object.isOpen && object.isFlag && !isGameStopped){
            object.isFlag = false;
            countFlags++;
            object.setIcon(gameField.tile);
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
                List<GameObject> result = getNeighbors(gameField.buttons[i][j]);
                int cnt = 0;
                for (GameObject gameObject : result) {
                    if (gameObject.isMine) cnt++;
                }
                gameField.buttons[i][j].countMineNeighbors = cnt;
            }
        }
    }

    public List<GameObject> getNeighbors(GameObject gameObject) {
        List<GameObject> result = new ArrayList<>();
        for (int i = gameObject.x - 1; i <= gameObject.x + 1; i++) {
            for (int j = gameObject.y - 1; j <= gameObject.y + 1; j++) {
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