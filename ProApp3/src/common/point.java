package common;

/* 2次元座標クラス
 * メソッドによっては、ベクトル的なふるまいも行う
 */
public class point<T> {
    /* メンバ変数 */
    public T x, y; // 2次元座標

    /* コンストラクタ */
    /*
     * デフォルトコンストラクタ
     * 引数：なし
     */
    @SuppressWarnings("hiding")
    public <T> point() {
        set(0.0, 0.0);
    }

    /*
     * コピーコンストラクタ
     * 引数：コピー元
     */
    @SuppressWarnings("hiding")
    public <T> point(point<T> obj) {
        set(obj.x, obj.y);
    }

    /*
     * 座標指定コンストラクタ
     * 引数：(x, y)
     */
    @SuppressWarnings("hiding")
    public <T> point(T X, T Y) {
        set(X, Y);
    }


    /* メソッド */
    /*
     * point<Double> to point<Integer>
     * 引数  ：point<Double >
     * 戻り値：point<Integer>
     */
    public Point<Integer> DtoI(){

    }

    /*
     * (0, 0)もしくは指定された座標から自座標までのユークリッド距離を求める
     * 引数  ：なし or 原点
     * 戻り値：ユークリッド距離
     */
    @SuppressWarnings("hiding")
    public <T> double length() {
        return length(new point<Double>());
    }
    @SuppressWarnings("hiding")
    public <T> double length(point<T> root) {
        point<Double> p = new point<Double>((Double)x - (Double)root.x, (Double)y - (Double)root.y);
        return Math.sqrt((p.x * p.x) + (p.y * p.y));// √(x^2 + y^2);
    }

    /*
     * 変数セット
     * 引数  ：(x, y)
     * 戻り値：なし
     */
    @SuppressWarnings("unchecked")
    private void set(Object X, Object Y){
        x = (X.getClass() != null)? (T)X : null;
        y = (Y.getClass() != null)? (T)Y : null;
    }
}
