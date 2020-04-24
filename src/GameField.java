import javax.swing.*;
import java.awt.*;

public class GameField extends JFrame {
    public GameObject buttons[];
    private JPanel panelMain;
    public JButton reset, leftMines, score;


    public GameField(int x, int y, int row, int col, int mines) {
        buttons = new GameObject[row * col];
        panelMain = new JPanel();
        panelMain.setBounds(10, 70, x, y);
        panelMain.setBackground(Color.gray);
        panelMain.setLayout(new GridLayout(row, col));
        for(int i = 0; i < (row * col); i++){
            buttons[i] = new GameObject(false);
            panelMain.add(buttons[i]);
        }

        leftMines = new JButton("99");
        //leftMines.setEnabled(false);
        score = new JButton("99!");
        reset = new JButton("!!");
        leftMines.setBounds(10,10,70,40);
        reset.setBounds((x/2) - 10 ,10,40,40);
        score.setBounds(x-60,10,70,40);
        leftMines.setBackground(Color.GRAY);
        score.setBackground(Color.GRAY);

        add(panelMain);
        add(leftMines); add(score); add(reset);
        setLayout(null);
        setBackground(Color.lightGray);
        setSize(x + 35, y + 120);
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
