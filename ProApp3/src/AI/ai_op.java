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

    public    static final int            AI_NORM        = 0x10000000;
    public    static final int            AI_BIRD        = 0x20000000;
    public	  static final int			  AI_SYLPH      = 0x10000001;
    public	  static final int			  AI_SHOOT  	 = 0x10000002;
    public    static final int            AI_BIRD_SWING  	 = 0x20000001;
    public    static final int            AI_WAY  	     = 0x10000003;
    public	  static final int			  AI_AIRSHOO	 = 0x10000004;
    public	  static final int			  AI_BIGISLAND	 = 0x10000005;
    public	  static final int			  AI_URCHIN	 	 = 0x20000003;

    public    static final int            AI_USER_INPUT  = 0xFFFFFFFF;                          //ユーザ入力

    //attack用
    // ATTACK_攻撃インデックス = 攻撃インデックスの形で記述してください
    public    static final short          ATTACK_NONE = (short)0xFFFF;                       //攻撃なし
    
    public    static final short          ATTACK_0    = (short)0x0000;                               //攻撃0
    public    static final short          ATTACK_1    = (short)0x0001;                               //攻撃1
    public    static final short          ATTACK_2     = (short)0x0002;
    public    static final short          ATTACK_3    = (short)0x0003;                               //左下
    public    static final short          ATTACK_4    = (short)0x0004;                               //下
    public    static final short          ATTACK_5    = (short)0x0005;                               //右下
    public    static final short          ATTACK_6    = (short)0x0006;                               //右
    public    static final short          ATTACK_7    = (short)0x0007;                               //右上
    public    static final short          ATTACK_8    = (short)0x0008;                               //上
    public    static final short          ATTACK_9    = (short)0x0009;                               //左上

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
            case AI_NORM:
            	ai_norm.run(_belong, this);
            	return;
            case AI_BIRD:
            	ai_bird.run(_belong, this);
            	return;
            case AI_SYLPH:
            	ai_sylph.run(_belong, this);
            	return;
            case AI_SHOOT:
            	ai_shoot.run(_belong, this);
            	return;
            case AI_BIRD_SWING:
            	ai_bird_swing.run(_belong, this);
            	return;
            case AI_WAY:
            	ai_way.run(_belong, this);
            	return;
            case AI_AIRSHOO:
            	ai_airshoo.run(_belong, this);
            	return;
            case AI_BIGISLAND:
            	ai_bigisland.run(_belong, this);
            	return;
            case AI_URCHIN:
            	ai_urchin.run(_belong, this);
            case AI_NO_USE     :
                return;
        }
    }
    
    public static short attack_index_to_attack_opcode(int _index){
    	switch(_index){
    	case 0:
    		return ATTACK_0;
    	case 1:
    		return ATTACK_1;
    	case 2:
    		return ATTACK_2;
    	case 3:
    		return ATTACK_3;
    	case 4:
    		return ATTACK_4;
    	case 5:
    		return ATTACK_5;
    	case 6:
    		return ATTACK_6;
    	case 7:
    		return ATTACK_7;
    	case 8:
    		return ATTACK_8;
    	case 9:
    		return ATTACK_9;
    	default:
    		return ATTACK_NONE;
    		
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
            case "ai_norm":
            	return AI_NORM;
            case "ai_bird":
            	return AI_BIRD;
            case "ai_sylph":
            	return AI_SYLPH;
            case "ai_shoot":
            	return AI_SHOOT;
            case "ai_bird_swing":
            	return AI_BIRD_SWING;
            case "ai_way":
            	return AI_WAY;
            case "ai_airshoo":
            	return AI_AIRSHOO;
            case "ai_bigisland":
            	return AI_BIGISLAND;
            case "ai_urchin":
            	return AI_URCHIN;
            default :
                return AI_NO_USE;
        }
    }
}