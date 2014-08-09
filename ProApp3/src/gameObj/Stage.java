package gameObj;

import java.io.BufferedReader;
import java.io.FileReader;
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
     * ファイルの内容は1行ごとに区切られ、
     *
     * プレイヤーオブジェクト読み込み用スクリプトへの相対パス
     * マップオブジェクト読み込み用スクリプトへの相対パス
     * ダメージオブジェクト読み込み用スクリプトへの相対パス
     * キャラクタオブジェクト読み込み用スクリプトへの相対パス
     *
     * を読み込む
     * 引数：ファイル名
     */
    public Stage(String _file_name) {
        try{
            BufferedReader bRead = new BufferedReader(new FileReader(_file_name));
            String         str   = "";

            str         = bRead.readLine();
            player_data = playerObj.file_to_playerObj(str, this);

            str         = bRead.readLine();
            map_data    = mapObj.file_to_mapObj(str);

            str         = bRead.readLine();
            damage      = dmgObj.file_to_dmgObj_ArrayList(str);

            str         = bRead.readLine();
            person      = charObj.file_to_charObj_ArrayList(str, this);

            bRead.close();
        }catch(Exception e){
        }
    }

    /* ステージ全体の描画
     * 引数  ：なし
     * 戻り値：なし
     */
    public void draw(){
        map_data.draw();
        for(int i = 0; i < person.size(); i++){
            person.get(i).draw();
        }
        player_data.draw();
        for(int i = 0; i < person.size(); i++){
            damage.get(i).draw();
        }
    }




}
