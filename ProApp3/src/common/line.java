package common;

/* 線分クラス
 * (p0, p1)間をつなぐ線分
 */

public class line {
	/* クラス内変数 */
	public point p0, p1;//線分

	/* コンストラクタ */

	/* デフォルトコンストラクタ
	 * 引数：なし
	 * 目的：((0, 0), (0, 0))の線分作成
	 */
	public line(){
		set(new point(), new point());
	}

	/* コピーコンストラクタ
	 * 引数：コピー元
	 * 目的：内容のコピー
	 */
	public line(line obj){
		set(obj.p0, obj.p1);
	}

	/* 座標指定型コンストラクタ
	 * 引数：p0, p1
	 * 木tケイ：(p0, p1)の線分作成
	 */
	public line(point P0, point P1){
		set(P0, P1);
	}


	/* メソッド */

	/* 変数セット
	 * 引数：p0, p1
	 * 目的：変数セット
	 */
	private void set(point P0, point P1){
		p0 = new point(P0);
		p1 = new point(P1);
	}

	/* 線分の長さを求める
	 * 引数：なし
	 * 目的：線分の長さを求める
	 */
	public double length(){

		return new point(p1.x - p0.x,p1.y - p1.y).length();
	}



}
