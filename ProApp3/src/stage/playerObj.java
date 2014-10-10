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
import AI.ai_op;
import IO.debugLog;

import common.point;
import common.rect;

public class playerObj extends charObj {
    /* メンバ変数 */
    //状態変数



    public               boolean        is_shooting;   //シューティングモードか
    //アクションモード用
    public               point<Integer> act_size;            //アクションモード時のサイズ
    public               point<Double>  act_move_rate;       //移動力
    public               double         act_highspeed_rate;  //高速移動時の倍率
    private              texture        act_texture_m;       //アクションモード用テクスチャ
    //シューティングモード用
    public               point<Integer> stg_size;            //シューティングモード時のサイズ
    public               point<Double>  stg_move_rate;       //移動力
    public               double         stg_highspeed_rate;  //高速移動時の倍率
    private              texture        stg_texture_m;       //シューティングモード用テクスチャ
    //変形中用
    public               point<Integer> dfm_size;            //変形中のサイズ
    private              texture        dfm_texture_m;       //変形中用テクスチャ
    private              boolean        is_act_to_stg;       //アクション→シューティングの変形かどうか
    public               boolean        do_deform;           //変形中か
    private              point<Integer> dfm_use_texture_num; //変形中に使用するテクスチャ番号
    private              int            dfm_time;            //変形にかけるフレーム数

    //タイマー変数
    protected            int            timer_deform;        //シューティング←→アクションの変形残りフレーム


    /* 定数 */
    //モード共通

    //アクションモード用
    private static final double ACT_DAMAGE_RATE = 1.0d;  //ダメージ倍率
    //シューティングモード用
    private static final double STG_DAMAGE_RATE = 2.0d;  //ダメージ倍率
    //変形中用
    private static final double DFM_DAMAGE_RATE = 8.0d;  //ダメージ倍率
    private static final int    DFM_TEXTURE_MAX = 4;     //テクスチャの分割数
    //その他




    //ユーザ入力関係

    /* コンストラクタ */
    /* デフォルトコンストラクタ
     * 引数：なし
     */
    public playerObj() {
        init(new point<Double >(0, 0), new point<Double>(0, 0),                       "",    "",
             new point<Integer>(0, 0),                      "", new point<Double>(0, 0) ,   0.0,
             new point<Integer>(0, 0),                      "", new point<Double>(0, 0) ,   0.0,
             new point<Integer>(0, 0),                      "",               TIMER_STOP,
             new point<Integer>(0, 0),                       0,                   false,
                                 50.0,         Direction.RIGHT,                   false,
                                false,         ai_op.AI_NO_USE,                    null);
    }

    public playerObj(playerObj obj){
        init(obj.location           , obj.accel                        , obj.bullet_path      , obj.reload_path       ,
             obj.act_size           , obj.act_texture_m.get_file_path(), obj.act_move_rate    , obj.act_highspeed_rate,
             obj.stg_size           , obj.stg_texture_m.get_file_path(), obj.stg_move_rate    , obj.stg_highspeed_rate,
             obj.dfm_size           , obj.dfm_texture_m.get_file_path(), obj.timer_deform     ,
             obj.dfm_use_texture_num, obj.dfm_time                     , obj.is_act_to_stg                ,
             obj.hp                 , obj.dir                          , obj.is_gnd           ,
             obj.is_shooting        , obj.ai.using_AI                  , obj.belong           );
    }


    public playerObj(point<Double > _location              , String        _bullet_path     , String        _reload_path  ,
                     point<Integer> _action_size           , String        _act_texture_path, point<Double> _act_move_rate, double        _act_highspeed_rate,
                     point<Integer> _shooting_size         , String        _stg_texture_path, point<Double> _stg_move_rate, double        _stg_highspeed_rate,
                     point<Integer> _deform_size           , String        _dfm_texture_path, int           _dfm_time     ,
                     double         _hp                    , Direction     _dir             , boolean       _is_gnd       ,
                     boolean _is_gravitied_and_not_shooting, int           _use_ai          , Stage         _belong       ){

        init(_location                     , new point<Double>(0.0d, 0.0d), _bullet_path     , _reload_path       ,
             _action_size                  , _act_texture_path            , _act_move_rate   , _act_highspeed_rate,
             _shooting_size                , _stg_texture_path            , _stg_move_rate   , _stg_highspeed_rate,
             _deform_size                  , _dfm_texture_path            , TIMER_STOP       ,
             new point<Integer>(0, 0)      , _dfm_time                    , false            ,
             _hp                           , _dir                         , _is_gnd          ,
             _is_gravitied_and_not_shooting, _use_ai                      ,_belong           );
    }

