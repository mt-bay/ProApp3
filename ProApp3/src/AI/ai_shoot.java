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

		//攻撃
		/*
		 * 攻撃の激しさが距離によって変化する
		 * １，7００～2００
		 * ２，2００～０
		 */
		if(Math.abs(_belong.location.x - _belong.belong.player_data.location.x) < 700.0 && 200.0 < Math.abs(_belong.location.x - _belong.belong.player_data.location.x) &&
				_code.time_attack == ai_op.TIME_MIN){
			_code.time_attack = 30;
			_code.attack = ai_op.ATTACK_0;
		}
		else if(Math.abs(_belong.location.x - _belong.belong.player_data.location.x) < 200.0 && 0.0 < Math.abs(_belong.location.x - _belong.belong.player_data.location.x) &&
				_code.time_attack == ai_op.TIME_MIN){
			_code.time_attack = 60;
			_code.attack = ai_op.ATTACK_0;
		}
		//プレイヤーがこのオブジェクトからx座標上で400以上離れたら削除
		if(_belong.belong.player_data.location.x - _belong.location.x > 400.0){
			_belong.is_dead = true;
		}
	}
}
