package 座標系;



/* 2次元座標クラス
 * メソッドによっては、ベクトル的なふるまいも行う
 */
public class point {
	/* クラス内変数 */

	public double x, y;//2次元座標


	/* コンストラクタ */

	/* デフォルトコンストラクタ
	 * 引数：なし
	 * 目的：(0, 0)を生成
	 */
	public point(){
		set( 0.0d,  0.0d);
	}

	/* コピーコンストラクタ
	 * 引数：コピー元
	 * 目的：クラスのコピー
	 */
	public point(point obj){
		set(obj.x, obj.y);
	}

	/* 座標指定コンストラクタ
	 * 引数：(x, y)
	 * 目的：指定された座標を生成する
	 */
	public point(double X, double Y){
		set(    X,     Y);
	}


	/* メソッド */
	/* 変数セット
	 * 引数  ：(x, y)
	 * 戻り値：なし
	 * 目的  ：引数に指定された変数をセット
	 */
	private void set(double X, double Y){
		x = X; y = Y;
	}

	/* (0, 0)もしくは指定された座標から自座標までのユークリッド距離を求める
	 * 引数  ：なし or 原点
	 * 戻り値：ユークリッド距離
	 */
	public double length(){
		return lengtgh_body(new point());
	}
	public double length(point root){
		return lengtgh_body(root);
	}
	private double lengtgh_body(point o){
		point p = new point(x - o.x, y - o.y);
		return Math.sqrt((p.x * p.x) + (p.y * p.y));//√(x^2 + y^2)
	}


}