    /* メソッド */
    /* 状態アップデート(オーバーライド)
     * 引数  ：なし
     * 戻り値：生成コード
     */
    @Override
    public void update() {
        //前処理
        // AIの状態更新
        update_ai();

        //変形中なら処理を抜ける
        if(deform())
            return;

        //独自行動
        // フラグチェック
        //  変形
        if((ai.unique & ai_op.UNIQUE_DEFORM) != ai_op.UNIQUE_NONE){
            deform_begin();
        }

        //移動処理
        point<Double> move_dir = new point<Double>(0.0, 0.0);
        // フラグチェック
        //  移動方向チェック
        //   左右
        if((ai.move & ai_op.MOVE_DIR_LEFT_RIGHT) != ai_op.MOVE_NONE){
            //左
            if((ai.move & ai_op.MOVE_DIR_LEFT ) != ai_op.MOVE_NONE){
                dir = Direction.LEFT;
                move_dir.x = -1.0;
            }
            //右
            if((ai.move & ai_op.MOVE_DIR_RIGHT) != ai_op.MOVE_NONE){
                dir = Direction.RIGHT;
                move_dir.x = +1.0;
            }
        }
        else{
            accel.x = 0.0;
        }
        //   上下
        //    重力依存チェック(依存しているなら上下移動しない)
        if(!is_gravitied){
            if((ai.move & ai_op.MOVE_DIR_UP_DOWN) != ai_op.MOVE_NONE){
                //上
                if((ai.move & ai_op.MOVE_DIR_UP  ) != ai_op.MOVE_NONE){
                    move_dir.y = -1.0;
                }
                //下
                if((ai.move & ai_op.MOVE_DIR_DOWN) != ai_op.MOVE_NONE){
                    move_dir.y = +1.0;
                }
            }
            else{
                accel.y = 0.0;
            }
        }
        //高速移動時の処理
        //移動速度チェック
        if((ai.move & ai_op.MOVE_MOVE_NORMAL    ) != ai_op.MOVE_NONE){
            if(move_dir.x != 0.0){
                accel.x = move_dir.x * ((is_shooting)? stg_move_rate.x : act_move_rate.x);
            }
            if(move_dir.y != 0.0 && !is_gravitied){
                accel.y = move_dir.y * stg_move_rate.y;
            }
        }
        if((ai.move & ai_op.MOVE_MOVE_HIGHSPEED) != ai_op.MOVE_NONE){
            if(move_dir.x != 0.0){
                accel.x = move_dir.x * ((is_shooting)? stg_highspeed_rate * stg_move_rate.x : act_highspeed_rate * act_move_rate.x);
            }
            if(move_dir.y != 0.0 && !is_gravitied){
                accel.y *= stg_highspeed_rate * move_dir.y * stg_move_rate.y;
            }
        }

        //ジャンプ処理
        // フラグチェック
        if((ai.move & ai_op.MOVE_JUMP) != ai_op.MOVE_NONE){

            //重力依存チェック(依存していないならジャンプしない)
            if(is_gravitied){

                //通常ジャンプ
                if((ai.move & ai_op.MOVE_JUMP_NORMAL) != ai_op.MOVE_NONE){
                    //接地かつ1つ前のフレームの入力でジャンプ命令がない場合
                    if((!((ai_prev.get(0).move & ai_op.MOVE_JUMP_NORMAL) != ai_op.MOVE_NONE)) &&
                        is_gnd){
                        accel.y -= act_move_rate.y;
                    }
                }
            }
        }


        //攻撃処理
        if(ai.attack != ai_op.ATTACK_NONE){
            //通常攻撃
            if((ai.attack & ai_op.ATTACK_NOMAL) != ai_op.ATTACK_NONE){
                attack(((is_shooting)? 1 : 0), this.to_rect());
            }
        }

        //後処理
        // タイマー値の修正
        //  リロード
        for(int i = 0; i < timer_reload.size(); i++){
            if(timer_reload.get(i) > TIMER_STOP){
                timer_reload.set(i, timer_reload.get(i) - 1);
            }
        }

        // 重力値分の移動力補正
        if(is_gravitied){
            accel.x += belong.gravitiy.x;
            accel.y += belong.gravitiy.y;
        }
    }


