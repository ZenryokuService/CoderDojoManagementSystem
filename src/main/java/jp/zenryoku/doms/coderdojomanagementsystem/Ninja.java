package jp.zenryoku.doms.coderdojomanagementsystem;

import java.util.Date;

public class Ninja {
    private long id;
    private String name;
    private int age;
    private String area;

    /** デフォルトコンストラクタ */
    public Ninja(){
    }

    /**
     * コンストラクタ。バーコードの入力値を使用する場合。
     * @param id バーコードの入力値
     * @param name 手入力の名前
     * @param age 手入力の年齢
     * @param area 手入力のエリア(地区)
     */
    public Ninja(String id, String name, String age, String area) {
        this.id = Long.parseLong(id);
        this.name = name;
        if (age != null) {
            this.age = Integer.parseInt(age);
        }
        this.area = area;
    }

    /**
     * コンストラクタ。バーコードを使用しない場合。
     * @param name 手入力の名前
     * @param age 手入力の年齢
     * @param area 手入力のエリア(地区)
     */
    public Ninja(String name, String age, String area) {
        this.name = name;
        if (age != null) {
            this.age = Integer.parseInt(age);
        }
        this.area = area;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getArea() {
        return area;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
