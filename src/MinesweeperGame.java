import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class MinesweeperGame extends MouseAdapter implements Runnable {
    Thread t = null;
    private static int side = 10;
    private static int mines = 12;
    public static GameField gameField;                                             //Declaring game field
    private int countMinesOnField;
    private int countFlags;
    public static boolean isGameStopped;
    private int countClosedTiles;
    private static final Icon rs2 = new ImageIcon("icons/reset1.png");            //Another reset icons
    private static final Icon open = new ImageIcon("icons/open.png");
    private static final Icon dead = new ImageIcon("icons/dead.png");
    private static final Icon bomb = new ImageIcon("icons/bomb.png");             //Bomb icon
    private static final Icon flag = new ImageIcon("icons/flag.png");             //Flag icon
    private static final Icon i1 = new ImageIcon("icons/1.png");
    private static final Icon i2 = new ImageIcon("icons/2.png");
    private static final Icon i3 = new ImageIcon("icons/3.png");
    private static final Icon i4 = new ImageIcon("icons/4.png");
    private static final Icon i5 = new ImageIcon("icons/5.png");
    private static final Icon i6 = new ImageIcon("icons/6.png");
    private static final Icon i7 = new ImageIcon("icons/7.png");
    private static final Icon i8 = new ImageIcon("icons/8.png");
    private int time;

    public MinesweeperGame() {
        t = new Thread(this);
        gameField = new GameField(240, 240, side);   //Creating new game field
        for (int i = 0; i < side; i++) {
            for (int j = 0; j < side; j++) {                                //Adding Mouse Listener to all buttons
                gameField.buttons[i][j].addMouseListener(this);
            }
        }
        gameField.reset.addMouseListener(this);
        createGame();

        t.start();

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1: {
                if (e.getSource() == gameField.reset) {
                    //firstClick = false;
                    restart();
                    //minutes = 0;
                    gameField.score.setText("--   :   --");
                }
                for (int i = 0; i < side; i++) {
                    for (int j = 0; j < side; j++) {
                        if (e.getSource() == gameField.buttons[i][j]) {
                            openTile(gameField.buttons[i][j]);
                        }
                    }
                }
            }
            break;
            case MouseEvent.BUTTON3: {
                for (int i = 0; i < side; i++) {
                    for (int j = 0; j < side; j++) {
                        if (e.getSource() == gameField.buttons[i][j]) {
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
                        && !gameField.buttons[i][j].isOpen && !isGameStopped) {
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
                if (e.getSource() == gameField.buttons[i][j] && !gameField.buttons[i][j].isOpen
                        && !isGameStopped) {
                    gameField.reset.setIcon(gameField.rs);
                }
            }
        }
    }

    private void openTile(GameObject object) {
        if (!object.isFlag && !object.isOpen && !isGameStopped) {
            if (object.isMine) {
                object.setIcon(bomb);
                object.isOpen = true;
                countClosedTiles--;
                gameField.reset.setIcon(dead);
                gameOver();
                object.setBackground(Color.red);
            } else if (object.countMineNeighbors == 0) {
                object.isOpen = true;
                object.setIcon(null);
                object.setEnabled(false);
                countClosedTiles--;
                List<GameObject> objects = getNeighbors(object);
                for (GameObject gameObject : objects) {
                    if (!gameObject.isOpen) openTile(gameObject);
                }
            } else {
                object.isOpen = true;
                object.setEnabled(false);
                countClosedTiles--;
                switch (object.countMineNeighbors) {
                    case 1:
                        object.setIcon(i1);
                        break;
                    case 2:
                        object.setIcon(i2);
                        break;
                    case 3:
                        object.setIcon(i3);
                        break;
                    case 4:
                        object.setIcon(i4);
                        break;
                    case 5:
                        object.setIcon(i5);
                        break;
                    case 6:
                        object.setIcon(i6);
                        break;
                    case 7:
                        object.setIcon(i7);
                        break;
                    case 8:
                        object.setIcon(i8);
                        break;
                }
            }
        }
        if (countMinesOnField == countClosedTiles && !object.isMine) {
            win();
            gameField.reset.setIcon(rs2);
        }
    }

    private void markTile(GameObject object) {
        if (!object.isFlag && !object.isOpen && !isGameStopped) {
            object.isFlag = true;
            countFlags--;
            gameField.leftMines.setText(String.valueOf(countFlags));
            object.setIcon(flag);
        } else if (!object.isOpen && object.isFlag && !isGameStopped) {
            object.isFlag = false;
            countFlags++;
            gameField.leftMines.setText(String.valueOf(countFlags));
            object.setIcon(gameField.tile);
        }
    }

    private void createGame() {
        time = 0;
        isGameStopped = false;
        countClosedTiles = side * side;
        countMinesOnField = mines;
        countFlags = countMinesOnField;
        gameField.leftMines.setText(String.valueOf(countFlags));


        Random random = new Random();
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

        countMineNeighbors();
    }

    private void countMineNeighbors() {
        for (int i = 0; i < side; i++) {
            for (int j = 0; j < side; j++) {
                if (gameField.buttons[i][j].isMine) continue;
                List<GameObject> result = getNeighbors(gameField.buttons[i][j]);
                int cnt = 0;
                for (GameObject object : result) {
                    if (object.isMine) cnt++;
                }
                gameField.buttons[i][j].countMineNeighbors = cnt;
            }
        }
    }

    private List<GameObject> getNeighbors(GameObject gameObject) {
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

    private void gameOver() {
        showMines();
        isGameStopped = true;
    }

    private void win() {
        showMines();
        isGameStopped = true;
    }

    private void restart() {
        for (int i = 0; i < side; i++) {
            for (int j = 0; j < side; j++) {
                gameField.buttons[i][j].setEnabled(true);
                gameField.buttons[i][j].setIcon(gameField.tile);
                gameField.buttons[i][j].setBackground(Color.white);
                gameField.buttons[i][j].isOpen = false;
                gameField.buttons[i][j].isMine = false;
                gameField.buttons[i][j].isFlag = false;

            }
        }
        createGame();
    }

    private void showMines() {
        for (int i = 0; i < side; i++) {
            for (int j = 0; j < side; j++) {
                if (gameField.buttons[i][j].isMine) {
                    gameField.buttons[i][j].setIcon(bomb);
                    gameField.buttons[i][j].setBackground(Color.white);
                }
            }
        }
    }


    @Override
    public void run() {
        int minutes = 0;
        try {
            while (true) {
                if (time == 60) {
                    time = 0;
                    minutes++;
                }
                //if (time % 60 == 0 && time != 0) minutes++;
                while (isGameStopped) {
                    minutes = 0;
                    gameField.score.setText(gameField.score.getText());
                }
                if (minutes < 10 && time < 10) gameField.score.setText("0" + minutes + " : " +"0" + time);
                else if (minutes > 9 && time < 10) gameField.score.setText(minutes + " : " + "0" + time);
                else if (minutes < 10) gameField.score.setText("0" + minutes + " : " + time);
                else gameField.score.setText(minutes + " : " + time);
                time++;
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
