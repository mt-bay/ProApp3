package AI;

import stage.charObj;
import window.Main;

/*
 * 移動用命令コードクラス
 */
public class ai_op {
    /* メンバ変数 */
    //命令コード
    public short attack;       //攻撃
    public short move;         //移動・ジャンプ
    public short unique;       //独自な行動
    public int texture_number; //使用するテクスチャ番号

    //使用するAI
    public int using_AI;  //使用するAI

    /* 定数 */
    //使用するAIの決定用
    public static final int AI_NO_USE      = 0x00000000; //どのAIも使用しない
    public static final int AI_USER_INPUT  = 0xFFFFFFFF; //ユーザ入力

    //attack用
    public static final short ATTACK_NONE  =  0x0000; //攻撃なし
    public static final short ATTACK_NOMAL =  0x0001; //通常攻撃

    //move用
    public static final short MOVE_NONE           = (short)0x0000; //左右移動なし

    public static final short MOVE_DIR            = (short)0x0F00; //方向
    public static final short MOVE_DIR_LEFT       = (short)0x0100; //左
    public static final short MOVE_DIR_UP         = (short)0x0200; //上
    public static final short MOVE_DIR_DOWN       = (short)0x0400; //下
    public static final short MOVE_DIR_RIGHT      = (short)0x0800; //右
    public static final short MOVE_DIR_LEFT_RIGHT = MOVE_DIR_LEFT | MOVE_DIR_RIGHT; //左右
    public static final short MOVE_DIR_UP_DOWN    = MOVE_DIR_UP   | MOVE_DIR_DOWN;  //上下

    public static final short MOVE_MOVE           = (short)0x00F0; //移動種類
    public static final short MOVE_MOVE_NOMAL     = (short)0x0010; //通常移動
    public static final short MOVE_MOVE_HIGHSPEED = (short)0x0020; //高速移動

    public static final short MOVE_JUMP           = (short)0x000F; //ジャンプ
    public static final short MOVE_JUMP_NOMAL     = (short)0x0001; //通常ジャンプ

    //unique用
    public static final int   UNIQUE_NONE         =  0x00000000; //独自処理なし

    public static final int   UNIQUE_DEFORM       =  0x00000001; //変形処理(USER_INPUTでの使用を想定)

    /* コンストラクタ */
    /*
     * デフォルトコンストラクタ
     * 引数  ：なし
     */
    public ai_op(){
        attack         = ATTACK_NONE;
        move           = MOVE_NONE;
        unique         = UNIQUE_NONE;
        texture_number = 0;

        using_AI       = AI_NO_USE;
    }

    /*
     * コピーコンストラクタ
     * 引数  ：コピー元
     */
    public ai_op(ai_op _obj){
        attack         = _obj.attack;
        move           = _obj.move;
        unique         = _obj.unique;
        texture_number = _obj.texture_number;

        using_AI       = _obj.using_AI;
    }

    /*
     * 使用するAIを指定するタイプのAI
     * 引数  ：使用するAI
     */
    public ai_op(int _use_ai){
        attack         = ATTACK_NONE;
        move           = MOVE_NONE;
        unique         = UNIQUE_NONE;
        texture_number = 0;

        using_AI       = _use_ai;
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
                ai_user_input();
                return;
            case AI_NO_USE     :
                return;
        }
    }



    /*
     * ユーザー入力を基にして入力生成
     * 引数  ：なし
     * 戻り値：なし
     */
    private void ai_user_input(){
        //攻撃
        attack = ATTACK_NONE;
        if(Main.user_input.get(0).attack){
            attack = ATTACK_NOMAL;
        }

        //移動
        move = MOVE_NONE;
        // 移動方向の決定
        //  左右両方 or 入力なし
        if     (( Main.user_input.get(0).left &&
                  Main.user_input.get(0).right  ) ||
                (!Main.user_input.get(0).left &&
                 !Main.user_input.get(0).right  )){

        }
        //  左
        else if(Main.user_input.get(0).left){
            move += MOVE_DIR_LEFT;
        }
        //  右
        else if(Main.user_input.get(0).right){
            move += MOVE_DIR_RIGHT;
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
            move += MOVE_DIR_UP;
        }
        //  下
        else if(Main.user_input.get(0).down){
            move += MOVE_DIR_DOWN;
        }

        // 移動速度の決定
        //  移動フラグが立っている (= 方向フラグが立ってる)ならば、高速移動か判定して追加
        if((move  & MOVE_DIR) != MOVE_NONE){
            move += (Main.user_input.get(0).highsp)? MOVE_MOVE_HIGHSPEED : MOVE_MOVE_NOMAL;
        }

        // ジャンプの決定
        if(Main.user_input.get(0).jump){
            move += MOVE_JUMP_NOMAL;
        }

        //ユニーク操作
        unique = UNIQUE_NONE;
        // モードチェンジ
        if(Main.user_input.get(0).change)
            unique += UNIQUE_DEFORM;

    }

    public static int name_to_using_ai(String _name){
        switch(_name.toLowerCase()){
            case "user_input"   :
            case "ai_user_input":
                return AI_USER_INPUT;
            default :
                return AI_NO_USE;
        }
    }



}
