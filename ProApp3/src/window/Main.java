/*
 * mainクラス
 * プログラムの起動・終了
 */

package window;

import java.awt.Font;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

import stage.Stage;
import IO.debugLog;

public class Main extends BasicGame {
     /* メンバ変数 */

    public static final int          FONT_SIZE  = 11;
    public static final Color        FONT_COLOR = new Color(0x777777); //HTML的記法
    public static       TrueTypeFont M_TTF;
    public static Input user_input;
    public static Stage stg_test;


    //デバッグ用
    public static final boolean     _DEBUG = true; //デバッグモードかどうか

    public Main(String title) {
        super(title);
    }

    public static void main(String[] args) throws SlickException {




        try {

            AppGameContainer app = new AppGameContainer(new Main("ゲーム名"));
            app.setDisplayMode(window.SIZE.x, window.SIZE.y, false);
            app.setTargetFrameRate(window.FPS);
            app.start();
        }
        catch (Exception e) {
            debugLog.getInstance().write_exception(e);
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

        user_input = gc.getInput();
        stg_test = new Stage("stage\\test.stage");

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
        user_input = gc.getInput();
        stg_test.update();


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
        stg_test.draw(g);

        if(_DEBUG){
            M_TTF = new TrueTypeFont(new Font("メイリオ", Font.BOLD, FONT_SIZE), false);
            g.setFont(M_TTF);
            g.setColor(FONT_COLOR);
            g.drawString("player - location = " + stg_test.player_data.location.toString(), (float)FONT_SIZE, (float)FONT_SIZE);
            g.drawString("player - is_gnd   = " + stg_test.player_data.is_gnd, (float)FONT_SIZE, (float)FONT_SIZE * 2);

        }

        g.setColor(Color.black);

    }
}
