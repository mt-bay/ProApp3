package stage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;

import org.newdawn.slick.Input;

import IO.config;
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

    protected            Input          ip_now;       //最新の入力



    /* 定数 */
    private static       double ACT_MV     = 2.5;       //アクションモード時の左右移動力

    private static final double DAMAGE_RATE_ACT = 1.0d; //アクションモード時のダメージ比率
    private static final double DAMAGE_RATE_STG = 2.0d; //シューティングモード時のダメージ比率

    private static final int    TIMER_STOP = -1;        //タイマー変数の停止状態用

    //ユーザ入力関係

    /* コンストラクタ */
    /* デフォルトコンストラクタ
     * 引数：なし
     */
    public playerObj() {

    }

    public playerObj(playerObj obj){
        init(obj.location   , obj.size_act                     , obj.texture_act_m.get_file_path(),
             obj.size_stg   , obj.texture_stg_m.get_file_path(), obj.accel                        ,
             obj.hp         , obj.dir                          , obj.is_gnd                       ,
             obj.is_shooting, obj.timer_deform                 , obj.timer_not_visible            ,
             obj.belong     );
    }


    public playerObj(point<Double > _location     , point<Integer> _size_action     , String        _texture_path_act,
                     point<Integer> _size_shooting, String         _texture_path_stg, point<Double> _accel           ,
                     double _hp, Direction _dir, boolean _is_gnd,
                     boolean _is_gravitied_and_shooting, Stage _belong){
       init(_location, _size_action   , _texture_path_act,
            _size_shooting            , _texture_path_stg, _accel    ,
            _hp                       , _dir             , _is_gnd   ,
            _is_gravitied_and_shooting, TIMER_STOP       , TIMER_STOP,
            _belong                   );
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

        //共通操作
        //攻撃(ダメージオブジェクト生成)
        if (ip_now.isKeyDown(config.attack)) {
         }

        //左右移動
        if (ip_now.isKeyDown(config.left)) {
            accel.x = -ACT_MV * ((ip_now.isKeyDown(config.highsp) ? 1 : 2));
            dir = Direction.LEFT;
        }
        else if (ip_now.isKeyDown(config.right)) {
            accel.x = ACT_MV * ((ip_now.isKeyDown(config.highsp) ? 1 : 2));
            dir = Direction.RIGHT;
        }
        //固有操作
        //アクションモード
        if (!is_shooting) {

            //ジャンプ
            if (ip_now.isKeyDown(config.jump) && is_gnd) {
                accel.y = -20.0;
            }
        }

        else {
            //上下操作
            if (ip_now.isKeyDown(config.down)) {
                accel.y = ACT_MV * ((ip_now.isKeyDown(config.highsp) ? 1 : 2));
            }
            else if (ip_now.isKeyDown(config.up)) {
                accel.y = -ACT_MV * ((ip_now.isKeyDown(config.highsp) ? 1 : 2));
            }
        }

    }


    /* 描画
     *
     * 引数  ：なし or 描画倍率
     * 戻り値：なし
     */
    @Override
    public void draw(){
        draw(1.0f);
    }
    @Override
    public void draw(float _scale){
        //シューティングモード時の描画
        if(is_shooting){
            texture_stg_m.draw(location.DtoF(), belong, _scale);
        }
        //アクションモード時の描画
        else{
            texture_act_m.draw(location.DtoF(), belong, _scale);

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
     * 加速度_x 加速度_y  HP       向き        接地しているか シューティングモードか(重力の影響を受けるか)
     * <double> <double>  <double> <Direction> <boolean>      <boolean>
     *
     * 引数  ：ファイル名
     * 戻り値：プレイヤーオブジェクト
     */
    public static playerObj file_to_playerObj(String _filename, Stage _belong) throws FileNotFoundException{
        playerObj p_obj = new playerObj();
        try{
            BufferedReader bRead = new BufferedReader(new FileReader(_filename));
            String[] str = bRead.readLine().split(" ");

            p_obj = new playerObj(new point<Double >(Double.parseDouble(str[ 0]), Double.parseDouble(str[ 1])), //座標
                                  new point<Integer>(Integer.parseInt  (str[ 2]), Integer.parseInt  (str[ 3])), //アクションモード時のサイズ
                                  str[ 4]                                                                     , //アクションモード時のテクスチャへのパス
                                  new point<Integer>(Integer.parseInt(str[ 5])  , Integer.parseInt(str[ 6]))  , //シューティングモード時のサイズ
                                  str[ 7]                                                                     , //シューティングモード時のテクスチャへのパス
                                  new point<Double> (Double.parseDouble(str[ 8]), Double.parseDouble(str[ 9])), //加速度
                                  Double.parseDouble(str[10])                                                 , //HP
                                  Direction.parseDirection(str[11])                                           , //向き
                                  Boolean.parseBoolean(str[12])                                               , //接地しているか
                                  Boolean.parseBoolean(str[13])                                               , //シューティングモードか(重力の影響を受けるか)
                                  _belong                                                                     );//どのステージに所属しているか
            bRead.close();
        }catch(Exception e){
            debugLog.getInstance().write_exception(e, new Throwable());
        }
        return p_obj;
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
        //直線描画用
        point<Integer> location_i = new point<Integer>(location.x.intValue(), location.y.intValue()); //現在位置(int)
        point<Integer> dest_i     = new point<Integer>((int)new BigDecimal(location.x + accel.x).setScale(0, BigDecimal.ROUND_UP).doubleValue(), //目的位置(int)
                                                       (int)new BigDecimal(location.y + accel.y).setScale(0, BigDecimal.ROUND_UP).doubleValue());
        point<Integer> accel_i    = new point<Integer>(location_i.x - dest_i.x, location_i.y - dest_i.y); //移動サイズ(int)

        point<Integer> inc_i      = new point<Integer>((accel_i.x > 0)? 1 : -1 , (accel_i.y > 0)? 1 : -1); //加算する方向

        double         error      =0.5 * ((Math.abs(accel_i.x) > Math.abs(accel_i.y))? accel_i.x : accel_i.y); //誤差
        //処理

        //x軸の方が大きい場合
        if(Math.abs(accel_i.x) > Math.abs(accel_i.y)){
            for(point<Integer> p = new point<Integer>(0, 0); Math.abs(p.x) < Math.abs(accel_i.x); p.x += inc_i.x){
                location.x += (Math.abs(accel.x - p.x.doubleValue()) < 1.0d)? accel.x - p.x.doubleValue() : inc_i.x.doubleValue();

                process_contract(new point<Integer>(inc_i.x, 0));

                error      += accel_i.y;
                if(error >= accel_i.x.doubleValue()){
                    location.y += (Math.abs(accel.y - p.y.doubleValue()) < 1.0d)? accel.y - p.y.doubleValue() : inc_i.y.doubleValue();

                    process_contract(new point<Integer>(0, inc_i.y));

                    p.y        += inc_i.y;
                }


            }
        }
        //y軸の方が大きい場合
        else
        {
            for(point<Integer> p = new point<Integer>(0, 0); Math.abs(p.y) < Math.abs(accel_i.y); p.y += inc_i.y){
                location.y += (Math.abs(accel.y - p.y.doubleValue()) < 1.0d)? accel.y - p.y.doubleValue() : inc_i.y.doubleValue();

                process_contract(new point<Integer>(0, inc_i.y));

                error += accel_i.x;
                if(error >= accel.y.doubleValue()){
                    location.x += (Math.abs(accel.x - p.x.doubleValue()) < 1.0d)? accel.x - p.x.doubleValue() : inc_i.x.doubleValue();

                    process_contract(new point<Integer>(inc_i.x, 0));

                    p.x += inc_i.x;
                }


            }
        }
    }

    /*
     * 衝突処理
     * 引数  ：移動量
     * 戻り値：なし
     */
    @Override
    protected void process_contract(point<Integer> move){
        //マップオブジェクト
        if(move.y != 0)
            is_gnd = false;
        if(belong.map_data.is_collision(this.to_rect())){
            location.x -= move.x; location.y -= move.y;
            if(move.y > 0)
                is_gnd = true;
        }

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

        hp -= _source.atk * ((is_shooting)? DAMAGE_RATE_STG : DAMAGE_RATE_ACT);
        _source.is_dead = true;
        if(hp < 0.0d)
            is_dead = true;

        return;
    }

    /*
     * 初期化
     * 引数  ：それぞれのデータ
     */
    private void init(point<Double > _location                 , point<Integer> _size_action     , String        _texture_path_act ,
                      point<Integer> _size_shooting            , String         _texture_path_stg, point<Double> _accel            ,
                      double         _hp                       , Direction      _dir             , boolean       _is_gnd           ,
                      boolean        _is_gravitied_and_shooting, int           _timer_deform     , int           _timer_not_visible,
                      Stage          _belong                   ){
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
        is_shooting       = is_gravitied =_is_gravitied_and_shooting;
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