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

import common.point;
import common.rect;

public class charObj extends rect {
    /* メンバ変数 */

    public                 point<Double>      location;         //位置情報
    public                 point<Double>      accel;            //移動力
    public                 double             hp;               //体力値
    public                 Direction          dir;              //向き
    public                 boolean            is_gnd;           //接地しているか

    public                 Force              force_m;           //自オブジェクトの勢力(自分の勢力の攻撃は受けない)
    public                 texture            texture_m;         //テクスチャ

    public                 ai_op              ai;                //使用するAI
    public                 ArrayList<ai_op>   ai_prev;           //AIの過去データ

    public                 point<Double>      move_rate;         //移動力
    public                 double             highspeed_rate;    //高速移動時の倍率

    public                 ArrayList<dmgObj>  bullet;            //使用する弾リスト
    public                 String             bullet_path;       //弾リストへのパス

    public                 ArrayList<Integer> reload;            //攻撃間隔を記録
    public                 String             reload_path;       //配列へのパス
    public                 ArrayList<Integer> timer_reload;      //攻撃間隔

    public                 Stage              belong;            //自オブジェクトの所属ステージ

    // 状態変数
    public                 boolean            is_gravitied;      //重力に依存するかどうか

    public                 boolean            is_dead;           //オブジェクトがそのフレームで消えるか

    protected static final int                TIMER_STOP = -1;   //タイマー変数の停止状態用
    /* 定数 */
    public    static final int                AI_PREV_MAX = 5;   //過去の移動命令の保存数

    //その他

    /* コンストラクタ・デストラクタ */
    /*
     * デフォルトコンストラクタ
     * 引数：なし
     */
    public charObj() {
/*
        init(new rect(), new point<Double>(),  ""           , ""            ,
                0.0    , new point<Double>(),           0.0d, Direction.LEFT,
                false  , false              , Force.NEUTRAL ,
                ""     , ai_op.AI_NO_USE    ,    null       );
        */
    }

    /* コピーコンストラクタ
     * 引数  ：元データ
     */
    public charObj(charObj _obj){
        init(_obj.location                 , _obj.size          , _obj.accel  ,
             _obj.bullet_path              , _obj.reload_path   , _obj.hp     ,
             _obj.move_rate                , _obj.highspeed_rate, _obj.dir    ,
             _obj.is_gnd                   , _obj.is_gravitied  , _obj.force_m,
             _obj.texture_m.get_file_path(), _obj.ai.using_AI   , _obj.belong );

    }

    /*
     * データ指定型コンストラクタ
     * 引数：それぞれのデータ
     */
    public charObj(point<Double> _location      , point<Integer> _size  , String        _bullet_path ,
                   String        _reload_path   , double         _hp    , point<Double> _move_rate   ,
                   double        _highspeed_rate, Direction      _dir   , boolean       _is_gnd      ,
                   boolean       _is_gravitied  , Force          _force , String        _texture_path,
                   int           _using_ai      , Stage          _belong){
        init(_location, _size, new point<Double>(0.0d, 0.0d),
             _bullet_path , _reload_path   , _hp    ,
             _move_rate   , _highspeed_rate, _dir   ,
             _is_gnd      , _is_gravitied  , _force ,
             _texture_path, _using_ai      , _belong);

    }

