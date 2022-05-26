package World;

import Others.Comperator;
import Types.MapDirection;
import Types.Vector2d;
import Objects.Animal;
import Objects.Grass;

import java.util.*;

import static java.lang.Math.abs;

public class WorldMap{
    public int width;
    public int height;

    public double startEnergy;

    public double moveEnergy;

    public double plantEnergy;

    public int widthOfJungle;
    public int heightOfJungle;

    public Map<Vector2d, LinkedList<Animal>> animals;
    public Map<Vector2d, Grass> grasses;

    public int deadAnimalsPerDay;
    public int daysOfDeadAnimalsPerDay;
    public int newBabies;

    public void addAnimals(int AnimalsAtStart){
        for (int i = 0; i < AnimalsAtStart; i++) {
            Animal animal = new Animal(this, startEnergy);
            if (this.isOccupied(animal.getPosition())){
                i-=1;
            }
            else{
                this.place(animal);
            }
        }
    }


    public WorldMap(int width, int height, int plantEnergy, int moveEnergy, int startEnergy, double jungleRatio, int AnimalsAtStart) throws Exception {
        this.width=width;
        this.height=height;

        if (width<=0 || height<=0){
            throw new Exception("Incorrect width or height");
        }

        this.startEnergy=startEnergy;

        this.moveEnergy=moveEnergy;

        this.plantEnergy=plantEnergy;

        this.widthOfJungle=(int)(width*jungleRatio);
        this.heightOfJungle=(int)(height*jungleRatio);

        if (jungleRatio<0 || jungleRatio>1){
            throw new Exception("Incorrect jungle ratio");
        }

        animals = new HashMap<>();
        grasses = new HashMap<>();

        if (AnimalsAtStart>width*height){
            throw new Exception("Too many animals at start");
        }

        this.addAnimals(AnimalsAtStart);

        this.deadAnimalsPerDay=0;
        this.daysOfDeadAnimalsPerDay=0;
        this.newBabies=0;
    }


    public void runDay(){
        this.deadAnimalsPerDay=0;
        this.daysOfDeadAnimalsPerDay=0;
        this.removeDead();
        this.runAnimals();
        this.addDayToAnimals();
        this.eating(this.plantEnergy);
        this.multiply();
        this.addGrass();
        this.endOfDay(this.moveEnergy);
        this.sortByEnergy();
    }


    private void removeDead(){
        Map<Vector2d, LinkedList<Animal>> newAnimals = new HashMap<>();
        Vector2d vectorTMP = new Vector2d(-1,-1);
        for (LinkedList<Animal> animalsList : this.animals.values()){
            LinkedList<Animal> newAnimalsList = new LinkedList<>();
            for (Animal animal : animalsList){
                if (!animal.isDead()){ // zyje
                    vectorTMP=animal.getPosition();
                    newAnimalsList.add(animal);
                } else { // nie zyje
                    this.daysOfDeadAnimalsPerDay+=animal.daysOnWorld;
                    this.deadAnimalsPerDay++;
                }
            }
            if (!newAnimalsList.isEmpty()){ // cos zyje
                newAnimals.put(vectorTMP,newAnimalsList);
            }
        }
        this.animals=newAnimals;

    }


    private void addDayToAnimals(){
        for (LinkedList<Animal> animalsList : this.animals.values()) {
            for (Animal animal : animalsList) {
                animal.daysOnWorld+=1;
            }
        }
    }


    private void runAnimals() {
        Map<Vector2d, LinkedList<Animal>> newAnimals = new HashMap<>();
        for (LinkedList<Animal> animalsList : this.animals.values()){
            for (Animal animal : animalsList){
                animal.move();
                Vector2d position=animal.getPosition();
                if (newAnimals.get(position)==null){ // nie ma tam zwierzat
                    LinkedList<Animal> newAnimalsList = new LinkedList<>();
                    newAnimalsList.add(animal);
                    newAnimals.put(position,newAnimalsList);
                }else { //cos jest na tym polu
                    newAnimals.get(animal.getPosition()).add(animal);
                }
            }
        }
        this.animals=newAnimals;
    }


    private void eating(double energyPerGrass){
        for (Vector2d position : this.animals.keySet()){
            if (this.grasses.get(position)!=null){
                Comparator comp = new Comperator();
                this.animals.get(position).sort(comp);
                LinkedList<Animal> sameEnergyAnimals = new LinkedList<>(); // lista zwierzat majacych te sama energie
                int i=0;
                double maxEnergy=this.animals.get(position).getFirst().energy;
                double currentEnergy=this.animals.get(position).get(i).energy;
                while (maxEnergy==currentEnergy){
                    sameEnergyAnimals.add(this.animals.get(position).get(i));
                    i+=1;
                    if (i==this.animals.get(position).size()){
                        break;
                    }
                    currentEnergy=this.animals.get(position).get(i).energy;
                }
                for (Animal animal : sameEnergyAnimals){
                    animal.eat(energyPerGrass/sameEnergyAnimals.size()); // zwierzeta sie dziela
                }
                this.grasses.remove(position);
            }
        }
    }


    public void place(Animal animal){
        if (this.animals.get(animal.getPosition())==null){
            LinkedList<Animal> AnimalsList = new LinkedList<>();
            AnimalsList.add(animal);
            this.animals.put(animal.getPosition(),AnimalsList);
        }else{
            this.animals.get(animal.getPosition()).add(animal);
        }

    }

