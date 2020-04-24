import javax.swing.*;

public class GameObject extends JButton{

    public boolean isMine;
    public int countMineNeighbors;
    public boolean isOpen;
    public boolean isFlag;

    public GameObject(boolean isMine){
        new JButton();
        this.isMine = isMine;
    }

}
