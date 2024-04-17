package L6_8.db;

import L6_8.data.City;
import L6_8.data.Place;
import L6_8.data.Region;
import L6_8.data.Village;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBWorker {
    public static final String PATH_TO_DB_FILE = "C:\\SQLite\\sqlite-tools-win-x64-3450200\\place.db";
    public static final String URL = "jdbc:sqlite:" + PATH_TO_DB_FILE;

    public static Connection conn;

    public static List<Place> list = new ArrayList<Place>();

    public static void initDB() {
        try {
            conn = DriverManager.getConnection(URL);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("Драйвер: " + meta.getDriverName());
                createDB();
            }
        } catch (SQLException ex) {
            System.out.println("Ошибка подключения к БД: " + ex);
        }
    }

    public static void closeConnection() throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }

    public static void createDB() throws SQLException {
        Statement statmt = conn.createStatement();
        statmt.execute("CREATE TABLE if not exists 'region' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'title' TEXT NOT NULL);");
        System.out.println("Таблица создана или уже существует.");
        statmt.execute("CREATE TABLE if not exists 'city' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' TEXT NOT NULL, 'area' REAL, population INTEGER, 'head' TEXT, 'region_id' INTEGER, FOREIGN KEY (region_id) REFERENCES region (id));");
        System.out.println("Таблица создана или уже существует.");
        statmt.execute("CREATE TABLE if not exists 'village' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' TEXT NOT NULL, 'area' REAL, population INTEGER, 'chief' TEXT, 'region_id' INTEGER, FOREIGN KEY (region_id) REFERENCES region (id));");
        System.out.println("Таблица создана или уже существует.");
    }

    public static String search(String str) throws SQLException {
        String name_s = str;
        boolean one = false;
        for(Place p : list) {
            if (p.name.equals(name_s)){
                if (p instanceof City) {
                    one = true;
                    return ((City) p).report();
                } else {
                    one = true;
                    return ((Village) p).report();
                }
            }
        }
        if(!one){
            return "Место не найдено";
        }
        return "Ошибка";
    }

    public static void addRegion(Region region) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(
                "INSERT INTO region(`title`) " +
                        "VALUES(?)");
        statement.setObject(1, region.getTitle());
        statement.execute();
        statement.close();
    }


    public static void addCity(City city) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(
                "INSERT INTO city(`name`, `area`,`population`,`head`,`region_id`) " +
                        "VALUES(?,?,?,?,?)");
        statement.setObject(1, city.getName());
        statement.setObject(2, city.getArea());
        statement.setObject(3, city.getPopulation());
        statement.setObject(4, city.getHead());
        statement.setObject(5, city.getRegion_id());
        statement.execute();
        statement.close();
    }

    public static void addVillage(Village village) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(
                "INSERT INTO village(`name`, `area`,`population`,`chief`,`region_id`) " +
                        "VALUES(?,?,?,?,?)");
        statement.setObject(1, village.getName());
        statement.setObject(2, village.getArea());
        statement.setObject(3, village.getPopulation());
        statement.setObject(4, village.getChief());
        statement.setObject(5, village.getRegion_id());
        statement.execute();
        statement.close();
    }

    public static int getRegionId(String rgName) throws SQLException {

        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT id FROM region WHERE region.title ='"+rgName+"'");
        int rgId = -1;
        rgId = resultSet.getInt(1);
        resultSet.close();
        statement.close();
        return rgId;
    }

    public static String getRegionName(int rgId) throws SQLException {

        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT title FROM region WHERE region.id ="+rgId);
        String rgName = "";
        rgName = resultSet.getString(1);
        resultSet.close();
        statement.close();
        return rgName;
    }

    public static void getAllCity() throws SQLException {
        Statement statement = conn.createStatement();

        ResultSet resultSet = statement.executeQuery("SELECT city.id, city.name, city.area, city.population, city.head, city.region_id, region.title FROM city JOIN region ON region.id = city.region_id");
        while (resultSet.next()) {
            list.add(new City(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getDouble("area"), resultSet.getInt("population"), resultSet.getString("head"), resultSet.getInt("region_id")));
        }
        resultSet.close();
        statement.close();
    }

    public static void getAllVillage() throws SQLException {
        Statement statement = conn.createStatement();

        ResultSet resultSet = statement.executeQuery("SELECT village.id, village.name, village.area, village.population, village.chief, village.region_id, region.title FROM village JOIN region ON region.id = village.region_id");
        while (resultSet.next()) {
            list.add(new Village(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getDouble("area"), resultSet.getInt("population"), resultSet.getString("chief"), resultSet.getInt("region_id")));
        }
        resultSet.close();
        statement.close();
    }

    public static void deletePlace(Place place) throws SQLException {
        Statement statement = conn.createStatement();
        if (place instanceof City){
            statement.execute("DELETE FROM city WHERE city.id ="+place.getId());
        }else if(place instanceof Village){
            statement.execute("DELETE FROM village WHERE village.id ="+place.getId());
        }
        System.out.println("deleted!");
        statement.close();
    }

    public static int getCount() {
        return list.size();
    }

    public static void updatePlace(Place place) throws SQLException {
        String nameTable = "";
        if (place instanceof City) {
            nameTable = "city";
        } else if (place instanceof Village) {
            nameTable = "village";
        }

        PreparedStatement statement = conn.prepareStatement(
                "UPDATE " + nameTable + " SET area = ?, population = ? WHERE " + nameTable + ".id = ?");
            statement.setObject(1, place.getArea());
            statement.setObject(2, place.getPopulation());
            statement.setObject(3, place.getId());
        statement.executeUpdate();
        System.out.println("update!");
        statement.close();
    }
}



