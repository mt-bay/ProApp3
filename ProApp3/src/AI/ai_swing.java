package AI;

import stage.charObj;

public class ai_swing {
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
		
			//初期化処理
			_code.attack = ai_op.ATTACK_NONE;
			_code.move   = ai_op.MOVE_NONE;
			_code.unique = ai_op.UNIQUE_NONE;
		
			//移動方向の決定
			_code.move += ai_op.MOVE_DIR_RIGHT;
			_code.move += ai_op.MOVE_DIR_DOWN;
			if((_code.time_move == ai_op.TIME_MIN  && _belong.ai_prev.get(0).time_move != _code.time_move) ||
					((_code.move & ai_op.MOVE_DIR) == ai_op.MOVE_NONE)){
				if(_code.time_move == ai_op.TIME_MIN){
					_code.time_move = 20;
					_code.time_move += ai_op.MOVE_DIR_UP;
				}
			}
			else{
				_code.move += _belong.ai_prev.get(0).move & ai_op.MOVE_DIR;
			}
			
			
			//移動力の決定
			_code.move += ai_op.MOVE_MOVE_NORMAL;
			
			//攻撃
			if(Math.abs(_belong.location.x - _belong.belong.player_data.location.x) < 50.0){
				_code.attack += ai_op.ATTACK_NONE;
			}
			}

}
