package menu;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

import stage.Stage;
import window.Main;
import window.window;
import IO.debugLog;

import common.point;

/* ステージの選択・実行を行うためのメニュー */
public class stageSelector extends selector{
    /* メンバ変数 */
    private mainMenu         belong;

    public Stage             selected;
    public boolean           index_is_run;
    public ArrayList<String> index_path;   //index番を実行する際のファイルパス

    /* コンストラクタ */
    /*
     * デフォルトコンストラクタ
     * 引数  ：なし
     */
    public stageSelector(){

    }

    public stageSelector(mainMenu _belong){
        init("stage\\Stage_selector.txt",      12, true,
                                      16, _belong);
    }

    /*
     * ファイルから生成するコンストラクタ
     * ファイルにはStageスクリプトファイルへのパスが1行に1つ入力されているものとする
     * 引数  ：ステージリストが保存されているファイルパス, 親オブジェクト
     */
    public stageSelector(String _stage_list_file_path, mainMenu _belong){
        init(_stage_list_file_path,      12, true,
                                16, _belong);
    }


    /* メソッド */
    public void update(GameContainer gc){
        //選択されている要素が実行中の場合
        if(index_is_run){
            selected.update();
            if(!index_is_run)
                selected = null;
            return;
        }

        //選択されている要素が実行中でない場合
        // 決定
        if(Main.user_input.get(0).ok){
            if(!Main.user_input.get(1).ok){
                selected   = new Stage(index_path.get(index), this);
                index_is_run = true;
                return;
            }
        }
        // キャンセル(この画面から抜ける)
        if(Main.user_input.get(0).cancel){
            if(!Main.user_input.get(1).cancel){
                reflesh();
            }
        }

        // 上下移動
        //  上下両方
        if(Main.user_input.get(0).up &&
           Main.user_input.get(0).down){

        }
        //  上
        else if(Main.user_input.get(0).up){
            if(!Main.user_input.get(1).up){
                decrement();
            }
        }
        else if(Main.user_input.get(0).down){
            if(!Main.user_input.get(1).down){
                increment();
            }
        }



    }

    /*
     * 描画
     * 引数  ：なし
     * 戻り値：なし
     */
    public void draw(Graphics g){
        //要素が実行中の場合
        if(index_is_run){
            selected.draw(g);
            return;
        }

        //要素が実行中でない場合
        g.setBackground(new Color(0xa8bb70)); //背景色設定

        super.draw(g, new point<Float>(window.SIZE.x.floatValue() / 2.0f, 150.0f));

        Color base_color = g.getColor();         //変更前の色
        Font  base_font  = g.getFont();          //変更前のフォント

        g.setColor(new Color(0x000000));

        if(ttf_m != null){
            g.setFont(ttf_m);
        }

        String str       = "OK : Select, cancel : Go back, up or down : Move corsor";
        float  str_width = get_string_width(str);
        //文字列を中央揃えに描画
        g.drawString(str, (window.SIZE.x.floatValue() / 2.0f) - (str_width / 4.0f), window.SIZE.y.floatValue() - font_size * 3.0f);

        g.setColor(base_color);
        g.setFont(base_font);

        if(Main._DEBUG){
            base_color = g.getColor();
            g.setColor(new Color(0x0000ff));
            g.drawLine(window.SIZE.x.floatValue() / 2.0f, 0.0f, window.SIZE.x.floatValue() / 2.0f, window.SIZE.y.floatValue());
            g.setColor(base_color);
        }
    }

    /*
     * この画面を抜ける際の処理
     * 引数  ：なし
     * 戻り値：なし
     */
    public void reflesh(){
        index = 0;
        belong.index_is_run = false;
    }

    /*
     * 初期化
     * 引数  ：各種データ
     * 戻り値：なし
     */
    protected void init(String _stage_list_file_path, int      _drawable, boolean  _do_loop,
                        float  _font_size           , mainMenu _belong  ){
        index = 0;

        // Stageファイルパスを要素に追加
        index_path = new ArrayList<String>();
        index_name = new ArrayList<String>();
        try{
            String         script_path = (Paths.get(_stage_list_file_path).getParent() == null)?
                                             "" : Paths.get(_stage_list_file_path).getParent().toString() + "\\";

            BufferedReader bRead = new BufferedReader(new FileReader(_stage_list_file_path));
            String str;
            while((str = bRead.readLine()) != null){
                str = window.file_path_corres(script_path + str);
                index_path.add(str);
                index_name.add(window.get_file_name_prefix(str));
            }
            bRead.close();
        }catch(Exception e){
            debugLog.getInstance().write_exception(e);
            e.printStackTrace();
        }

        length = index_name.size();

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

        index_is_run = false;

        belong = _belong;
    }
}
