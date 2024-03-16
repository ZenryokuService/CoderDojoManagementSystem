package jp.zenryoku.doms.coderdojomanagementsystem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * HelloControllerクラスをテストするクラス。
 * JUnitを使用して実装します。
 */
public class HelloControllerTest {
    /** テスト対象クラス */
    private static HelloController target;

    @BeforeAll
    private static void init() {
        // テスト対象クラスをインスタンス化する
        target = new HelloController();
    }

    @Test
    public void testInputCheck() {
        assertFalse(target.inputCheck("a"));
        assertFalse(target.inputCheck("あいう"));
        assertFalse(target.inputCheck(""));
        assertFalse(target.inputCheck(null));
        assertFalse(target.inputCheck("0123456"));
        assertTrue(target.inputCheck("1"));
        assertTrue(target.inputCheck("0"));
        assertTrue(target.inputCheck("12345"));
    }
}