    private void multiply(){
        LinkedList<Animal> newAnimals = new LinkedList<>();
        for (LinkedList<Animal> animalsList : this.animals.values()){
            if (animalsList.size()>=2){ // 2 animale na jednym polu
                Comparator comp = new Comperator();
                animalsList.sort(comp);
                Animal father = animalsList.get(0);
                Animal mother = animalsList.get(1);
                Vector2d position = father.getPosition();
                if (mother.isHorny()){ // stworz dziecko
                    int isFull=1;
                    MapDirection tmp= MapDirection.NORTH;
                    for (int i=0; i<8; i++){ // sprawdz czy jest wolne miejsce
                        if (!this.isOccupied(position.add(tmp.toUnitVector()).mod(new Vector2d(this.width,this.height)))){
                            isFull=0;
                            break;
                        }
                    }
                    while (true){
                        Random rand = new Random();
                        int ort=abs(rand.nextInt())%(8);
                        tmp = MapDirection.NORTH;
                        for (int i=0; i<ort; i++){
                            tmp=tmp.next();
                        }
                        Vector2d newPosition= position.add(tmp.toUnitVector()).mod(new Vector2d(this.width,this.height));
                        if (!this.isOccupied(newPosition) || isFull==1){ // wolne miejsce
                            Animal child = new Animal(this, newPosition,(father.energy/4+mother.energy/4));
                            father.loseEnergy(father.energy/4);
                            mother.loseEnergy(mother.energy/4);
                            father.childrenBorn++;
                            mother.childrenBorn++;
                            father.newChildrenBorn++;
                            mother.newChildrenBorn++;
                            this.newBabies++;
                            child.inheritGenes(father,mother);
                            newAnimals.add(child);
                            break;
                        }
                    }
                }
            }
        }
        for (Animal animal : newAnimals){
            this.place(animal);
        }
    }


    public boolean isOccupied(Vector2d position) {
        if (this.grasses.get(position)!=null){
            return true;
        }
        return this.animals.get(position)!=null;
    }

    private void addGrass(){ // dodać rózne miejsca
        Random rand = new Random();

        // jungle
        int isFull=1;
        for (int i=0; i<this.width; i++){
            for (int j=0; j<this.height; j++){
                if (!this.isOccupied(new Vector2d(i,j)) && this.inJungle(new Vector2d(i,j))){
                    isFull=0;
                }
            }
        }
        if (isFull==0){
            while (true){
                int x = abs(rand.nextInt())%(this.width);
                int y = abs(rand.nextInt())%(this.height);
                Vector2d positionGrass1 = new Vector2d(x,y);
                if (!isOccupied(positionGrass1) && this.inJungle(positionGrass1)){
                    Grass grass = new Grass(positionGrass1);
                    this.grasses.put(positionGrass1,grass);
                    break;
                }
            }

        }
        
        // poza jungle
        isFull=1;
        for (int i=0; i<this.width; i++){
            for (int j=0; j<this.height; j++){
                if (i>=this.widthOfJungle || j>=this.heightOfJungle){
                    if (!this.isOccupied(new Vector2d(i,j)) && !this.inJungle(new Vector2d(i,j))){
                        isFull=0;
                    }
                }
            }
        }
        if (isFull==0){
            while (true){
                int a = abs(rand.nextInt())%(this.width);
                int b = abs(rand.nextInt())%(this.height);
                Vector2d positionGrass2 = new Vector2d(a,b);
                if (!isOccupied(positionGrass2) && !this.inJungle(positionGrass2)){
                    Grass grass = new Grass(positionGrass2);
                    this.grasses.put(positionGrass2,grass);
                    break;
                }
            }
        }
    }


    private void endOfDay(double energyLostPerDay){
        for (LinkedList<Animal> animalsList : this.animals.values()) {
            for (Animal animal : animalsList) {
                animal.loseEnergy(energyLostPerDay);
            }
        }
    }


    private void sortByEnergy(){
        Comparator comp = new Comperator();
        for (LinkedList<Animal> animalsList : this.animals.values()) {
            animalsList.sort(comp);
        }
    }



    public Object objectAt(Vector2d position){
        if (this.grasses.get(position)!=null){
            return this.grasses.get(position);
        }
        if (this.animals.get(position)!=null){
            return this.animals.get(position).getFirst();
        }
        return null;
    }


    public int animalsOnMap() {
        int numberOfAnimals = 0;
        for (LinkedList<Animal> animalsList : this.animals.values()) {
            numberOfAnimals+=animalsList.size();
        }
        return numberOfAnimals;
    }


    public int grassesOnMap(){
        return grasses.size();
    }


    public double energyOnMap(){
        double numberOfEnergy = 0;
        for (LinkedList<Animal> animalsList : this.animals.values()) {
            for (Animal animal : animalsList) {
                numberOfEnergy+=animal.energy;
            }
        }
        return numberOfEnergy;
    }


    public boolean inJungle(Vector2d position){
        int x = (this.width-this.widthOfJungle)/2;
        int y = (this.height-this.heightOfJungle)/2;
        return position.follows(new Vector2d(x, y)) && position.precedes(new Vector2d(x + this.widthOfJungle - 1, y + this.heightOfJungle - 1));
    }


    public int dominateGenotype(){
        int[] genes = new int[8];
        for (int i=0; i<8; i++){
            genes[i]=0;
        }
        for (LinkedList<Animal> animalsList : this.animals.values()) {
            for (Animal animal : animalsList) {
                genes[animal.dominateGen()]++;
            }
        }
        int gen=0;
        int maks=0;
        for (int i=0; i<8; i++){
            if (genes[i]>maks){
                gen=i;
                maks= genes[i];
            }
        }
        return gen;
    }


    public int childrenOfLivingParents(){
        int children=0;
        for (LinkedList<Animal> animalsList : this.animals.values()) {
            for (Animal animal : animalsList) {
                children+=animal.childrenBorn;
            }
        }
        return children;
    }
}
