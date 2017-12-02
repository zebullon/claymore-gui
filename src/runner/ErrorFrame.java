package runner;

import javax.swing.*;
import java.awt.*;

public class ErrorFrame extends JFrame{
    public ErrorFrame(String errMessage){
        super("Error: " + errMessage);
        this.setBounds(100,100,800,200);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GridLayout mainLayout = new GridLayout(1,1,2,2);

        Container container = this.getContentPane();
        container.setLayout(mainLayout);

        JLabel errLabel = new JLabel(errMessage);
        container.add(errLabel);
    }
}
