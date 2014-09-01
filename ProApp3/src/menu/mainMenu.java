package menu;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

import window.Main;
import window.window;
import IO.debugLog;

import common.point;

/* メインメニュークラス */
public class mainMenu extends selector{
    /* メンバ変数 */
    public boolean       elm_is_run; //選択している要素が実行中か
    public Main          belong;
    public configMenu    config;     //コンフィグメニュー
    public stageSelector stage;      //ステージ選択メニュー

    /* コンストラクタ */
    public mainMenu(Main _belong){
        ArrayList<String> elm_name = new ArrayList<String>();
        elm_name.add("Stage select"); elm_name.add("config"); elm_name.add("exit");

        init(elm_name, 3, true, 24, "メイリオ", _belong);

    }


    /* メソッド */
    /*
     * 状態アップデート
     * 引数  ：なし
     * 戻り値：なし
     */
    public void update(){
        //選択中の要素を実行中の時
        if(elm_is_run){
            switch(index){
                case 0:
                    stage.update();
                    break;

                case 1:
                    config.update();
                    break;

                case 2:
                    belong.end();
                    break;

            }
            return;
        }

        //このメニューをカレントで実行中の時
        //indexの内容実行
        if(Main.user_input.get(0).ok){
            elm_is_run = true;
            return;
        }

        //indexの上下操作
        // 上
        if(Main.user_input.get(0).up  &&
           Main.user_input.get(0).down){

        }
        else if(Main.user_input.get(0).up){
            if(!Main.user_input.get(1).up)
                this.decrement();
        }
        else if(Main.user_input.get(0).down){
            if(!Main.user_input.get(1).down)
                this.increment();
        }
    }

    /*
     * 描画
     * 引数  ：なし
     * 戻り値：なし
     */
    public void draw(Graphics g){
        if(elm_is_run){
            switch(index){
                case 0:
                    stage.draw();
                    break;

                case 1:
                    config.draw();
                    break;

                case 2:
                    break;

            }
            return;
        }

        //このメニューをカレントで実行中の時
        g.setBackground(new Color(0xffffff));
        super.draw(g, new point<Float>(window.SIZE.x.floatValue() / 2.0f, 300.0f));
    }

    /*
     * 再使用向けの状態変更
     * 引数  ：なし
     * 戻り値：なし
     */
    public void reflesh(){
        elm_is_run = false;
    }

    /*
     * 初期化
     * 引数  ：各種データ
     * 戻り値：なし
     */
    protected void init(ArrayList<String> _index_name, int    _drawable , boolean _do_loop,
                        float             _font_size , String _font_name, Main    _belong ){
        index = 0;

        index_name = new ArrayList<String>();
        for(int i = 0; i < _index_name.size(); i++)
            index_name.add(_index_name.get(i));

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

        elm_is_run = false;

        belong = _belong;
    }

}
