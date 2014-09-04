package IO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.newdawn.slick.Input;

import common.volume;

/* 設定ファイルクラス
 * シングルトンクラス
 */
public class config {
    /* メンバ変数 */
    //自インスタンス
    private static config instance;

    // ファイル関係
    private static final String fName  = "config.cfg";

    // キーアサイン関係
    public               int    attack;
    public               int    jump;
    public               int    highsp;
    public               int    change;
    public               int    left;
    public               int    down;
    public               int    up;
    public               int    right;

    public               int    quit;
    public               int    restart;

    public               int    ok;
    public               int    cancel;

    // 難易度関係

    /* 定数 */
    private final int           KEY_MAX = 12;  // アサインできるボタン数(最大)

    /* コンストラクタ */
    private config() {
        try {
            // ファイル読み込み, 変数割り当て
            read();

        }
        catch (Exception e) {
            // ファイル読み込みに失敗したら、コンフィグファイルを作成
            try {

                init(Input.KEY_Z    , Input.KEY_X   , Input.KEY_LSHIFT, Input.KEY_V    ,
                     Input.KEY_LEFT , Input.KEY_DOWN, Input.KEY_UP    , Input.KEY_RIGHT,
                     Input.KEY_Q    , Input.KEY_R   ,
                     Input.KEY_Z    , Input.KEY_X   ,
                                 100,            100);
                write();

            }
            catch (Exception e1) {
                // 未実装
                System.exit(1);
            }
        }
    }

    /* メソッド */
    /*
     * インスタンス取得
     */
    public static synchronized config getInstance(){
        if (instance == null) {
            if (instance == null) {
                instance = new config();
            }
        }
        return instance;
    }

    /*
     * 使用ボタンファイル書き込み
     * 引数：なし
     */
    public void write() throws IOException {
        BufferedWriter bWrite = new BufferedWriter(new FileWriter(fName));
        // ファイルに書き込む内容
        String sWrite = String.format("%08X", attack) + String.format("%08X", jump   ) + String.format("%08X", highsp) + String.format("%08X", change)
                      + String.format("%08X", left  ) + String.format("%08X", down   ) + String.format("%08X", up    ) + String.format("%08X", right )
                      + String.format("%08X", quit  ) + String.format("%08X", restart)
                      + String.format("%08X", ok    ) + String.format("%08X", cancel );
        bWrite.write(sWrite);

        System.out.println(bgm.getInstance().vol.get_volume());

        bWrite.newLine();
        sWrite = String.format("%08X", bgm.getInstance().vol.get_volume())
               + String.format("%08X", soundEffect.getInstance().vol.get_volume());
        bWrite.write(sWrite);

        bWrite.close();

        return;
    }

    /*
     * 使用ボタンファイル読み込み 引数：なし
     */
    public void read() throws IOException {
        // ファイル入力
        BufferedReader bRead = new BufferedReader(new FileReader(fName));
        String fst = bRead.readLine();

        // データ用に文字列分割
        String[] str_key = new String[KEY_MAX];
        for (int i = 0; i < str_key.length; i++)
            str_key[i] = fst.substring(i * 8, (i + 1) * 8);

        String[] str_sound = new String[2];
        fst                = bRead.readLine();
        for(int i = 0; i < str_sound.length; i++)
            str_sound[i] = fst.substring(i * 8, (i + 1) * 8);

        bRead.close();

        // データ挿入
        // 求：もっといい感じの代入方法
        init(Integer.parseInt(str_key[ 0] , 16), Integer.parseInt(str_key[ 1] , 16), Integer.parseInt(str_key[ 2], 16), Integer.parseInt(str_key[ 3], 16),
             Integer.parseInt(str_key[ 4] , 16), Integer.parseInt(str_key[ 5] , 16), Integer.parseInt(str_key[ 6], 16), Integer.parseInt(str_key[ 7], 16),
             Integer.parseInt(str_key[ 8] , 16), Integer.parseInt(str_key[ 9] , 16),
             Integer.parseInt(str_key[10] , 16), Integer.parseInt(str_key[11] , 16),
             Integer.parseInt(str_sound[0], 16), Integer.parseInt(str_sound[1], 16));

        return;
    }

    /*
     * 使用ボタンセット
     * 引数：それぞれのデータ
     */
    private void init(int _attack   , int _jump   , int _highsp, int _change,
                      int _left     , int _down   , int _up    , int _right ,
                      int _quit     , int _restart,
                      int _ok       , int _cancel ,
                      int _music_vol, int _se_vol ) {

        attack  = _attack;
        jump    = _jump;
        highsp  = _highsp;
        change  = _change;

        left    = _left;
        down    = _down;
        up      = _up;
        right   = _right;

        quit    = _quit;
        restart = _restart;

        ok      = _ok;
        cancel  = _cancel;

        bgm.getInstance().vol = new volume(_music_vol);
        soundEffect.getInstance().vol = new volume(_se_vol);
    }
}
