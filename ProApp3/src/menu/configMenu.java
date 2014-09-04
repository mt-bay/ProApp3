package menu;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
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
    private mainMenu belong;

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
        ArrayList<String> elm_name = new ArrayList<String>();
        elm_name.add("music        volume");
        elm_name.add("sound effect volume");
        elm_name.add("confirm");

        init(elm_name,       8,    true,
                   16, _belong);
    }

    public void update(GameContainer gc){
        //決定操作
        if(Main.user_input.get(0).ok){
            if(Main.user_input.get(1).ok){
                switch(index){
                    case 2:
                        reflesh(true);
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

        int[] dw_index   = get_drawable_index(); //描画する要素インデックス
        Color base_color = g.getColor();         //変更前の色
        Font  base_font  = g.getFont();          //変更前のフォント

        //描画用バッファ変数
        String str;    //描画内容
        Color  dw_col; //描画色

        if(ttf_m != null){
            g.setFont(ttf_m);
        }

        for(int i = 0; i < dw_index.length; i++){
            // 各種データの更新
            str    = ((dw_index[i] == get_index_num())? "" : "") + index_name.get(dw_index[i]);
            dw_col = ( dw_index[i] == get_index_num())? Color.red : Color.black;

            g.setColor(dw_col);

            //float str_width = get_string_width(str);

            // 文字列を左揃えに描画
            g.drawString(str, _upper_left.x, _upper_left.y + font_size * (float)i);

            // 追加の情報がある場合、描画する
            switch(dw_index[i]){
                case 0:
                    str = bgm.getInstance().vol.toString() + "[%]";
                    break;
                case 1:
                    str = soundEffect.getInstance().vol.toString()  + "[%]";
                    break;
                default:
                    str = "";
                    break;
            }
            float str_width = get_string_width(str);
            g.drawString(str, _data_right - (str_width / 2.0f), _upper_left.y + font_size * (float)i);

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
        float str_width = get_string_width(str);
        g.drawString(str, window_center - (str_width / 4.0f), window.SIZE.y.floatValue() - font_size * (float)4);

        str       = "cancel : Go back without save";
        str_width = get_string_width(str);
        g.drawString(str, window_center - (str_width / 4.0f), window.SIZE.y.floatValue() - font_size * (float)3);

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
}
