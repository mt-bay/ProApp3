package common;

/* 長方形クラス
 *
 */
public class rect {
    /* メンバ変数 */
    public point<Double > location; // 座標
    public point<Integer> size;    // サイズ

    /* コンストラクタ */
    /*
     * デフォルトコンストラクタ 引数：なし 目的：座標(0, 0), サイズ(0, 0)の四角形生成
     */
    public rect() {
        set(new point<Double>(), new point<Integer>());
    }

    /*
     * 座標つきコンストラクタ 引数：座標 目的：座標のみ指定，サイズ(0, 0)の四角形生成
     */
    public rect(point<Double> _location) {
        set(_location, new point<Integer>());
    }

    /*
     * 座標つき正方形コンストラクタ 引数：座標，正方形の1辺のサイズ 目的：座標指定された正方形を生成する
     */
    public rect(point<Double> Loc, int len) {
        set(Loc, new point<Integer>(len, len));
    }

    /*
     * 座標付き長方形コンストラクタ 引数：座標, サイズ 目的：座標, サイズそれぞれを指定した長方形を生成する
     */
    public rect(point<Double> _location, point<Integer> _size) {
        set(_location, _size);
    }

    /* メソッド */

    /*
     * それぞれの端点を返す
     * 引数  ：なし
     * 戻り値：左上 or 右上 or 左下 or 右下
     */
    public  point<Double> UpperLeft() {
        return new point<Double >(location.x                , location.y                 );
    }
    public point<Double> UpperRight() {
        return new point<Double>(location.x + (double)size.x, location.y                 );
    }

    public point<Double> LowerLeft() {
        return new point<Double>(location.x                 , location.y + (double)size.y);
    }
    public point<Double> LowerRight() {
        return new point<Double>(location.x + (double)size.x, location.y + (double)size.y);
    }

    /*
     * 変数セット 引数 ：セットする変数 戻り値：なし 目的 ：引数の変数をセット
     */
    protected void set(point<Double > _location, point<Integer> _size) {
        location = new point<Double >(_location);
        size     = new point<Integer>(_size);
    }

}
