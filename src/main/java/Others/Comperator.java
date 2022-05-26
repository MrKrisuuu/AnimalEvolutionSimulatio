package Others;

import Objects.Animal;

import java.util.Comparator;

public class Comperator implements Comparator<Animal> {
    @Override
    public int compare(Animal o1, Animal o2) {
        if (o1.energy<o2.energy){
            return 1;
        } else if (o1.energy==o2.energy){
            return 0;
        }
        return -1;
    }
}
