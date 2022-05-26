package Visualising;

import Objects.Animal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyLittleFrame extends JFrame implements ActionListener {
    JButton button;
    JTextField textField;
    Animal animal;

    public MyLittleFrame(Animal animal){
        this.animal=animal;
        this.setSize(350,300);
        this.setResizable(false);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setLayout(new FlowLayout());

        JLabel label = new JLabel();
        label.setText(genesString(animal.genes));
        label.setVerticalAlignment(1);
        label.setHorizontalAlignment(0);
        this.add(label);

        this.button = new JButton("Podaj n");
        this.button.addActionListener(this);

        this.add(this.button);

        this.textField = new JTextField();
        this.textField.setPreferredSize(new Dimension(250,40));
        this.add(this.textField);

        this.pack();
    }

    private String genesString(int[] genes){
        String genesName = "";
        for (int i=0; i<31; i++){
            genesName+=genes[i];
            genesName+=",";
        }
        genesName+=genes[31];
        return genesName;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==this.button){
            int n = Integer.parseInt(this.textField.getText());
            this.dispose();

            // ewaluowanie n dni
            int day=-1;
            animal.map.newBabies=0;
            animal.newChildrenBorn=0;
            for (int i=0; i<n; i++){
                this.animal.map.runDay();
                if (animal.isDead()){
                    day=i;
                    break;
                }
            }

            JFrame frame = new JFrame();
            frame.setVisible(true);
            frame.setSize(200,155);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);

            JLabel death = new JLabel();
            death.setBounds(0,0,200,50);
            if (day==-1){
                death.setText("Zwierze nie umarlo!");
            } else {
                death.setText("Zwierze umarlo w "+ day +" dniu.");
            }

            JLabel kids = new JLabel();
            kids.setBounds(0,60,200,50);
            kids.setText("Zwierze urodzilo "+ animal.newChildrenBorn +" dzieci.");

            JLabel children = new JLabel();
            children.setBounds(0,120,200,50);
            children.setText("Urodzilo sie "+ animal.map.newBabies +" potomkow.");

            frame.add(death);
            frame.add(kids);
            frame.add(children);

        }
    }
}
