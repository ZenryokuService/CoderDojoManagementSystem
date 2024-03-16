package jp.zenryoku.doms.coderdojomanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class AddNinjaController {
    private static final int COLUMN_COUNT = 4;
    @FXML
    private Label title;
    @FXML
    private Label barcodeText;
    @FXML
    private TextField ninjaName;
    @FXML
    private TextField ninjaAge;
    @FXML
    private TextField ninjaArea;
    @FXML
    private Properties prop;

    @FXML
    public void selectNinja() {
        Dao dao = Dao.getInstance();
        List<Ninja> list = null;
        try {
            list = dao.selectNinja();
        } catch (SQLException e) {
            System.out.println("ニンジャの照会に失敗しました。");
            e.printStackTrace();
        }

    }
    @FXML
    public void addNinja(ActionEvent event) {
        String id = barcodeText.getText();
        String name = ninjaName.getText();
        String age = ninjaAge.getText();
        String area = ninjaArea.getText();
        System.out.println("*** AddButton ***");

        if (inputCheck(name, age, area)) {
            Ninja ninja = new Ninja(id, name, age, area);
            Dao.getInstance().addNinja(ninja);
            changeComponent(event);
        } else {
            title.setText("登録に失敗しました。");
        }
    }

    /** バーコードの入力値をセット */
    public void setID(String barcodeText) {
        this.barcodeText.setText(barcodeText);
    }

    /**
     * 入力チェックを行う。
     * @param name 名前
     * @param age 年齢
     * @param area 住んでいるエリア(地区)
     * @return true: 入力OK false: 入力に不備あり
     */
    public boolean inputCheck(String name, String age, String area) {
        // 0. 名前と年齢がNULLでない
        if (name == null || age == null) {
            return false;
        }
        // 1.文字列が０～９の数字かつ１～２文字であること
        if (age.matches("[0-9]{1,2}") == false) {
            return false;
        }

        return true;
    }

    /**
     * 表示コンポーネントを、ニンジャ一覧に変更する。
     * @param event
     */
    private void changeComponent(ActionEvent event) {
        VBox vbox = (VBox) ((Node) event.getTarget()).getParent().getParent();
        VBox pane = null;
        List<Ninja> list = null;
        // 画面の幅を取得
        Properties prop = HelloController.getProperty();
        double areaWidth = Integer.parseInt((String) prop.get("width")) * 2/ 3;
        double colWidth = areaWidth / COLUMN_COUNT + 10;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("loginChamp.fxml"));
            pane = fxmlLoader.load();
            list = Dao.getInstance().selectNinja();
            TableView<Ninja> table = (TableView<Ninja>) pane.getChildren().get(1);
            table.setItems(FXCollections.observableArrayList(list));
            ObservableList obList = table.getColumns();
            obList.add(0, createColumn("ID", "id", colWidth));
            obList.add(1, createColumn("名前", "name", colWidth));
            obList.add(2, createColumn("年齢", "age", colWidth));
            obList.add(3, createColumn("エリア", "area", colWidth));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (SQLException se) {
            se.printStackTrace();;
            System.exit(-1);
        } catch (ClassCastException ce) {
            ce.printStackTrace();;
            System.exit(-1);
        }

        VBox root = (VBox) vbox.getParent();
        root.getChildren().remove(vbox);
        root.getChildren().add(pane);

    }
    /** システム終了 */
    public void close() {
        System.out.println("終了します。");
        System.exit(0);
    }

    /**
     * TableViewのカラムを関連付けする。
     * @param viewName カラムの名前
     * @param propName Beenのプロパティ名
     * @param width 画面の幅
     * @return TableColumn
     */
    private TableColumn createColumn(String viewName, String propName, double width) {
        TableColumn col = new TableColumn(viewName);
        col.setMinWidth(width);
        col.setCellValueFactory(new PropertyValueFactory<>(propName));
        return col;
    }

    public Properties getProp() {
        return prop;
    }

    public void setProp(Properties prop) {
        this.prop = prop;
    }
}