    /* メソッド */
    /*
     * 状態アップデート
     * 引数：なし
     */
    public void update() {
        //前処理
        // AIの状態更新
        update_ai();

        //移動処理
        // フラグチェック
        point<Double> move_dir = new point<Double>(0.0, 0.0);
        //移動方向チェック
        //左右
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
        //上下
        //重力依存チェック(依存しているなら上下移動しない)
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
        //移動速度チェック
        if((ai.move & ai_op.MOVE_MOVE_NOMAL    ) != ai_op.MOVE_NONE){
            if(move_dir.x != 0.0){
                accel.x = move_dir.x * move_rate.x;
            }
            if(move_dir.y != 0.0 && !is_gravitied){
                accel.y = move_dir.y * move_rate.y;
            }
        }
        if((ai.move & ai_op.MOVE_MOVE_HIGHSPEED) != ai_op.MOVE_NONE){
            if(move_dir.x != 0.0){
                accel.x = highspeed_rate * move_dir.x * move_rate.x;
            }
            if(move_dir.y != 0.0 && !is_gravitied){
                accel.y *= highspeed_rate * move_dir.y * move_rate.y;
            }
        }

        //ジャンプ処理
        // フラグチェック
        if((ai.move & ai_op.MOVE_JUMP) != ai_op.MOVE_NONE){

            //重力依存チェック(依存していないならジャンプしない)
            if(is_gravitied){

                //通常ジャンプ
                if((ai.move & ai_op.MOVE_JUMP_NOMAL) != ai_op.MOVE_NONE){
                    //接地かつ1つ前のフレームの入力でジャンプ命令がない場合
                    if((!((ai_prev.get(0).move & ai_op.MOVE_JUMP_NOMAL) != ai_op.MOVE_NONE)) &&
                        is_gnd){
                        accel.y -= move_rate.y;
                    }
                }
            }
        }


        //攻撃処理
        if(ai.attack != ai_op.ATTACK_NONE){
            //通常攻撃
            if((ai.attack & ai_op.ATTACK_NOMAL) != ai_op.ATTACK_NONE){
                attack(0, this.to_rect());
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

    /*
     * AI更新処理
     * 引数  ：なし
     * 戻り値：なし
     */
    protected void update_ai(){
        ai_prev.add(0, new ai_op(ai));
        while(ai_prev.size() > AI_PREV_MAX){
            ai_prev.remove(ai_prev.size() - 1);
        }

        ai.update(this);
    }

    /*
     * 描画
     * 引数  ：なし or 描画倍率
     * 戻り値：なし
     */
    public void draw(Graphics g){
        texture_m.draw(g, location.DtoF(), ai.texture_num.x, ai.texture_num.y, belong, ((dir == Direction.RIGHT)? false : true));
        if(Main._DEBUG){
            if(window.comprise(this.to_rect(), belong)){
                rect r = belong.relative_camera_rect(this.to_rect());
                g.setColor(new Color(0xffff00));
                g.drawRect(r.location.x.floatValue() * window.SCALE, r.location.y.floatValue() * window.SCALE,
                           r.size.x.floatValue()     * window.SCALE, r.size.y.floatValue()     * window.SCALE);
            }
        }
    }

    /*
     * rectにキャスト
     * 引数  ：なし
     * 戻り値：rectにキャストした自オブジェクト
     */
    public rect to_rect(){
        return new rect(new point<Double >(location),
                        new point<Integer>(size)    );
    }

    /*
     * 攻撃処理
     * 引数  ：攻撃間隔
     * 戻り値：なし
     */
    protected void attack(int _use_number, rect _r){
        if(timer_reload.get(_use_number) <= 0){
            dmgObj _d = new dmgObj(bullet.get(_use_number));
            _d.accel.x    *= ((this.dir == Direction.RIGHT)? 1.0d : -1.0d);

            _d.location.x += _r.location.x + ((this.dir == Direction.RIGHT)? _r.size.x.doubleValue() + 1.0 : -(_d.size.x.doubleValue() + 1.0)) + accel.x;
            _d.location.y += _r.location.y;
            _d.dir = dir;

            belong.create_dmg.add(new dmgObj(_d));
            timer_reload.set(_use_number, reload.get(_use_number));
        }
    }

    /*
     * ダメージ処理
     * 引数  ：ダメージ源となるdmgObj
     * 戻り値：なし
     */
    protected void receve_damage(dmgObj _source){
        if(_source == null)
            return;
        if(!_source.force_m.is_attackable(this.force_m))
            return;
        if(_source.is_dead)
            return;

        hp -= _source.atk;
        _source.is_dead = true;
        if(hp <= 0.0d)
            is_dead = true;

        return;
    }

    /* ファイルからArrayList<charObj>生成
     * 要素は1行1つで、要素1つごとに以下の内容を持っているものとする
     *
     * 座標_x   座標_y   サイズ_x サイズ_y 攻撃時に生成するダメージオブジェクトのスクリプトパス 発射間隔のスクリプトパス
     * <double> <double> <int>    <int>    <String>                                             <String>
     *
     * 体力値   移動力_x 移動力_y 高速移動時の倍率 向き        接地しているか 重力に依存するか 自オブジェクトの勢力 テクスチャへのファイルパス 使用するAI
     * <double> <double> <double> <double>         <Direction> <boolean>      <boolean>        <Force>              <String>                   <ai_op>
     *
     * 引数  ：ファイルパス
     * 戻り値：生成されたArrayList<charObj>
     */
    public static ArrayList<charObj> file_to_charObj_ArrayList(String _file_path, Stage _belong){
        ArrayList<charObj> char_obj_al = new ArrayList<charObj>();
        String             script_path = ((Paths.get(window.file_path_corres(_file_path)).getParent() == null)?
                                             "" : Paths.get(window.file_path_corres(_file_path)).getParent().toString() + "\\");

        try{



            BufferedReader bRead = new BufferedReader(new FileReader(window.file_path_corres(_file_path)));
            String         line  = "";
            String[]       str   = null;

            int i = 0;

            point<Double>  _location;
            point<Integer> _size;
            String         _bullet_path;
            String         _reload_path;
            double         _hp;
            point<Double>  _move_rate;
            double         _highspeed_rate;
            Direction      _dir;
            boolean        _is_gnd;
            boolean        _is_gravitied;
            Force          _force;
            String         _texture_path;
            int            _use_ai;

            while((line = bRead.readLine()) != null){
                str = line.split(" ");
                i = 0;

                _location       = new point<Double >(Double.parseDouble (str[  i]) ,
                                                     Double.parseDouble (str[++i]));
                _size           = new point<Integer>(Integer.parseInt   (str[++i]) ,
                                                    Integer.parseInt    (str[++i]));
                _bullet_path    = window.file_path_corres(script_path +  str[++i]) ;
                _reload_path    = window.file_path_corres(script_path +  str[++i]) ;
                _hp             = Double.parseDouble                    (str[++i]) ;
                _move_rate      = new point<Double>(Double.parseDouble  (str[++i]) ,
                                                    Double.parseDouble  (str[++i]));
                _highspeed_rate = Double.parseDouble                    (str[++i]) ;
                _dir            = Direction.parseDirection              (str[++i]) ;
                _is_gnd         = Boolean.parseBoolean                  (str[++i]) ;
                _is_gravitied   = Boolean.parseBoolean                  (str[++i]) ;
                _force          = Force.parceForce                      (str[++i]) ;
                _texture_path   = window.file_path_corres(script_path +  str[++i]) ;
                _use_ai         = ai_op.name_to_using_ai                (str[++i]) ;

                char_obj_al.add(new charObj(_location      , _size  , _bullet_path ,
                                            _reload_path   , _hp    , _move_rate   ,
                                            _highspeed_rate, _dir   , _is_gnd      ,
                                            _is_gravitied  , _force , _texture_path,
                                            _use_ai        , _belong));
            }
            bRead.close();

        }catch(Exception e){
            e.printStackTrace();
        }
        return char_obj_al;
    }

    /*
     * ファイルからint型配列を生成(改行区切り)
     * 引数  ：ファイルパス
     * 戻り値：ファイルの内容を基にしたint型配列
     */
    public static ArrayList<Integer> file_to_intArrayList(String _file_path){
        ArrayList<Integer> ret_val = new ArrayList<Integer>();
        try{
            BufferedReader bRead = new BufferedReader(new FileReader(_file_path));
            String str;
            while((str = bRead.readLine()) != null){
                ret_val.add(Integer.parseInt(str));
            }
            bRead.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return ret_val;
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
     * 交差処理
     * 引数  ：移動量
     * 戻り値：なし
     */
    protected void process_contract(point<Double> move, point<Double> move_actual){
        //交差判定
        // マップオブジェクト
        if(move.y != 0)
            is_gnd = false;

        if(belong.map_data.is_collision(this.to_rect())){
            location.x    -= move.x.doubleValue(); location.y    -= move.y.doubleValue();
            move_actual.x -= move.x.doubleValue(); move_actual.y -= move.y.doubleValue();

            if(move.y > 0)
                is_gnd = true;
        }

        for(int i = 0; i < belong.damage.size(); i++){
            if(belong.damage.get(i).is_collision(this.to_rect()))
                receve_damage(belong.damage.get(i));
        }
    }




    /*
     * 初期化
     * 引数  ：必要なデータ
     * 戻り値：なし
     */
    protected void init(point<Double> _location    , point<Integer> _size          , point<Double> _accel ,
                        String        _bullet_path , String         _reload_path   , double        _hp    ,
                        point<Double> _move_rate   ,double          _highspeed_rate, Direction     _dir   ,
                        boolean       _is_gnd      , boolean        _is_gravitied  , Force         _force ,
                        String        _texture_path, int            _use_ai        , Stage         _belong){

        belong       = _belong;

        location       = new point<Double >(_location);
        size           = new point<Integer>(_size);

        force_m        = _force;

        accel          = new point<Double>(_accel);
        hp             = _hp;

        move_rate      = new point<Double>(_move_rate);
        highspeed_rate = _highspeed_rate;

        bullet_path    = _bullet_path;
        bullet         = dmgObj.file_to_dmgObj_ArrayList(bullet_path, belong);

        reload_path    = _reload_path;
        reload         = file_to_intArrayList(reload_path);
        timer_reload   = new ArrayList<Integer>();
        for(int i = 0; i < reload.size(); i++){
            timer_reload.add(TIMER_STOP);
        }

        dir            = _dir;

        is_gnd         = _is_gnd;
        is_gravitied   = _is_gravitied;
        texture_m      = new texture(_texture_path, size);

        ai             = new ai_op(_use_ai);
        ai_prev        = new ArrayList<ai_op>();

        for(int i = 0; i < AI_PREV_MAX; i++){
            ai_prev.add(new ai_op(ai));
        }

    }
}
