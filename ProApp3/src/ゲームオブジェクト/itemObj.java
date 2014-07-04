package ゲームオブジェクト;


public class itemObj {
	point  accel;	//移動力
	double hp;		//体力値
	
	/* デフォルトコンストラクタ
	 * 引数：なし
	 */
	public itemObj(){
		set(new point(), new point(), new point(), 0.0);
	}
	/* データ指定型コンストラクタ
	 * 引数：それぞれのデータ
	 */
	public itemObj(rect Rec, point Acc, double HP){
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
