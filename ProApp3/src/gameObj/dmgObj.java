package gameObj;

import common.point;
import common.rect;

public class dmgObj extends rect {
    /* メンバ変数 */
    public point<Double>  accel; // 移動力
    public double         atk;   // charObj衝突時の体力変動値

    /* コンストラクタ */
    /*
     * デフォルトコンストラクタ 引数：なし
     */
    public dmgObj() {
        set(new rect(), new point<Double>(), 0.0);
    }

    /*
     * コピーコンストラクタ
     * 引数：コピー元
     */
    public dmgObj(dmgObj obj) {
        set(obj.location, obj.size, obj.accel, obj.atk);
    }

    /*
     * 値指定型コンストラクタ
     * 引数：それぞれのデータ
     */
    public dmgObj(rect _rect, point<Double> _accel, double _atk) {
        set(_rect, _accel, _atk);
    }


    /* メソッド */
    /*
     * setter
     * 引数：それぞれのデータ
     */
    private void set(rect _rect, point<Double> _accel, double _atk) {
        set(_rect.location, _rect.size, _accel, _atk);
    }
    private void set(point<Double> _location, point<Integer> _size, point<Double> _accel, double _atk) {
        accel = new point<Double>(accel);
        atk   = _atk;
        set(_location, _size);
    }

    /* ファイル->
     *
     */

}
