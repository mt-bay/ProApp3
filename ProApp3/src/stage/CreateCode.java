package stage;

import common.point;

/* (Char or Player)Object -(オブジェクト生成指示)-> Stage用クラス
 */
public class CreateCode {
    /* メンバ変数 */
    public int           objCode;
    public point<Double> where;

    /* コンストラクタ */
    /*
     * デフォルトコンストラクタ 引数 ：なし
     */
    public CreateCode() {
        set(0x00000000, new point<Double>());
    }

    /*
     * 各種データ付きコンストラクタ 引数 ：各種データ
     */
    public CreateCode(int _objCode, point<Double> _where) {
        set(_objCode, _where);
    }

    /* メソッド */
    /*
     * 変数セット 引数 ：各種データ 戻り値：なし
     */
    private void set(int _objCode, point<Double> _whrere) {
        objCode = _objCode;
        where = _whrere;
    }
}
