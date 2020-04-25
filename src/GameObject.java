import javax.swing.*;

public class GameObject extends JButton{

    public int x;
    public int y;
    public boolean isMine;              //If button is bomb
    public int countMineNeighbors;      //How many mine neighbors button has
    public boolean isOpen;              //If button is open
    public boolean isFlag;              //If there is a flag on button

    public GameObject(boolean isMine){  //Button constructor
        new JButton();
        this.isMine = isMine;
    }

    /*public void setMine(boolean isMine){
        this.isMine = isMine;
    }*/

}
