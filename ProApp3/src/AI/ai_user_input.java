package AI;

import stage.charObj;
import window.Main;

import common.point;

public class ai_user_input {
    /*
     * ユーザー入力を基にして入力生成
     * 引数  ：呼び出したクラス
     * 戻り値：なし
     */
    public static void run(charObj _belong, ai_op _code){
        //旧データの保存
        short old_attack = _code.attack;
        short old_move   = _code.move;
        short old_unique = _code.unique;

        //攻撃
        _code.attack = ai_op.ATTACK_NONE;
        if(Main.user_input.get(0).attack){
            _code.attack = ai_op.ATTACK_NOMAL;
        }

        // アップデート前と同値かチェック
        _code.time_move = (_code.attack == old_attack)? _code.time_move + 1 : ai_op.TIME_MIN;

        //移動
        _code.move = ai_op.MOVE_NONE;
        // 移動方向の決定
        //  左右両方 or 入力なし
        if     (( Main.user_input.get(0).left &&
                  Main.user_input.get(0).right  ) ||
                (!Main.user_input.get(0).left &&
                 !Main.user_input.get(0).right  )){

        }

        //  左
        else if(Main.user_input.get(0).left){
            _code.move += ai_op.MOVE_DIR_LEFT;
        }
        //  右
        else if(Main.user_input.get(0).right){
            _code.move += ai_op.MOVE_DIR_RIGHT;
        }

        // 上下移動
        //  上下両方 or 入力なし
        if    (( Main.user_input.get(0).up  &&
                 Main.user_input.get(0).down  ) ||
               (!Main.user_input.get(0).up  &&
                !Main.user_input.get(0).down  )){

        }
        //  上
        else if(Main.user_input.get(0).up){
            _code.move += ai_op.MOVE_DIR_UP;
        }
        //  下
        else if(Main.user_input.get(0).down){
            _code.move += ai_op.MOVE_DIR_DOWN;
        }

        // 移動速度の決定
        //  移動フラグが立っている (= 方向フラグが立ってる)ならば、高速移動か判定して追加
        if((_code.move  & ai_op.MOVE_DIR) != ai_op.MOVE_NONE){
            _code.move += (Main.user_input.get(0).highsp)? ai_op.MOVE_MOVE_HIGHSPEED : ai_op.MOVE_MOVE_NORMAL;
        }

        // ジャンプの決定
        if(Main.user_input.get(0).jump){
            _code.move += ai_op.MOVE_JUMP_NORMAL;
        }

        // アップデート前と同値かチェック
         _code.time_move = (_code.move == old_move)? _code.time_move + 1 : ai_op.TIME_MIN;

        //ユニーク操作
        _code.unique = ai_op.UNIQUE_NONE;
        // モードチェンジ
        if(Main.user_input.get(0).change)
            _code.unique += ai_op.UNIQUE_DEFORM;

        // アップデート前と同値かチェック
        _code.time_unique = (_code.move == old_unique)? _code.time_unique + 1 : ai_op.TIME_MIN;


        //テクスチャの状態更新
        //定数の宣言
        final int LOOP_MOVE_NOMAL_LENGTH = 16;  //移動タイマー最大値
        //テクスチャ指定用の変数の初期化
        _code.texture_num = new point<Integer>(0, 0);
        // 重力依存チェック
        if((_belong.is_gravitied)){
            // 静止状態
            if((_code.move & ai_op.MOVE_MOVE_NORMAL) == ai_op.MOVE_NONE){
                _code.texture_num.x = 0;
            }
            // 移動状態
            if((_code.move & ai_op.MOVE_MOVE          ) != ai_op.MOVE_NONE &&
               (_code.move & ai_op.MOVE_DIR_LEFT_RIGHT) != ai_op.MOVE_NONE){
                //通常移動
                if((_code.move & ai_op.MOVE_MOVE_NORMAL) != ai_op.MOVE_NONE){
                    _code.texture_num.x =((_code.time_move % LOOP_MOVE_NOMAL_LENGTH) < LOOP_MOVE_NOMAL_LENGTH / 2)? 1 : 2;
                }
                //高速移動
                if((_code.move & ai_op.MOVE_MOVE_HIGHSPEED) != ai_op.MOVE_NONE){
                    _code.texture_num.x = ((_code.time_move % LOOP_MOVE_NOMAL_LENGTH) < LOOP_MOVE_NOMAL_LENGTH / 2)? 1 : 2;
                }
            }
            // ジャンプ中判定
            if(!_belong.is_gnd){
                _code.texture_num.x = 3;
            }
            // 攻撃
            if((_code.attack & ai_op.ATTACK_NOMAL) != ai_op.ATTACK_NONE){
                _code.texture_num.y = 1;
            }
        }
        else{
            _code.texture_num.x = 3;
         // 攻撃
            if((_code.attack & ai_op.ATTACK_NOMAL) != ai_op.ATTACK_NONE){
                _code.texture_num.y = 1;
            }
        }

    }
}
