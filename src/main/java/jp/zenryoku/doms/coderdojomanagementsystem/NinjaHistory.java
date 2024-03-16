package jp.zenryoku.doms.coderdojomanagementsystem;

/**
 * ニンジャの履歴情報
 */
public class NinjaHistory {
    private long ninja_id;
    private String play_date;
    private String todays_plan;
    private String todays_result;

    public long getNinja_id() {
        return ninja_id;
    }

    public void setNinja_id(long ninja_id) {
        this.ninja_id = ninja_id;
    }

    public String getPlay_date() {
        return play_date;
    }

    public void setPlay_date(String play_date) {
        this.play_date = play_date;
    }

    public String getTodays_plan() {
        return todays_plan;
    }

    public void setTodays_plan(String todays_plan) {
        this.todays_plan = todays_plan;
    }

    public String getTodays_result() {
        return todays_result;
    }

    public void setTodays_result(String todays_result) {
        this.todays_result = todays_result;
    }
}
