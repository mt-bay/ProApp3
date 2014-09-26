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
    protected               int               index;      //選択中の要素
    protected               ArrayList<String> index_name; //要素名
    protected               int               length;     //選択し得る要素数
    protected               boolean           do_loop;    //カーソルが要素からあふれる場合、ループさせるか

    //描画関係
    protected               int               drawable;   //描画可能な要素数
    protected               float             font_size;  //文字列描画時のサイズ
    protected               TrueTypeFont      ttf_m;      //描画時のフォント

    /* 定数 */
    protected static final  String            FONT_NAME = "Consolas";  //描画時のフォント名


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
                    float             _font_size ){
        init(_index_name, _drawable , _do_loop,
             _font_size );

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
                index   = get_min();
            }
            if(index < get_min()){
                index   = get_max();
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
     * 引数  ：グラフィックス, 中心上座標
     * 戻り値：なし
     */
    public void draw(Graphics g, point<Float> _upper_center){
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
                        float             _font_size ){
        index = 0;

        for(int i = 0; i < _index_name.size(); i++)
            index_name.add(new String(_index_name.get(i)));

        length    = _index_name.size();

        drawable  = _drawable;
        do_loop   = _do_loop;
        font_size = _font_size;


        try{
            ttf_m = new TrueTypeFont(new java.awt.Font(FONT_NAME, 0, (int)font_size), false);
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
    protected float get_string_width(String _source){
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
    protected Integer[] get_drawable_index(){
        ArrayList<Integer> dw_index = new ArrayList<Integer>(); //描画する要素
        int draw_min = index - ((drawable / 2) + ((drawable % 2 == 0)? 1 : 0)); //描画可能な要素番号の最小値導出
        int draw_max = draw_min + (drawable);                                   //描画可能な要素番号の最大値導出

        //描画要素数が配列長と同じか、それ以上の場合
        if(drawable >= length){
            for(int i = 0; i < length; i++){
                dw_index.add(i);
            }
        }
        //描画可能な要素番号の最小値が、配列の要素番号の最小値を下回っている場合
        else if(draw_min < get_min()){
            for(int i = 0; i < drawable; i++){
                dw_index.add(i);
            }
        }
        //描画可能な要素番号の最大値が、配列の要素番号の最大値を上回っている場合
        else if(draw_max > get_max()){
            for(int i = drawable - 1; i >= 0; i--){
                dw_index.add(get_max() - i);
            }
        }
        //どれでもない場合
        else{
            for(int i = 0; i < drawable; i++){
                dw_index.add(get_relative_index(i - (drawable / 2)));
            }
        }

        return (Integer[])dw_index.toArray(new Integer[0]);
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

        return buf_index;
    }


}
