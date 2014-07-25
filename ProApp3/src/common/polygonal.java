package common;

import java.util.ArrayList;
/* 折れ線クラス
 * (p0, p1, …, pn)の概念で動作することを想定
 */

public class polygonal{
	/* メンバ変数 */

	public ArrayList<point<Double>> p;//折れ線の要素

	/* コンストラクタ */

	/* デフォルトコンストラクタ
	 * 引数：なし
	 * 目的：空の折れ線クラスを作成
	 */
	public polygonal(){
		p = new ArrayList<point>();
	}

	/* コピーコンストラクタ
	 * 引数：コピー元
	 * 目的：内容のコピー
	 */
	public polygonal(polygonal obj){
		p = new ArrayList<point>(obj.p.size());
		for(int i = 0; i < obj.p.size(); i++)
			p.set(i, new point(obj.p.get(i)));
	}

	/* 配列コンストラクタ
	 * 引数：point配列
	 * 目的：座標配列を使って折れ線作成
	 */
	public polygonal(point[] P){
		p = new ArrayList<point>(P.length);
		for(int i = 0; i < P.length; i++)
			p.set(i, P[i]);
	}

	/* 線分指定コンストラクタ
	 * 引数：線分
	 * 目的：線分のみを持っている状態で作成
	 */
	public polygonal(line l){
		p = new ArrayList<point>(2);
		p.set(0,l.p0); p.set(1, l.p1);
	}

	/* 座標指定コンストラクタ
	 * 引数：point
	 * 目的：座標1つのみ持っている状態で作成
	 */
	public polygonal(point P){
		p = new ArrayList<point>(1);
		p.set(0, P);
	}


	/* メソッド */

	/* 内容追加
	 * 引数  ：追加する座標
	 * 戻り値：なし
	 * 目的  ：折れ線座標の追加
	 */
	public void add(point value){
		p.add(value);
	}

	/* 線分取り出し
	 * 引数  ：p0に当たる座標のインデックス
	 * 戻り値：インデックスを基に作成された座標 or null(生成できなかった場合)
	 * 目的：引数を基に、p0, p1の線分を取り出す
	 */
	public line toLine(int index){
		if(index < 0 || index >= p.size() - 1)
			return null;
		return new line(p.get(index), p.get(index + 1));
	}



}
