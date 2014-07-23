package gameObj;

import org.newdawn.slick.*;

import IO.config;

public class playerObj extends charObj{
	/* メンバ変数 */
	//状態変数
	public    boolean isShooting; //シューティングモードか
	protected int     t_deform;   //シューティング←→アクションの変形残りフレーム

	private   double  act_mv;    //アクションモード時の左右移動力


	//ユーザ入力関係

	/* コンストラクタ */

	/* メソッド */
	/* 状態アップデート(オーバーライド)
	 * 引数：なし
	 */
	@Override
	public void update(){
		//変形中 or 変形終了時
		if(t_deform >= 0){
			if(t_deform == 0)
				isShooting = !isShooting;
			t_deform--;
		}
		//通常時
		else
		{
			//アクションモード時
			if(!isShooting){
				if(Input.isKeyDown(config.left))
			}
		}

	}



}
