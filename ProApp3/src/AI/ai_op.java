package AI;

import stage.charObj;

import common.point;

/*
 * 移動用命令コードクラス
 */
public class ai_op {
    /* メンバ変数 */
    //命令コード
    public                 short          attack;                                               //攻撃
    public                 short          move;                                                 //移動・ジャンプ
    public                 short          unique;                                               //独自な行動
    public                 point<Integer> texture_num = new point<Integer>(0, 0);               //使用するテクスチャ番号_x
    //使用するAI
    public                 int            using_AI;                                             //使用するAI
    //テクスチャの決定用
    //各種命令コードが同じ内容だったフレーム数
    protected              int            time_attack;                                          //attack
    protected              int            time_move;                                            //move
    protected              int            time_unique;                                          //unique
    /* 定数 */
    //使用するAIの決定用
    public    static final int            AI_NO_USE      = 0x00000000;                          //どのAIも使用しない
    public    static final int            AI_CHIMBO      = 0x00000001;
    public    static final int            AI_TOBI        = 0x00000002;
    public	  static final int			  AI_HANECHIMBO  = 0x00000045;
    public	  static final int			  AI_SHOOT  	 = 0x00010400;
    public    static final int            AI_SWING  	 = 0x00000003;
    public    static final int            AI_TETETON  	 = 0x01905400;
    public    static final int            AI_USER_INPUT  = 0xFFFFFFFF;                          //ユーザ入力

    //attack用
    public    static final short          ATTACK_NONE  =  0x0000;                               //攻撃なし
    public    static final short          ATTACK_NOMAL =  0x0001;                               //通常攻撃

    //move用
    public    static final short          MOVE_NONE           = (short)0x0000;                  //移動なし

    public    static final short          MOVE_DIR            = (short)0x0F00;                  //方向
    public    static final short          MOVE_DIR_LEFT       = (short)0x0100;                  //左
    public    static final short          MOVE_DIR_UP         = (short)0x0200;                  //上
    public    static final short          MOVE_DIR_DOWN       = (short)0x0400;                  //下
    public    static final short          MOVE_DIR_RIGHT      = (short)0x0800;                  //右
    public    static final short          MOVE_DIR_LEFT_RIGHT = MOVE_DIR_LEFT | MOVE_DIR_RIGHT; //左右
    public    static final short          MOVE_DIR_UP_DOWN    = MOVE_DIR_UP   | MOVE_DIR_DOWN;  //上下

    public    static final short          MOVE_MOVE           = (short)0x00F0;                  //移動種類
    public    static final short          MOVE_MOVE_NORMAL    = (short)0x0010;                  //通常移動
    public    static final short          MOVE_MOVE_HIGHSPEED = (short)0x0020;                  //高速移動

    public    static final short          MOVE_JUMP           = (short)0x000F;                  //ジャンプ
    public    static final short          MOVE_JUMP_NORMAL    = (short)0x0001;                  //通常ジャンプ

    //unique用
    public    static final int            UNIQUE_NONE         =  0x00000000;                    //独自処理なし

    public    static final int            UNIQUE_DEFORM       =  0x00000001;                    //変形処理(USER_INPUTでの使用を想定)

    //timer変数
    protected static final int            TIME_MIN            = 0;                              //タイマーの最小値

    /* コンストラクタ */
    /*
     * デフォルトコンストラクタ
     * 引数  ：なし
     */
    public ai_op(){
        attack      = ATTACK_NONE;
        move        = MOVE_NONE;
        unique      = UNIQUE_NONE;
        texture_num = new point<Integer>(0, 0);

        using_AI    = AI_NO_USE;

        time_attack = TIME_MIN;
        time_move   = TIME_MIN;
        time_unique = TIME_MIN;
    }

    /*
     * コピーコンストラクタ
     * 引数  ：コピー元
     */
    public ai_op(ai_op _obj){
        attack      = _obj.attack;
        move        = _obj.move;
        unique      = _obj.unique;
        texture_num = new point<Integer>(_obj.texture_num);

        using_AI    = _obj.using_AI;

        time_attack = _obj.time_attack;
        time_move   = _obj.time_move;
        time_unique = _obj.time_unique;
    }

    /*
     * 使用するAIを指定するタイプのAI
     * 引数  ：使用するAI
     */
    public ai_op(int _use_ai){
        attack      = ATTACK_NONE;
        move        = MOVE_NONE;
        unique      = UNIQUE_NONE;
        texture_num = new point<Integer>(0, 0);

        using_AI    = _use_ai;

        time_attack = TIME_MIN;
        time_move   = TIME_MIN;
        time_unique = TIME_MIN;
    }


    /* メソッド */
    /*
     * 状態アップデート
     * 引数  ：親オブジェクト
     */
    public void update(charObj _belong){
        //using_AIを基にAI呼び出し
        switch(using_AI){
            case AI_USER_INPUT :
                ai_user_input.run(_belong, this);
                return;
            case AI_CHIMBO:
            	ai_chimbo.run(_belong, this);
            	return;
            case AI_TOBI:
            	ai_tobi.run(_belong, this);
            	return;
            case AI_HANECHIMBO:
            	ai_hanechimbo.run(_belong, this);
            	return;
            case AI_SHOOT:
            	ai_shoot.run(_belong, this);
            	return;
            case AI_SWING:
            	ai_swing.run(_belong, this);
            	return;
            case AI_TETETON:
            	ai_teteton.run(_belong, this);
            	return;
            case AI_NO_USE     :
                return;
        }
    }


    /*
     *  文字列からどの使用AI用の定数を導出
     * 引数  ：元になる文字列
     * 戻り値：AI使用用の定数
     */
    public static int name_to_using_ai(String _name){
        switch(_name.toLowerCase()){
            case "user_input"   :
            case "ai_user_input":
                return AI_USER_INPUT;
            case "ai_chimbo":
            	return AI_CHIMBO;
            case "ai_tobi":
            	return AI_TOBI;
            case "ai_hanechimbo":
            	return AI_HANECHIMBO;
            case "ai_shoot":
            	return AI_SHOOT;
            case "ai_swing":
            	return AI_SWING;
            case "ai_teteton":
            	return AI_TETETON;
            default :
                return AI_NO_USE;
        }
    }
}
