package AI;

import stage.charObj;

import common.point;

public class ai_bird {
	final static int LOOP_MOVE_NOMAL_LENGTH = 8;  //移動タイマー最大値

	public static void run(charObj _belong, ai_op _code){
		if(_code.time_attack != ai_op.TIME_MIN){
			--_code.time_attack;
		}
		if(_code.time_move != ai_op.TIME_MIN){
			--_code.time_move;
		}
		if(_code.time_move <= ai_op.TIME_MIN){
			_code.time_move = LOOP_MOVE_NOMAL_LENGTH;
		}
		if(_code.time_unique != ai_op.TIME_MIN){
			--_code.time_unique;
		}

		//初期化処理
		_code.attack = ai_op.ATTACK_NONE;
		_code.move   = ai_op.MOVE_NONE;
		_code.unique = ai_op.UNIQUE_NONE;

		//移動方向の決定
		if((Math.abs(_belong.location.x - _belong.belong.player_data.location.x) < 500.0)){
			_code.move += ai_op.MOVE_DIR_LEFT;
		}else{
			_code.move += _belong.ai_prev.get(0).move & ai_op.MOVE_DIR;
		}

		//移動力の決定
		_code.move += ai_op.MOVE_MOVE_NORMAL;

		//x座標が40.0を超えると削除
		if(_belong.location.x <= 40.0){
			_belong.is_dead = true;
		}

		//攻撃
		if(Math.abs(_belong.location.x - _belong.belong.player_data.location.x) < 300.0){
			if(_code.time_attack == ai_op.TIME_MIN){
				_code.time_attack = 40;
				_code.attack = ai_op.ATTACK_0;
			}
		}

		//テクスチャの状態更新
        //定数の宣言

        //テクスチャ指定用の変数の初期化
        _code.texture_num = new point<Integer>(0, 0);
        // 静止状態
        if((_code.move & ai_op.MOVE_MOVE_NORMAL) == ai_op.MOVE_NONE){
            _code.texture_num.x = 0;
        }
        // 移動状態
        if((_code.move & ai_op.MOVE_MOVE          ) != ai_op.MOVE_NONE &&
           (_code.move & ai_op.MOVE_DIR_LEFT) != ai_op.MOVE_NONE){
            //通常移動
            if((_code.move & ai_op.MOVE_MOVE_NORMAL) != ai_op.MOVE_NONE){
                _code.texture_num.x =((_code.time_move % LOOP_MOVE_NOMAL_LENGTH) < LOOP_MOVE_NOMAL_LENGTH / 2)? 0 : 1;
            }
        }
	}
}
