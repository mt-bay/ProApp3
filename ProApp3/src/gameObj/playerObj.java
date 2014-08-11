package gameObj;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.newdawn.slick.Input;
import org.newdawn.slick.SpriteSheet;

import IO.config;

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
    protected            int            timer_deform; //シューティング←→アクションの変形残りフレーム
    protected            Input[]        ip_prev;      //過去の入力
    protected            Input          ip_now;       //最新の入力

    //その他
    private static final int    num_prev = 6;         //入力の記憶数
    private static       double act_mv   = 2.5;       //アクションモード時の左右移動力

    //ユーザ入力関係

    /* コンストラクタ */
    /* デフォルトコンストラクタ
     * 引数：なし
     */
    public playerObj() {
        init(new rect()        , new point<Double>(), 0.0d               ,
             Direction.RIGHT   , false              , true               ,
             null              , null               , false              ,
             -1                , new Input[num_prev]);
    }

    public playerObj(playerObj obj){
    	init(new rect(new point<Double>(obj.location)   , new point<Integer>(obj.size)),
             new point<Double>(obj.accel), obj.hp     ,
             obj.dir                     , obj.is_gnd , obj.is_gravitied,
             obj.texture                 , obj.belong , obj.is_shooting ,
             obj.timer_deform            , obj.ip_prev);

    }

    /* ファイルから生成用コンストラクタ
     * 引数：ファイル名
     */
    public playerObj(String filename){

    }

    public playerObj(rect      _rect    , point<Double> _accel              , double  _hp         ,
                     Direction _dir     , boolean       _is_gnd_and_shooting, boolean _isGravitied,
                     String    _texture , Stage         _where_i_am){
       init(_rect, _hp, _dir, _is_gnd_and_shooting, _isGravitied, _texture, _where_i_am);
    }

    /* メソッド */
    /* 状態アップデート(オーバーライド)
     * 引数  ：なし
     * 戻り値：生成コード
     */
    @Override
    public CreateCode[] update() {
        CreateCode[] cc = null;

        //変形処理を行い、変形時でなければ通常操作
        if(!deform())
        {
            //共通操作
            //攻撃(ダメージオブジェクト生成)
            if (ip_now.isKeyDown(config.attack)) {

            }

            //左右移動
            if (ip_now.isKeyDown(config.left)) {
                accel.x = -act_mv * ((ip_now.isKeyDown(config.highsp) ? 1 : 2));
                dir = Direction.LEFT;
            }
            else if (ip_now.isKeyDown(config.right)) {
                accel.x = act_mv * ((ip_now.isKeyDown(config.highsp) ? 1 : 2));
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
                    accel.y = act_mv * ((ip_now.isKeyDown(config.highsp) ? 1 : 2));
                }
                else if (ip_now.isKeyDown(config.up)) {
                    accel.y = -act_mv * ((ip_now.isKeyDown(config.highsp) ? 1 : 2));
                }
            }
        }

        return cc;
    }


    /* 描画(作成中)
     * テクスチャは2行で収められている前提で、行とモードの関係は以下のようになっているとする
     *
     * 1行目：アクションモード
     * 2行目：シューティングモード
     *
     * 引数  ：なし or 描画倍率
     * 戻り値：なし
     */
    @Override
    public void draw(){

    }
    @Override
    public void draw(float _scale){
        if(is_shooting){
            texture_act_m.draw(location.DtoF(), _scale);
        }
    }

    /* ファイルからプレイヤーオブジェクトのデータを読み込む
     * データは1行に格納，各データは空白で区切られ、以下のデータを持っている前提で動作する
     * 座標_x   座標_y   サイズ_x サイズ_y HP       向き        接地しているか シューティングモードか(重力の影響を受けるか) テクスチャへのパス
     * <double> <double> <int>    <int>    <double> <Direction> <boolean>      <boolean>                                    <string>
     * 引数  ：ファイル名
     * 戻り値：プレイヤーオブジェクト
     */
    public static playerObj file_to_playerObj(String _filename, Stage _belong) throws FileNotFoundException{
        playerObj p_obj = new playerObj();
        try{
            BufferedReader bRead = new BufferedReader(new FileReader(_filename));
            String[] str = bRead.readLine().split(" ");

            p_obj = new playerObj(new rect(new point<Double >(Double.parseDouble(str[ 0]), Double.parseDouble(str[1])),
                                           new point<Integer>(Integer.parseInt(str[ 2])  , Integer.parseInt(str[3]))) ,
                                  new point<Double>(0.0d, 0.0d)                                                       ,
                                  Double.parseDouble(str[ 4])                                                         ,
                                  Direction.parseDirection(str[ 5])                                                   ,
                                  Boolean.parseBoolean(str[ 6])                                                       ,
                                  Boolean.parseBoolean(str[ 7])                                                       ,
                                  str[8]                                                                              ,
                                  _belong                                                                             );
            bRead.close();
        }catch(Exception e){
            dLog.write_exception(e, new Throwable());
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
     * 初期化
     * 引数  ：それぞれのデータ
     */
    private void init(rect    _rect      , double  _hp                       , Direction   _dir         ,
                      boolean _isGnd     , boolean _is_gravitied_and_shooting, String      _texture_path,
                      Stage   _belong){
        SpriteSheet sp;
        try{
            sp = new SpriteSheet(_texture_path, _rect.size.x, _rect.size.y);
        }catch(Exception e){
            sp = null;
            dLog.write_exception(e                                                ,
                                 new Throwable().getStackTrace()[0].getClassName(),
                                 new Throwable().getStackTrace()[0].getMethodName());
        }
        init(_rect, new point<Double>(0.0d, 0.0d), _hp,
             _dir , _isGnd                       , _is_gravitied_and_shooting,
             sp   , _where_i_am                  , _is_gravitied_and_shooting,
             -1   , new Input[num_prev]          );
    }

    private void init(rect        _rect    , point<Double> _accel     , double  _hp         ,
                      Direction   _dir     , boolean       _isGnd     , boolean _isGravitied,
                      SpriteSheet _texture , Stage         _where_i_am, boolean _isShooting ,
                      int         _t_deform, Input[]       _ip_prev   ){
    }

}