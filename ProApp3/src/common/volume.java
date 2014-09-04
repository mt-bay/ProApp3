package common;

/*
 * ボリュームを記録するためのクラス
 */
public class volume{
    /* メンバ変数 */
    private int volume_m; //ボリューム
    /* 定数 */
    private final int VOLUME_MIN =   0; //ボリュームの最小値
    private final int VOLUME_MAX = 100; //ボリュームの最大値

    /* コンストラクタ */
    /*
     * デフォルトコンストラクタ
     * 引数  ：なし
     */
    public volume(){
        volume_m = VOLUME_MAX;
    }

    /*
     * コピーコンストラクタ
     * 引数  ：元データ
     */
    public volume(final volume _obj){
        volume_m = 0;
        add(_obj.volume_m);
    }

    /*
     * 初期状態指定型コンストラクタ
     * 引数  ：初期状態
     */
    public volume(int _vol){
        volume_m = 0;
        add(_vol);
    }

    /*
     * 初期状態指定型コンストラクタ
     * 引数  ：正規化された初期状態
     */
    public volume(float _nomarizeation){
        if(volume_m == 0.0f)
            volume_m = 0;
        else
            volume_m = (int)((VOLUME_MAX - VOLUME_MIN) / volume_m);

        add(0);

    }

    /* メソッド */
    /*
     * toString
     * 引数  ：なし
     * 戻り値：文字列化したインスタンス情報
     */
    public String toString(){
        return String.valueOf(volume_m);
    }

    /*
     * ボリューム値を第1引数分変化させる
     * 引数  ：変化量
     * 戻り値：変化後のvolume_mの値
     */
    public int add(int _var){
        volume_m += _var;

        if(volume_m < VOLUME_MIN)
            volume_m = VOLUME_MIN;
        if(volume_m > VOLUME_MAX)
            volume_m = VOLUME_MAX;

        return volume_m;
    }

    /*
     * getter
     * 引数  ：なし
     * 戻り値：現在のvolume_mの値
     */
    public int get_volume(){
        return volume_m;
    }

    /*
     * 0.0～1.0の範囲に整形されたボリューム値を返す
     * 引数  ：なし
     * 戻り値：0.0～1.0の範囲に整形されたボリューム値
     */
    public float get_nomalization_volume(){
        return (float)volume_m / (VOLUME_MAX - VOLUME_MIN);
    }

}
