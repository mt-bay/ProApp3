package 座標系;


/* 長方形クラス
 *
 */
public class rect {
	/* クラス内変数 */
	public point location;  //座標
	public point size;      //サイズ

	/* コンストラクタ */

	/* デフォルトコンストラクタ
	 * 引数：なし
	 * 目的：座標(0, 0), サイズ(0, 0)の四角形生成
	 */
	public rect(){
		set(new point(), new point());
	}

	/* 座標つきコンストラクタ
	 * 引数：座標
	 * 目的：座標のみ指定，サイズ(0, 0)の四角形生成
	 */
	public rect(point Loc){
		set(Loc, new point());
	}

	/* 座標つき正方形コンストラクタ
	 * 引数：座標，正方形の1辺のサイズ
	 * 目的：座標指定された正方形を生成する
	 */
	public rect(point Loc, double len){
		set(Loc, new point(len, len));
	}

	/* 座標付き長方形コンストラクタ
	 * 引数：座標, サイズ
	 * 目的：座標, サイズそれぞれを指定した長方形を生成する
	 */
	public rect(point Loc, point Siz){
		set(Loc, Siz);
	}


	/* メソッド */

	/* 変数セット
	 * 引数  ：セットする変数
	 * 戻り値：なし
	 * 目的  ：引数の変数をセット
	 */
	private void set(point Location, point Size){
		location = new point(Location);
		size     = new point(Size);
	}

}
