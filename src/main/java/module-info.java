module jp.zenryoku.doms.coderdojomanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires com.h2database;
    requires java.naming;

    opens jp.zenryoku.doms.coderdojomanagementsystem to javafx.fxml;
    exports jp.zenryoku.doms.coderdojomanagementsystem;
}