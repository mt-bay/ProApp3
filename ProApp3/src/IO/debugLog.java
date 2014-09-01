package IO;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

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
    public static synchronized debugLog getInstance() {
        if (instance == null) {
            if (instance == null) {
                instance = new debugLog();
            }
        }
        return instance;
    }


    /*
     * インスタンス削除
     * 引数  ：なし
     * 戻り値：なし
     */
    public static void delInstance(){
        try{
            log.close();
        }catch(IOException e){
            System.out.println("debugLog close error");
        }
        return;
    }

    /*
     * ログに追記
     * 引数  ：なし         or
     *         追加する内容
     * 戻り値：なし
     */
    public void write(){
        write("");
    }
    public void write(String str) {
        try {
            log.write(str);
            log.newLine();
            log.flush();
        }
        catch (IOException e) {
            //何もしません
        }
        return;
    }

    /*
     * printStackTraceをログに追記
     * 引数  ：例外
     * 戻り値：なし
     */
    public void write_exception(Exception e){
        StringWriter sw = new StringWriter();
        PrintWriter  pw = new PrintWriter(sw);

        e.printStackTrace(pw);
        pw.flush();

        write(sw.toString());
    }
}
