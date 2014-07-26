package gameObj;

import org.newdawn.slick.Input;
import org.newdawn.slick.SpriteSheet;

import IO.config;

import common.point;
import common.rect;

public class playerObj extends charObj {
    /* メンバ変数 */
    //状態変数
    public boolean    isShooting;     //シューティングモードか
    protected int     t_deform;       //シューティング←→アクションの変形残りフレーム
    protected Input[] ip_prev;        //過去の入力
    protected Input   ip_now;         //最新の入力

    //その他
    private final int num_prev = 6;   //入力の記憶数
    private double    act_mv   = 2.5; //アクションモード時の左右移動力

    //ユーザ入力関係

    /* コンストラクタ */
    /* デフォルトコンストラクタ
     * 引数：なし
     */
    public playerObj() {
        set(new rect()        , new point<Double>(), 0.0d               ,
            Direction.RIGHT   , false              , true               ,
            null              , false              , -1                 ,
            new Input[num_prev]);
    }

    /* メソッド */
    /* 状態アップデート(オーバーライド)
     * 引数  ：なし
     * 戻り値：生成コード
     */
    @Override
    public CreateCode[] update() {
        CreateCode[] cc = null;

        //変形中 or 変形終了時
        if (t_deform >= 0) {
            if (t_deform == 0)
                isShooting = !isShooting;
            t_deform--;
        }
        //通常時
        else
        {
            //共通操作
            //攻撃(ダメージオブジェクト生成)
            if (ip_now.isKeyDown(config.attack)) {

            }

            //左右移動
            if (ip_now.isKeyDown(config.left)) {
                accel.x = -act_mv * ((ip_now.isKeyDown(config.highsp) ? 1 : 2));
                dir = Direction.LEFT;
            }
            else if (ip_now.isKeyDown(config.right)) {
                accel.x = act_mv * ((ip_now.isKeyDown(config.highsp) ? 1 : 2));
                dir = Direction.RIGHT;
            }
            //固有操作
            //アクションモード
            if (!isShooting) {
                //ジャンプ
                if (ip_now.isKeyDown(config.jump) && isGnd) {
                    accel.y = -20.0;
                }
            }
            else {
                //上下操作
                if (ip_now.isKeyDown(config.down)) {
                    accel.y = act_mv * ((ip_now.isKeyDown(config.highsp) ? 1 : 2));
                }
                else if (ip_now.isKeyDown(config.up)) {
                    accel.y = -act_mv * ((ip_now.isKeyDown(config.highsp) ? 1 : 2));
                }
            }
        }

        return cc;
    }

    /*
     * 変数セット
     * 引数  ：それぞれのデータ
     */
    private void set(rect        _rect   , point<Double> _accel       , double  _hp         ,
                     Direction   _dir    , boolean       _isGnd       , boolean _isGravitied,
                     SpriteSheet _texture, boolean       _isShooting  , int     _t_deform   ,
                     Input[]     _ip_prev){
        isShooting = _isShooting;
        t_deform   = _t_deform;
        ip_prev    = _ip_prev;

        set(_rect, _accel, _hp, _dir, _isGnd, _isGravitied, _texture);
    }

}
