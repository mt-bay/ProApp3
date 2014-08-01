package gameObj;

import java.util.ArrayList;
import java.util.Stack;

public class Stage {
    /* クラス内変数 */
    public playerObj          player_data;
    public mapObj             map_data;
    public ArrayList<dmgObj>  damage;
    public ArrayList<charObj> person;
    //状態管理
    public boolean            is_end;
    //オブジェクト生成コール用スタック
    public Stack<dmgObj>      create_dmg;
    public Stack<charObj>     create_person;



    /* コンストラクタ */
    
    /* ファイルからステージを生成する
     * 引数：ファイル名
     */
    public Stage(String _mName) {
    	
    }
    
    
    
    private void set(playerObj          _player_data, mapObj _map_data, ArrayList<dmgObj> _damage,
    		         ArrayList<charObj> _person     ){
    	player_data = new playerObj(_player_data);
    }
}
