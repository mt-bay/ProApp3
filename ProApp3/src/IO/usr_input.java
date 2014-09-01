package IO;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

/*
 * Slick2DのInputのラッパークラス
 *
 */
public class usr_input {
    /* メンバ変数 */
    public boolean attack;
    public boolean jump;
    public boolean highsp;
    public boolean change;
    public boolean left;
    public boolean down;
    public boolean up;
    public boolean right;
    public boolean ok;
    public boolean cancel;

    /* コンストラクタ */
    /*
     * デフォルトコンストラクタ
     * 引数  ：なし
     */
    public usr_input(){
        init(false, false, false, false,
             false, false, false, false,
             false, false);
    }

    /*
     * コピーコンストラクタ
     * 引数  ：元データ
     */
    public usr_input(usr_input _obj){
        init(_obj.attack, _obj.jump, _obj.highsp, _obj.change,
             _obj.left  , _obj.down, _obj.up    , _obj.right ,
             _obj.ok    , _obj.cancel);
    }

    /*
     * データ指定形コンストラクタ
     * 引数  ：各種データ
     */
    private usr_input(boolean _attack, boolean _jump , boolean _highsp, boolean _change,
                      boolean _left  , boolean _down , boolean _up    , boolean _right ,
                      boolean _ok    , boolean _cancel){
        init(_attack, _jump  , _highsp, _change,
             _left  , _down  , _up    , _right ,
             _ok    , _cancel);


    }

    /* メソッド */
    /*
     * 入力を基にデータ作成
     * 引数  ：ゲームコンテナ
     * 戻り値：ユーザ入力
     */
    public static usr_input input_to_usr_input(GameContainer gc){
        Input ip = gc.getInput();
        config cfg = config.getInstance();

        return new usr_input(ip.isKeyDown(cfg.attack), ip.isKeyDown(cfg.jump)  , ip.isKeyDown(cfg.highsp), ip.isKeyDown(cfg.change),
                             ip.isKeyDown(cfg.left)  , ip.isKeyDown(cfg.down)  , ip.isKeyDown(cfg.up)    , ip.isKeyDown(cfg.right) ,
                             ip.isKeyDown(cfg.ok)    , ip.isKeyDown(cfg.cancel));
    }

    /* toString
     * 引数  ：なし
     * 戻り値：文字列化したインスタンス情報
     */
    public String toString(){
        String str = "";
        str += " " + ((attack)? Input.getKeyName(config.getInstance().attack) : "");
        str += " " + ((jump  )? Input.getKeyName(config.getInstance().jump  ) : "");
        str += " " + ((highsp)? Input.getKeyName(config.getInstance().highsp) : "");
        str += " " + ((change)? Input.getKeyName(config.getInstance().change) : "");
        str += " " + ((left  )? Input.getKeyName(config.getInstance().left  ) : "");
        str += " " + ((down  )? Input.getKeyName(config.getInstance().down  ) : "");
        str += " " + ((up    )? Input.getKeyName(config.getInstance().up    ) : "");
        str += " " + ((right )? Input.getKeyName(config.getInstance().right ) : "");
        str += " " + ((ok    )? Input.getKeyName(config.getInstance().ok    ) : "");
        str += " " + ((cancel)? Input.getKeyName(config.getInstance().cancel) : "");

        return str;
    }


    /*
     * 初期化
     * 引数  ：各種データ
     * 戻り値：なし
     */
    private void init(boolean _attack, boolean _jump , boolean _highsp, boolean _change,
                      boolean _left  , boolean _down , boolean _up    , boolean _right ,
                      boolean _ok    , boolean _cancel){
        attack = _attack;
        jump   = _jump;
        highsp = _highsp;
        change = _change;
        left   = _left;
        down   = _down;
        up     = _up;
        right  = _right;
        ok     = _ok;
        cancel = _cancel;
    }

}
