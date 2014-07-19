package gameObj;

import common.point;
import common.rect;

public class dmgObj extends rect {
	public point  accel;	//移動力
	public double atk;		//charObj衝突時の体力変動値
	
	/* コンストラクタ */
	/* デフォルトコンストラクタ
	 * 引数：なし
	 */
	public dmgObj(){
		set(new point(), new point(), new point(), 0.0);
	}
	/* コピーコンストラクタ
	 * 引数：コピー元
	 */
	public dmgObj(dmgObj obj){
		set(obj.location, obj.size, obj.accel, obj.atk);
	}
	/* 値指定型コンストラクタ
	 * 引数：それぞれのデータ
	 */
	public dmgObj(rect Rec, point Acc, double Atk){
		set(Rec.location, Rec.size, Acc, Atk);		
	}
	
	
	/* メソッド */
	
	
	/* setter
	 * 引数：それぞれのデータ
	 */
	private void set(point Loc, point Siz, point Acc, double Atk){
		location = new point(Loc);
		size     = new point(Siz);
		accel    = new point(Acc);
		atk      = Atk;
	}
}