    /* 描画
     *
     * 引数  ：なし or 描画倍率
     * 戻り値：なし
     */
    @Override
    public void draw(Graphics g){
        //変形中の描画
        if(do_deform){
            dfm_texture_m.draw(g, location.DtoF(), dfm_use_texture_num.x, dfm_use_texture_num.y, belong, ((dir == Direction.RIGHT)? false : true));

            if(Main._DEBUG){
                if(window.comprise(this.to_rect(), belong)){
                    rect r = belong.relative_camera_rect(this.to_rect());
                    g.setColor(new Color(0x00ff00));
                    g.drawRect(r.location.x.floatValue() * window.SCALE, r.location.y.floatValue() * window.SCALE,
                               r.size.x.floatValue()     * window.SCALE, r.size.y.floatValue()     * window.SCALE);
                    }

                return;
            }
        }
        //シューティングモード時の描画
        else if(is_shooting){
            stg_texture_m.draw(g, location.DtoF(), ai.texture_num.x, ai.texture_num.y, belong, ((dir == Direction.RIGHT)? false : true));

            if(Main._DEBUG){
                if(window.comprise(this.to_rect(), belong)){
                    rect r = belong.relative_camera_rect(this.to_rect());
                    g.setColor(new Color(0x00ff00));
                    g.drawRect(r.location.x.floatValue() * window.SCALE, r.location.y.floatValue() * window.SCALE,
                               r.size.x.floatValue()     * window.SCALE, r.size.y.floatValue()     * window.SCALE);
                }
            }
            return;
        }
        //アクションモード時の描画
        else{
            act_texture_m.draw(g, location.DtoF(), ai.texture_num.x, ai.texture_num.y, belong, ((dir == Direction.RIGHT)? false : true));
            if(Main._DEBUG){
                if(window.comprise(this.to_rect(), belong)){
                    rect r = belong.relative_camera_rect(this.to_rect());
                    g.setColor(new Color(0x00ff00));
                    g.drawRect(r.location.x.floatValue() * window.SCALE, r.location.y.floatValue() * window.SCALE,
                               r.size.x.floatValue()     * window.SCALE, r.size.y.floatValue()     * window.SCALE);
                }
            }
            return;
        }
    }

    /*
     * 長方形クラスへの変換
     * 引数  ：なし
     * 戻り値：現在の状態を長方形に変換したもの
     */
    public rect to_rect(){
        return new rect(new point<Double>(location)                          ,
                        new point<Integer>((is_shooting)? stg_size : act_size));
    }

