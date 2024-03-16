package jp.zenryoku.doms.coderdojomanagementsystem;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.EventObject;

/**
 * チャンピオンのログイン画面。
 */
public class ChampController {
    @FXML
    private TableView<Ninja> ninjaList;
    @FXML
    private Button closeButton;
    @FXML
    private Button addNinjaButton;

    @FXML
    public void close() {
        System.out.println("終了します。");
        System.exit(0);
    }

    @FXML
    public void changeAddNinja(ActionEvent event) {
        // ボタンの親の親
        VBox vbox = (VBox) ((Node) event.getTarget()).getParent().getParent();
        VBox pane = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("addNinja.fxml"));
            pane = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        ObservableList obList = vbox.getChildren();
        int listSize = obList.size();
        System.out.println("Size=" + listSize);
        for (int i = 0; i < listSize - 1; i++) {
            System.out.println(obList.get(i).getClass().getSimpleName());
            obList.remove(i);
        }
        obList.remove(ninjaList);

        obList.add(pane);
    }
}
