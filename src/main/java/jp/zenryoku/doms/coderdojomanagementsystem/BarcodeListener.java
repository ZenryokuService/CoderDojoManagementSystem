package jp.zenryoku.doms.coderdojomanagementsystem;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

/**
 * バーコードの入力を受け付ける。
 */
public class BarcodeListener implements EventHandler<KeyEvent> {
    private StringBuilder buf;
    private HelloController controller;
    public BarcodeListener(HelloController controller) {
        this.buf = new StringBuilder();
        this.controller = controller;
    }

    @Override
    public void handle(KeyEvent keyEvent) {
        String input = keyEvent.getCharacter();
        buf.append(input);
        if (buf.length() >= 13) {
            controller.login(buf.toString());
        }
    }

}
