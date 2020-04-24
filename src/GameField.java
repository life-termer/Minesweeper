import javax.swing.*;
import java.awt.*;

public class GameField extends JFrame {
    public GameObject buttons[];                            //Array of buttons
    private JPanel panelMain;                               //Panel were will be buttons
    public JButton reset, leftMines, score;                 //Some top buttons
    Icon rs = new ImageIcon("icons/reset.png");     //Reset button icon
    Icon tile = new ImageIcon("icons/tile.png");    //Close tile icon
    Image icon = Toolkit.getDefaultToolkit().getImage("icons/minesweeper.png");//Game icon


    public GameField(int x, int y, int row, int col, int mines) {
        super("Minesweeper");                           //Game title
        buttons = new GameObject[row * col];                //Initializing array of buttons
        panelMain = new JPanel();
        panelMain.setBounds(10, 70, x, y);
        panelMain.setBackground(Color.gray);
        panelMain.setLayout(new GridLayout(row, col));      //Setting GridLayoud, filling array with
        for(int i = 0; i < (row * col); i++){               //button and setting buttons icons
            buttons[i] = new GameObject(false);
            buttons[i].setIcon(tile);
            panelMain.add(buttons[i]);                      //Adding buttons to panel
        }

        //Initializing, setting positions, text, background and colors for top buttons
        leftMines = new JButton();
        //leftMines.setEnabled(false);
        score = new JButton("999");
        reset = new JButton();
        reset.setIcon(rs);
        leftMines.setBounds(10,10,50,40);
        leftMines.setText(String.valueOf(mines));
        reset.setBounds((x/2) - 10 ,10,40,40);
        score.setBounds(x-50,10,60,40);
        leftMines.setBackground(Color.GRAY);
        score.setBackground(Color.GRAY);
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
        //reset.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

        add(panelMain);                                 //Adding panel and buttons to the frame
        add(leftMines); add(score); add(reset);

        //Setting color, size, itc to the frame
        setIconImage(icon);
        setLayout(null);
        setBackground(Color.lightGray);
        setSize(x + 35, y + 120);
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
