package gameObj;

import common.point;
import common.rect;

public class charObj extends rect {
	/* メンバ変数 */
	//基本的
	public point  accel;	//移動力
	public double hp;		//体力値
	
	//状態変数
	protected int t_invisible;	//残りの無敵フレーム数
	
	
	
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
	/* 状態アップデート
	 * 引数：なし
	 */
	public void update(){
		
	}
	
	
	/* 変数セット
	 * 引数：それぞれのデータ
	 */
	private void set(point Loc, point Siz, point Acc, double HP){
		location = Loc;
		size     = Siz;
		accel    = Acc;
		hp       = HP;
	}
}
