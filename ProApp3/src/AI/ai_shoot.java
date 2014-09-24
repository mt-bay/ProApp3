package AI;

import stage.charObj;

public class ai_shoot {
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

		//プレイヤーと接触した時の処理
		/*if(Math.abs(_belong.get_center().x - _belong.belong.player_data.get_center().x) < 30.0 &&
		   _code.time_move == ai_op.TIME_MIN){
			_code.time_move = 15;
		}*/
		//移動力の決定
		_code.move += ai_op.MOVE_MOVE_NORMAL;

		//攻撃
		/*
		 * プレイヤーから130以内に入ってきたら弾を撃つ
		 */
		if(Math.abs(_belong.location.x - _belong.belong.player_data.location.x) < 130.0){
			_code.attack += ai_op.ATTACK_NOMAL;
		}

		//プレイヤーから離れすぎたときの処理
		/*
		 * ちんぼがプレイヤーから300後ろに来てしまった時に
		 * １．右へ移動して
		 * ２．15秒ごとに弾を撃つ（これないと絶え間なく撃ち続けてきて強すぎるかなと思った）
		if((_belong.belong.player_data.location.x - _belong.location.x) > 300){
			_code.move += ai_op.MOVE_DIR_RIGHT;
			if(_code.time_attack == ai_op.TIME_MIN){
				_code.time_attack = 15;
				_code.attack += ai_op.ATTACK_NOMAL;
		*/
	}
}
