package menu;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.TrueTypeFont;

import window.Main;
import window.window;
import IO.bgm;
import IO.config;
import IO.debugLog;
import IO.soundEffect;

import common.point;

/*
 * コンフィグ用メニュークラス
 */

public class configMenu extends selector{
    /* メンバ変数 */
    private mainMenu  belong;

    private boolean   elm_is_run;

    private boolean startup_debug_flg;

    /* 定数 */
    private final int INPUT_NONE = 0;


    /*
     * デフォルトコンストラクタ
     * 引数  ：なし
     */
    public configMenu(){

    }

    /*
     * 親メニュー指定型コンストラクタ
     * 引数  ：親メニュー
     */
    public configMenu(mainMenu _belong){
        startup_debug_flg = Main._DEBUG;

        ArrayList<String> elm_name = new ArrayList<String>();
        elm_name.add("music        volume");
        elm_name.add("sound effect volume");
        elm_name.add("debug flag");

        elm_name.addAll(config.getInstance().name_toStringAL());

        elm_name.add("confirm");

        init(elm_name,       8,    true,
                   16, _belong);
    }

    /*
     * update
     * 引数  ：ゲームコンテナ
     * 戻り値：なし
     */
    public void update(GameContainer gc){
        if(elm_is_run){
            if(index >= 3){
                if(!(Main.user_input.get(0).ok &&
                     Main.user_input.get(1).ok)){
                    renew_key(config.name.index_to_name(index - 3), gc);
                }
            }
            return;
        }

        //決定操作
        if(Main.user_input.get(0).ok){
            if(!Main.user_input.get(1).ok){
                switch(index){
                    case 15:
                        reflesh(true);
                }

                if(index == 2){
                    if(!(Main.user_input.get(0).ok &&
                         Main.user_input.get(1).ok)){
                        Main._DEBUG = !Main._DEBUG;
                    }
                }

                if(index >= 3 && index <= 14){
                    elm_is_run = true;
                }
                return;
            }
        }

        //キャンセル操作
        if(Main.user_input.get(0).cancel){
            if(!Main.user_input.get(1).cancel){
                reflesh(false);
                return;
            }
        }

        //左右操作
        // 左右両方
        if(Main.user_input.get(0).left &&
           Main.user_input.get(0).right){

        }
        // 左
        else if(Main.user_input.get(0).left){
            if(!Main.user_input.get(1).left){
                int change_value = ((!Main.user_input.get(0).highsp)? 5 : 10);
                switch(index){
                    case 0:
                        bgm.getInstance().vol.add(-1 * change_value);
                        break;

                    case 1:
                        soundEffect.getInstance().vol.add(-1 * change_value);
                        break;
                }
            }
        }
        // 右
        else if(Main.user_input.get(0).right){
            if(!Main.user_input.get(1).right){
                int change_value = ((!Main.user_input.get(0).highsp)? 5 : 10);
                switch(index){
                    case 0:
                        bgm.getInstance().vol.add( 1 * change_value);
                        break;

                    case 1:
                        soundEffect.getInstance().vol.add( 1 * change_value);
                        break;
                }
            }
        }


        //上下操作
        // 上下両方
        if(Main.user_input.get(0).up   &&
           Main.user_input.get(0).down){

        }
        // 上
        else if(Main.user_input.get(0).up){
            if(!Main.user_input.get(1).up)
                decrement();
        }
        // 下
        else if(Main.user_input.get(0).down){
            if(!Main.user_input.get(1).down)
                increment();
        }
    }

    /*
     * 外部から呼び出される描画処理
     * 引数  ：なし
     * 戻り値：なし
     */
    public void draw(Graphics g){
        float padding = 200.0f;
        draw(g, new point<Float>(padding, 150.0f), window.SIZE.x.floatValue() - padding);
    }

