package common;

/* 線分クラス
 * (p0, p1)間をつなぐ線分
 */

public class line {
    /* メンバ変数 */
    public point<Integer> p0, p1; // 線分

    /* コンストラクタ */

    /*
     * デフォルトコンストラクタ 引数：なし
     * 目的：((0, 0), (0, 0))の線分作成
     */
    public line() {
        set(new point<Integer>(), new point<Integer>());
    }

    /*
     * コピーコンストラクタ 引数：コピー元 目的：内容のコピー
     */
    public line(line obj) {
        set(obj.p0, obj.p1);
    }

    /*
     * 座標指定型コンストラクタ
     * 引数：p0, p1
     */
    public line(point<Integer> P0, point<Integer> P1) {
        set(P0, P1);
    }

    /* メソッド */

    /*
     * 変数セット
     * 引数  ：p0, p1
     * 戻り値：なし
     */
    private void set(point<Integer> P0, point<Integer> P1) {
        p0 = new point<Integer>(P0);
        p1 = new point<Integer>(P1);
    }

    /*
     * 線分の長さを求める
     * 引数  ：なし
     * 戻り値：線分の長さ
     */
    public double length() {

        return new point<Integer>(p1.x - p0.x, p1.y - p1.y).length();
    }

}
