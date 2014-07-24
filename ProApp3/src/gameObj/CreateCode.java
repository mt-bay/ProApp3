package gameObj;

import common.point;

/* (Char or Player)Object -(オブジェクト生成指示)-> Stage用クラス
 */
public class CreateCode {
	/* メンバ変数 */
	public int   objCode;
	public point where;

	/* コンストラクタ */
	/* デフォルトコンストラクタ
	 * 引数  ：なし
	 */
	public CreateCode(){
		set(0x00000000, new point());
	}
	/* 各種データ付きコンストラクタ
	 * 引数  ：各種データ
	 */
	public CreateCode(int _objCode, point _where){
		set(_objCode, _where);
	}

	/* メソッド */
	/* 変数セット
	 * 引数  ：各種データ
	 * 戻り値：なし
	 */
	private void set(int _objCode, point _whrere){
		objCode = _objCode;
		where   = _whrere;
	}
}
