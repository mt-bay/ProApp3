package AI;

import stage.charObj;

public class ai_bigisland {
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
		 * 左に移動
		 */
		if((_code.time_move == ai_op.TIME_MIN &&
		   _belong.ai_prev.get(0).time_move != _code.time_move) ||
		   ((_code.move & ai_op.MOVE_DIR) == ai_op.MOVE_NONE)){
				_code.move += ai_op.MOVE_DIR_LEFT;
		}
		else{
			_code.move += _belong.ai_prev.get(0).move & ai_op.MOVE_DIR;
		}
		
		/*攻撃パターン*/
		
		//突進
		if((_code.time_move == ai_op.TIME_MIN &&
				   _belong.ai_prev.get(0).time_move != _code.time_move) ||
				   ((_code.move & ai_op.MOVE_DIR) == ai_op.MOVE_NONE)){
					if(_code.time_move == ai_op.TIME_MIN){
						_code.time_move = 45;
					}
					if(_code.time_move >= 30){
						
					}
					else if(_code.time_move >= 16){
						
					}
					else if(_code.time_move <= 15){
						
					}
				}
				else{
					_code.move += _belong.ai_prev.get(0).move & ai_op.MOVE_DIR;
				}
		
		//ふみつけ
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
		
		//ショット
		if(Math.abs(_belong.location.x - _belong.belong.player_data.location.x) < 130.0){
			_code.attack += ai_op.ATTACK_NOMAL;
			_code.move += ai_op.MOVE_JUMP_NORMAL;
		}
		
		//移動力の決定
		_code.move += ai_op.MOVE_MOVE_NORMAL;
	}
}
