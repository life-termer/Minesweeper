import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class GameField extends JFrame {
    public GameObject[][] buttons;                              //Array of buttons
    private final JPanel panelMain;                             //Panel were will be buttons
    public JButton reset, leftMines, score;                     //Some top buttons
    private final JMenuBar menuBar;                             //Menu and menu Items
    public JMenuItem newItem;
    public JMenuItem beginner;
    public JMenuItem intermediate;
    public JMenuItem expert;
    public JMenuItem bestT;
    public JMenuItem exit;
    public Icon rs = new ImageIcon("icons/reset.png");     //Reset button icon
    public Icon tile = new ImageIcon("icons/tile.png");    //Close tile icon
    private Image icon = Toolkit.getDefaultToolkit().getImage("icons/minesweeper.png");//Game icon

    public GameField(int x, int y, int side) {
        super("Minesweeper");                                   //Game title
        buttons = new GameObject[side][side];                       //Initial array of buttons
        panelMain = new JPanel();                                   //Initial panel were game tiles will be
        panelMain.setBounds(10, 70, x, y);                    //Set bounds, color
        panelMain.setBackground(Color.gray);
        panelMain.setLayout(new GridLayout(side, side));            //Set GridLayoud, fill array with buttons
        for (int i = 0; i < side; i++) {
            for (int j = 0; j < side; j++) {
                buttons[i][j] = new GameObject(false);
                buttons[i][j].x = i;                                //Write button coordinates
                buttons[i][j].y = j;
                buttons[i][j].setIcon(tile);
                buttons[i][j].setBackground(Color.white);
                panelMain.add(buttons[i][j]);                       //Add buttons to panel
            }
        }

        //Initializing, setting positions, text, background and colors for top buttons
        leftMines = new JButton();
        score = new JButton("0");
        reset = new JButton();
        reset.setIcon(rs);
        leftMines.setBounds(10, 10, 50, 40);
        leftMines.setText("12");
        reset.setBounds((x / 2) - 10, 10, 40, 40);
        score.setBounds(x - 55, 10, 60, 40);
        leftMines.setBackground(Color.lightGray);
        score.setBackground(Color.lightGray);
        leftMines.setForeground(Color.blue);
        score.setForeground(Color.blue);
        leftMines.setBorderPainted(false);
        score.setBorderPainted(false);
        leftMines.setFocusPainted(false);
        score.setFocusPainted(false);

        reset.setOpaque(false);
        reset.setFocusPainted(false);
        reset.setBorderPainted(false);
        reset.setContentAreaFilled(false);

        //Add panel and buttons to the frame
        add(panelMain);
        add(leftMines);
        add(score);
        add(reset);

        //Initial menu, add menu buttons
        menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        gameMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(gameMenu);
        newItem = new JMenuItem("New");
        beginner = new JMenuItem("Beginner");
        intermediate = new JMenuItem("Intermediate");
        expert = new JMenuItem("Expert");
        bestT = new JMenuItem("Best Times");
        exit = new JMenuItem("Exit");
        gameMenu.add(newItem); gameMenu.addSeparator(); gameMenu.add(beginner);
        gameMenu.add(intermediate);gameMenu.add(expert);gameMenu.addSeparator();
        gameMenu.add(bestT); gameMenu.addSeparator();
        gameMenu.add(exit);


        //Setting color, size, etc to the frame
        setIconImage(icon);
        setLayout(null);
        setJMenuBar(menuBar);       //Add menu bar to the frame
        setBackground(Color.lightGray);
        setSize(x + 35, y + 140);
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
