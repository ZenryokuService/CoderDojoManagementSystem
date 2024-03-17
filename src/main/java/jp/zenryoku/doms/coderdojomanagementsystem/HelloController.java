package jp.zenryoku.doms.coderdojomanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class HelloController {
    private static final int COLUMN_COUNT = 4;
    /** タイトル部分のテキスト */
    @FXML
    private Label coderDojo;
    /** 読み仮名 */
    @FXML
    private Label area_ja;
    /** アルファベット */
    @FXML
    private Label area_en;
    @FXML
    private Button button;
    @FXML
    private Label textMeessage;
    @FXML
    private TextField inputText;
    @FXML
    protected Pane res_area;
    @FXML
    protected Label error_label;
    /** DAOクラス */
    private Dao dao;
    /** プロパティファイル */
    private static Properties prop;
    public HelloController() {
    }

    /** 画面の初期設定 */
    public void initView() {
        // タイトルの設定
        coderDojo.setText(prop.getProperty("title"));
        // 読み
        area_ja.setText(prop.getProperty("area_ja"));
        // アルファベット
        area_en.setText(prop.getProperty("dojoName_en"));

    }
    /**
     * 読み込んだ、プロパティファイルを取得する。
     * @return prop `Properties
     */
    public static Properties getProperty() {
        return prop;
    }

    /**
     * バーコード入力を受け取るための、イベントハンドラーを設定する。
     */
    public void setBarcodeRead() {
        inputText.addEventHandler(KeyEvent.KEY_TYPED, new BarcodeListener(this));
    }

    /**
     * DAOがセットされているか判定する
     * @return true: セット済み、false: セットしていない
     */
    private boolean isDao() {
        return dao != null;
    }

    /** DAOのセッター */
    public void setDao(Dao dao) {
        this.dao = dao;
    }

    /**
     * バーコード入力を使用しないログイン処理。
     */
    @FXML
    public void login() {
        String input = inputText.getText();
        System.out.println("Loginボタン: " + input);
        if (isDao() == false) {
            System.out.println("DAOが作成されていません。");
            System.exit(-1);
        }

        if (inputCheck(input) == false) {
            error_label.setText("入力が不適切です。[" + input + "]");
            return;
        }

        if ("0000".equals(input)) {
            // チャンピオンは「0000」でログイン
            loginChamp();
            return;
        }
        if (dao.loginNinja(input)) {
            System.out.println("*** ログイン画面の作成 ***");
            loginNinja(dao.getNinja());
        }

    }

    /**
     * バーコード入力を使用するログイン処理。
     * @param input
     */
    public void login(String input) {
        System.out.println("Loginボタン: " + input);
        if (isDao() == false) {
            System.out.println("DAOが作成されていません。");
            System.exit(-1);
        }

        // バーコード入力時はChampionログインはできない
        if (dao.firstLogin(input)) {
            // ニンジャ登録処理
            changeAddNinja(input);
            return;
        }
        // Ninjaオブジェクトは参照渡しで値をセット
        if (dao.loginNinja(input)) {
            System.out.println("*** ログイン画面の作成 ***");
            loginNinja(dao.getNinja());
        }

    }

    /**
     * ニンジャ登録画面を表示
     * @param input バーコードの入力値
     */
    @FXML
    public void changeAddNinja(String input) {
        System.out.println("*** changeAddNinja ***");
        // 入力コンポーネントを日活性にする
        inputText.setEditable(false);
        // ボタンの親の親
        VBox vbox = (VBox) res_area.getParent();
        VBox pane = null;
        AddNinjaController ctl = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("addNinja.fxml"));
            pane = fxmlLoader.load();
            ctl = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        vbox.getChildren().remove(res_area);
        vbox.getChildren().add(pane);
        ctl.setID(input);
        ctl.setProp(prop);
    }

    /**
     * 入力チェックを行う。以下の内容でチェック。
     * 　※「チェック」という言葉は抽象的でよくない。。。
     * 1. 文字列が０～９の数字であること
     *
     * @param in 入力(引数)の文字列
     * @return true: 入力値として適切 false: 入力値として不適切
     */
    public boolean inputCheck(String in) {
        // 0. 入力値がNULLでない
        if (in == null) {
            return false;
        }
        // 1.文字列が０～９の数字かつ１～５文字であること
        if (in.matches("[0-9]{1,5}")) {
            return true;
        }
        return false;
    }

    /**
     * ニンジャのログイン処理。
     * @param ninja ニンジャデータクラス
     */
    public void loginNinja(Ninja ninja) {
        Pane pane = null;
        VBox vb = (VBox) res_area.getParent();
        vb.getChildren().remove(res_area);
        NinjaController ctl = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("loginNinja.fxml"));
            pane = fxmlLoader.load();
            ctl = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        vb.getChildren().add(pane);
        ctl.setNinja(ninja);
        ctl.setDao(dao);
        ctl.setProp(prop);
        ctl.init();
    }

    /**
     * チャンピオンのログイン
     */
    public void loginChamp() {
        VBox pane = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("loginChamp.fxml"));
            pane = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        int listSize = res_area.getChildren().size();
        for (int i = 0; i < listSize - 1; i++) {
            res_area.getChildren().remove(i);
        }
        Node node = pane.getChildren().get(1);
        System.out.println(node.getClass().getSimpleName());
        TableView table = (TableView) node;

        try {
            List<Ninja> ninjaList = dao.selectNinja();
            System.out.println("List.size(): " + ninjaList.size());
            table.setItems(FXCollections.observableArrayList(ninjaList));
            // 画面の幅を取得
            double areaWidth = Integer.parseInt((String) prop.get("width")) * 2/ 3;
            double colWidth = areaWidth / COLUMN_COUNT + 10;

            ObservableList obList = table.getColumns();
            obList.add(0, createColumn("ID", "id", colWidth));
            obList.add(1, createColumn("名前", "name", colWidth));
            obList.add(2, createColumn("年齢", "age", colWidth));
            obList.add(3, createColumn("エリア", "area", colWidth));


        } catch (SQLException e) {
            System.out.println("*** ニンジャリストの取得に失敗しました。");
            e.printStackTrace();
            System.exit(-1);
        }
        res_area.getChildren().set(0, pane);
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

    public void setProp(Properties prop) {
        this.prop = prop;
    }
}