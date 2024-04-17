package L6_8.GUI;

import L6_8.data.City;
import L6_8.data.Place;
import L6_8.data.Region;
import L6_8.data.Village;
import L6_8.db.DBWorker;
import org.sqlite.core.DB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MainWindow extends JFrame{
    private JTable jTable;
    private MyTableModel myTableModel;
    private JButton buttonDelete;
    private JButton buttonAdd;
    private JButton buttonSearch;
    private JScrollPane jScrollPane;

    public MainWindow() throws SQLException {
       setTitle("Список мест");
       setSize(800, 400);
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        DBWorker.initDB();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    DBWorker.closeConnection();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

       JPanel panelFlow = new JPanel();
       panelFlow.setLayout(new FlowLayout(FlowLayout.LEFT));

       this.buttonAdd = new JButton("Добавить");
        panelFlow.add(buttonAdd);

       this.buttonSearch = new JButton("Найти");
        panelFlow.add(buttonSearch);

       this.buttonDelete = new JButton("Удалить место");
       panelFlow.add(buttonDelete);

       JPanel panelBorder = new JPanel(new BorderLayout());
       panelBorder.add(panelFlow, BorderLayout.NORTH);



       jTable = new JTable();
       DBWorker.getAllCity();
       DBWorker.getAllVillage();
       System.out.println("DBWorker.getAllCity();");

       myTableModel = new MyTableModel(DBWorker.list);
       jTable.setModel(myTableModel);

       this.jScrollPane = new JScrollPane(jTable);
       this.add(jScrollPane);

       panelBorder.add(jScrollPane, BorderLayout.CENTER);

        buttonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    DBWorker.deletePlace(myTableModel.getPlace(jTable.getSelectedRow()));
                    myTableModel.delete(jTable.getSelectedRow());
                }catch(IndexOutOfBoundsException | SQLException ex){
                    String message = "Выделите место";
                    JOptionPane.showMessageDialog(null, message);
                }
            }
        });

        buttonAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new JDialog();
                dialog.setModal(true);
                dialog.setSize(250, 310);
                dialog.setTitle("Добавление");
                dialog.setLocationRelativeTo(null);

                JPanel grid = new JPanel();
                grid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                GridLayout gridLayout = new GridLayout(7, 2, 0, 15);
                grid.setLayout(gridLayout);

                grid.add(new JLabel("Название:"));
                TextField textName = new TextField(20);
                grid.add(textName);


                grid.add(new JLabel("Площадь:"));
                TextField textArea = new TextField(20);
                grid.add(textArea);

                grid.add(new JLabel("Население:"));
                TextField textPopulation = new TextField(20);
                grid.add(textPopulation);

                grid.add(new JLabel("Глава:"));
                TextField textHeadChief = new TextField(20);
                grid.add(textHeadChief);


                grid.add(new JLabel("Тип:"));

                JComboBox comboBoxType = new JComboBox();

                comboBoxType.addItem("Город");
                comboBoxType.addItem("Деревня");

                grid.add(comboBoxType);

                grid.add(new JLabel("Регион:"));
                TextField textRegion = new TextField(20);
                grid.add(textRegion);

                grid.add(new JLabel(""));

                JButton buttonAddDialog = new JButton("Добавить");
                grid.add(buttonAddDialog);

                buttonAddDialog.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        textName.setBackground(Color.WHITE);
                        textArea.setBackground(Color.WHITE);
                        textPopulation.setBackground(Color.WHITE);
                        textHeadChief.setBackground(Color.WHITE);
                        textRegion.setBackground(Color.WHITE);
                        Color paleRed = new Color(251, 170, 170);
                        boolean areaEmpty = false;
                        boolean populationEmpty = false;


                        try {
                            double area = -1;
                            int population = -1;
                            String empty = "";
                            String name = textName.getText();
                            try{area = Double.parseDouble(textArea.getText());}
                            catch (NumberFormatException exception){
                                areaEmpty = true;
                            }

                            try{population = Integer.parseInt(textPopulation.getText());}
                            catch (NumberFormatException exception){
                                populationEmpty = true;
                            }

                            String headChief = textHeadChief.getText();
                            String region = textRegion.getText();
                            if(textName.getText().equals(empty)) throw new NumberFormatException();
                            if(areaEmpty || area <= -1) throw new NumberFormatException();
                            if(populationEmpty || area <= -1) throw new NumberFormatException();
                            if(textHeadChief.getText().equals(empty)) throw new NumberFormatException();
                            if(textRegion.getText().equals(empty)) throw new NumberFormatException();
                            if (comboBoxType.getSelectedItem().equals("Город")) {
                                boolean cityExists = false;
                                boolean regionExists = false;
                                int region_id = -1;
                                for (Place item : DBWorker.list) {
                                    if (item.name.equals(name)) {
                                        cityExists = true;
                                        JOptionPane.showMessageDialog(null, "Город с таким названием уже существует!");
                                        break;
                                    }
                                    if (DBWorker.getRegionName(item.region_id).equals(region)){
                                        regionExists = true;
                                    }
                                }

                                if (regionExists) {  //проверка на существование региона
                                    region_id = DBWorker.getRegionId(region);
                                    System.out.println("Регион уже существует");
                                }
                                else if(!regionExists) {
                                    List<Place> regions = new ArrayList<>();
                                    DBWorker.addRegion(new Region(-1, region, regions));
                                    region_id = DBWorker.getRegionId(region);
                                    System.out.println("Добавили новый регион");
                                }

                                if (!cityExists) {  //проверка на существование города
                                    //myTableModel.addCity(name, area, population, headChief);
                                    myTableModel.fireTableDataChanged();
                                    DBWorker.addCity(new City(-1, name, area, population, headChief, region_id));
                                }

                            } else if (comboBoxType.getSelectedItem().equals("Деревня")) {
                                boolean villageExists = false;
                                boolean regionExists = false;
                                int region_id = -1;
                                for (Place item : DBWorker.list) {
                                    if (item.name.equals(name)) {
                                        villageExists = true;
                                        JOptionPane.showMessageDialog(null, "Деревня с таким названием уже существует!");
                                        break;
                                    }
                                    if (DBWorker.getRegionName(item.region_id).equals(region)){
                                        regionExists = true;
                                    }
                                }
                                if (regionExists) {  //проверка на существование региона
                                    region_id = DBWorker.getRegionId(region);
                                    System.out.println("Регион уже существует");
                                }
                                else if(!regionExists) {
                                    List<Place> regions = new ArrayList<>();
                                    DBWorker.addRegion(new Region(-1, region, regions));
                                    region_id = DBWorker.getRegionId(region);
                                    System.out.println("Добавили новый регион");
                                }

                                if (!villageExists) {
                                    //myTableModel.addVillage(name, area, population, headChief);
                                    myTableModel.fireTableDataChanged();
                                    DBWorker.addVillage(new Village(-1, name, area, population, headChief, region_id));
                                }
                            }
                            dialog.dispose();
                        }catch(NumberFormatException exception){
                            String empty = "";
                            if(textName.getText().equals(empty)) textName.setBackground(paleRed);
                            if(textArea.getText().equals(empty) || areaEmpty) textArea.setBackground(paleRed);
                            if(textPopulation.getText().equals(empty) || populationEmpty) textPopulation.setBackground(paleRed);
                            if(textHeadChief.getText().equals(empty)) textHeadChief.setBackground(paleRed);
                            if(textRegion.getText().equals(empty)) textRegion.setBackground(paleRed);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        try {
                            DBWorker.list.clear();
                            DBWorker.getAllCity();
                            DBWorker.getAllVillage();
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
                dialog.getContentPane().add(grid);
                dialog.setVisible(true);
            }
        });

        Frame f = new Frame();

        buttonSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new JDialog();
                dialog.setModal(true);
                dialog.setSize(400, 120);
                dialog.setTitle("Поиск");
                dialog.setLocationRelativeTo(null);

                JButton buttonSearch = new JButton("Искать");

                dialog.add(buttonSearch);
                JTextField textSearch = new JTextField(30);
                dialog.add(textSearch);

                JPanel panel = new JPanel();
                JPanel panel1 = new JPanel();
                JPanel panel2= new JPanel();

                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.setAlignmentX(Component.CENTER_ALIGNMENT);

                panel1.add(textSearch);
                panel2.add(buttonSearch);
                panel.add(panel1);
                panel.add(panel2);
                dialog.getContentPane().add(panel);

                buttonSearch.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String text = textSearch.getText();
                        String message = null;
                        try {
                            message = myTableModel.search(text);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        ;
                        JOptionPane.showMessageDialog(null, message);
                    }
                });
                dialog.setVisible(true);
            }
        });
        setContentPane(panelBorder);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
