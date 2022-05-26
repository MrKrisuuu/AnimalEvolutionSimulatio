package Visualising;

import Objects.Animal;
import Objects.Grass;
import Types.Vector2d;
import World.WorldMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;

public class MyFrame extends JFrame implements ActionListener, MouseListener {
    public JPanel[][] panels;

    public WorldMap map;

    public JLabel numberOfAnimals;
    public JLabel numberOfGrasses;
    public JLabel dominateGen;
    public JLabel averageEnergy;
    public JLabel averageLifespan;
    public JLabel averageOfKids;

    public double numberOfAnimalsSum;
    public double numberOfGrassesSum;
    public double averageEnergySum;
    public double averageLifespanSum;
    public double averageOfKidsSum;

    public int numberOfAnimalsInt;
    public int numberOfGrassesInt;
    public int[] dominateGenInt;
    public int averageEnergyInt;
    public int averageLifespanInt;
    public int averageOfKidsInt;

    public int sizeOfElement;
    public int width;
    public int height;
    public int size;

    public boolean pause;
    JButton stopButton;
    JButton exportButton;


    public MyFrame(int width, int height, int sizeOfElement, WorldMap map, int size){
        this.setVisible(true);
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setSize(width*sizeOfElement+16+size/8*5,height*sizeOfElement+39);
        this.setResizable(true);

        this.size=size;
        createJPanels(width,height,sizeOfElement,map);

        this.numberOfAnimals = new MyLabel(0,size);
        this.numberOfAnimals.setText("Liczba wszystkich zwierząt: ---");
        this.add(numberOfAnimals);

        this.numberOfGrasses = new MyLabel( size/10,size);
        this.numberOfGrasses.setText("Liczba wszystkich roślin: ---");
        this.add(numberOfGrasses);

        this.dominateGen = new MyLabel(size/10*2,size);
        this.dominateGen.setText("Dominujący gen: ---");
        this.add(dominateGen);

        this.averageEnergy = new MyLabel(size/10*3,size);
        this.averageEnergy.setText("Średni poziom energii zwierząt: ---");
        this.add(averageEnergy);

        this.averageLifespan = new MyLabel(size/10*4,size);
        averageLifespan.setText("Średnia długość życia zwierząt: ---");
        this.add(averageLifespan);

        this.averageOfKids = new MyLabel(size/10*5,size);
        this.averageOfKids.setText("Średnia liczba dzieci: ---");
        this.add(averageOfKids);

        this.map=map;

        this.sizeOfElement=sizeOfElement;
        this.width=width;
        this.height=height;

        //button
        this.pause=true;
        this.stopButton = new JButton();
        this.stopButton.setBounds(size/10*3,size/10*8,size/4,size/8);
        this.stopButton.addActionListener(this);
        this.stopButton.setFont(new Font("Arial",Font.PLAIN,size/25));
        this.stopButton.setText("START");
        this.stopButton.setFocusable(false);
        this.add(this.stopButton);

        //export button
        this.exportButton = new JButton();
        this.exportButton.setBounds(0,size/10*8,size/4,size/8);
        this.exportButton.addActionListener(this);
        this.exportButton.setFont(new Font("Arial",Font.PLAIN,size/25));
        this.exportButton.setText("EXPORT");
        this.exportButton.setFocusable(false);
        this.add(this.exportButton);

        //mouse
        this.addMouseListener(this);

        this.numberOfAnimalsSum=0;
        this.numberOfGrassesSum=0;
        this.averageEnergySum=0;
        this.averageLifespanSum=0;
        this.averageOfKidsSum=0;

        this.numberOfAnimalsInt=0;
        this.numberOfGrassesInt=0;
        this.averageEnergyInt=0;
        this.averageLifespanInt=0;
        this.averageOfKidsInt=0;

        this.dominateGenInt= new int[8];
        for (int i=0; i<8; i++){
            this.dominateGenInt[i]=0;
        }
    }


    private void createJPanels(int width, int height, int sizeOfElement, WorldMap map){
        JPanel[][] panels = new JPanel[width][height];
        for (int i=0; i<height; i++) {
            for (int j = 0; j < width; j++) {
                JPanel panel = new JPanel();
                if (map.inJungle(new Vector2d(i,j))){
                    panel.setBackground(MyColor.DIRT.toColor());
                } else {
                    panel.setBackground(MyColor.SAND.toColor());
                }
                //System.out.println(this.size);
                panel.setBounds(j*sizeOfElement+(this.size/8*5),sizeOfElement*(height-1)-i*sizeOfElement,sizeOfElement,sizeOfElement);
                panels[j][i]=panel;
                this.add(panel);
            }
        }
        this.panels=panels;
    }


