package Visualising;

import javax.swing.*;
import java.awt.*;

public class MyLabel extends JLabel {

    public MyLabel(int y, int size){
        this.setBounds(0,y,500,75);
        this.setFont(new Font("Arial",Font.PLAIN,size/32));
    }
}
