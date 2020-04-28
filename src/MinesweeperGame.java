import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class MinesweeperGame extends MouseAdapter implements Runnable, ActionListener, Serializable {
    private transient Thread t = new Thread(this);
    private static final long serialVersionUID = 1L;
    private int side;
    private int mines;
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
    private GameType gameType = GameType.BEGINNER;
    private int time;
    private String beginnerResult;
    private String interResult;
    private String expertResult;
    private String bestResults;
    private int beginnerBest;
    private int interBest;
    private int expertBest;

    private boolean firstClick;

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == gameField.newItem) {
            gameField.setVisible(false);
            gameField.dispose();
            gameType = GameType.BEGINNER;
            initGame(240, 10, 10);
        }
        if (e.getSource() == gameField.beginner) {
            gameField.setVisible(false);
            gameField.dispose();
            gameType = GameType.BEGINNER;
            initGame(240, 10, 10);
        }
        if (e.getSource() == gameField.intermediate) {
            gameField.setVisible(false);
            gameField.dispose();
            gameType = GameType.INTERMEDIATE;
            initGame(400, 16, 40);
        }
        if (e.getSource() == gameField.expert) {
            gameField.setVisible(false);
            gameField.dispose();
            gameType = GameType.EXPERT;
            initGame(550, 22, 99);
        }
        if (e.getSource() == gameField.bestT) {
            Object[] options = {"OK", "Reset"};
            int n = JOptionPane.showOptionDialog(gameField, bestResults, "Best Mine Swappers",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options, options[0]);
            if (n == 1) {
                resetScore();
            }
        }

        if (e.getSource() == gameField.exit) {
            gameField.dispatchEvent(new WindowEvent(gameField, WindowEvent.WINDOW_CLOSING));
        }
    }

    public MinesweeperGame() {
        initGame(240, 10, 10);
    }

    public void initGame(int xy, int side, int mines) {
        load();
        this.side = side;
        this.mines = mines;
        gameField = new GameField(xy, xy, side);   //Creating new game field
        for (int i = 0; i < side; i++) {
            for (int j = 0; j < side; j++) {                                //Adding Mouse Listener to all buttons
                gameField.buttons[i][j].addMouseListener(this);
            }
        }
        gameField.reset.addMouseListener(this);
        gameField.newItem.addActionListener(this);
        gameField.beginner.addActionListener(this);
        gameField.intermediate.addActionListener(this);
        gameField.expert.addActionListener(this);
        gameField.exit.addActionListener(this);
        gameField.bestT.addActionListener(this);

        createGame();
        if (!t.isAlive())
            t.start();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1: {
                firstClick = true;
                if (e.getSource() == gameField.reset) {
                    restart();
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
        if (countMinesOnField == countClosedTiles && !object.isMine && !isGameStopped) {
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
        isGameStopped = false;
        firstClick = false;
        countClosedTiles = side * side;
        countMinesOnField = mines;
        countFlags = countMinesOnField;
        gameField.leftMines.setText(String.valueOf(countFlags));
        gameField.score.setText("000");

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
        time = 0;
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
        isGameStopped = true;
        showMines();
        bestTimes();
        save();
    }

    private void restart() {
        firstClick = false;
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
        try {
            while (time < 1000) {
                while (isGameStopped || !firstClick) {
                    gameField.score.setText(gameField.score.getText());
                }
                if (time < 10) gameField.score.setText("00" + time);
                else if (time < 100) gameField.score.setText("0" + time);
                else gameField.score.setText(String.valueOf(time));
                time++;
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void bestTimes() {
        String beginner = "";
        String inter = "";
        String expert = "";
        switch (gameType) {
            case BEGINNER: {
                if (time < beginnerBest) {
                    beginner = (String) JOptionPane.showInputDialog(
                            gameField,
                            "You set the best beginner time!\n"
                                    + "Enter your name:",
                            "Best Time!",
                            JOptionPane.PLAIN_MESSAGE
                    );
                }
                beginnerBest = time - 1;
                beginnerResult = "Beginner:            " + beginnerBest +
                        " seconds       " + beginner + "\n\n";
            }
            break;
            case INTERMEDIATE: {
                if (time < interBest) {
                    inter = (String) JOptionPane.showInputDialog(
                            gameField,
                            "You set the best intermediate time!\n"
                                    + "Enter your name:",
                            "Best Time!",
                            JOptionPane.PLAIN_MESSAGE
                    );
                }
                interBest = time - 1;
                interResult = "Intermediate:     " + interBest +
                        " seconds       " + inter + "\n\n";
            }
            break;
            case EXPERT: {
                if (time < expertBest) {
                    expert = (String) JOptionPane.showInputDialog(
                            gameField,
                            "You set the best expert time!\n"
                                    + "Enter your name:",
                            "Best Time!",
                            JOptionPane.PLAIN_MESSAGE
                    );
                    expertBest = time - 1;
                    expertResult = "Expert:                " + expertBest +
                            " seconds       " + expert + "\n\n";

                }
                break;
            }
        }
        bestResults = beginnerResult + interResult + expertResult;
    }

    private void resetScore() {
        beginnerResult = "Beginner:            999 seconds       Unknown\n\n";
        interResult = "Intermediate:     999 seconds       Unknown\n\n";
        expertResult = "Expert:                999 seconds       Unknown\n\n";
        beginnerBest = 999;
        interBest = 999;
        expertBest = 999;
        bestResults = beginnerResult + interResult + expertResult;
        save();
    }

    private void save() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("minesweeper.ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void load() {
        MinesweeperGame loadGame = null;
        try {
            FileInputStream fileInputStream = new FileInputStream("minesweeper.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            loadGame = (MinesweeperGame) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (loadGame != null) {
            this.beginnerResult = loadGame.beginnerResult;
            this.interResult = loadGame.interResult;
            this.expertResult = loadGame.expertResult;
            this.bestResults = loadGame.bestResults;
            this.beginnerBest = loadGame.beginnerBest;
            this.interBest = loadGame.interBest;
            this.expertBest = loadGame.expertBest;
        }else resetScore();

    }

    public static void main(String[] args) {
        new MinesweeperGame();
    }
}
