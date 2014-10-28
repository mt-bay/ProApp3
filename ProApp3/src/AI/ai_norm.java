package AI;

import stage.charObj;

public class ai_norm {
	public static void run(charObj _belong, ai_op _code){

		if(_code.time_attack != ai_op.TIME_MIN){
			--_code.time_attack;
		}
		if(_code.time_move != ai_op.TIME_MIN){
			--_code.time_move;
		}
		if(_code.time_unique != ai_op.TIME_MIN){
			--_code.time_unique;
		}

		//初期化
		_code.attack = ai_op.ATTACK_NONE;
		_code.move   = ai_op.MOVE_NONE;
		_code.unique = ai_op.UNIQUE_NONE;
		//移動方向の決定
		/*プレイヤーが距離500以内に入ってきたら左へ移動開始
		 * （ステージの先に配置したオブジェクトが関係ないところで動き出すのを予防）
		 */
/*		if((Math.abs(_belong.location.x - _belong.belong.player_data.location.x) < 500.0)){
			_code.move += ai_op.MOVE_DIR_LEFT;
		}else{
			_code.move += _belong.ai_prev.get(0).move & ai_op.MOVE_DIR;
		}
*/
		//移動力の決定
		_code.move += ai_op.MOVE_MOVE_NORMAL;

		//攻撃
		/*
		 * プレイヤーから300以内に入ってきたら弾を撃つ
		 */
		if(Math.abs(_belong.location.x - _belong.belong.player_data.location.x) < 300.0){
			if(_code.time_attack == ai_op.TIME_MIN){
				_code.time_attack = 40;
				_code.attack = ai_op.ATTACK_1;
			}
		}

		//x座標が40.0を超えると削除
		if(_belong.location.x <= 40.0){
			_belong.is_dead = true;
		}
	}
}
