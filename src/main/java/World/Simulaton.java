package World;

import Visualising.MyFrame;

import static java.lang.Math.min;

public class Simulaton {
    public static void main(String[] args) throws InterruptedException {
        // dane wejsciowe
        int width = 50; // szerokosc mapy
        int height = 50; // wysokosc mapy
        int startEnergy = 50; // poczatkowa energia zwierzat
        int moveEnergy = 1; // energia stracona na ruch
        int plantEnergy = 200; // energia za rosline
        double jungleRatio = 0.2; // proporcje dzungli
        int AnimalsAtStart = 20; // ilosc zwierzat na poczatku
        int sizeOfWindow=500; // rozmiar okna
        int time=150; // predkosc symulacji



        int sizeOfElement = min(sizeOfWindow / width, sizeOfWindow / height);

        try {
            WorldMap map1 = new WorldMap(width, height, plantEnergy, moveEnergy, startEnergy, jungleRatio, AnimalsAtStart);
            MyFrame frame1 = new MyFrame(width, height, sizeOfElement, map1, sizeOfWindow);
            frame1.repaint();

            //Thread.sleep(2000);

            while (map1.animalsOnMap() > 0) {
                if (!frame1.pause){
                    map1.runDay();
                    frame1.actualize();
                    frame1.draw();

                }

                Thread.sleep(time);
            }
            frame1.draw();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}


