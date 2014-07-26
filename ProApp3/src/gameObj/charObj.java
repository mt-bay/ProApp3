package gameObj;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import IO.debugLog;

import common.point;
import common.rect;

public class charObj extends rect {
    /* メンバ変数 */
    public point<Double> accel;      // 移動力
    public double        hp;         // 体力値
    public Direction     dir;        // 向き
    public boolean       isGnd;      // 接地しているか
    public SpriteSheet   texture;    // テクスチャ
    public int           t_state;    // テクスチャの参照場所

    private Stage        im_Here;    //自分はどのステージのオブジェクトか

    // 状態変数
    protected int        invisible_c; // 残りの無敵フレーム数
    public boolean       isGravitied; // 重力に依存するかどうか

    //その他
    protected debugLog   dLog;       // デバッグログ

    /* コンストラクタ・デストラクタ */
    /*
     * デフォルトコンストラクタ
     * 引数：なし
     */
    public charObj() {
        set(new rect(), new point<Double>(), 0.0,
                Direction.LEFT, false, false,
                null);
    }

    /*
     * データ指定型コンストラクタ
     * 引数：それぞれのデータ
     */
    public charObj(rect _rect, point<Double> _accel, double _hp,
            Direction _dir, boolean _isGnd, boolean _isGravitied,
            String _path_texture) {
        SpriteSheet _texture;
        try {
            _texture = new SpriteSheet(_path_texture, _rect.size.x, _rect.size.y);
        }
        catch (SlickException e) {
            dLog.write("SpriteSheet\"" + _path_texture + "\" load failed");
            _texture = null;
        }
        set(_rect   , _accel, _hp         ,
            _dir    , _isGnd, _isGravitied,
            _texture);
    }

    /* メソッド */
    /*
     * 状態アップデート
     * 引数：なし
     */
    public CreateCode[] update() {
        CreateCode[] cc = null;

        return cc;
    }

    /*
     * 描画
     * 引数  ：なし or 描画倍率
     * 戻り値
     */
    public void draw() {

    }

    public void draw(float _scale) {
        if (texture == null)
            return;

        point<Float> _loc_f = point.DtoF(location), _size_f = point.ItoF(size);

        texture.startUse();
        texture.getSubImage(t_state, 0).drawEmbedded(_loc_f.x, _loc_f.y, _size_f.x, _size_f.y);
        texture.endUse();
    }

    /*
     * 変数セット
     * 引数：それぞれのデータ
     */
    protected void set(rect        _rect   , point<Double> _accel, double  _hp         ,
                       Direction   _dir    , boolean       _isGnd, boolean _isGravitied,
                       SpriteSheet _texture){
        accel       = _accel;
        hp          = _hp;
        dir         = _dir;
        isGnd       = _isGnd;
        isGravitied = _isGravitied;
        texture     = _texture;

        set(_rect.location, _rect.size);
    }
}
