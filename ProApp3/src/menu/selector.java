package menu;


/*
 * セレクタクラス
 * カーソル操作などを行うためのクラス
 */
public class selector {
    /* メンバ変数 */
    protected              int     index;    //選択中の要素

    protected              int     length;   //選択し得る要素数
    protected              boolean do_loop;  //カーソルが要素からあふれる場合、ループさせるか

    /* コンストラクタ */


    /* メソッド */

    /*
     * getter
     * 選択中の要素番号を返す
     * 引数  ：なし
     * 戻り値：なし
     */
    public final int get(){
        return index;
    }

    /*
     * 選択中の要素をargv[1]分進める
     * 引数  ：どれだけ進めるか
     * 戻り値：なし
     */
    public void add(int var){
        index += var;
        if(do_loop){
            if(index > get_max()){
                int mod = index - get_max();
                index   = get_min();
                add(mod);
            }
            if(index < get_min()){
                int mod = index + get_min();
                index   = get_max();
                add(mod);
            }
            return;
        }
        else{

            if(index > get_max())
                index = get_max();
            if(index < get_min())
                index = get_min();
            return;
        }
    }

    /*
     * 選択要素のインクリメント
     * 引数  ：なし
     * 戻り値：なし
     */
    public void increment(){
        add(1);
    }

    /*
     * 選択要素のデクリメント
     * 引数  ：なし
     * 戻り値：なし
     */
    public void decrement(){
        add(-1);
    }

    /*
     * セレクタが持ちうる最大値を取得する
     * 引数  ：なし
     * 戻り値：セレクタが持ちうる最大値
     */
    public final int get_max(){
        return length - 1;
    }

    /*
     * セレクタが持ちうる最小値を取得する
     * 引数  ：なし
     * 戻り値：セレクタが持ちうる最小値
     */
    public final int get_min(){
        return 0;
    }

}
