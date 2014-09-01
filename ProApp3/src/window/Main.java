/*
 * mainクラス
 * プログラムの起動・終了
 */

package window;

import java.awt.Font;
import java.util.ArrayList;

import menu.mainMenu;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

import IO.debugLog;
import IO.usr_input;

public class Main extends BasicGame {
    /* メンバ変数 */
    //ゲーム本体
    public static       AppGameContainer     app;                              //ゲームコンテナ
    //ユーザ入力
    public static       ArrayList<usr_input> user_input;                       //ユーザ入力
    public static       mainMenu             mm;
//    public static       Stage                stg_test;
    //デバッグ用
    public static       TrueTypeFont         m_ttf;

    /* 定数 */
    //ユーザ入力
    public static final int                  USER_INPUT_MAX = 5;               //ユーザインプットの最大記録数
    //デバッグ用
    public static final boolean              _DEBUG     = true;                //デバッグモードかどうか
    public static final float                FONT_SIZE  = 11.0f;               //デバッグ用の文字サイズ
    public static final Color                FONT_COLOR = new Color(0x777777); //デバッグ用の文字色

    public Main(String title) {
        super(title);
    }

    public static void main(String[] args) throws SlickException {


        try {
            app = new AppGameContainer(new Main("ゲーム名"));
            app.setDisplayMode(window.SIZE.x, window.SIZE.y, false);
            app.setTargetFrameRate(window.FPS);
            app.start();
        }
        catch (Exception e) {
            debugLog.getInstance().write_exception(e);
            e.printStackTrace();
        }

        //後処理
        debugLog.delInstance();

        return;
    }

    /*
     * 初期化
     * ファイルを読み込んで変数と関連付ける
     * 引数  ：なにこれ
     * 戻り値：なし
     * 例外  ：SlickException
     */
    @Override
    public void init(GameContainer gc) throws SlickException {

        gc.setShowFPS(false);

        get_input(gc);

        mm = new mainMenu(this);
//        stg_test = new Stage("stage\\test.stage");
    }

    /*
     * 内容の更新
     * 内部状態の更新(メインループ)
     * 引数  ：なにこれ
     * 戻り値：なし
     * 例外  ：SlickException
     */
    @Override
    public void update(GameContainer gc, int delta) throws SlickException {
        get_input(gc);

        mm.update();
//        stg_test.update();
    }

    /*
     * 描画
     * 画面描画(メインループ)
     * 引数  ：なにこれ
     * 戻り値：なし
     * 例外  ：SlickException
     */
    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
//        stg_test.draw(g);
        mm.draw(g);

        if(_DEBUG){
            Color prev_color = g.getColor();
            m_ttf = new TrueTypeFont(new Font("メイリオ", Font.BOLD, (int)FONT_SIZE), false);
            /*
            g.setFont(m_ttf);
            g.setColor(FONT_COLOR);
            g.drawString("player - location = " + stg_test.player_data.location.toString(), FONT_SIZE, FONT_SIZE);
            g.drawString("player - is_gnd   = " + stg_test.player_data.is_gnd, FONT_SIZE, FONT_SIZE * 2);
            g.drawString("user input[0] = " + user_input.get(0).toString(), FONT_SIZE, FONT_SIZE * 3);
            g.drawString("user input[1] = " + user_input.get(1).toString(), FONT_SIZE, FONT_SIZE * 4);

            g.setColor(prev_color);
            */
        }

    }

    /*
     * ユーザの入力を取得してメンバ変数更新
     * 引数  ：なし
     * 戻り値：なし
     */
    private static void get_input(GameContainer gc){
        if(user_input == null)
            user_input = new ArrayList<usr_input>();

        user_input.add(0, usr_input.input_to_usr_input(gc));

        //入力記録用の配列長が不足しているとき
        while(user_input.size() < USER_INPUT_MAX)
            user_input.add(0, usr_input.input_to_usr_input(gc));

        //入力記録用の配列長が超過しているとき
        while(user_input.size() > USER_INPUT_MAX)
            user_input.remove(user_input.size() - 1);

        return;
    }

    /*
     * 後処理
     * 引数  ：なし
     * 戻り値：なし
     */
    public void end(){
        debugLog.delInstance();
        app.exit();
    }
}