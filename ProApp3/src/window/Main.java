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

import IO.debugLog;

import common.point;


public class Main extends BasicGame{
    /* メンバ変数 */
    public static final point<Integer> WINDOW_SIZE  = new point<Integer>(800, 600);
    public static final int            FPS   = 30;


	public Main(String title) {
		super(title);
	}

	public static void main(String[] args) throws SlickException{

	    debugLog dLog = debugLog.getInstance();


	    /*
	     * AppGameContainerコンストラクタの引数の扱いは、引数名と違って
	     * (Game, 縦幅, 横幅, フルスクリーンか) のようだ
	     */
	    try{
	        AppGameContainer app = new AppGameContainer(new Main("ゲーム名"));
	        app.setDisplayMode(WINDOW_SIZE.x, WINDOW_SIZE.y, false);
	        app.setTargetFrameRate(FPS);
	        app.start();
	    }catch(Exception e){
	        dLog.write_exception(e, new Throwable().getStackTrace()[0].getClassName(), new Throwable().getStackTrace()[0].getMethodName());
	    }

	    //後処理
	    dLog.delInstance();

	    return;
	}


	/* 初期化
	 * 目的  ：ファイルを読み込んで変数と関連付ける
	 * 戻り値：なし
	 * 例外  ：SlickException
	 */
	@Override
	public void init(GameContainer gc) throws SlickException{
		//初期化ルーチン

	}


	/* 内容の更新
	 * 目的  ：内部状態の更新(メインループ)
	 * 引数  ：なにこれ
	 * 戻り値：なし
	 * 例外  ：SlickException
	 */
	@SuppressWarnings("static-access")
	@Override
	public void update(GameContainer gc, int delta) throws SlickException{

	}

	/* 描画
	 * 目的  ：画面描画(メインループ)
	 * 引数  ：なにこれ
	 * 戻り値：なし
	 * 例外  ：SlickException
	 */
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException{


	}
}
