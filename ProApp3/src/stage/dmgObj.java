package stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import window.Main;
import window.window;
import IO.debugLog;

import common.point;
import common.rect;

public class dmgObj extends rect {
    /* メンバ変数 */
    public                 point<Double>  accel;          // 移動力
    public                 double         atk;            // charObj衝突時の体力変動値
    public                 Force          force_m;        //自オブジェクトの勢力(同じ勢力にはダメージを与えない)
    public                 boolean        is_dead;        //そのフレームで消滅するか
    public                 int            timer_dead;     //消滅するまでのタイマー変数
    public                 Stage          belong;         //どのステージに存在しているか

    public                 Direction      dir;            //オブジェクトの方向

    private                texture        texture_m;      //テクスチャ

    private   static final int           TIMER_STOP = -1; //タイマー変数の停止状態用

    /* コンストラクタ */
    /*
     * デフォルトコンストラクタ 引数：なし
     */
    public dmgObj() {
        init(new rect()   , new point<Double>(), 0.0,
             Force.NEUTRAL, TIMER_STOP         , "" ,
             null);
    }

    /*
     * コピーコンストラクタ
     * 引数：コピー元
     */
    public dmgObj(dmgObj obj) {
        init(obj        , obj.accel     , obj.atk                      ,
             obj.force_m, obj.timer_dead, obj.texture_m.get_file_path(),
             obj.belong);
    }

    /*
     * 値指定型コンストラクタ
     * 引数：それぞれのデータ
     */
    public dmgObj(rect  _rect  , point<Double> _accel     , double _atk         ,
                  Force _force , int           _timer_dead, String _texture_path,
                  Stage _belong) {
        init(_rect , _accel     , _atk         ,
             _force, _timer_dead, _texture_path,
             _belong);
    }

    /*
     * 状態アップデート
     * 引数  ：なし
     * 戻り値：なし
     */
    public void update(){
        if(timer_dead == 0)
            is_dead = true;
        else if(timer_dead > 0)
            --timer_dead;

    }


    /* メソッド */
    /* 描画
     * 引数  ：なし or 描画倍率
     * 戻り値：なし
     */
    public void draw(Graphics g){
        if(is_dead)
            return;

        texture_m.draw(g, location.DtoF(), 0, 0, belong, ((dir == Direction.RIGHT)? false : true));
        if(Main._DEBUG){
            if(window.comprise(this.to_rect(), belong)){
                rect r = belong.relative_camera_rect(this.to_rect());
                g.setColor(new Color(0xff0000));
                g.drawRect(r.location.x.floatValue() * window.SCALE, r.location.y.floatValue() * window.SCALE,
                           r.size.x.floatValue()     * window.SCALE, r.size.y.floatValue()     * window.SCALE);
            }
        }
    }


    /* ファイルからArrayList<dmgObj>生成
     * 要素は1行1つで、要素1つごとに以下の内容を持っているものとする
     *
     * 座標_x   座標_y   サイズ_x サイズ_y 初期移動力_x 初期移動力_y 体力変動値 オブジェクトの勢力 消滅するまでのフレーム数(-1ならフレーム数で消えない) テクスチャへのファイルパス
     * <double> <double> <int>    <int>    <double>     <double>     <double>   <Force>            <int>                                                <string>
     *
     * 引数  ：ファイルパス
     * 戻り値：生成されたダメージオブジェクトリスト
     */
    public static ArrayList<dmgObj> file_to_dmgObj_ArrayList(String _file_path, Stage _belong){
        ArrayList<dmgObj> d_obj_al    = new ArrayList<dmgObj>();
        String            script_path = ((Paths.get(window.file_path_corres(_file_path)).getParent() == null)?
                                             "" : Paths.get(window.file_path_corres(_file_path)).getParent().toString() + "\\");

        try{
            BufferedReader bRead = new BufferedReader(new FileReader(window.file_path_corres(_file_path)));
            String         line  = "";
            String[]       str   = null;

            while((line = bRead.readLine()) != null){
                str = line.split(" ");
                d_obj_al.add(new dmgObj(new rect(new point<Double >(Double.parseDouble(str[0]), Double.parseDouble(str[1])),
                                                  new point<Integer>(Integer.parseInt  (str[2]), Integer.parseInt  (str[3]))),
                                        new point<Double>(Double.parseDouble(str[4]), Double.parseDouble(str[5]))           ,
                                        Double.parseDouble(str[6])                                                             ,
                                        Force.parceForce(str[7])                                                               ,
                                        Integer.parseInt(str[8])                                                               ,
                                        window.file_path_corres(script_path + str[9])                                       ,
                                        _belong                                                                             ));
            }

            bRead.close();
        }catch(Exception e){
            debugLog.getInstance().write_exception(e);
        }

        return d_obj_al;
    }


