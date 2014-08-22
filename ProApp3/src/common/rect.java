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
        init(new point<Double>(0, 0), new point<Integer>(0, 0));
    }

    /*
     * 座標つきコンストラクタ 引数：座標 目的：座標のみ指定，サイズ(0, 0)の四角形生成
     */
    public rect(point<Double> _location) {
        init(_location, new point<Integer>());
    }

    /*
     * 座標つき正方形コンストラクタ 引数：座標，正方形の1辺のサイズ 目的：座標指定された正方形を生成する
     */
    public rect(point<Double> Loc, int len) {
        init(Loc, new point<Integer>(len, len));
    }

    /*
     * 座標付き長方形コンストラクタ 引数：座標, サイズ 目的：座標, サイズそれぞれを指定した長方形を生成する
     */
    public rect(point<Double> _location, point<Integer> _size) {
        init(_location, _size);
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
     * 引数の長方形と自オブジェクトが交差 or 内包 or 被内包の関係にあるか
     * 引数  ：調査する長方形
     * 戻り値：交差 or 内包 or 被内包の関係にあるか
     */
    public boolean is_collision(rect _obj){
        //内包
        if(this.UpperLeft ().x >= _obj.UpperLeft ().x &&
           this.UpperRight().x <= _obj.UpperRight().x &&
           this.UpperLeft ().y <= _obj.UpperLeft ().y &&
           this.LowerLeft ().y >= _obj.LowerLeft ().y)
            return true;

        //被内包
        if(this.UpperLeft ().x <= _obj.UpperLeft ().x &&
           this.UpperRight().x >= _obj.UpperRight().x &&
           this.UpperLeft ().y >= _obj.UpperLeft ().y &&
           this.LowerLeft ().y <= _obj.LowerLeft ().y)
            return true;

        //交差
        if(this.UpperRight().x >= _obj.UpperLeft ().x && //自分の右端より対象の左端が左
           this.UpperLeft ().x <= _obj.UpperRight().x && //自分の左端より対象の右端が右
           this.LowerLeft ().y >= _obj.UpperLeft ().y && //自分の下端より対象の上端が上
           this.UpperLeft ().y <= _obj.LowerLeft ().y)   //自分の上端より対象の下端が下
            return true;

        //どれでもない
        return false;
    }

    /*
     * 変数セット 引数 ：セットする変数 戻り値：なし 目的 ：引数の変数をセット
     */
    protected void init(point<Double> _location, point<Integer> _size) {
        location = new point<Double >(_location);
        size     = new point<Integer>(_size);
    }




}
