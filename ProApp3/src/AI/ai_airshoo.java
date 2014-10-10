package AI;

import stage.charObj;
//aaaaaaaaa
public class ai_airshoo {
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

		
		//向く方向を変える
		if((_code.time_move == ai_op.TIME_MIN && _belong.ai_prev.get(0).time_move != _code.time_move) ||
				((_code.move & ai_op.MOVE_DIR) == ai_op.MOVE_NONE)){
			if(_code.time_move == ai_op.TIME_MIN){
				_code.time_move = 8;
			}
			if(_code.time_move >= 7){
				_code.move += ai_op.MOVE_DIR_UP;
				_code.move += ai_op.MOVE_DIR_RIGHT;
				_code.attack += ai_op.ATTACK_NOMAL;
			}
			else if(_code.time_move >= 5){ 
				_code.move += ai_op.MOVE_DIR_DOWN;
				_code.move += ai_op.MOVE_DIR_RIGHT;
				_code.attack += ai_op.ATTACK_NOMAL;
			}
			else if(_code.time_move >= 3){ 
				_code.move += ai_op.MOVE_DIR_DOWN;
				_code.move += ai_op.MOVE_DIR_LEFT;
				_code.attack += ai_op.ATTACK_NOMAL;
			}
			else if(_code.time_move <= 2){ 
				_code.move += ai_op.MOVE_DIR_UP;
				_code.move += ai_op.MOVE_DIR_LEFT;
				_code.attack += ai_op.ATTACK_NOMAL;
			}
		}
		else{
			_code.move += _belong.ai_prev.get(0).move & ai_op.MOVE_DIR;
		}
		//移動力の決定
		_code.move += ai_op.MOVE_MOVE_NORMAL;
		
		//攻撃
		/*
		 * 攻撃の激しさが距離によって変化する
		 * １，7００～2００
		 * ２，2００～０
		 */
		if(Math.abs(_belong.location.x - _belong.belong.player_data.location.x) < 700.0 && 200.0 < Math.abs(_belong.location.x - _belong.belong.player_data.location.x) &&
				_code.time_attack == ai_op.TIME_MIN){
			_code.time_attack = 30;
			_code.attack += ai_op.ATTACK_NOMAL;
		}
		else if(Math.abs(_belong.location.x - _belong.belong.player_data.location.x) < 200.0 && 0.0 < Math.abs(_belong.location.x - _belong.belong.player_data.location.x) &&
				_code.time_attack == ai_op.TIME_MIN){
			_code.time_attack = 60;
			_code.attack += ai_op.ATTACK_NOMAL;
		}
		
		//プレイヤーがこのオブジェクトからx座標上で400以上離れたら削除
		if(_belong.belong.player_data.location.x - _belong.location.x > 400.0){
			_belong.is_dead = true;
		}
	}
}
