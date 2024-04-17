package L6_8.GUI;

import L6_8.data.City;
import L6_8.data.Place;
import L6_8.data.Region;
import L6_8.data.Village;
import L6_8.db.DBWorker;

import javax.swing.table.AbstractTableModel;
import java.sql.SQLException;
import java.util.List;

public class MyTableModel extends AbstractTableModel{
    private List<Place> data;

    public MyTableModel(List<Place> places){
        this.data = places;
    }

    @Override
    public int getRowCount() {
        return DBWorker.getCount();
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex){
            case 0: return getPlace(rowIndex).getName();
            case 1: return getPlace(rowIndex).getArea();
            case 2: return getPlace(rowIndex).getPopulation();
            case 3: {
                Place p = getPlace(rowIndex);
                if(p instanceof City){
                    return ((City) p).getHead();
                }else{
                    return ((Village) p).getChief();
                }
            }
            case 4: {
                Place p = getPlace(rowIndex);
                if(p instanceof City){
                    return "Город";
                }else{
                    return "Деревня";
                }
            }
            case 5:
                try {
                    return DBWorker.getRegionName(getPlace(rowIndex).getRegion_id());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

        }
        return "default";
    }


    @Override
    public String getColumnName(int column){
        switch (column){
            case 0: return "Название";
            case 1: return "Площадь";
            case 2: return "Население";
            case 3: return "Мэр/Старейшина";
            case 4: return "Населенный пункт";
            case 5: return "Регион";
        }
        return "";
    }

    public void delete(int ind){
        this.data.remove(ind);
        this.fireTableDataChanged();
    }

    public void addCity(String name, double area, int population, String head) {
        data.add(new City(name, area, population, head));
        this.fireTableDataChanged();
    }

    public void addVillage(String name, double area, int population, String chief) {
        data.add(new Village(name, area, population, chief));
        this.fireTableDataChanged();
    }

    public String search(String str) throws SQLException {
        return DBWorker.search(str);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        boolean f = false;
        switch (columnIndex){
            case 0: getPlace(rowIndex).setName((String)aValue);
                    break;
            case 1:
                try{
                    getPlace(rowIndex).setArea(Double.parseDouble((String)aValue));
                    DBWorker.updatePlace(getPlace(rowIndex));
                    System.out.println("площадь изменилась");
                }catch (NumberFormatException e) {
                    System.out.println("площадь не изменилась");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case 2:
                try{
                    getPlace(rowIndex).setPopulation(Integer.parseInt((String)aValue));
                    DBWorker.updatePlace(getPlace(rowIndex));
                }catch (NumberFormatException e) {
                    System.out.println("площадь не изменилась");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case 3: {
                Place p = getPlace(rowIndex);
                if (p instanceof City) {
                    ((City) p).setHeadChief((String) aValue);
                } else {
                    ((Village) p).setHeadChief((String) aValue);
                }
                break;

            }
            case 4: break;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        switch (columnIndex){
            case 1: return true;
            case 2: return true;
        }
        return false;
    }

    public Place getPlace(int selectedRow) {
        return DBWorker.list.get(selectedRow);
    }

}
