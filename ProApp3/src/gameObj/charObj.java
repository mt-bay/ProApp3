package gameObj;

import org.newdawn.slick.SpriteSheet;

import common.point;
import common.rect;

public class charObj extends rect {
    /* メンバ変数 */
    // 基本的
    public point       accel;      // 移動力
    public double      hp;         // 体力値
    public Direction   dir;        // 向き
    public boolean     isGnd;      // 接地しているか
    public SpriteSheet texture;    // テクスチャ
                                    
    // 状態変数
    protected int      t_invisible; // 残りの無敵フレーム数
    public boolean     isGravitied; // 重力に依存するかどうか
                                    
    /*
     * デフォルトコンストラクタ 引数：なし
     */
    public charObj() {
        set(new rect(), new point(), 0.0, Direction.LEFT, false, false);
    }
    
    /*
     * データ指定型コンストラクタ 引数：それぞれのデータ
     */
    public charObj(rect _rect, point _accel, double _hp, Direction _dir,
            boolean _isGnd, boolean _isGravitied) {
        set(_rect, _accel, _hp, _dir, _isGnd, _isGravitied);
    }
    
    /* メソッド */
    /*
     * 状態アップデート 引数：なし
     */
    public CreateCode[] update() {
        CreateCode[] cc = null;
        
        return cc;
    }
    
    /*
     * 変数セット 引数：それぞれのデータ
     */
    protected void set(rect _rect, point _accel, double _hp, Direction _dir,
            boolean _isGnd, boolean _isGravitied) {
        accel = _accel;
        hp = _hp;
        dir = _dir;
        isGnd = _isGnd;
        isGravitied = _isGravitied;
        set(_rect.location, _rect.size);
    }
}
