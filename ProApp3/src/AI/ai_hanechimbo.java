package AI;

import stage.charObj;

public class ai_hanechimbo {
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
		/*
		 *11fps（？）毎に
		 *１．左への移動
		 *２．ジャンプ
		 *を行う．
		 */
		if((_code.time_move == ai_op.TIME_MIN &&
		   _belong.ai_prev.get(0).time_move != _code.time_move) ||
		   ((_code.move & ai_op.MOVE_DIR) == ai_op.MOVE_NONE)){
			if(_code.time_move == ai_op.TIME_MIN){
				_code.time_move = 11;
				_code.move += ai_op.MOVE_DIR_LEFT;
				_code.move += ai_op.MOVE_JUMP_NORMAL;
			}
		}
		else{
			_code.move += _belong.ai_prev.get(0).move & ai_op.MOVE_DIR;
		}

		//プレイヤーから離れすぎた時の処理
		/*
		 * はねちんぼがプレイヤーから300後ろに来てしまった時に
		 * １．右へ移動して
		 * ２．15秒ごとに弾を撃つ（これないと絶え間なく撃ち続けてきて強すぎるかなと思った）
		 */
		if((_belong.belong.player_data.location.x - _belong.location.x) > 300){
			_code.move += ai_op.MOVE_DIR_RIGHT;
			if(_code.time_attack == ai_op.TIME_MIN){
				_code.time_attack = 15;
				_code.attack += ai_op.ATTACK_0;
			}
		}
		//移動力の決定
		_code.move += ai_op.MOVE_MOVE_NORMAL;
		
		//x座標が40.0を超えると削除
		/*if(_belong.location.x <= 40.0){
			_belong.is_dead = true;
		}*/

		//プレイヤーがこのオブジェクトからx座標上で400以上離れたら削除
		if(_belong.belong.player_data.location.x - _belong.location.x > 400.0){
			_belong.is_dead = true;
		}
		
		//攻撃
		/*
		 * プレイヤーから130以内に入ってきたら弾を撃つ
		 */
		if(Math.abs(_belong.location.x - _belong.belong.player_data.location.x) < 130.0){
			_code.attack += ai_op.ATTACK_0;
		}
	}
}
