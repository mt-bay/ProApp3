package gameObj;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Queue;

import common.point;
import common.rect;

public class Stage {
    /* クラス内変数 */
    public playerObj          player_data;
    public mapObj             map_data;
    public ArrayList<dmgObj>  damage;
    public ArrayList<charObj> person;
    //状態管理
    public boolean            is_end;
    public point<Float>       camera_location;
    //オブジェクト生成コール用スタック
    public Queue<dmgObj>      create_dmg;
    public Queue<charObj>     create_person;


    /* コンストラクタ */

    /*
     * ファイルからステージを生成する
     * ファイルの内容は1行ごとに区切られ、
     *
     * プレイヤーオブジェクト読み込み用スクリプトへの相対パス
     * マップオブジェクト読み込み用スクリプトへの相対パス
     * ダメージオブジェクト読み込み用スクリプWトへの相対パス
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
            map_data    = mapObj.file_to_mapObj(str, this);

            str         = bRead.readLine();
            damage      = dmgObj.file_to_dmgObj_ArrayList(str, this);

            str         = bRead.readLine();
            person      = charObj.file_to_charObj_ArrayList(str, this);

            bRead.close();
        }catch(Exception e){
        }


    }

    /* メソッド */
    /* カメラからの相対座標の導出
     * 引数  ：元データ
     * 戻り値：カメラからの相対座標
     */
    public point<Double > relative_camera_d(point<Double > _obj){
        return new point<Double>(_obj.x - camera_location.x.doubleValue(),
                                 _obj.y - camera_location.y.doubleValue());
    }
    public point<Float  > relative_camera_f(point<Float  > _obj){
        return new point<Float>(_obj.x - camera_location.x,
                                _obj.y - camera_location.y);
    }
    public point<Integer> relative_camera_i(point<Integer> _obj){
        return new point<Integer>(_obj.x - camera_location.x.intValue(),
                                  _obj.y - camera_location.y.intValue());
    }

    /* カメラからの相対座標を持つrectの導出
     * 引数  ：元データ
     * 戻り値：カメラからの相対座標データを持つrect
     */
    public rect relative_camera_rect(rect _obj){
        return new rect(relative_camera_d (_obj.location),
                        new point<Integer>(_obj.size    ));
    }

    /*
     * 1フレーム中の入力、移動処理
     * 引数  ：なし
     * 戻り値：なし
     */
    public void update(){
        //状態アップデート
        // プレイヤーオブジェクト
        player_data.update();
        // キャラクタオブジェクト
        for(int i = 0; i < person.size(); i++)
            person.get(i).update();

        // ダメージオブジェクト
        for(int i = 0; i < damage.size(); i++)
            damage.get(i).update();

        //移動処理
        // プレイヤーオブジェクト
        player_data.move();

        // キャラクタオブジェクト
        for(int i = 0; i < person.size(); i++)
            person.get(i).move();

        //ダメージオブジェクト
        for(int i = 0; i < damage.size(); i++)
            damage.get(i).move();

        //追加処理
        // キャラクタオブジェクト
        while(!create_person.isEmpty())
            person.add(create_person.remove());

        // ダメージオブジェクト
        while(!create_dmg.isEmpty())
            damage.add(create_dmg.remove());

        return;
    }

    /*
     * ステージの描画処理
     */







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
