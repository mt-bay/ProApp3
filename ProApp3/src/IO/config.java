package IO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.Input;

import window.Main;
import window.window;

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

    // 変数名関係
    public static enum name{
        attack ,
        jump   ,
        highsp ,
        change ,
        left   ,
        down   ,
        up     ,
        right  ,
        quit   ,
        restart,
        ok     ,
        cancel ;
        public static name index_to_name(int _index){
            switch(_index){
                case  0:
                    return attack;
                case  1:
                    return jump;
                case  2:
                    return highsp;
                case  3:
                    return change;
                case  4:
                    return left;
                case  5:
                    return down;
                case  6:
                    return up;
                case  7:
                    return right;
                case  8:
                    return quit;
                case  9:
                    return restart;
                case 10:
                    return ok;
                case 11:
                    return cancel;

                default:
                    return attack;
            }
        }
    };

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

                init(false, Input.KEY_Z    , Input.KEY_X   , Input.KEY_LSHIFT, Input.KEY_V    ,
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
        BufferedWriter bWrite = new BufferedWriter(new FileWriter(window.file_path_corres(fName)));
        // ファイルに書き込む内容
        String sWrite = String.format("%01X", ((Main._DEBUG)? 1 : 0));
        bWrite.write(sWrite);
        bWrite.newLine();
        
        sWrite = String.format("%08X", attack) + String.format("%08X", jump   ) + String.format("%08X", highsp) + String.format("%08X", change)
               + String.format("%08X", left  ) + String.format("%08X", down   ) + String.format("%08X", up    ) + String.format("%08X", right )
               + String.format("%08X", quit  ) + String.format("%08X", restart)
               + String.format("%08X", ok    ) + String.format("%08X", cancel );
        bWrite.write(sWrite);

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
        BufferedReader bRead = new BufferedReader(new FileReader(window.file_path_corres(fName)));
        
        // データ用に文字列分割
        String   fst       = bRead.readLine();
        String   str_debug = ((fst != "0")? "true" : "false");
        
        fst                = bRead.readLine();
        String[] str_key   = new String[KEY_MAX];
        for (int i = 0; i < str_key.length; i++)
            str_key[i] = fst.substring(i * 8, (i + 1) * 8);

        String[] str_sound = new String[2];
        fst                = bRead.readLine();
        for(int i = 0; i < str_sound.length; i++)
            str_sound[i] = fst.substring(i * 8, (i + 1) * 8);

        bRead.close();

        // データ挿入
        int index = -1;
        init(Boolean.parseBoolean(str_debug)       , Integer.parseInt(str_key[++index], 16), Integer.parseInt(str_key[++index], 16), Integer.parseInt(str_key[++index], 16),
        	 Integer.parseInt(str_key[++index], 16), Integer.parseInt(str_key[++index], 16), Integer.parseInt(str_key[++index], 16), Integer.parseInt(str_key[++index], 16),
        	 Integer.parseInt(str_key[++index], 16), Integer.parseInt(str_key[++index], 16), Integer.parseInt(str_key[++index], 16), Integer.parseInt(str_key[++index], 16),
        	 Integer.parseInt(str_key[++index], 16),
             Integer.parseInt(str_sound[0], 16), Integer.parseInt(str_sound[1], 16));

        return;
    }


    /*
     * 変数名を指定してセット
     * 引数  ：対象の名前
     * 戻り値：なし
     */
    public void set_targeted(name _target, int _key_code){
        switch(_target){
            case attack:
                attack  = _key_code;
                break;
            case change:
                change  = _key_code;
                break;
            case highsp:
                highsp  = _key_code;
                break;
            case jump:
                jump    = _key_code;
                break;
            case left:
                left    = _key_code;
                break;
            case down:
                down    = _key_code;
                break;
            case up:
                up      = _key_code;
                break;
            case right:
                right   = _key_code;
                break;
            case quit:
                quit    = _key_code;
                break;
            case restart:
                restart = _key_code;
                break;
            case ok:
                ok      = _key_code;
                break;
            case cancel:
                cancel  = _key_code;
                break;
        }
    }

    
    /*
     * 変数名toString
     * 引数  ：なし
     * 戻り値：変数名群
     */
    public ArrayList<String> name_toStringAL(){
        ArrayList<String> name_list = new ArrayList<String>();
        name_list.add("attack");
        name_list.add("jump");
        name_list.add("move - high speed");
        name_list.add("change");
        name_list.add("move - left");
        name_list.add("move - down");
        name_list.add("move - up");
        name_list.add("move - right");
        name_list.add("stage - quit");
        name_list.add("stage - restart");
        name_list.add("ok");
        name_list.add("cancel");

        return name_list;
    }

    
    /*
     * 使用ボタンセット
     * 引数：それぞれのデータ
     */
    private void init(boolean _debug,int _attack   , int _jump   , int _highsp, int _change,
                      int _left     , int _down   , int _up    , int _right ,
                      int _quit     , int _restart,
                      int _ok       , int _cancel ,
                      int _music_vol, int _se_vol ) {
    	
    	Main._DEBUG = _debug;

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
