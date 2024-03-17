package jp.zenryoku.doms.coderdojomanagementsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import org.h2.jdbcx.JdbcDataSource;

/**
 * JavaFXによる画面作成と、H2DBのコネクション取得を行う。
 */
public class HelloApplication extends Application {
    /** プロパティファイルの値 */
    private static Properties prop;
    /** コントローラー */
    private HelloController controller;
    /** DAO */
    private Dao dao;


    /**
     * コンストラクタ
     */
    public HelloApplication() {
        // Propertyファイルの読み込み
        createProperties();
        //　DBコネクションの取得
        createConnection();

    }

    /** Propertyファイルの読み込み */
     private void createProperties() {
        try {
            prop = new Properties();
            InputStream in = getClass().getResourceAsStream("setting.properties");
            prop.load(new InputStreamReader(in, "UTF-8"));
        } catch (IOException e) {
            System.out.println("setting.propertiesが読み込めませんでした。");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /** DBコネクションを取得する */
    public void createConnection() {
        String dbUrl = prop.getProperty("dbUrl");
        String dbUser = prop.getProperty("dbUser");
        String dbPass = prop.getProperty("dbPass");
        boolean isDB = new File("~dojo.mv.db").exists();
        try {
            JdbcDataSource ds = new JdbcDataSource();
            ds.setURL(dbUrl);
            ds.setUser(dbUser);
            ds.setPassword(dbPass);
            dao = Dao.getInstance(ds.getConnection());
        } catch (SQLException se) {
            System.out.println("DB接続に失敗しました。"+ isDB);
            se.printStackTrace();
        } catch (Exception e) {
            System.out.println("想定外のエラーが起きました。" + isDB);
            e.printStackTrace();
        }

    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        int width = 0;
        int height = 0;
        try {
            width = Integer.parseInt(prop.getProperty("width"));
            height = Integer.parseInt(prop.getProperty("height"));
        } catch (NumberFormatException e) {
            System.out.println("プロパティファイルの「width」もしくは「height」の値が不適切です。");
            e.printStackTrace();
            System.exit(-1);
        }
        Scene scene = new Scene(fxmlLoader.load(), width, height);
        stage.setTitle(prop.getProperty("title"));
        stage.setScene(scene);
        stage.show();

        // 画面を表示してからコントローラーを取得する
        controller = fxmlLoader.getController();
        controller.setDao(dao);
        controller.setProp(prop);
        controller.setBarcodeRead();

        controller.initView();
    }

    public static void main(String[] args) {
        new HelloApplication();
        launch();
    }
}