package stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.nio.file.Paths;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import window.Main;
import window.window;
import IO.debugLog;

import common.point;
import common.rect;

public class playerObj extends charObj {
    /* メンバ変数 */
    //状態変数
    public               point<Integer> size_act;      //アクションモード時のサイズ
    private              texture        texture_act_m; //アクションモード用テクスチャ
    public               point<Integer> size_stg;      //シューティングモード時のサイズ
    private              texture        texture_stg_m; //シューティングモード用テクスチャ
    public               boolean        is_shooting;  //シューティングモードか
    //タイマー変数
    protected            int            timer_deform; //シューティング←→アクションの変形残りフレーム
    protected            int            timer_reload; //攻撃操作の、再攻撃待機タイマー

    /* 定数 */
    //モード共通
    private static final double HISPEED_RATE    =   1.5d; //高速移動時の移動力倍率

    //アクションモード用
    private static final double ACT_MV_GND      =   8.5d;  //接地時の左右移動力
    private static final double ACT_MV_JUMP     = -20.0d;  //ジャンプ時の上方向移動力
    private static final double ACT_MV_NOT_GND  =   8.5d;  //非接地時の左右移動力
    private static final double ACT_DAMAGE_RATE =   1.0d;  //ダメージ倍率

    //シューティングモード用
    private static final double STG_MV_LR       =   4.0d;  //左右移動力
    private static final double STG_MV_UD       =   2.0d;  //上下移動力
    private static final double STG_DAMAGE_RATE =   2.0d;  //ダメージ倍率

    //その他
    private static final int    TIMER_STOP = -1;        //タイマー変数の停止状態用



    //ユーザ入力関係

    /* コンストラクタ */
    /* デフォルトコンストラクタ
     * 引数：なし
     */
    public playerObj() {
        init(new point<Double >(0, 0), new point<Integer>(0, 0),                      "",
             new point<Integer>(0, 0),                       "", new point<Double>(0, 0),
                                 50.0,          Direction.RIGHT,                   false,
                                false,               TIMER_STOP,              TIMER_STOP,
             null);
    }

    public playerObj(playerObj obj){
        init(obj.location   , obj.size_act                     , obj.texture_act_m.get_file_path(),
             obj.size_stg   , obj.texture_stg_m.get_file_path(), obj.accel                        ,
             obj.hp         , obj.dir                          , obj.is_gnd                       ,
             obj.is_shooting, obj.timer_deform                 , obj.timer_not_visible            ,
             obj.belong     );
    }


    public playerObj(point<Double > _location              , point<Integer> _size_action     , String        _texture_path_act,
                     point<Integer> _size_shooting         , String         _texture_path_stg, point<Double> _accel           ,
                     double         _hp                    , Direction      _dir             , boolean       _is_gnd          ,
                     boolean _is_gravitied_and_not_shooting, Stage _belong){
        init(_location, _size_action       , _texture_path_act,
             _size_shooting                , _texture_path_stg, _accel    ,
             _hp                           , _dir             , _is_gnd   ,
             _is_gravitied_and_not_shooting, TIMER_STOP       , TIMER_STOP,
             _belong                       );
    }

    /* メソッド */
    /* 状態アップデート(オーバーライド)
     * 引数  ：なし
     * 戻り値：生成コード
     */
    @Override
    public void update() {

        //変形処理を行い、変形時でなければ通常操作
        if(deform())
            return;

        //前処理
        //重力値分の移動力補正
        if(is_gravitied){
            accel.x += belong.gravitiy.x;
            accel.y += belong.gravitiy.y;
        }

        //共通操作
        //攻撃(ダメージオブジェクト生成)
        if (Main.user_input.get(0).attack) {
         }

        //固有操作
        //アクションモード
        if (!is_shooting) {
            //ジャンプ
            if (Main.user_input.get(0).jump && is_gnd) {
                if(!Main.user_input.get(1).jump)
                    accel.y = ACT_MV_JUMP;
            }

            //左右移動
            // 右
            if (Main.user_input.get(0).right) {
                accel.x = +1.0 * ((is_gnd)? ACT_MV_GND : ACT_MV_NOT_GND);
                dir = Direction.RIGHT;
            }
            // 左
            else if (Main.user_input.get(0).left) {
                accel.x = -1.0 * ((is_gnd)? ACT_MV_GND : ACT_MV_NOT_GND);
                dir = Direction.LEFT;
            }
            // 入力なし
            else{
                accel.x = 0.0;
            }

            //高速移動
            if(Main.user_input.get(0).highsp){
                accel.x *= HISPEED_RATE;
            }

        }
        // シューティングモード
        else {
            //左右移動
            // 右
            if (Main.user_input.get(0).right){
                accel.x = +1.0 * STG_MV_LR;
                dir = Direction.RIGHT;
            }
            // 左
            else if (Main.user_input.get(0).left) {
                accel.x = -1.0 * STG_MV_LR;
                dir = Direction.LEFT;
            }
            // 入力なし
            else{
                accel.x = 0.0;
            }

            //上下操作
            // 上
            if (Main.user_input.get(0).up) {
                accel.y = -1.0 * STG_MV_UD;
            }
            // 下
            else if (Main.user_input.get(0).down) {
                accel.y = +1.0 * STG_MV_UD;
            }
            // 入力なし
            else{
                accel.y = 0.0;
            }

            //高速移動
            if(Main.user_input.get(0).highsp){
                accel.x *= HISPEED_RATE;
                accel.y *= HISPEED_RATE;
            }
        }

    }


