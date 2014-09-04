package AI;

import stage.charObj;
import window.Main;

/*
 * 移動用命令コードクラス
 */
public class action_opcode {
    /* メンバ変数 */
    //命令コード
    public int attack;         //攻撃
    public int move_lr;        //左右移動(右方向が正)
    public int move_ud;        //上下移動(下方向が正)
    public int move_jump;      //ジャンプ移動
    public int unique;         //独自な行動
    public int texture_number; //使用するテクスチャ番号

    //使用するAI
    public int using_AI;  //使用するAI

    /* 定数 */
    //使用するAIの決定用
    public static final int AI_NO_USE      = 0x00000000; //どのAIも使用しない
    public static final int AI_USER_INPUT  = 0xFFFFFFFF; //ユーザ入力

    //attack用
    public static final int ATTACK_NONE  =  0; //攻撃なし
    public static final int ATTACK_NOMAL =  1; //通常攻撃

    //move_lr用
    public static final int MOVE_LR_NONE               =  0; //左右移動なし

    //
    public static final int MOVE_LEFT               = 0x01000000; //左
    public static final int MOVE_UP                 = 0x02000000; //上
    public static final int MOVE_DOWN               = 0x04000000; //下
    public static final int MOVE_RIGHT              = 0x08000000; //右
    public static final int MOVE_NOMAL              = 0x00100000; //通常移動
    public static final int MOVE_HIGHSPEED          = 0x00200000; //高速移動
    public static final int MOVE_DO                 = 0x00000000; //変更
    public static final int MOVE_CONTINUE           = 0x10000000; //継続

    public static final int MOVE_LR_LEFT_DO            = -1; //左に通常移動開始
    public static final int MOVE_LR_LEFT_HIGHSPEED_DO  = -2; //左に高速移動開始
    public static final int MOVE_LR_LEFT_CONTINUE      = -3; //左に移動継続

    public static final int MOVE_LR_RIGHT_DO           =  1; //右に通常移動開始
    public static final int MOVE_LR_RIGHT_HIGHSPEED_DO =  2; //右に高速移動開始
    public static final int MOVE_LR_RIGHT_CONTINUE     =  3; //右に移動継続
    //move_ud用
    public static final int MOVE_UD_NONE              =  0; //上下移動なし

    public static final int MOVE_UD_UP_DO             = -1; //上に通常移動開始
    public static final int MOVE_UD_UP_HIGHSPEED_DO   = -2; //上に高速移動開始
    public static final int MOVE_UD_UP_CONTINUE       = -3; //上に移動継続

    public static final int MOVE_UD_DOWN_DO           =  1; //下に通常移動開始
    public static final int MOVE_UD_DOWN_HIGHSPEED_DO =  2; //下に高速移動開始
    public static final int MOVE_UD_DOWN_CONTINUE     =  3; //下に移動継続
    //move_jump用
    public static final int MOVE_JUMP_NONE     =  0; //ジャンプなし

    public static final int MOVE_JUMP_DO       =  1; //ジャンプ開始
    public static final int MOVE_JUMP_CONTINUE =  2; //ジャンプ継続
    //unique用
    public static final int UNIQUE_NONE    =  0; //独自処理なし

    public static final int UNIQUE_DEFORM  =  1; //変形処理(USER_INPUTでの使用を想定)

    /* コンストラクタ */
    /*
     * デフォルトコンストラクタ
     * 引数  ：なし
     */
    public action_opcode(){
        attack         = ATTACK_NONE;
        move_lr        = MOVE_LR_NONE;
        move_ud        = MOVE_UD_NONE;
        unique         = UNIQUE_NONE;
        texture_number = 0;

        using_AI       = AI_NO_USE;
    }

    /*
     * 使用するAIを指定するタイプのAI
     * 引数  ：使用するAI
     */
    public action_opcode(int _use_ai){
        attack         = ATTACK_NONE;
        move_lr        = MOVE_LR_NONE;
        move_ud        = MOVE_UD_NONE;
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
        if(Main.user_input.get(0).attack)
            attack = ATTACK_NOMAL;

        //左右移動
        // 左右両方 or 入力なし
        if     (( Main.user_input.get(0).left &&
                  Main.user_input.get(0).right  ) ||
                (!Main.user_input.get(0).left &&
                 !Main.user_input.get(0).right  )){

        }
        // 右
        else if(Main.user_input.get(0).left)
            move_lr = ((Main.user_input.get(1).left)? MOVE_LR_LEFT_CONTINUE :
                                                      ((Main.user_input.get(0).highsp)?MOVE_LR_LEFT_HIGHSPEED_DO : MOVE_LR_LEFT_DO));
        // 左
        else if(Main.user_input.get(0).right)
            move_lr = (Main.user_input.get(1).right)? MOVE_LR_RIGHT_CONTINUE :
                                                      ((Main.user_input.get(0).highsp)? MOVE_LR_LEFT_HIGHSPEED_DO : MOVE_LR_RIGHT_DO);

        //上下移動
        move_ud = MOVE_UD_NONE;
        if(Main.user_input.get(0).up  &&
           Main.user_input.get(0).down){

        }
        else if(Main.user_input.get(0).up)
            move_ud = (Main.user_input.get(0).highsp)? MOVE_UD_UP_HIGHSPEED   : MOVE_UD_UP;
        else if(Main.user_input.get(0).down)
            move_ud = (Main.user_input.get(0).highsp)? MOVE_UD_DOWN_HIGHSPEED : MOVE_UD_DOWN;

        //ジャンプ
        if(Main.user_input.get(0).jump)
            move_jump = (Main.user_input.get(1).jump)? MOVE_JUMP_CONTINUE : MOVE_JUMP_DO;
        else
            move_jump = MOVE_JUMP_NONE;

        //ユニーク操作
        if(Main.user_input.get(0).change)
            unique    = UNIQUE_DEFORM;

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
