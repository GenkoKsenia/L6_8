package L6_8.data;

import L6_8.db.DBWorker;

import java.sql.SQLException;

public class Village extends Place { //класс деревня, потомок place
    protected String chief; //старейшина
    public Village(String name, double area, int population, String chief) {
        super(name, area, population);
        this.chief = chief;
    }

    public Village() {
    }

    public Village(int id, String name, double area, int population, String chief, int region_id) {
        super(id, name, area, population, region_id);
        this.chief = chief;
    }

    @Override //переопределение метода report класса place
    public String report() throws SQLException {
        String info = "Деревня " + this.name + ": площадь - " + this.area + " км²" + ", население - " + this.population + ", староста - " + this.chief  + ", регион - " + DBWorker.getRegionName(this.region_id);
        return info;
    }

    public String getChief() {
        return chief;
    }

    @Override
    public void setHeadChief(String str) {
        this.chief = str;
    }
}
