package stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import menu.stageSelector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

import window.Main;
import window.window;
import IO.debugLog;

import common.point;
import common.rect;


public class Stage {
    /* 定数 */
    // プレイヤオブジェクトの、下底の中心をどこに映すか
    public final point<Float> PLAYER_SHOOT = new point<Float>((window.scaled_window_size().x  / 2), window.scaled_window_size().y / 4);

    /* メンバ変数・インスタンス */
    public point<Double>        gravitiy;

    //親オブジェクト
    public stageSelector        belong;          //親オブジェクト

    //現フレームのオブジェクト
    public playerObj            player_data;     //プレイヤーオブジェクト
    public mapObj               map_data;        //マップオブジェクト
    public ArrayList<dmgObj>    damage;          //ダメージオブジェクト
    public ArrayList<charObj>   person;          //キャラクタオブジェクト
    //状態管理
    public int                  timer_end;       //オブジェクトを抜けるまでのタイマー変数
    public boolean              is_clear;        //クリアしたかどうか

    public point<Float>         camera_location; //カメラ位置
    //オブジェクト生成コール用待ち行列
    public Queue<dmgObj>        create_dmg;      //ダメージオブジェクト
    public Queue<charObj>       create_person;   //キャラクタオブジェクト

    //過去のオブジェクトデータ
    public ArrayList<playerObj> player_prev;     //プレイヤーオブジェクト

    //親に処理を返す際のデータ
    public boolean              do_restart;      //自ステージを再度読み込むか

