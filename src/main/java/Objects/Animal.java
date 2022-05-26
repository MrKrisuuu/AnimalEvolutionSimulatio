package Objects;

import Types.MapDirection;
import Types.Vector2d;
import World.WorldMap;

import java.util.Arrays;
import java.util.Random;

import static java.lang.Math.abs;


public class Animal {

    public MapDirection orientation;
    public Vector2d position;
    public int[] genes;
    public final WorldMap map;
    public double energy;
    public int daysOnWorld;
    public int childrenBorn;
    public int newChildrenBorn;


    private int[] createGenes(){
        Random rand = new Random();
        int[] genes = new int[32];
        int gen;
        for (int i=0; i<32; i++){
            gen = abs(rand.nextInt())%8;
            genes[i]=gen;
        }
        Arrays.sort(genes);
        return genes;
    }


    private MapDirection createOrientation(){
        Random rand = new Random();
        MapDirection orientation = MapDirection.NORTH;
        int ort = abs(rand.nextInt())%8;
        for (int i=0; i<ort; i++){
            orientation = orientation.next();
        }
        return orientation;
    }


    private Vector2d createVector(WorldMap map){
        Random rand = new Random();
        int x = abs(rand.nextInt())%(map.width);
        int y = abs(rand.nextInt())%(map.height);
        return new Vector2d(x,y);
    }


    public Animal(WorldMap map, double energy) {
        this.genes = createGenes();
        this.repairGenes();
        this.orientation = createOrientation();
        this.position = createVector(map);
        this.map = map;
        this.energy = energy;
        this.daysOnWorld=0;
        this.childrenBorn=0;
    }


    public Animal(WorldMap map, Vector2d initialPosition, double energy){
        this.genes = createGenes();
        this.repairGenes();
        this.orientation = createOrientation();
        this.position=initialPosition;
        this.map=map;
        this.energy=energy;
        this.daysOnWorld=0;
        this.childrenBorn=0;
    }


    public String toString(){
        return "A";
    }


    public void move(){
        Random rand = new Random();
        int ort = abs(rand.nextInt())%32;
        for (int i=0; i<this.genes[ort]; i++){
            this.orientation = this.orientation.next();
        }
        this.position = this.position.add(this.orientation.toUnitVector()).mod(new Vector2d(this.map.width,this.map.height));
    }

    private void repairGenes(){
        while (true){
            int czy=1;
            for (int i=0; i<8; i++){
                int jest=0;
                for (int j=0; j<32; j++) {
                    if (this.genes[j] == i){
                        jest=1;
                        break;
                    }
                }
                if (jest==0){
                    czy=0;
                    Random rand = new Random();
                    int gen = abs(rand.nextInt())%(32);
                    this.genes[gen]=i;
                }
            }
            if (czy==1){
                break;
            }
        }
        Arrays.sort(this.genes);
    }

    public void inheritGenes(Animal father, Animal mother){
        Random rand = new Random();
        int gen1 = abs(rand.nextInt())%(32);
        int gen2 = abs(rand.nextInt())%(32-gen1)+gen1;
        for (int i=0; i<gen1; i++){ // pierwsza grupa
            this.genes[i]=father.genes[i];
        }
        for (int i=gen1; i<gen2; i++){ // druga grupa
            this.genes[i]=mother.genes[i];
        }
        for (int i=gen2; i<32; i++){ // trzecia grupa
            this.genes[i]=father.genes[i];
        }
        this.repairGenes();
    }


    public int dominateGen(){
        int[] numbersOfGenes = new int[8];
        for (int i=0; i<8; i++){
            numbersOfGenes[i]=0;
        }
        for (int i=0; i<32; i++){
            numbersOfGenes[this.genes[i]]++;
        }
        int gen=0;
        int maks=0;
        for (int i=0; i<8; i++){
            if (numbersOfGenes[i]>maks){
                gen=i;
                maks= numbersOfGenes[i];
            }
        }
        return gen;
    }



    public Vector2d getPosition() {
        return this.position;
    }

    public void eat(double energy) {
        this.energy+=energy;
    }

    public void loseEnergy(double energy){
        this.energy-=energy;
    }

    public boolean isDead(){
        return this.energy<=0;
    }


    public boolean isHorny(){
        return this.energy>this.map.startEnergy/2;
    }

    public boolean isLow(){
        return this.energy<this.map.startEnergy;
    }

    public boolean isOK(){
        return this.energy<2*this.map.startEnergy;
    }

    public boolean isGood(){
        return this.energy<5*this.map.startEnergy;
    }

}
