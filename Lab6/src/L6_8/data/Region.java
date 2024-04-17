package L6_8.data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Region {

    private static Scanner scanner = new Scanner(System.in);
    private int id;
    private String title = "название региона";
    private List <Place> places = new ArrayList();

    /*
    public Region(List<Place> allPlace) {
        places=allPlace;
    }
    */

    public Region(int id, String title, List<Place> allPlace) {
        this.id = id;
        this.title = title;
        this.places = allPlace;
    }


    public void add(Place p)
    {
        places.add(p);
    }

    public void print() throws SQLException {   for(Place p : places) {
            p.report();
        }
    }
    /*
    public static void delete()
    {
        System.out.println("Введите название места:");
        String name_s = scanner.next();
        boolean  one = false;

        for (int i = 0; i < places.size(); i++) {
            if (places.get(i).name.equals(name_s)) {
                places.remove(places.get(i));
                one = true;
            }
        }
        if(!one){
            System.out.println("Место не найдено");
        }
    }*/



    public Place getPlace(int index) {
        return places.get(index);
    }

    public String getTitle() {
        return title;
    }


    public void remove(int ind) {
        this.places.remove(ind);
    }


}
