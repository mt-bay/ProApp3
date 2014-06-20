package ゲームオブジェクト;

import 座標系.point;
import 座標系.rect;

public class charObj extends rect {
	point  accel;	//移動力
	double hp;		//体力値
	
	/* デフォルトコンストラクタ
	 * 引数：なし
	 */
	public charObj(){
		set(new point(), new point(), new point(), 0.0);
	}
	/* データ指定型コンストラクタ
	 * 引数：それぞれのデータ
	 */
	public charObj(rect Rec, point Acc, double HP){
		set(Rec.location, Rec.size, Acc, HP);
	}
	
	
	/* メソッド */
	private void set(point Loc, point Siz, point Acc, double HP){
		location = Loc;
		size     = Siz;
		accel    = Acc;
		hp       = HP;
	}
}