    /* 描画
     *
     * 引数  ：なし or 描画倍率
     * 戻り値：なし
     */
    @Override
    public void draw(Graphics g){
        draw(g, 1.0f);
    }
    @Override
    public void draw(Graphics g, float _scale){
        //シューティングモード時の描画
        if(is_shooting){
            texture_stg_m.draw(g, location.DtoF(), belong, _scale);

            if(Main._DEBUG){
                if(window.comprise(this.to_rect(), belong)){
                    rect r = belong.relative_camera_rect(this.to_rect());
                    g.setColor(new Color(0x00ff00));
                    g.drawRect(r.location.x.floatValue(), r.location.y.floatValue(),
                               r.size.x.floatValue()    , r.size.y.floatValue());
                }
            }

        }
        //アクションモード時の描画
        else{
            texture_act_m.draw(g, location.DtoF(), belong, _scale);
            if(Main._DEBUG){
                if(window.comprise(this.to_rect(), belong)){
                    rect r = belong.relative_camera_rect(this.to_rect());
                    g.setColor(new Color(0x00ff00));
                    g.drawRect(r.location.x.floatValue(), r.location.y.floatValue(),
                               r.size.x.floatValue()    , r.size.y.floatValue());
                }
            }
        }
    }

    /*
     * 長方形クラスへの変換
     * 引数  ：なし
     * 戻り値：現在の状態を長方形に変換したもの
     */
    public rect to_rect(){
        return new rect(new point<Double>(location)                          ,
                        new point<Integer>((is_shooting)? size_stg : size_act));
    }

    /* ファイルからプレイヤーオブジェクトのデータを読み込む
     * データは1行に格納，各データは空白で区切られ、以下のデータを持っている前提で動作する
     * 座標_x   座標_y   アクションモード時のサイズ_x アクションモード時のサイズ_y アクションモード時のテクスチャへのファイルパス
     * <double> <double> <int>                        <int>                        <String>
     * シューティングモード時のサイズ_x シューティングモード時のサイズ_y シューティングモード時のテクスチャへのファイルパス
     * <int>                            <int>                            <String>
     * 加速度_x 加速度_y  HP       向き        接地しているか シューティングモードか(重力の影響を受けないか)
     * <double> <double>  <double> <Direction> <boolean>      <boolean>
     *
     * 引数  ：ファイル名
     * 戻り値：プレイヤーオブジェクト
     */
    public static playerObj file_to_playerObj(String _file_path, Stage _belong) /*throws FileNotFoundException*/{
        String    script_path = ((Paths.get(_file_path).getParent() == null)?
                                     "" : Paths.get(_file_path).getParent().toString() + "\\");

        try{
            BufferedReader bRead = new BufferedReader(new FileReader(_file_path));
            String[] str = bRead.readLine().split(" ");
            bRead.close();

            point<Double >  _loc                    = new point<Double >(Double.parseDouble(str[ 0]),  //座標
                                                                         Double.parseDouble(str[ 1]));
            point<Integer> _size_act                = new point<Integer>(Integer.parseInt  (str[ 2]),  //アクションモード時のサイズ
                                                                         Integer.parseInt  (str[ 3]));
            String         _texture_act             = script_path +                         str[ 4];   //アクションモード時のテクスチャへのパス
            point<Integer> _size_stg                = new point<Integer>(Integer.parseInt  (str[ 5]),  //シューティングモード時のサイズ
                                                                         Integer.parseInt  (str[ 6]));
            String         _texture_stg             = script_path +                         str[ 7];   //シューティングモード時のテクスチャへのパス
            point<Double > _accel                   = new point<Double> (Double.parseDouble(str[ 8]),  //加速度
                                                                         Double.parseDouble(str[ 9]));
            double         _hp                      = Double.parseDouble                   (str[10]);  //HP
            Direction      _dir                     = Direction.parseDirection             (str[11]);  //向き
            boolean        _is_gnd                  = Boolean.parseBoolean                 (str[12]);  //接地しているか
            boolean        _is_shooting_and_not_gnd = Boolean.parseBoolean                 (str[13]);  //シューティングモードか(重力の影響を受けるか)

            return new playerObj(_loc                    , _size_act   , _texture_act,
                                 _size_stg               , _texture_stg, _accel      ,
                                 _hp                     , _dir        , _is_gnd     ,
                                 _is_shooting_and_not_gnd, _belong     );
        }catch(Exception e){
            e.printStackTrace();
            debugLog.getInstance().write_exception(e);
            debugLog.getInstance().write("    filename : " + _file_path);
        }
        return new playerObj();
    }