    /* 定数 */
    public final int TIMER_STOP = -1; //タイマー変数の停止状態を表す
    public final int PREV_MAX   =  5; //過去のオブジェクトデータの最大保存数
    private static final float font_size  = 30.0f;
    private static final TrueTypeFont ttf = new TrueTypeFont(new java.awt.Font("consolas", 0, (int)font_size), false);

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
     * <double> <double>
     * を読み込む
     * 引数：ファイル名
     */
    public Stage(String _file_path, stageSelector _belong) {
        belong = _belong;

        try{
            BufferedReader bRead = new BufferedReader(new FileReader(window.file_path_corres(_file_path)));



            debugLog.getInstance().write("stage load : " + _file_path);

            String         str         = "";
            String         script_path = (Paths.get(_file_path).getParent() == null)?
                                          "" : Paths.get(_file_path).getParent().toString() + "\\";

            str          = window.file_path_corres(script_path + bRead.readLine());
            debugLog.getInstance().write("    mapObj load : " + str);
            map_data     = mapObj.file_to_mapObj(str, this);

            str          = window.file_path_corres(script_path + bRead.readLine());
            debugLog.getInstance().write("    dmgObj load : " + str);
            damage       = dmgObj.file_to_dmgObj_ArrayList(str, this);

            str          = window.file_path_corres(script_path + bRead.readLine());
            debugLog.getInstance().write("    plyObj load : " + str);
            player_data  = playerObj.file_to_playerObj(str, this);

            str          = window.file_path_corres(script_path + bRead.readLine());
            debugLog.getInstance().write("    chrObj load : " + str);
            person       = charObj.file_to_charObj_ArrayList(str, this);

            String[] elm = bRead.readLine().split(" ");
            gravitiy     = new point<Double>(Double.parseDouble(elm[0]), Double.parseDouble(elm[1]));


            bRead.close();
        }catch(IOException e){
            debugLog.getInstance().write("read failed : " + _file_path);
            reflesh();
        }
        catch(Exception e){
            debugLog.getInstance().write_exception(e);
            reflesh();
        }

        create_dmg    = new LinkedList<dmgObj>();
        create_person = new LinkedList<charObj>();

        player_prev   = new ArrayList<playerObj>();
        add_playerObj_prev();

        timer_end     = TIMER_STOP;
        is_clear      = false;
        do_restart    = false;

        camera_position_correction();

        debugLog.getInstance().write();
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
        //処理を抜ける場合
        if(Main.user_input.get(0).quit    ||
           timer_end == 0){
            reflesh();
        }
        if(timer_end != TIMER_STOP){
            --timer_end;
        }
        if(Main.user_input.get(0).restart &&
           !is_clear                      &&
           timer_end == TIMER_STOP          ){

            do_restart = true;
            timer_end = 150;
        }

        //状態アップデート
        // プレイヤーオブジェクト
        add_playerObj_prev();
        if(!player_data.is_dead){
            player_data.update();
        }


        //プレイヤーが変形中ならば、他のオブジェクトの状態変更はしない
        if(player_data.do_deform)
            return;

        // キャラクタオブジェクト
        for(int i = 0; i < person.size(); i++)
            person.get(i).update();

        // ダメージオブジェクト
        for(int i = 0; i < damage.size(); i++)
            damage.get(i).update();

        //移動処理
        // プレイヤーオブジェクト
        if(!player_data.is_dead){
            player_data.move();
        }

        // キャラクタオブジェクト
        for(int i = 0; i < person.size(); i++){
            person.get(i).move();
        }

        //ダメージオブジェクト
        for(int i = 0; i < damage.size(); i++)
            damage.get(i).move();

        //カメラ位置修正
        camera_position_correction();

        //終了条件チェック
        if(person.size() > 0){
            if(person.get(0).is_dead && timer_end == TIMER_STOP){
                is_clear  = true;
                timer_end = 150;
            }
        }
        if(player_data.is_dead   && timer_end == TIMER_STOP){
            is_clear  = false;
            timer_end = 150;
        }


        //削除処理
        // キャラクタオブジェクト
        for(int i = 0; i < person.size(); i++){
            if(person.get(i).is_dead)
                person.remove(i);
        }

        // ダメージオブジェクト
        for(int i = 0; i < damage.size(); i++){
            if(damage.get(i).is_dead)
                damage.remove(i);
        }

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
            player_prev.add(new playerObj(player_data));
        }

    }

    /*
     * ゲームオーバー・クリア処理
     * 引数  ：ゲームクリア処理かどうか
     * 戻り値：なし
     */
    public void stage_end_draw(Graphics g){
        //終了時の処理
        if(do_restart){
            Font  prev_font  = g.getFont();
            Color prev_color = g.getColor();

            g.setFont(ttf);
            g.setColor(new Color(0xffffff));
            g.drawString("Restart", 0.0f, window.SIZE.y.floatValue() - font_size);

            g.setFont(prev_font);
            g.setColor(prev_color);

        }
        else if(!is_clear){
            Font  prev_font  = g.getFont();
            Color prev_color = g.getColor();

            g.setFont(ttf);
            g.setColor(new Color(0xffffff));
            g.drawString("Game Over!", 0.0f, window.SIZE.y.floatValue() - font_size);

            g.setFont(prev_font);
            g.setColor(prev_color);
        }
        else{
            Font  prev_font  = g.getFont();
            Color prev_color = g.getColor();

            g.setFont(ttf);
            g.setColor(new Color(0xffffff));
            g.drawString("Clear!", 0.0f, window.SIZE.y.floatValue() - font_size);

            g.setFont(prev_font);
            g.setColor(prev_color);
        }
    }

    /*
     * 親オブジェクトに処理を返す
     * 引数  ：なし
     * 戻り値：なし
     */
    public void reflesh(){
        belong.index_is_run = false;
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

        for(int i = 0; i < damage.size(); i++){
            damage.get(i).draw(g);
        }

        if(!player_data.is_dead){
            player_data.draw(g);
        }

        if(Main._DEBUG){
            Color prev_color = g.getColor();

            g.setFont(Main.debug_ttf);
            g.setColor(Main.DEBUG_FONT_COLOR);
            int line = 2;
            g.drawString("camera = " + camera_location, Main.DEBUG_FONT_SIZE, Main.DEBUG_FONT_SIZE * (float)++line);
            g.drawString("player - location = " + player_data.location.toString(), Main.DEBUG_FONT_SIZE, Main.DEBUG_FONT_SIZE * (float)++line);
            g.drawString("player - is_gnd   = " + player_data.is_gnd             , Main.DEBUG_FONT_SIZE, Main.DEBUG_FONT_SIZE * (float)++line);
            g.drawString("player - ai - move    = " + player_data.ai.move  , Main.DEBUG_FONT_SIZE, Main.DEBUG_FONT_SIZE * (float)++line);
            g.drawString("player - ai - attack  = " + player_data.ai.attack, Main.DEBUG_FONT_SIZE, Main.DEBUG_FONT_SIZE * (float)++line);
            g.drawString("player - timer_reload = " + player_data.timer_reload.toString(), Main.DEBUG_FONT_SIZE, Main.DEBUG_FONT_SIZE  * (float)++line);

            g.setColor(prev_color);
        }

        if(timer_end != TIMER_STOP)
            stage_end_draw(g);
    }

    /*
     * カメラ位置の修正
     * 引数  ：なし
     * 戻り値：なし
     */
    private void camera_position_correction(){
        rect shoot_area = new rect(player_location_to_camera_location(),
                                   window.scaled_window_size().FtoI()  );

        //描画範囲がステージ内に収まっているとき
        if(map_data.is_field_conntraction(shoot_area)){
            camera_location = shoot_area.location.DtoF();
            return;
        }

        //x軸の修正
        if(shoot_area.UpperLeft(). x < 0.0d)
            shoot_area.location.x = 0.0d;
        if(shoot_area.UpperRight().x > map_data.get_field_size().x)
            shoot_area.location.x = map_data.get_field_size().x - window.scaled_window_size().x.doubleValue();
        if(window.SIZE.x > map_data.get_field_size().x)
            shoot_area.location.x = (map_data.get_field_size().x.doubleValue() - window.scaled_window_size().x) / 2;

        //y軸の修正
        if(shoot_area.UpperLeft().y < 0.0d)
            shoot_area.location.y = 0.0d;
        if(shoot_area.LowerLeft().y > map_data.get_field_size().y.doubleValue())
            shoot_area.location.y = map_data.get_field_size().y.doubleValue() - window.scaled_window_size().y;
        if(window.SIZE.y > map_data.get_field_size().y)
            shoot_area.location.y = (map_data.get_field_size().y.doubleValue() - window.scaled_window_size().y) / 2;

        camera_location = shoot_area.location.DtoF();
        return;
    }

    private point<Double> player_location_to_camera_location(){
        return new point<Double>(player_data.get_center().x - (window.scaled_window_size().x - PLAYER_SHOOT.x.doubleValue()),
                                 player_data.LowerRight().y - (window.scaled_window_size().y - PLAYER_SHOOT.y.doubleValue()));
    }


}
