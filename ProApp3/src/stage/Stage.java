package stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import org.newdawn.slick.Graphics;

import window.Main;
import window.window;
import IO.debugLog;
import common.point;
import common.rect;


public class Stage {
    /* 定数 */
    // プレイヤオブジェクトの、下底の中心をどこに映すか
    public final point<Float> PLAYER_SHOOT = new point<Float>((window.SIZE.x.floatValue() * 3 / 4), window.SIZE.y.floatValue()  / 2);

    /* メンバ変数・インスタンス */
    public point<Double>     gravitiy;

    //現フレームのオブジェクト
    public playerObj            player_data;     //プレイヤーオブジェクト
    public mapObj               map_data;        //マップオブジェクト
    public ArrayList<dmgObj>    damage;          //ダメージオブジェクト
    public ArrayList<charObj>   person;          //キャラクタオブジェクト
    //状態管理
    public boolean              is_end;          //このフレームで抜けるか

    public point<Float>         camera_location; //カメラ位置
    //オブジェクト生成コール用待ち行列
    public Queue<dmgObj>        create_dmg;      //ダメージオブジェクト
    public Queue<charObj>       create_person;   //キャラクタオブジェクト

    //過去のオブジェクトデータ
    public ArrayList<playerObj> player_prev;     //プレイヤーオブジェクト

    /* 定数 */
    public final int PREV_MAX = 5; //過去のオブジェクトデータの最大保存数

    /* コンストラクタ */

    /*
     * ファイルからステージを生成する
     * ファイルの内容は、
     *
     * マップオブジェクト    読み込み用スクリプトへの相対パス[改行]
     * <String>
     * ダメージオブジェクト  読み込み用スクリプトへの相対パス[改行]
     * <String>
     * プレイヤーオブジェクト読み込み用スクリプトへの相対パス[改行]
     * <String>
     * キャラクタオブジェクト読み込み用スクリプトへの相対パス[改行]
     * <String>
     * 重力値_x 重力値_y
     * <int>    <int>
     * を読み込む
     * 引数：ファイル名
     */
    public Stage(String _file_path) {
        try{
            BufferedReader bRead = new BufferedReader(new FileReader(_file_path));

            String         str         = "";
            String         script_path = (Paths.get(_file_path).getParent() == null)?
                                          null : Paths.get(_file_path).getParent().toString() + "\\";

            str          = script_path + bRead.readLine();
            debugLog.getInstance().write(str + " load.");
            map_data     = mapObj.file_to_mapObj(str, this);

            str          = script_path + bRead.readLine();
            debugLog.getInstance().write(str + " load.");
            damage       = dmgObj.file_to_dmgObj_ArrayList(str, this);

            str          = script_path + bRead.readLine();
            debugLog.getInstance().write(str + " load.");
            player_data  = playerObj.file_to_playerObj(str, this);

            str          = script_path + bRead.readLine();
            debugLog.getInstance().write(str + " load.");
            person       = charObj.file_to_charObj_ArrayList(str, this);

            String[] elm = bRead.readLine().split(" ");
            gravitiy     = new point<Double>(Double.parseDouble(elm[0]), Double.parseDouble(elm[1]));

            bRead.close();
        }catch(IOException e){
            debugLog.getInstance().write("read failed : " + _file_path);
        }
        catch(Exception e){
            debugLog.getInstance().write_exception(e);
        }

        create_dmg    = new LinkedList<dmgObj>();
        create_person = new LinkedList<charObj>();

        player_prev   = new ArrayList<playerObj>();
        add_playerObj_prev();

        camera_position_correction();

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
        add_playerObj_prev();
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

        //カメラ位置修正
        camera_position_correction();

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
     * アップデート前のplayerObjをprevに追加
     * 引数  ：なし
     * 戻り値：なし
     */
    private void add_playerObj_prev(){
        player_prev.add(0, new playerObj(player_data));

        //規定の数を超過している場合
        while(player_prev.size() > PREV_MAX){
            player_prev.remove(player_prev.size() - 1);
        }

        //規定の数より不足している場合
        while(player_prev.size() < PREV_MAX){
            player_prev.add(player_data);
        }

    }


    /* ステージの描画
     * 引数  ：なし
     * 戻り値：なし
     */
    public void draw(Graphics g){

        map_data.draw(g);

        for(int i = 0; i < person.size(); i++){
            person.get(i).draw(g);
        }

        for(int i = 0; i < person.size(); i++){
            damage.get(i).draw(g);
        }


        player_data.draw(g);
    }

    /*
     * カメラ位置の修正
     * 引数  ：なし
     * 戻り値：なし
     */
    private void camera_position_correction(){
        rect shoot_area = new rect(player_location_to_camera_location(),
                                   window.SIZE                         );

        //描画範囲がステージ内に収まっているとき
        if(map_data.is_field_conntraction(shoot_area)){
            camera_location = shoot_area.location.DtoF();
            return;
        }

        //x軸の修正
        if(shoot_area.UpperLeft(). x < 0.0d)
            shoot_area.location.x = 0.0d;
        if(shoot_area.UpperRight().x > map_data.get_field_size().x)
            shoot_area.location.x = map_data.get_field_size().x - window.SIZE.x.doubleValue();
        if(window.SIZE.x > map_data.get_field_size().x)
            shoot_area.location.x = (map_data.get_field_size().x.doubleValue() - window.SIZE.x.doubleValue()) / 2;

        //y軸の修正
        if(shoot_area.UpperLeft().y < 0.0d)
            shoot_area.location.y = 0.0d;
        if(shoot_area.LowerLeft().y > map_data.get_field_size().y.doubleValue())
            shoot_area.location.y = map_data.get_field_size().y.doubleValue() - window.SIZE.y.doubleValue();
        if(window.SIZE.y > map_data.get_field_size().y)
            shoot_area.location.y = (map_data.get_field_size().y.doubleValue() - window.SIZE.x.doubleValue()) / 2;

        camera_location = shoot_area.location.DtoF();
        return;
    }

    private point<Double> player_location_to_camera_location(){
        return new point<Double>(player_data.LowerRight().x - (window.SIZE.x.doubleValue() - PLAYER_SHOOT.x.doubleValue()),
                                 player_data.LowerRight().y - (player_data.to_rect().size.y.doubleValue() / 2)
                                     - (window.SIZE.y.doubleValue() - PLAYER_SHOOT.y.doubleValue()));
    }


}