    public void draw(){
        for (int i=0; i<this.map.height; i++){
            for (int j=0; j<this.map.width; j++){
                Vector2d position = new Vector2d(j,i);
                if (this.map.objectAt(position) instanceof Grass){
                    this.panels[j][i].setBackground(MyColor.GRASS.toColor());
                } else if (this.map.objectAt(position) instanceof Animal){
                    Animal animal = (Animal) this.map.objectAt(position);

                    if (!animal.isHorny()){ // nie jest zdolne do rozmnazania
                        this.panels[j][i].setBackground(MyColor.VERYLOWANIMAL.toColor());
                    } else if (animal.isLow()){ // ma mniej energii niz na poczatku
                        this.panels[j][i].setBackground(MyColor.LOWANIMAL.toColor());
                    } else if (animal.isOK()){ // jest OK ze zwierzeciem
                        this.panels[j][i].setBackground(MyColor.ANIMAL.toColor());
                    } else if (animal.isGood()){ // jest dobrze ze zwierzakiem
                        this.panels[j][i].setBackground(MyColor.HIGHANIMAL.toColor());
                    } else { // jest wysmienicie ze zwierzakiem
                        this.panels[j][i].setBackground(MyColor.VERYHIGHANIMAL.toColor());
                    }

                } else {
                    if (this.map.inJungle(position)){
                        this.panels[j][i].setBackground(MyColor.DIRT.toColor());
                    } else {
                        this.panels[j][i].setBackground(MyColor.SAND.toColor());
                    }
                }
            }
        }
    }


    public void actualize(){
        this.numberOfAnimals.setText("Liczba wszystkich zwierząt: " + this.map.animalsOnMap());
        this.numberOfGrasses.setText("Liczba wszystkich roślin: " + this.map.grassesOnMap());
        this.dominateGen.setText("Dominujący gen: "+ this.map.dominateGenotype());
        this.averageEnergy.setText("Średni poziom energii zwierząt: " + this.map.energyOnMap() / this.map.animalsOnMap());
        if (this.map.deadAnimalsPerDay > 0) {
            this.averageLifespan.setText("Średnia długość życia zwierząt: " + this.map.daysOfDeadAnimalsPerDay / (double)this.map.deadAnimalsPerDay);
        }
        this.averageOfKids.setText("Średnia liczba dzieci: "+ this.map.childrenOfLivingParents()/(double)this.map.animalsOnMap()/2);

        this.numberOfAnimalsSum+=this.map.animalsOnMap();
        this.numberOfGrassesSum+=this.map.grassesOnMap();
        this.averageEnergySum+=this.map.energyOnMap() / this.map.animalsOnMap();
        if (this.map.deadAnimalsPerDay > 0) {
            this.averageLifespanSum += (this.map.daysOfDeadAnimalsPerDay / (double)this.map.deadAnimalsPerDay);
        }
        this.averageOfKidsSum+=this.map.childrenOfLivingParents()/(double)this.map.animalsOnMap()/2;

        this.numberOfAnimalsInt++;
        this.numberOfGrassesInt++;
        this.averageEnergyInt++;
        if (this.map.deadAnimalsPerDay > 0) {
            this.averageLifespanInt++;
        }
        this.averageOfKidsInt++;

        this.dominateGenInt[this.map.dominateGenotype()]++;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==this.stopButton){
            if (this.pause){
                this.pause=false;
                this.stopButton.setText("STOP");
            } else {
                this.pause=true;
                this.stopButton.setText("START");
                for (LinkedList<Animal> animalsList : this.map.animals.values()){ // zwierzeta z dominujacym genem
                    if (animalsList.getFirst().dominateGen()==this.map.dominateGenotype()){
                        int x=animalsList.getFirst().getPosition().x;
                        int y=animalsList.getFirst().getPosition().y;
                        this.panels[x][y].setBackground(MyColor.DOMINATE.toColor());
                    }
                }
            }
        }
        if (e.getSource()==this.exportButton){
            if (this.pause) {
                PrintWriter zapis = null;
                try {
                    zapis = new PrintWriter("statystyki.txt");
                    zapis.println("Średnia liczba wszystkich zwierzat: "+this.numberOfAnimalsSum/this.numberOfAnimalsInt);
                    zapis.println("Średnia liczba wszystkich roślin: "+this.numberOfGrassesSum/this.numberOfGrassesInt);

                    int gen=0;
                    int maks=0;
                    for (int i=0; i<8; i++){
                        if (this.dominateGenInt[i]>maks){
                            gen=i;
                            maks=dominateGenInt[i];
                        }
                    }

                    zapis.println("Średni dominujący gen: "+gen);
                    zapis.println("Średnia średniego poziomu energii zwierząt: "+this.averageEnergySum/this.averageEnergyInt);
                    if (this.averageLifespanInt>0){
                        zapis.println("Średnia średniej długości życia zwierząt: "+this.averageLifespanSum/this.averageLifespanInt);
                    }
                    zapis.println("Średnia średniej liczby dzieci: "+this.averageOfKidsSum/this.averageOfKidsInt);
                    zapis.close();
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }

            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e){
        if (this.pause){ // kiedy jest pauza
            int x = e.getX();
            int y = e.getY();
            x-=size/8*5+8; // usuwanie lewej czesci
            y-=31;
            if (x<0 || y<0){
                return;
            }
            x/=this.sizeOfElement; // skalowanie
            y/=this.sizeOfElement; // skalowanie
            y=this.height-1-y;
            //this.panels[x][y].setBackground(Color.red);
            //System.out.print(x);
            //System.out.print(" ");
            //System.out.print(y);
            //System.out.println(this.map.objectAt(new Vector2d(x,y)));
            if (this.map.objectAt(new Vector2d(x,y)) instanceof Animal){
                Animal animal = (Animal) this.map.objectAt(new Vector2d(x,y));

                MyLittleFrame frame = new MyLittleFrame(animal);
            }
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // nic
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // nic
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // nic
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // nic
    }
}
