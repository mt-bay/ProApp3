package IO;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/*
 * ログファイル出力用クラス
 * シングルトン
 */
public class debugLog {
    /* 自インスタンス */
    private static debugLog       instance = new debugLog();
    /* メンバ変数 */
    private static BufferedWriter log;

    /* コンストラクタ */
    private debugLog() {
        try {
            log = new BufferedWriter(new FileWriter("DebugLog.txt"));
        }
        catch (IOException e) {
            System.out.println("make Log error");
            System.exit(1);
        }
    }

    /* メソッド */
    /*
     * インスタンス取得
     * 引数  ：なし
     * 戻り値：インスタンス
     */
    public static debugLog getInstance() {
        if (instance == null) {
            synchronized (debugLog.instance) {
                if (instance == null) {
                    instance = new debugLog();
                }
            }
        }
        return instance;
    }

    /*
     * インスタンス削除
     * 引数  ：なし
     * 戻り値：なし
     */
    public void delInstance() throws IOException {
        log.close();
    }

    /*
     * ログに追記
     * 引数  ：追加する内容
     * 戻り値：なし
     * 例外  ：IOException
     */
    public void write(String str) {
        try {
            log.write(str);
            log.newLine();
        }
        catch (IOException e) {
            //何もしません
        }
        return;
    }

}
