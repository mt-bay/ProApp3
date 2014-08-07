package gameObj;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import IO.debugLog;

import common.point;
import common.rect;

public class charObj extends rect {
    /* メンバ変数 */
    public           point<Double> accel;             //移動力
    public           double        hp;                //体力値
    public           Direction     dir;               //向き
    public           boolean       is_gnd;            //接地しているか

    public           SpriteSheet   texture;           //テクスチャ
    public           int           t_state;           //テクスチャの参照場所

    public           Stage         belong;            //自オブジェクトの所属ステージ

    // 状態変数
    protected        int           timer_not_visible; //残りの無敵フレーム数
    public           boolean       is_gravitied;      //重力に依存するかどうか

    //その他
    protected static debugLog      dLog;       // デバッグログ

    /* コンストラクタ・デストラクタ */
    /*
     * デフォルトコンストラクタ
     * 引数：なし
     */
    public charObj() {
        set(new rect()    , new point<Double>(), 0.0  ,
            Direction.LEFT, false              , false,
            null          , null               );
    }

    /*
     * データ指定型コンストラクタ
     * 引数：それぞれのデータ
     */
    public charObj(rect      _rect        , point<Double> _accel     , double _hp          ,
                   Direction _dir         , boolean       _isGnd     , boolean _is_gravitied,
                   String    _path_texture, Stage         _belong){
        SpriteSheet _texture;
        try {
            _texture = new SpriteSheet(_path_texture, _rect.size.x, _rect.size.y);
        }
        catch (SlickException e) {
            dLog.write("SpriteSheet\"" + _path_texture + "\" load failed");
            _texture = null;
        }
        set(_rect   , _accel     , _hp         ,
            _dir    , _isGnd     , _is_gravitied,
            _texture, _belong);
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
     * 引数  ：なし                 or
     *         カメラ位置           or
     *         カメラ位置, 描画倍率
     * 戻り値：なし
     */
    public void draw() {
        draw(new point<Float>(0.0f, 0.0f), 1.0f);
    }
    public void draw(point<Float> _camera_location){
        draw(_camera_location, 1.0f);
    }
    public void draw(point<Float> _camera_location, float _scale) {
        if (texture == null)
            return;
        point<Float> _loc_f = new point<Float>(location.x.floatValue() - _camera_location.x,
                                               location.y.floatValue() - _camera_location.y);
        point<Float> _size_f = point.ItoF(size);

        texture.startUse();
        texture.getSubImage(t_state, 0).drawEmbedded(_loc_f.x, _loc_f.y, _size_f.x, _size_f.y);
        texture.endUse();
    }

    /*
     * 変数セット
     * 引数：それぞれのデータ
     *
     */
    protected void set(rect        _rect   , point<Double> _accel, double  _hp         ,
                       Direction   _dir    , boolean       _isGnd, boolean _isGravitied,
                       SpriteSheet _texture, Stage         _belong){
        accel        = _accel;
        hp           = _hp;
        dir          = _dir;
        is_gnd       = _isGnd;
        is_gravitied = _isGravitied;
        texture      = _texture;

        set(_rect.location, _rect.size);

        belong       = _belong;
    }
}
