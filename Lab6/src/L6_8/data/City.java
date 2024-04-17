package L6_8.data;

import L6_8.db.DBWorker;

import java.sql.SQLException;

public class City extends Place { //класс город, потомок place
    protected String head; //мэр

    public City(String name, double area, int population, String head) {
        super(name, area, population);
        this.head = head;
    }

    public City(int id, String name, double area, int population, String head, int id_region) {
        super(id, name, area, population, id_region);
        this.head = head;
    }

    public City() {
    }

    @Override //переопределение метода report класса place
    public String report() throws SQLException { //переопределение метода report класса place
        String info = "Город " + this.name + ": площадь - " + this.area + " км²" + ", население - " + this.population + ", мэр - " + this.head + ", регион - " + DBWorker.getRegionName(this.region_id);
        return info;
    }
    public String getHead() {
        return head;
    }

    @Override
    public void setHeadChief(String str) {
        this.head = str;
    }
}

