package AI;

import stage.charObj;

public class ai_chimbo {
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
		if((_code.time_move == ai_op.TIME_MIN &&
		   _belong.ai_prev.get(0).time_move != _code.time_move) ||
		   ((_code.move & ai_op.MOVE_DIR) == ai_op.MOVE_NONE)){
			//if(_belong.location.x > _belong.belong.player_data.location.x){
				_code.move += ai_op.MOVE_DIR_LEFT;
			//}
			/*else{
				_code.move += ai_op.MOVE_DIR_LEFT;
			}*/
		}
		else{
			_code.move += _belong.ai_prev.get(0).move & ai_op.MOVE_DIR;
		}
		//プレイヤーと接触した時の処理
		if(Math.abs(_belong.get_center().x - _belong.belong.player_data.get_center().x) < 30.0 &&
		   _code.time_move == ai_op.TIME_MIN){
			_code.time_move = 15;
		}

		if((_belong.belong.player_data.location.x - _belong.location.x) > 430){
			_code.move += ai_op.MOVE_DIR_RIGHT;
		}
		//移動力の決定
		_code.move += ai_op.MOVE_MOVE_NOMAL;

		//攻撃
		if(Math.abs(_belong.location.x - _belong.belong.player_data.location.x) < 50.0){
			_code.attack += ai_op.ATTACK_NOMAL;
		}


	}
}