    /* ファイルからプレイヤーオブジェクトのデータを読み込む
     * データは1行に格納，各データは空白で区切られ、以下のデータを持っている前提で動作する
     * 座標_x   座標_y   攻撃の際に使用するダメージオブジェクトのリストへのパス 発射間隔のスクリプトパス
     * <double> <double> <String>                                               <String>
     * アクションモード時のサイズ_x アクションモード時のサイズ_y アクションモード時のテクスチャへのファイルパス
     * <int>                        <int>                        <String>
     * アクションモード時の移動力_x ジャンプ力 アクションモード時の高速移動倍率
     * <double>                     <double>   <double>
     * シューティングモード時のサイズ_x シューティングモード時のサイズ_y シューティングモード時のテクスチャへのファイルパス
     * <int>                            <int>                            <String>
     * シューティングモード時の移動力_x シューティングモード時の移動力_y シューティングモード時の高速移動倍率
     * <double>                         <double>                         <double>
     * 変形中のサイズ_x 変形中のサイズ_y 変形中のテクスチャへのファイルパス 変形に要するフレーム数
     * <int>            <int>            <String>                           <int>
     * HP       向き        接地しているか シューティングモードか(重力の影響を受けないか) 使用するAIの名前
     * <double> <Direction> <boolean>      <boolean>                                      <ai_op>
     *
     * 引数  ：ファイル名
     * 戻り値：プレイヤーオブジェクト
     */
    public static playerObj file_to_playerObj(String _file_path, Stage _belong) /*throws FileNotFoundException*/{
        String    script_path = ((Paths.get(window.file_path_corres(_file_path)).getParent() == null)?
                                     "" : Paths.get(window.file_path_corres(_file_path)).getParent().toString() + "\\");

        try{
            BufferedReader bRead = new BufferedReader(new FileReader(window.file_path_corres(_file_path)));
            String[] str = bRead.readLine().split(" ");
            bRead.close();

            int i = 0;

            point<Double >  _loc                    = new point<Double >(Double.parseDouble(str[  i]),  //座標
                                                                         Double.parseDouble(str[++i]));
            String          _bullet_path            = window.file_path_corres(script_path + str[++i]);  //攻撃時に生成するオブジェクトリストへのパス
            String          _reload_path            = window.file_path_corres(script_path + str[++i]);  //bulletの発射間隔

            point<Integer> _act_size                = new point<Integer>(Integer.parseInt  (str[++i]),  //アクションモード時のサイズ
                                                                         Integer.parseInt  (str[++i]));
            String         _act_texture             = window.file_path_corres(script_path + str[++i]);  //アクションモード時のテクスチャへのパス
            point<Double > _act_move_rate           = new point<Double >(Double.parseDouble(str[++i]),  //アクションモード時の移動力
                                                                         Double.parseDouble(str[++i]));
            double         _act_highspeed_rate      = Double.parseDouble                   (str[++i]);  //アクションモード時の高速移動倍率
            point<Integer> _stg_size                = new point<Integer>(Integer.parseInt  (str[++i]),  //シューティングモード時のサイズ
                                                                         Integer.parseInt  (str[++i]));
            String         _stg_texture             = window.file_path_corres(script_path + str[++i]);  //シューティングモード時のテクスチャへのパス
            point<Double > _stg_move_rate           = new point<Double >(Double.parseDouble(str[++i]),  //シューティングモード時の移動力
                                                                         Double.parseDouble(str[++i]));
            double         _stg_highspeed_rate      = Double.parseDouble                   (str[++i]);  //シューティングモード時の高速移動倍率
            point<Integer> _dfm_size                = new point<Integer>(Integer.parseInt  (str[++i]),  //変形中のサイズ
                                                                         Integer.parseInt  (str[++i]));
            String         _dfm_texture             = window.file_path_corres(script_path + str[++i]);  //変形中のテクスチャへのパス
            int            _dfm_time                = Integer.parseInt                     (str[++i]);  //変形に使用するフレーム数
            double         _hp                      = Double.parseDouble                   (str[++i]);  //HP
            Direction      _dir                     = Direction.parseDirection             (str[++i]);  //向き
            boolean        _is_gnd                  = Boolean.parseBoolean                 (str[++i]);  //接地しているか
            boolean        _is_shooting_and_not_gnd = Boolean.parseBoolean                 (str[++i]);  //シューティングモードか(重力の影響を受けるか)
            int            _use_ai                  = ai_op.name_to_using_ai               (str[++i]);  //使用するAI

            return new playerObj(_loc                    , _bullet_path       , _reload_path  ,
                                 _act_size               , _act_texture       , _act_move_rate, _act_highspeed_rate,
                                 _stg_size               , _stg_texture       , _stg_move_rate, _stg_highspeed_rate,
                                 _dfm_size               , _dfm_texture       , _dfm_time     ,
                                 _hp                     , _dir               , _is_gnd       ,
                                 _is_shooting_and_not_gnd, _use_ai            , _belong            );
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
            do_deform = true;
            dfm_use_texture_num.x = ((is_act_to_stg)? DFM_TEXTURE_MAX - (timer_deform / DFM_TEXTURE_MAX) : timer_deform / DFM_TEXTURE_MAX);
            if(dfm_use_texture_num.x >= DFM_TEXTURE_MAX)
                dfm_use_texture_num.x = DFM_TEXTURE_MAX - 1;
            if(dfm_use_texture_num.x < 0)
                dfm_use_texture_num.x = 0;

            if(timer_deform == 0){
                //タイマー値の変更
                //状態遷移
                is_shooting  = !is_shooting;
                is_gravitied = !is_gravitied;
                //サイズ変更処理


                location = new point<Double>(location.x - (((is_act_to_stg)? stg_size.x : act_size.x) - dfm_size.x) / 2,
                                             location.y - (((is_act_to_stg)? stg_size.y : act_size.y) - dfm_size.y));
            }
            return true;
        }
        do_deform = false;
        return false;
    }

