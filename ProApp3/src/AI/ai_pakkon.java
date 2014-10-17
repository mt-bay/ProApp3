package AI;

import java.util.Random;

import stage.charObj;

public class ai_pakkon {
	public static void run(charObj _belong, ai_op _code){
		//ランダムクラスの生成
		Random r = new Random();
		
		//乱数の取得
		int i = r.nextInt(8);
		
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
		/*
		 time_moveを30に更新。
		 if  time_moveが16以上の時に上昇
		     time_moveが15以下の時は下降
		*/
		_code.time_move += ai_op.MOVE_NONE;
		
//		if((_code.time_move == ai_op.TIME_MIN && _belong.ai_prev.get(0).time_move != _code.time_move) ||
//				((_code.move & ai_op.MOVE_DIR) == ai_op.MOVE_NONE)){
//			if(_code.time_move == ai_op.TIME_MIN){
//				_code.time_move = 30;
//			}
//			if(_code.time_move >= 16){
//				_code.move += ai_op.MOVE_DIR_UP;
//			}
//			else if(_code.time_move <= 15){ 
//				_code.move += ai_op.MOVE_DIR_DOWN;
//			}
//		}
//		else{
//			_code.move += _belong.ai_prev.get(0).move & ai_op.MOVE_DIR;
//		}
		
		//移動力の決定
		_code.move += ai_op.MOVE_MOVE_NORMAL;
		
		//攻撃
		/*
		 iが0～７によって攻撃方向を決める
		 */
		switch(i){
			case 0:
				_code.attack += ai_op.ATTACK_NOMAL;
			case 1:
				_code.attack += ai_op.ATTACK_LEFTDOWN;
			case 2:
				_code.attack += ai_op.ATTACK_DOWN;
			case 3:
				_code.attack += ai_op.ATTACK_RIGHTDOWN;
			case 4:
				_code.attack += ai_op.ATTACK_RIGHT;
			case 5:
				_code.attack += ai_op.ATTACK_RIGHTUP;
			case 6:
				_code.attack += ai_op.ATTACK_UP;
			case 7:
				_code.attack += ai_op.ATTACK_LEFTUP;
				
		}
		/*
		if(Math.abs(_belong.location.x - _belong.belong.player_data.location.x) < 500.0){
			_code.attack += ai_op.ATTACK_NOMAL;
			
		}
		*/
		
	}

}
