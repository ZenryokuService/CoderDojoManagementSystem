package jp.zenryoku.doms.coderdojomanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;

/**
 * Ninjaがログインして入力する画面のコントローラ
 */
public class NinjaController {
    private boolean isResult;
    private boolean isSecondLoad;
    private static final int COLUMN_COUNT = 4;
    /** ニンジャデータ */
    private Ninja ninja;
    /** DAO */
    private Dao dao;
    @FXML
    private Button kettei;
    @FXML
    private Label message;
    @FXML
    private TextArea taskText;
    @FXML
    private TableView<NinjaHistory> historyTable;
    private Properties prop;

    public NinjaController() {
    }
    /** ニンジャをセット */
    public void setNinja(Ninja nin) {
        this.ninja = nin;
    }
    /** 今日のやることを登録 */
    @FXML
    public void addTask(ActionEvent event) {
        if ("".equals(this.taskText.getText())) {
            taskText.setStyle("-fx-control-inner-background: red");
            return;
        } else {
            taskText.setStyle("-fx-control-inner-background: white");
        }
        System.out.println("*** addTask(" + taskText.getText() + ") " + isResult + "***");
        if (isResult) {
            dao.updateTask(ninja, taskText.getText());
        } else {
            dao.insertTask(ninja, taskText.getText());
        }
        init();
        kettei.setText("終了");
        kettei.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                System.exit(-1);
            }
        });

    }

    public Ninja getNinja() {
        return ninja;
    }

    public Dao getDao() {
        return dao;
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }

    public void init() {
        if (this.ninja == null || this.dao == null) {
            System.out.println("Ninja Controllerがセットアップできていません。");
            System.exit(-1);
        }

        double areaWidth = Integer.parseInt((String) prop.get("width")) * 2/ 3;
        double colWidth = areaWidth / COLUMN_COUNT + 10;
        List<NinjaHistory> list = dao.selectNijaHistory(ninja);
        // 今日の目標が入力済みかどうか？
        welcome(list.get(0).getPlay_date());
        isSecondLoad = dao.isSecondLogin(ninja);
        historyTable.refresh();
        // 履歴の表示
        historyTable.setItems(FXCollections.observableArrayList(list));
        System.out.println("History : " + list.size());
        if (isSecondLoad) {
            System.out.println("isSecond");
            message.setText("今日はどうだった？");
        }

        ObservableList obList = historyTable.getColumns();
        if (obList.size() < 1) {
            obList.add(0, createColumn("ID", "ninja_id",  colWidth));
            obList.add(1, createColumn("日付", "play_date", colWidth));
            obList.add(2, createColumn("目標", "todays_plan", colWidth));
            obList.add(3, createColumn("結果", "todays_result", colWidth));
        }

    }

    /**
     * 引数の日付文字列が、今日と等しいか判定、フィールド変数にセット
     * @param date
     */
    private void welcome(String date) {
        DateTimeFormatter form = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        isResult = LocalDate.now().format(form).equals(date);
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
