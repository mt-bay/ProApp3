package gameObj;

import java.util.*;

public class Stage {
    /* クラス内変数 */
    public playerObj          p;
    public mapObj[][]         mapdata;
    public ArrayList<dmgObj>  damage;
    public ArrayList<charObj> person;
    //状態管理
    public boolean            is_end;
    //オブジェクト生成コール用スタック
    public Stack<dmgObj>      create_dmg;
    public Stack<charObj>     create_person;



    /* コンストラクタ */

    public Stage(String _mName) {

    }



}