    /*
     * 描画処理本体
     * 引数  ：なし
     * 戻り値：なし
     */
    public void draw(Graphics g, point<Float> _upper_left, float _data_right){
        g.setBackground(new Color(0xa8bb70));  //背景色設定

        Integer[] dw_index   = get_drawable_index(); //描画する要素インデックス
        Color     base_color = g.getColor();         //変更前の色
        Font      base_font  = g.getFont();          //変更前のフォント

        //描画用バッファ変数
        String str;    //描画内容
        Color  dw_col; //描画色

        if(ttf_m != null){
            g.setFont(ttf_m);
        }

        for(int i = 0; i < dw_index.length; i++){
            // 各種データの更新
            str    = index_name.get(dw_index[i]);
            dw_col = ( dw_index[i] == get_index_num())? Color.red : Color.black;

            g.setColor(dw_col);

            //float str_width = get_string_width(str);

            // 文字列を左揃えに描画
            g.drawString(str, _upper_left.x, _upper_left.y + font_size * (float)i);

            // 追加の情報がある場合、描画する
            switch(dw_index[i]){
                case  0:
                    str = bgm.getInstance().vol.toString() + "[%]";
                    break;
                case  1:
                    str = soundEffect.getInstance().vol.toString()  + "[%]";
                    break;
                case  2:
                    str = (Main._DEBUG)? "true" : "false";
                    break;
                case  3:
                    str = Input.getKeyName(config.getInstance().attack );
                    break;
                case  4:
                    str = Input.getKeyName(config.getInstance().jump   );
                    break;
                case  5:
                    str = Input.getKeyName(config.getInstance().highsp );
                    break;
                case  6:
                    str = Input.getKeyName(config.getInstance().change );
                    break;
                case  7:
                    str = Input.getKeyName(config.getInstance().left   );
                    break;
                case  8:
                    str = Input.getKeyName(config.getInstance().down   );
                    break;
                case  9:
                    str = Input.getKeyName(config.getInstance().up     );
                    break;
                case 10:
                    str = Input.getKeyName(config.getInstance().right  );
                    break;
                case 11:
                    str = Input.getKeyName(config.getInstance().quit   );
                    break;
                case 12:
                    str = Input.getKeyName(config.getInstance().restart);
                    break;
                case 13:
                    str = Input.getKeyName(config.getInstance().ok     );
                    break;
                case 14:
                    str = Input.getKeyName(config.getInstance().cancel );
                    break;
                default:
                    str = "";
                    break;
            }
            if(dw_index[i] == index &&
               elm_is_run             ){
                str = "please press key";
            }

            g.drawString(str, _data_right - ttf_m.getWidth(str), _upper_left.y + font_size * (float)i);

        }

        if(Main._DEBUG){
            g.setColor(new Color(0x0000ff));
            g.drawLine(_upper_left.x, 0.0f, _upper_left.x, window.SIZE.y.floatValue());
            g.drawLine(_data_right  , 0.0f, _data_right  , window.SIZE.y.floatValue());
        }

        g.setColor(new Color(0x000000));

        //文字列を中央揃えに描画
        float window_center = (window.SIZE.x.floatValue() / 2.0f);

        str       = "left or right : volume : +- 5[%], high speed + left or right : +- 10[%]";
        g.drawString(str, window_center - ttf_m.getWidth(str) / 2.0f, window.SIZE.y.floatValue() - font_size * (float)4);

        str       = "cancel : Go back without save";

        g.drawString(str, window_center - ttf_m.getWidth(str) / 2.0f, window.SIZE.y.floatValue() - font_size * (float)3);

        //フォント，色データを元に戻す
        g.setColor(base_color);
        g.setFont(base_font);

    }

    /*
     * この画面を抜ける際の処理
     * 引数  ：なし
     * 戻り値：なし
     */
    public void reflesh(boolean _confirm){
        //自身の変数を戻す
        index = 0;

        //内容の確定
        if(_confirm){
            try{
                config.getInstance().write();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

        else{
            try{
                config.getInstance().read();
                Main._DEBUG = startup_debug_flg;
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

        //親要素の要素変更
        belong.index_is_run = false;

    }

    /*
     * 初期化
     * 引数  ：各種データ
     * 戻り値：なし
     */
    protected void init(ArrayList<String> _index_name, int    _drawable , boolean  _do_loop,
                        float             _font_size , mainMenu  _belong ){
        index = 0;

        index_name = new ArrayList<String>();
        for(int i = 0; i < _index_name.size(); i++)
            index_name.add(_index_name.get(i));

        length = _index_name.size();

        drawable = _drawable;
        do_loop  = _do_loop;

        font_size = _font_size;
        try{
            ttf_m = new TrueTypeFont(new java.awt.Font(FONT_NAME, java.awt.Font.BOLD, (int)font_size), false);
        }
        catch(Exception e){
            debugLog.getInstance().write_exception(e);
            e.printStackTrace();
            ttf_m = null;
        }

        belong = _belong;
    }


    /*
     * キー入力更新
     * 引数  ：ゲームコンテナ
     * 戻り値：なし
     */
    private void renew_key(config.name _target, GameContainer gc){
        int key_code;
        if((key_code = check_input(gc)) != INPUT_NONE){
            config.getInstance().set_targeted(_target, key_code);
            elm_is_run = false;
        }
        return;
    }

    /*
     * 入力されているキーコードを調べる
     * 引数  ：ゲームコンテナ
     * 戻り値：戻り値：入力されたキーコード or
     *         INPUT_NONE(入力なし)
     */
    private int check_input(GameContainer gc){
        Input ip = gc.getInput();
        for(int i = 0; i < 255; ++i){
            if(ip.isKeyDown(i))
                return i;
        }
        return INPUT_NONE;
    }
}
