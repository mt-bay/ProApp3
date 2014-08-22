package stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;

import common.point;
import common.rect;

public class charObj extends rect {
    /* メンバ変数 */

    public                 point<Double> location;          //位置情報
    public                 point<Double> accel;             //移動力
    public                 double        hp;                //体力値
    public                 Direction     dir;               //向き
    public                 boolean       is_gnd;            //接地しているか

    public                 Force         force_m;           //自オブジェクトの勢力(自分の勢力の攻撃は受けない)
    public                 texture       texture_m;         //テクスチャ

    public                 Stage         belong;            //自オブジェクトの所属ステージ

    // 状態変数
    protected              int           timer_not_visible; //残りの無敵フレーム数
    public                 boolean       is_gravitied;      //重力に依存するかどうか

    public                 boolean       is_dead;           //オブジェクトがそのフレームで消えるか

    //その他

    /* コンストラクタ・デストラクタ */
    /*
     * デフォルトコンストラクタ
     * 引数：なし
     */
    public charObj() {

        init(new rect()    , new point<Double>(), 0.0  ,
             Direction.LEFT, false              , false,
             Force.NEUTRAL , null               , null );
    }

    /* コピーコンストラクタ
     * 引数  ：元データ
     */
    public charObj(charObj _obj){
        init(_obj        , _obj.accel                    , _obj.hp          ,
             _obj.dir    , _obj.is_gnd                   , _obj.is_gravitied,
             _obj.force_m, _obj.texture_m.get_file_path(), _obj.belong      );

    }

    /*
     * データ指定型コンストラクタ
     * 引数：それぞれのデータ
     */
    public charObj(rect      _rect        , point<Double> _accel     , double _hp            ,
                   Direction _dir         , boolean       _is_gnd     , boolean _is_gravitied,
                   Force     _force       , String    _texture_path, Stage         _belong){
        init(_rect , _accel      , _hp          ,
             _dir  , _is_gnd     , _is_gravitied,
             _force, _texture_path, _belong);
    }

    /* メソッド */
    /*
     * 状態アップデート
     * 引数：なし
     */
    public void update() {

    }

    /*
     * 描画
     * 引数  ：なし or 描画倍率
     * 戻り値：なし
     */
    public void draw(){
        draw(1.0f);
    }
    public void draw(float _scale) {
        texture_m.draw(location.DtoF(), belong, _scale);
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
     * 座標_x   座標_y   サイズ_x サイズ_y 加速度_x 加速度_y 体力値   方向        接地しているか 重力に依存するか 自オブジェクトの勢力 テクスチャへのファイルパス
     * <double> <double> <int>    <int>    <double> <double> <double> <Direction> <boolean>      <boolean>        <Force>              <String>
     *
     * 引数  ：ファイルパス
     * 戻り値：生成されたArrayList<charObj>
     */
    public static ArrayList<charObj> file_to_charObj_ArrayList(String _file_path, Stage _belong){
        ArrayList<charObj> char_obj_al = new ArrayList<charObj>();

        try{
            BufferedReader bRead = new BufferedReader(new FileReader(_file_path));
            String         line  = "";
            String[]       str   = null;

            while((line = bRead.readLine()) != null){
                str = line.split(" ");
                char_obj_al.add(new charObj(new rect(new point<Double >(Double.parseDouble(str[0]), Double.parseDouble(str[1])) ,
                                                     new point<Integer>(Integer.parseInt  (str[2]), Integer.parseInt  (str[3]))),
                                            new point<Double>(Double.parseDouble(str[4]), Double.parseDouble(str[5]))           ,
                                            Double.   parseDouble   (str[6])                                                    ,
                                            Direction.parseDirection(str[7])                                                    ,
                                            Boolean.  parseBoolean  (str[8])                                                    ,
                                            Boolean.  parseBoolean  (str[9])                                                    ,
                                            Force.    parceForce    (str[10])                                                   ,
                                            str[11]                                                                             ,
                                            _belong                                                                             ));
            }
            bRead.close();

        }catch(Exception e){
        }
        return char_obj_al;
    }


    /*
     * 移動
     * ブレゼンハムの直線描画アルゴリズムを基にする
     * 引数  ：なし
     * 戻り値：なし
     */
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
     * 交差処理
     * 引数  ：移動量
     * 戻り値：なし
     */
    protected void process_contract(point<Integer> move){
        //交差判定
        // マップオブジェクト
        if(move.y != 0)
            is_gnd = false;

        if(belong.map_data.is_collision(this)){
            location.x -= move.x.doubleValue(); location.y -= move.y.doubleValue();

            if(move.y > 0)
                is_gnd = true;
        }

        for(int i = 0; i < belong.damage.size(); i++){
            if(belong.damage.get(i).is_collision(this))
                receve_damage(belong.damage.get(i));
        }
    }


    /*
     * 初期化
     * 引数  ：必要なデータ
     * 戻り値：なし
     */
    protected void init(rect      _rect        , point<Double> _accel       , double  _hp         ,
                        Direction _dir         , boolean       _is_gnd      , boolean _is_gravitied,
                        Force     _force       ,String         _texture_path, Stage   _belong     ){
        init(_rect.location, _rect.size);

        location      = new point<Double>(0.0d, 0.0d);

        accel         = new point<Double>(_accel);
        hp            = _hp;
        dir           = _dir;
        is_gnd        = _is_gnd;
        is_gravitied  = _is_gravitied;
        force_m       = _force;
        texture_m     = new texture(_texture_path, size);

        belong       = _belong;
    }
}
