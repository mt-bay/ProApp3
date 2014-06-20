package ゲームオブジェクト;

import 座標系.point;
import 座標系.rect;

public class dmgObj extends rect {
	public point  accel;	//移動力
	public double atk;		//charObj衝突時の体力変動値
	
	/* コンストラクタ */
	/* デフォルトコンストラクタ
	 * 引数：なし
	 */
	public dmgObj(){
		super();
	}
	
	
	/* メソッド */
	private void set(point Loc, point Siz, point Acc, double Atk){
		location = new point(Loc);
		size     = new point(Siz);
		accel    = new point(Acc);
		atk      = Atk;
	}
}
