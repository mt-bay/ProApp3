package common;

/* 2次元座標クラス
 * メソッドによっては、ベクトル的なふるまいも行う
 * ジェネリッククラス(T = {Integer || Float || Double}を想定)
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
    public static point<Integer> DtoI(point<Double> _p){
        return new point<Integer>(_p.x.intValue(), _p.y.intValue());
    }
    /*
     * point<Double> to point<Float>
     * 引数  ：point<Double>
     * 戻り値：point<Float>
     */
    public static point<Float> DtoF(point<Double> _p){
        return new point<Float>(_p.x.floatValue(), _p.y.floatValue());
    }

    /*
     * point<Float> to point<Double>
     * 引数  ：point<Float>
     * 戻り値：point<Double>
     */
    public static point<Double> FtoD(point<Float> _p){
        return new point<Double>(_p.x.doubleValue(), _p.y.floatValue());
    }
    /*
     * point<Float> to point<Integer>
     * 引数  ：point<Float>
     * 戻り値：point<Integer>
     */
    public static point<Integer> FtoI(point<Float> _p){
        return new point<Integer>(_p.x.intValue(), _p.y.intValue());
    }

    /*
     * point<Integer> to point<Double>
     * 引数  ：point<Integer>
     * 戻り値：point<Double>
     */
    public static point<Double> ItoD(point<Integer> _p){
        return new point<Double>(_p.x.doubleValue(), _p.y.doubleValue());
    }
    /*
     * point<Integer> to point<Float>
     * 引数  ：point<Integer>
     * 戻り値：point<Float>
     */
    public static point<Float> ItoF(point<Integer> _p){
        return new point<Float>(_p.x.floatValue(), _p.y.floatValue());
    }



    /*
     * (0, 0)もしくは指定された座標から自座標までのユークリッド距離を求める
     * 引数  ：なし or 原点
     * 戻り値：ユークリッド距離
     */

    public double length() {
        return length(new point<Double>());
    }
    @SuppressWarnings("hiding")
    public <T> double length(point<T> _root) {
        point<Double> p = new point<Double>((Double)x - (Double)_root.x, (Double)y - (Double)_root.y);
        return Math.sqrt((p.x * p.x) + (p.y * p.y));// √(x^2 + y^2);
    }

    /*
     * 変数セット
     * 引数  ：(x, y)
     * 戻り値：なし
     */
    @SuppressWarnings("unchecked")
    private void set(Object _x, Object _y){
        x = (_x.getClass() != null)? (T)_x : null;
        y = (_y.getClass() != null)? (T)_y : null;
    }
}
