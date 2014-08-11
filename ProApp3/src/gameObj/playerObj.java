package gameObj;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.newdawn.slick.Input;

import IO.config;

import common.point;

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

    /* 定数 */
    private static final int    NUM_PREV   = 6;         //入力の記憶数
    private static       double ACT_MV     = 2.5;       //アクションモード時の左右移動力
    private static final int    TIMER_STOP = -1;        //タイマー変数の停止状態用

    //ユーザ入力関係

    /* コンストラクタ */
    /* デフォルトコンストラクタ
     * 引数：なし
     */
    public playerObj() {

    }

    public playerObj(playerObj obj){

    }

    /* ファイルから生成用コンストラクタ
     * 引数：ファイル名
     */
    public playerObj(String filename){

    }

    public playerObj(point<Double>  _location     , point<Integer> _size_action     , String        _texture_path_act,
                     point<Integer> _size_shooting, String         _texture_path_stg, point<Double> _accel           ,
                     double _hp, Direction _dir, boolean _is_gnd,
                     boolean _is_gravitied_and_shooting, Stage _belong){
       init(_location, _size_action, _texture_path_act,
            _size_shooting, _texture_path_stg, _accel,
            _hp, _dir, _is_gnd,
            _is_gravitied_and_shooting, TIMER_STOP, TIMER_STOP,
            _belong, new Input[NUM_PREV]);
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
        //シューティングモード時の描画
        if(is_shooting){
            texture_stg_m.draw(location.DtoF(), _scale);
        }
        //アクションモード時の描画
        else{
            texture_act_m.draw(location.DtoF(), _scale);

        }
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

            p_obj = new playerObj(new point<Double> (Double.parseDouble(str[ 0]), Double.parseDouble(str[ 1])), //座標
                                  new point<Integer>(Integer.parseInt(str[ 2])  , Integer.parseInt(str[ 3]))  , //アクションモード時のサイズ
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
    private void init(point<Double>  _location                 , point<Integer> _size_action     , String        _texture_path_act ,
                      point<Integer> _size_shooting            , String         _texture_path_stg, point<Double> _accel            ,
                      double         _hp                       , Direction      _dir             , boolean       _is_gnd           ,
                      boolean        _is_gravitied_and_shooting, int           _timer_deform     , int           _timer_not_visible,
                      Stage          _belong                   , Input[]       _ip_prev   ){
        //それぞれの変数を適した形で初期化
        location          = new point<Double>(_location);
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

}