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


		/*攻撃パターン*/
		//突進 →ジャンプ移動→ジャンプショット
		if((Math.abs(_belong.location.x - _belong.belong.player_data.location.x) < 700.0)){
					if(_code.time_move == ai_op.TIME_MIN){
						_code.time_move = 90;
						_code.move   = ai_op.MOVE_NONE;
					}
					if(_code.time_move >= 75){
						_code.move += ai_op.MOVE_DIR_LEFT;
						_code.move += ai_op.MOVE_JUMP_NORMAL;
						//_code.attack += ai_op.ATTACK_1;
					}
					else if(_code.time_move >= 60){
						_code.move += ai_op.MOVE_DIR_LEFT;
					}
					else if(_code.time_move >= 45){
						_code.move += ai_op.MOVE_JUMP_NORMAL;
						_code.move += ai_op.MOVE_DIR_LEFT;
						//_code.attack += ai_op.ATTACK_1;
					}
					else if(_code.time_move >= 30){
						_code.move += ai_op.MOVE_DIR_RIGHT;
						_code.move += ai_op.MOVE_JUMP_NORMAL;
						//_code.attack += ai_op.ATTACK_1;
					}
					else if(_code.time_move >= 15){
						_code.move += ai_op.MOVE_DIR_RIGHT;
					}
					else if(_code.time_move <= 14){
						_code.move += ai_op.MOVE_JUMP_NORMAL;
						_code.move += ai_op.MOVE_DIR_RIGHT;
						//_code.attack += ai_op.ATTACK_1;
					}
				}
				else{
					_code.move += _belong.ai_prev.get(0).move & ai_op.MOVE_DIR;
				}

		//攻撃頻度
		if(Math.abs(_belong.location.x - _belong.belong.player_data.location.x) < 300.0){
			if(_code.time_attack == ai_op.TIME_MIN){
				_code.time_attack = 10;
				_code.attack += ai_op.ATTACK_1;
			}
		}

		//移動力の決定
		_code.move += ai_op.MOVE_MOVE_NORMAL;
	}
}