    /*
     * 移動
     * ブレゼンハムの直線描画アルゴリズムを基にする
     * 引数  ：なし
     * 戻り値：なし
     */
    public void move(){

        //ローカル変数宣言
        point<Double>  move_actual  = new point<Double>(0.0, 0.0); //実際の移動量
        //直線用
        point<Integer> location_i   = new point<Integer>(location.x.intValue(), location.y.intValue()); //現在位置(int)
        point<Integer> dest_i       = new point<Integer>((int)(new BigDecimal(location.x + accel.x).                                                 //目的位置(int)
                                                                 setScale(0, BigDecimal.ROUND_DOWN).doubleValue() + ((accel.x < 0)? -1.0d : 1.0d)),
                                                       (int)(new BigDecimal(location.y + accel.y).
                                                                 setScale(0, BigDecimal.ROUND_DOWN).doubleValue() + ((accel.y < 0)? -1.0d : 1.0d)));
        point<Integer> accel_i      = new point<Integer>(dest_i.x - location_i.x, dest_i.y - location_i.y); //移動サイズ(int)

        point<Integer> inc_i        = new point<Integer>((accel_i.x > 0)? 1 : -1 , (accel_i.y > 0)? 1 : -1); //加算する方向

        point<Double>  move_this_pr = new point<Double>(0.0d, 0.0d);  //1プロセス中の移動量

        double         error      = 0.5 * Math.abs(((Math.abs(accel_i.x) > Math.abs(accel_i.y))? accel_i.y : accel_i.x)); //誤差

        //処理

        //x軸の方が大きい場合
        if(Math.abs(accel_i.x) > Math.abs(accel_i.y)){
            for(point<Integer> p = new point<Integer>(0, 0); Math.abs(p.x) < Math.abs(accel_i.x); p.x += inc_i.x){
                move_this_pr.x = (Math.abs(accel.x - p.x.doubleValue()) < 1.0d)? accel.x - p.x.doubleValue() : inc_i.x.doubleValue();
                move_this_pr.y = 0.0d;

                location.x    += move_this_pr.x;
                move_actual.x += move_this_pr.x;

                process_contract();

                error      += Math.abs(accel.y);
                if(error >= Math.abs(accel.x.doubleValue())){
                    move_this_pr.x = 0.0d;
                    move_this_pr.y = (Math.abs(accel.y - p.y.doubleValue()) < 1.0d)? accel.y - p.y.doubleValue() : inc_i.y.doubleValue();

                    location.y    += move_this_pr.y;
                    move_actual.y += move_this_pr.y;

                    process_contract();

                    error -= Math.abs(accel.x.doubleValue());
                    p.y        += inc_i.y;
                }
            }
        }
        //y軸の方が大きい場合
        else
        {
            for(point<Integer> p = new point<Integer>(0, 0); Math.abs(p.y) < Math.abs(accel_i.y); p.y += inc_i.y){
                move_this_pr.x = 0.0d;
                move_this_pr.y = (Math.abs(accel.y - p.y.doubleValue()) < 1.0d)? accel.y - p.y.doubleValue() : inc_i.y.doubleValue();

                location.y    += move_this_pr.y;
                move_actual.y += move_this_pr.y;

                process_contract();

                error += Math.abs(accel.x);
                if(error >= Math.abs(accel.y.doubleValue())){
                    move_this_pr.x = (Math.abs(accel.x - p.x.doubleValue()) < 1.0d)? accel.x - p.x.doubleValue() : inc_i.x.doubleValue();
                    move_this_pr.y = 0.0d;

                    location.x    += move_this_pr.x;
                    move_actual.x += move_this_pr.x;

                    process_contract();

                    error -= Math.abs(accel.y.doubleValue());
                    p.x   += inc_i.x;
                }
            }
        }
    }

    /*
     * rectにキャスト
     * 引数  ：なし
     * 戻り値：rectにキャストした自オブジェクト
     */
    public rect to_rect(){
        return new rect(new point<Double> (location),
                        new point<Integer>(size)    );
    }


    /*
     * 衝突処理
     * 引数  ：なし
     * 戻り値：なし
     */
    protected void process_contract(){
        // マップオブジェクト
        if(belong.map_data.is_collision(this.to_rect()))
            is_dead = true;

        // キャラクタオブジェクト
        for(int i = 0; i < belong.person.size(); i++){
            if(belong.person.get(i).to_rect().is_collision(this.to_rect()))
                belong.person.get(i).receve_damage(this);
        }

        // プレイヤオブジェクト
        if(belong.player_data.is_collision(this.to_rect()))
            belong.player_data.receve_damage(this);
    }

    /*
     * 初期化
     * 引数：それぞれのデータ
     */
    private void init(rect  _rect  , point<Double> _accel       , double _atk         ,
                      Force _force , int           _timer_dead  , String _texture_path,
                      Stage _belong){
        init(_rect.location, _rect.size);

        accel      = new point<Double>(_accel);
        atk        = _atk;
        dir        = Direction.RIGHT;

        force_m    = _force;

        texture_m  = new texture(_texture_path, size);

        is_dead    = false;
        timer_dead =_timer_dead;

        belong     = _belong;
    }
}