    /* アクション←→シューティング処理
     * 引数  ：なし
     * 戻り値：変形中かどうか
     */
    private boolean deform(){
        if(timer_deform >= 0){
            timer_deform--;
            if(timer_deform == 0){
                //状態遷移
                is_shooting  = !is_shooting;
                is_gravitied = !is_gravitied;
                //サイズ変更処理
                location = new point<Double>(location.x +  (size_act.x - size_stg.x)      * ((is_shooting)? -1 : 1),
                                             location.y + ((size_act.y - size_stg.y) / 2) * ((is_shooting)? -1 : 1));
            }
            return true;
        }
        return false;
    }

    /*
     * 移動
     * ブレゼンハムの直線描画アルゴリズムを基にする
     * 引数  ：なし
     * 戻り値：なし
     */
    @Override
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

                process_contract(move_this_pr, move_actual);

                error      += Math.abs(accel.y);
                if(error >= Math.abs(accel.x.doubleValue())){
                    move_this_pr.x = 0.0d;
                    move_this_pr.y = (Math.abs(accel.y - p.y.doubleValue()) < 1.0d)? accel.y - p.y.doubleValue() : inc_i.y.doubleValue();

                    location.y    += move_this_pr.y;
                    move_actual.y += move_this_pr.y;

                    process_contract(move_this_pr, move_actual);

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

                process_contract(move_this_pr, move_actual);

                error += Math.abs(accel.x);
                if(error >= Math.abs(accel.y.doubleValue())){
                    move_this_pr.x = (Math.abs(accel.x - p.x.doubleValue()) < 1.0d)? accel.x - p.x.doubleValue() : inc_i.x.doubleValue();
                    move_this_pr.y = 0.0d;

                    location.x    += move_this_pr.x;
                    move_actual.x += move_this_pr.x;

                    process_contract(move_this_pr, move_actual);

                    error -= Math.abs(accel.y.doubleValue());
                    p.x   += inc_i.x;
                }
            }
        }

        accel = new point<Double>(move_actual);
    }

    /*
     * 衝突処理
     * 引数  ：移動量
     * 戻り値：なし
     */
    @Override
    protected void process_contract(point<Double> move, point<Double> move_actual){
        //マップオブジェクト
        if(Math.abs(move.y) >= 1.0){
            is_gnd = false;
        }
        if(belong.map_data.is_collision(this.to_rect())){
            location.x    -= move.x; location.y    -= move.y;
            move_actual.x -= move.x; move_actual.y -= move.y;


            if(move.y > 0)
                is_gnd = true;
        }

        //ダメージオブジェクト
        for(int i = 0; i < belong.damage.size(); i++){
            if(belong.damage.get(i).is_collision(this.to_rect()))
                receve_damage(belong.damage.get(i));
        }
    }

    /*
     * ダメージ処理
     * 引数  ：ダメージ源
     * 戻り値：なし
     */
    @Override
    protected void receve_damage(dmgObj _source){
        if(_source == null)
            return;
        if(!_source.force_m.is_attackable(this.force_m))
            return;
        if(_source.is_dead)
            return;

        hp -= _source.atk * ((is_shooting)? STG_DAMAGE_RATE : ACT_DAMAGE_RATE);
        _source.is_dead = true;
        if(hp < 0.0d)
            is_dead = true;

        return;
    }

    /*
     * 初期化
     * 引数  ：それぞれのデータ
     */
    private void init(point<Double > _location                     , point<Integer> _size_action     , String        _texture_path_act ,
                      point<Integer> _size_shooting                , String         _texture_path_stg, point<Double> _accel            ,
                      double         _hp                           , Direction      _dir             , boolean       _is_gnd           ,
                      boolean        _is_shooting_and_not_gravitied, int           _timer_deform     , int           _timer_not_visible,
                      Stage          _belong                       ){
        //それぞれの変数を適した形で初期化
        location          = new point<Double >(_location);
        size_act          = new point<Integer>(_size_action);
        texture_act_m     = new texture(_texture_path_act, size_act);
        size_stg          = new point<Integer>(_size_shooting);
        texture_stg_m     = new texture(_texture_path_stg, size_stg);

        accel             = new point<Double>(_accel);
        hp                = _hp;
        dir               = _dir;
        is_gnd            = _is_gnd;
        is_shooting       = _is_shooting_and_not_gravitied;
        is_gravitied      = !_is_shooting_and_not_gravitied;
        timer_deform      = _timer_deform;
        timer_not_visible = _timer_not_visible;

        //参照を受け取る
        belong        = _belong;

    }


    /*
     * それぞれの端点を返す
     * 引数  ：なし
     * 戻り値：左上 or 右上 or 左下 or 右下
     */
    @Override
    public  point<Double> UpperLeft() {
        return this.to_rect().UpperLeft();
    }
    @Override
    public point<Double> UpperRight() {
        return this.to_rect().UpperRight();
    }
    @Override
    public point<Double> LowerLeft() {
        return this.to_rect().LowerLeft();
    }
    @Override
    public point<Double> LowerRight() {
        return this.to_rect().LowerRight();
    }


}