import javax.swing.*;

public class GameObject extends JButton{

    public boolean isMine;              //If button is bomb
    public int countMineNeighbors;      //How many mine neighbors button has
    public boolean isOpen;              //If button is open
    public boolean isFlag;              //If there is a flag on button

    public GameObject(boolean isMine){  //Button constructor
        new JButton();
        this.isMine = isMine;
    }

}
