package menu;


import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

import IO.debugLog;

import common.point;


/*
 * セレクタクラス
 * カーソル操作などを行うためのクラス
 */
public class selector {
    /* メンバ変数 */
    //要素関係
    protected        int               index;      //選択中の要素
    protected        ArrayList<String> index_name; //要素名
    protected        int               length;     //選択し得る要素数
    protected        boolean           do_loop;    //カーソルが要素からあふれる場合、ループさせるか

    //描画関係
    protected        int               drawable;   //描画可能な要素数
    protected        float             font_size;  //文字列描画時のサイズ
    protected        String            font_name;  //描画時のフォント名
    protected        TrueTypeFont      ttf_m;      //描画時のフォント


    /* コンストラクタ */
    /*
     * デフォルトコンストラクタ
     * 引数  ：なし
     */
    public selector(){

    }

    /*
     * 各種データ指定型コンストラクタ
     * 引数  ：各種データ
     */
    public selector(ArrayList<String> _index_name, int    _drawable , boolean _do_loop,
                    float             _font_size , String _font_name){
        init(_index_name, _drawable , _do_loop,
             _font_size , _font_name);

    }

    /* メソッド */

    /*
     * 選択中の要素番号を返す
     * 引数  ：なし
     * 戻り値：選択中の要素番号
     */
    public final int get_index_num(){
        return index;
    }

    /*
     * 選択中の要素をargv[1]分進める
     * 引数  ：どれだけ進めるか
     * 戻り値：なし
     */
    public void add(int var){
        index += var;
        if(do_loop){
            if(index > get_max()){
                index   = index % length;
            }
            if(index < get_min()){
                index   = get_max() - Math.abs(index % length);
            }
            return;
        }
        else{

            if(index > get_max())
                index = get_max();
            if(index < get_min())
                index = get_min();
            return;
        }
    }

    /*
     * 選択要素のインクリメント
     * 引数  ：なし
     * 戻り値：なし
     */
    public void increment(){
        add(1);
    }

    /*
     * 選択要素のデクリメント
     * 引数  ：なし
     * 戻り値：なし
     */
    public void decrement(){
        add(-1);
    }

    /*
     * セレクタが持ちうる最大値を取得する
     * 引数  ：なし
     * 戻り値：セレクタが持ちうる最大値
     */
    public final int get_max(){
        return length - 1;
    }

    /*
     * セレクタが持ちうる最小値を取得する
     * 引数  ：なし
     * 戻り値：セレクタが持ちうる最小値
     */
    public final int get_min(){
        return 0;
    }

    /*
     * 描画
     * 引数  ：グラフィックス, 左上座標
     * 戻り値：なし
     */
    public void draw(Graphics g, point<Float> _upper_center){
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

            float str_width = get_string_width(str);

            //文字列を中央揃えに描画
            g.drawString(str, _upper_center.x - (str_width / 4.0f), _upper_center.y + font_size * (float)i);
        }

        //フォント，色データを元に戻す
        g.setColor(base_color);
        g.setFont(base_font);

    }


    /*
     * 初期化
     * 引数  ：各種データ
     * 戻り値：なし
     */
    protected void init(ArrayList<String> _index_name, int    _drawable , boolean _do_loop,
                        float             _font_size , String _font_name){
        index = 0;

        for(int i = 0; i < _index_name.size(); i++)
            index_name.add(new String(_index_name.get(i)));

        length = _index_name.size();

        drawable = _drawable;
        do_loop  = _do_loop;
        font_size = _font_size;

        font_name = _font_name;
        try{
            ttf_m = new TrueTypeFont(new java.awt.Font(font_name, 0, (int)font_size), false);
        }
        catch(Exception e){
            debugLog.getInstance().write_exception(e);
            e.printStackTrace();
            ttf_m = null;
        }
    }

    /* 文字列の長さを取得する
     * 引数  ：長さを取得したい文字列
     * 戻り値：長さ
     */
    private float get_string_width(String _source){
        float  str_wid   = 0.0f;
        char[] source_ca = _source.toCharArray();
        for(int i = 0; i < _source.length(); i++){
            //半角文字の場合
            if((source_ca[i] <= '\u007e')                            ||
               (source_ca[i] == '\u00a5')                            ||
               (source_ca[i] == '\u203e')                            ||
               (source_ca[i] >= '\uff61' && source_ca[i] <= '\uff9f'))
                str_wid += font_size;
            //全角文字の場合
            else{
                str_wid += font_size * 2;
            }
        }
        return str_wid;
    }


    /*
     * 現在選択されている要素を可能な限り中心とする描画可能な要素を取得する
     * (描画可能な要素数が偶数の場合、中心より1つ上が選択されている要素になるようにする)
     * 引数  ：なし
     * 戻り値：描画可能な要素インデックスが入っている配列
     */
    private int[] get_drawable_index(){
        ArrayList<Integer> dw_index = new ArrayList<Integer>(); //描画する要素

        if(drawable >= length){
            for(int i = 0; i < length; i++){
                dw_index.add(i);
            }
            int[] rt_index = new int[dw_index.size()]; //戻り値用配列
            for(int i = 0; i < dw_index.size(); i++)    //要素代入
                rt_index[i] = dw_index.get(i).intValue();

            return rt_index;

        }

        int draw_min = index - (drawable / 2) - ((drawable % 2 == 0)? 1 : 0); //描画可能な要素数の最小値導出
        dw_index.add(get_relative_index(draw_min + 0)); //最初の要素追加
        for(int i = 1; i < drawable; i++){
            if(dw_index.get(dw_index.size() - 1) != get_relative_index(draw_min + i)) //前要素と同じ値を取得していなければ
                dw_index.add(get_relative_index(draw_min + i)); //要素追加
        }

        int[] rt_index = new int[dw_index.size()]; //戻り値用配列
        for(int i = 0; i < dw_index.size(); i++)    //要素代入
            rt_index[i] = dw_index.get(i).intValue();

        return rt_index;
    }


    /*
     * 現在の要素から第1引数分移動した際のインデックスを取得
     * 引数  ：移動させる分の値
     * 戻り値：移動させた際の値
     */
    private int get_relative_index(int _var){
        int buf_index = index + _var; //要素バッファ

        if(buf_index > get_max())
            buf_index = get_max();
        if(buf_index < get_min())
            buf_index = get_min();

        /*
        if(do_loop){
            while(buf_index > get_max() &&
                  buf_index < get_min()){
                buf_index += ((buf_index < get_min())? 1 : -1) * (get_max() + 0);
            }
        }
        */
        return buf_index;
    }


}
