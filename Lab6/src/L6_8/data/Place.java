package L6_8.data;

import java.sql.SQLException;

public abstract class Place { //супер класс место
    protected int id; //id
    public String name; //название
    protected double area; //площадь
    protected int population; //население
    public int region_id; //id региона


    public Place(String name, double area, int population) {
        this.name = name;
        this.area = area;
        this.population = population;
    }

    public Place(int id, String name, double area, int population, int region_id) {
        this.id = id;
        this.name = name;
        this.area = area;
        this.population = population;
        this.region_id = region_id;
    }

    public Place() {
    }

    protected abstract String report() throws SQLException; //метод для вывода инфо

    protected abstract void setHeadChief(String str);

    public int getId(){
        return id;
    }
    public void setId(int i) {this.id = i;}

    public String getName(){
        return name;
    }
    public void setName(String n) {
        this.name = n;
    }

    public double getArea(){
        return area;
    }
    public void setArea(double a) {
        this.area = a;
    }

    public int getPopulation(){
        return population;
    }
    public void setPopulation(int p) {
        this.population = p;
    }

    public int getRegion_id(){
        return region_id;
    }
    public void setRegion_id(int r) {
        this.region_id = r;
    }

    public int getIdRegion(){
        return region_id;
    }
    public void setRegion(int ir) {this.region_id = ir;}
}

