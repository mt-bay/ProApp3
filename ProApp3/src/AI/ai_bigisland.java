package AI;

import stage.charObj;

import common.point;

public class ai_bigisland {
	public static void run(charObj _belong, ai_op _code){

		if(_code.time_attack != ai_op.TIME_MIN){
			--_code.time_attack;
		}
		if(_code.time_move != ai_op.TIME_MIN){
			--_code.time_move;
		}

		++_code.time_island;

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
						_code.time_move = 180;
						_code.move   = ai_op.MOVE_NONE;
					}
					if(_code.time_move >= 150){
						_code.move += ai_op.MOVE_DIR_LEFT;
						_code.move += ai_op.MOVE_JUMP_NORMAL;
						//_code.attack += ai_op.ATTACK_1;
					}
					else if(_code.time_move >= 120){
						_code.move += ai_op.MOVE_DIR_LEFT;
					}
					else if(_code.time_move >= 90){
						_code.move += ai_op.MOVE_JUMP_NORMAL;
						_code.move += ai_op.MOVE_DIR_LEFT;
						//_code.attack += ai_op.ATTACK_1;
					}
					else if(_code.time_move >= 60){
						_code.move += ai_op.MOVE_DIR_RIGHT;
						_code.move += ai_op.MOVE_JUMP_NORMAL;
						//_code.attack += ai_op.ATTACK_1;
					}
					else if(_code.time_move >= 30){
						_code.move += ai_op.MOVE_DIR_RIGHT;
					}
					else if(_code.time_move <= 29){
						_code.move += ai_op.MOVE_JUMP_NORMAL;
						_code.move += ai_op.MOVE_DIR_RIGHT;
						//_code.attack += ai_op.ATTACK_1;
					}
				}
				else{
					_code.move += _belong.ai_prev.get(0).move & ai_op.MOVE_DIR;
				}

		//移動力の決定
		_code.move += ai_op.MOVE_MOVE_NORMAL;

		//攻撃頻度
		if(Math.abs(_belong.location.x - _belong.belong.player_data.location.x) < 300.0){
			if(_code.time_attack == ai_op.TIME_MIN){
				_code.time_attack = 10;
				_code.attack = ai_op.ATTACK_0;
			}
		}

		//テクスチャの状態更新
        //定数の宣言
        final int LOOP_MOVE_NOMAL_LENGTH = 8;  //移動タイマー最大値
        //テクスチャ指定用の変数の初期化
        _code.texture_num = new point<Integer>(0, 0);
        // 静止状態
        if((_code.move & ai_op.MOVE_MOVE_NORMAL) == ai_op.MOVE_NONE){
            _code.texture_num.x = 0;
        }
        // 移動状態
        if((_code.move & ai_op.MOVE_MOVE_NORMAL) != ai_op.MOVE_NONE){
            _code.texture_num.x =((_code.time_island % LOOP_MOVE_NOMAL_LENGTH) < LOOP_MOVE_NOMAL_LENGTH / 2)? 1 : 2;
        }
        // ジャンプ中判定
        if(!_belong.is_gnd){
            _code.texture_num.x = 3;
        }
        // 攻撃
        if(_code.attack != ai_op.ATTACK_NONE){
            _code.texture_num.x = 4;
        }

	}
}