    /*
     * 変形の開始処理
     * 引数  ：なし
     * 戻り値：なし
     */
    private void deform_begin(){

        is_act_to_stg = !is_shooting;
        timer_deform  = dfm_time;
        dfm_use_texture_num.x = ((is_act_to_stg)? 0 : DFM_TEXTURE_MAX - 1);
        do_deform = true;

        //サイズ変更処理
        location = new point<Double>(location.x + (((is_act_to_stg)? act_size.x : stg_size.x) - dfm_size.x) / 2,
                                     location.y + (((is_act_to_stg)? act_size.y : stg_size.y) - dfm_size.y));

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

        hp -= _source.atk * ((do_deform)? DFM_DAMAGE_RATE : ((is_shooting)? STG_DAMAGE_RATE : ACT_DAMAGE_RATE));
        _source.is_dead = true;
        if(hp < 0.0d)
            is_dead = true;

        return;
    }

    /*
     * 中心点を返す
     * 引数  ：なし
     * 戻り値：中心点
     */
    @Override
    public point<Double> get_center(){
        return new point<Double>(location.x + ((double)((is_shooting)? stg_size.x : act_size.x) / 2),
                                 location.y + ((double)((is_shooting)? stg_size.y : act_size.y) / 2));
    }

    /*
     * 初期化
     * 引数  ：それぞれのデータ
     */
    private void init(point<Double > _location                     , point<Double> _accel           , String         _bullet_path  , String _reload_path                ,
                      point<Integer> _action_size                  , String        _act_texture_path, point<Double>  _act_move_rate, double _act_highspeed_rate         ,
                      point<Integer> _shooting_size                , String        _stg_texture_path, point<Double>  _stg_move_rate, double _stg_highspeed_rate         ,
                      point<Integer> _deform_size                  , String        _dfm_texture_path, int           _timer_deform  ,
                      point<Integer> _dfm_use_texture_num          , int           _dfm_time        , boolean       _is_act_to_stg ,
                      double         _hp                           , Direction     _dir             , boolean        _is_gnd       ,
                      boolean        _is_shooting_and_not_gravitied, int           _using_AI        , Stage         _belong        ){
        //参照を受け取る
        belong        = _belong;

        //それぞれの変数を適した形で初期化
        location          = new point<Double >(_location);
        accel             = new point<Double>(_accel);

        force_m           = Force.FRIEND;

        bullet_path       = _bullet_path;
        bullet            = dmgObj.file_to_dmgObj_ArrayList(bullet_path, belong);
        reload_path       = _reload_path;
        reload            = file_to_intArrayList(reload_path);
        timer_reload      = new ArrayList<Integer>();
        for(int i = 0; i < reload.size(); i++)
            timer_reload.add(TIMER_STOP);

        act_size            = new point<Integer>(_action_size);
        act_texture_m       = new texture(_act_texture_path, act_size);
        act_move_rate       = new point<Double>(_act_move_rate);
        act_highspeed_rate  = _act_highspeed_rate;

        stg_size            = new point<Integer>(_shooting_size);
        stg_texture_m       = new texture(_stg_texture_path, stg_size);
        stg_move_rate       = new point<Double>(_stg_move_rate);
        stg_highspeed_rate  = _stg_highspeed_rate;

        dfm_size            = new point<Integer>(_deform_size);
        dfm_texture_m       = new texture(_dfm_texture_path, dfm_size);
        dfm_use_texture_num = new point<Integer>(_dfm_use_texture_num);
        timer_deform        = _timer_deform;
        dfm_time            = _dfm_time;
        is_act_to_stg       = _is_act_to_stg;

        hp                  = _hp;
        dir                 = _dir;
        is_gnd              = _is_gnd;
        is_shooting         = _is_shooting_and_not_gravitied;
        is_gravitied        = !_is_shooting_and_not_gravitied;


        ai                  = new ai_op(ai_op.AI_USER_INPUT);
        ai_prev             = new ArrayList<ai_op>();
        for(int i = 0; i < AI_PREV_MAX; i++){
            ai_prev.add(0, new ai_op(ai));
        }

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