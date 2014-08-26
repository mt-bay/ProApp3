/*
 * mainクラス
 * プログラムの起動・終了
 */

package window;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import stage.Stage;
import IO.debugLog;

public class Main extends BasicGame {
     /* メンバ変数 */

    public static Stage stg_test;


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
            debugLog.getInstance().write_exception(e, new Throwable());
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
        //初期化ルーチン
        stg_test = new Stage("stage\\test.stage");

    }

    /*
     * 内容の更新
     * 内部状態の更新(メインループ)
     * 引数  ：なにこれ
     * 戻り値：なし
     * 例外  ：SlickException
     */
    @SuppressWarnings("static-access")
    @Override
    public void update(GameContainer gc, int delta) throws SlickException {
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
        stg_test.draw();
    }
}
