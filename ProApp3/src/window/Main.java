/*
 * mainクラス
 * プログラムの起動・終了
 */

package window;

import java.awt.Font;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import menu.mainMenu;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

import IO.bgm;
import IO.debugLog;
import IO.soundEffect;
import IO.usr_input;

public class Main extends BasicGame {
    /* メンバ変数 */
    //ゲーム本体
    public static       AppGameContainer     app;                                    //ゲームコンテナ
    //ユーザ入力
    public static       ArrayList<usr_input> user_input;                             //ユーザ入力
    public static       mainMenu             mm;                                     //メインメニュー
    //デバッグ用
    public static       TrueTypeFont         debug_ttf;                              //デバッグ用フォント

    /* 定数 */
    //ユーザ入力
    public static final int                  USER_INPUT_MAX   = 5;                   //ユーザインプットの最大記録数
    //デバッグ用
    public static final boolean              _DEBUG           = false;               //デバッグモードかどうか
    public static final float                DEBUG_FONT_SIZE  = 11.0f;               //デバッグ用の文字サイズ
    public static final Color                DEBUG_FONT_COLOR = new Color(0x777777); //デバッグ用の文字色


    public Main(String title) {
        super(title);
    }

    public static void main(String[] args) throws SlickException {
        try {
            app = new AppGameContainer(new Main("アイランドバスター"));
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

        gc.setShowFPS((_DEBUG)?true : false);

        //各種音量の初期設定

        //
        debug_ttf = new TrueTypeFont(new Font("consolas", 0, (int)DEBUG_FONT_SIZE), false);
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
        mm.update(gc);
//        stg_test.update();

        gc.setMusicVolume(bgm.getInstance().vol.get_nomalization_volume());
        gc.setSoundVolume(soundEffect.getInstance().vol.get_nomalization_volume());
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
        mm.draw(g);

        if(_DEBUG){
            Color                   base_color = g.getColor();
            org.newdawn.slick.Font  base_font  = g.getFont();
            g.setFont(debug_ttf);
            g.setColor(DEBUG_FONT_COLOR);
            g.drawString("user input[0] = " + user_input.get(0).toString(), DEBUG_FONT_SIZE, window.SIZE.y.floatValue() - (DEBUG_FONT_SIZE * 7));
            g.drawString("user input[1] = " + user_input.get(1).toString(), DEBUG_FONT_SIZE, window.SIZE.y.floatValue() - (DEBUG_FONT_SIZE * 6));
            g.drawString("user input[2] = " + user_input.get(2).toString(), DEBUG_FONT_SIZE, window.SIZE.y.floatValue() - (DEBUG_FONT_SIZE * 5));
            g.drawString("user input[3] = " + user_input.get(3).toString(), DEBUG_FONT_SIZE, window.SIZE.y.floatValue() - (DEBUG_FONT_SIZE * 4));
            g.drawString("user input[4] = " + user_input.get(4).toString(), DEBUG_FONT_SIZE, window.SIZE.y.floatValue() - (DEBUG_FONT_SIZE * 3));

            g.setColor(base_color);
            g.setFont(base_font);
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
     * 第2引数の正規表現に一致する文字列全てを第3引数の文字に置換
     * 引数  ：置換前の文字列, 置換対象にする正規表現, 置換時の文字列
     * 戻り値：置換後の文字列
     * 例外  ：なし
     */
    public static String regex_replace(String _source, String _regex, String _replace_str){
        Matcher regex = Pattern.compile(_regex).matcher(_source);
        return regex.replaceAll(_replace_str);
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