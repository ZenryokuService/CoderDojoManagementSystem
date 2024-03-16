package jp.zenryoku.doms.coderdojomanagementsystem;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.chrono.HijrahChronology;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * DBコントロールするためのクラス。
 * シングルトン実装になっている。
 */
public class Dao {
    /** このクラスのインスタンス */
    private static Dao instance;
    /** DBコネクション */
    private Connection con;
    /** ニンジャデータ */
    private Ninja ninja;

    /**
     * プライベートコンストラクタ、new出来ません。
     * this.getInstance()でインスタンスを取得してください。
     */
    private Dao(Connection con) {
        if (this.con == null) {
            this.con = con;
        }
        try {
            // テーブル作成開始
            Statement stm = con.createStatement();
            createNinjaTable(stm);
            createDojoHistoryTable(stm);
        } catch (SQLException e) {
            System.out.println("テーブルの作成に失敗しました。");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * ニンジャがログインするための処理を行う。取得したニンジャデータはフィールド変数にセット。
     * this#getNinja()で取得する。
     *
     * @param input バーコードもしくは手入力のID
     * @return TRUEはログインOKなので情報を返す。そうでなければ返さない
     * @throws SQLException
     */
    public boolean loginNinja(String input) {
        boolean isLogin = false;
        String sql = "select * from ninja where id=" + input;

        try {
            Statement stm = con.createStatement();
            ResultSet result = stm.executeQuery(sql);
            Ninja ninja = null;
            if (result.next()) {
                ninja = new Ninja();
                // ID
                ninja.setId(result.getLong("id"));
                // 名前
                ninja.setName(result.getString("name"));
                // 年齢
                ninja.setAge(result.getInt("age"));
                // エリア
                ninja.setArea(result.getString("area"));
                this.setNinja(ninja);
                isLogin = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            terminated();
        }
        return isLogin;
    }

    /**
     * ニンジャテーブルを作成する
     * @param stm　java.sql.Statement
     * @throws SQLException
     */
    private void createNinjaTable(Statement stm) throws SQLException {
        String createTable = "create table if not exists ninja("
                + "id bigint not null auto_increment"
                + ", name varchar2(20) not null"
                + ", age smallint"
                + ", area varchar(10)"
                + ", primary key(id)"
                + ");";
        // ニンジャ情報テーブルの作成
        stm.executeUpdate(createTable);
    }

    /**
     * DOJO履歴テーブルの作成
     * @param stm　java.sql.Statement
     * @throws SQLException
     */
    private void createDojoHistoryTable(Statement stm) throws SQLException {
        String createTabke = "create table if not exists dojo_history("
                + "ninja_id bigint not null"
                + ", play_date date not null"
                + ", todays_plan varchar(100) not null"
                + ", todays_result varchar(100) not null"
                + ", primary key(ninja_id, play_date)"
                + ");";
        // ニンジャ情報テーブルの作成
        stm.executeUpdate(createTabke);
    }

    /**
     * 引数のバーコードの値が既に登録されているか確認。
     * @param id バーコードの入力値
     * @return true: 未登録 / false 登録済み
     */
    public boolean firstLogin(String id) {
        System.out.println("*** firstLogin ***");
        try {
            Statement stm = con.createStatement();
            String sql = "select count(*) from ninja where id=" + id;
            ResultSet result = stm.executeQuery(sql);
            if (result.next()) {
                int count = result.getInt(1);
                return count == 0;
            }
        } catch (SQLException e) {
            System.out.println("firstLoginで処理が失敗しました。");
            e.printStackTrace();
            System.exit(-1);
        }
        return false;
    }

    /**
     * 今日のやることを登録する。
     * @param ninja 登録するニンジャのデータ
     * @param task 入力した値
     */
    public void insertTask(Ninja ninja, String task) {
        DateTimeFormatter form = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String sql = "insert into dojo_history(ninja_id, play_date, todays_plan, todays_result)"
                + " values("
                + ninja.getId()
                + ", '" + LocalDate.now().format(form).toString() + "'"
                + ", '" + task + "', '')";
        try {
            Statement stm = con.createStatement();
            stm.executeUpdate(sql);
        } catch (SQLException se) {
            System.out.println("<<< 今日のやることを登録できませんでした。 >>>");
            se.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * 今日の結果を登録する。
     * @param ninja 登録するニンジャのデータ
     * @param result 入力した値
     */
    public void updateTask(Ninja ninja, String result) {
        DateTimeFormatter form = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String today = LocalDate.now().format(form).toString();
        String selectSql = "select * from dojo_history where ninja_id = " + ninja.getId()
                + " and play_date = '" + today + "' for update";

        try {
            Statement stm = con.createStatement();
            stm.executeQuery(selectSql);
            String update = "update dojo_history set todays_result='" + result + "'"
                    + " where ninja_id = " + ninja.getId() + " and play_date = '" + today + "'" ;
            stm.executeUpdate(update);
        } catch (SQLException se) {
            System.out.println("<<< 今日のやることを登録できませんでした。 >>>");
            se.printStackTrace();
            System.exit(-1);
        }
    }

    public List<NinjaHistory> selectNijaHistory(Ninja ninja) {
        List<NinjaHistory> list = new ArrayList<>();
        String sql = "select * from dojo_history where ninja_id = " + ninja.getId()
                + " order by play_date desc";
        try {
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                NinjaHistory hist = new NinjaHistory();
                hist.setNinja_id(rs.getLong("ninja_id"));
                hist.setPlay_date(rs.getString("play_date"));
                hist.setTodays_plan(rs.getString("todays_plan"));
                hist.setTodays_result(rs.getString("todays_result"));
                list.add(hist);
            }
        } catch (SQLException se) {
            System.out.println("ニンジャの履歴を取得できませんでした。");
            se.printStackTrace();
            System.exit(-1);
        }
        return list;
    }

    public boolean isSecondLogin(Ninja ninja) {
        DateTimeFormatter form = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String today = LocalDate.now().format(form).toString();
        String sql = "select * from dojo_history where ninja_id = " + ninja.getId()
                + " and play_date = '" + today + "'";
        try {
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            return rs.next();
        } catch (SQLException se) {
            System.out.println("ニンジャの履歴を取得できませんでした。");
            se.printStackTrace();
            System.exit(-1);
        }
        return false;
    }
    /**
     * DAOはシングルトン実装とする。
     * @param con java.sql.Connection
     * @return jp.zenryoku.doms.coderdojomanagementsystem.Dao
     */
    public static Dao getInstance(Connection con) {

        if (instance == null) {
            // DBコントロール、DAOの作成
            instance = new Dao(con);
        }
        return instance;
    }

    public static Dao getInstance() {
        if (instance == null) {
            System.out.println("インスタンス化されていません。");
            System.exit(-1);
        }
        return instance;
    }

    /**
     * ニンジャを追加する。
     * @param ninja Ninja情報クラス
     */
    public void addNinja(Ninja ninja) {
        String sql = "insert into ninja(id, name, age, area) values("
                + ninja.getId()
                + ", '" + ninja.getName() + "'"
                + ", " + ninja.getAge() + ""
                + ", '" + ninja.getArea() + "'"
                + ")";
        try {
            Statement stm = con.createStatement();
            stm.executeUpdate(sql);
        } catch (SQLException se) {
            System.out.println("SQLの実行に失敗しました。: " + sql);
            se.printStackTrace();
            System.exit(-1);
        }
    }

    public List<Ninja> selectNinja() throws SQLException {
        String sql = "select * from ninja";
        ResultSet result = null;
        List<Ninja> list = new ArrayList<>();
        try {
            Statement stm = con.createStatement();
            result = stm.executeQuery(sql);
            while (result.next()) {
                Ninja nin = setToNinja(result);
                list.add(nin);
            }
        } catch (SQLException e) {
            System.out.println("ニンジャのSELECTに失敗しました。");
            e.printStackTrace();
            System.exit(-1);
        } finally {
            result.close();
        }
        return list;
    }

    /**
     * ResultSetからNinjaクラスに変換する。
     * @param result ResultSet
     * @return Ninja
     * @throws SQLException
     */
    private Ninja setToNinja(ResultSet result) throws SQLException {
        Ninja ninja = new Ninja();
        ninja.setId(result.getLong("id"));
        ninja.setName(result.getString("name"));
        ninja.setAge(result.getInt("age"));
        ninja.setArea(result.getString("area"));
        return ninja;
    }
    public void terminated() {
        try {
            this.con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(-1);
        } finally {
            this.con = null;
        }
    }

    public Ninja getNinja() {
        return this.ninja;
    }

    public void setNinja(Ninja ninja) {
        this.ninja = ninja;
    }
}
