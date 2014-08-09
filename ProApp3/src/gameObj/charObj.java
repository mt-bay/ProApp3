package gameObj;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import window.window;
import IO.debugLog;

import common.point;
import common.rect;

public class charObj extends rect {
    /* メンバ変数 */
    public           point<Double> accel;             //移動力
    public           double        hp;                //体力値
    public           Direction     dir;               //向き
    public           boolean       is_gnd;            //接地しているか

    public           SpriteSheet   texture;           //テクスチャ
    public           int           texture_state;     //テクスチャの参照場所

    public           Stage         belong;            //自オブジェクトの所属ステージ

    // 状態変数
    protected        int           timer_not_visible; //残りの無敵フレーム数
    public           boolean       is_gravitied;      //重力に依存するかどうか

    //その他
    protected static debugLog      dLog;       // デバッグログ

    /* コンストラクタ・デストラクタ */
    /*
     * デフォルトコンストラクタ
     * 引数：なし
     */
    public charObj() {
        set(new rect()    , new point<Double>(), 0.0  ,
            Direction.LEFT, false              , false,
            null          , null               );
    }

    /*
     * データ指定型コンストラクタ
     * 引数：それぞれのデータ
     */
    public charObj(rect      _rect        , point<Double> _accel     , double _hp          ,
                   Direction _dir         , boolean       _is_gnd     , boolean _is_gravitied,
                   String    _path_texture, Stage         _belong){
        SpriteSheet _texture;
        try {
            _texture = new SpriteSheet(_path_texture, _rect.size.x, _rect.size.y);
        }
        catch (SlickException e) {
            dLog.write("SpriteSheet\"" + _path_texture + "\" load failed");
            _texture = null;
        }
        set(_rect   , _accel     , _hp         ,
            _dir    , _is_gnd     , _is_gravitied,
            _texture, _belong);
    }

    /* メソッド */
    /*
     * 状態アップデート
     * 引数：なし
     */
    public CreateCode[] update() {
        CreateCode[] cc = null;
        return cc;
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
        if (texture == null)
            return;

        if(!window.comprise(this))
            return;
        texture.startUse();
        rect o = window.relative_camera_rect(this);
        texture.getSubImage(0, texture_state).drawEmbedded(point.DtoF(o.location).x, point.DtoF(o.location).y,
                                                           point.ItoF(o.size).x     ,point.ItoF(o.size).y    );
        texture.endUse();
    }

    /* ファイルからArrayList<charObj>生成
     * 要素は1行1つで、要素1つごとに以下の内容を持っているものとする
     *
     * 座標_x   座標_y   サイズ_x サイズ_y 加速度_x 加速度_y 体力値   方向        接地しているか 重力に依存するか テクスチャへのファイルパス
     * <double> <double> <int>    <int>    <double> <double> <double> <Direction> <boolean>      <boolean>        <String>
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
                char_obj_al.add(new charObj(new rect(new point<Double> (Double.parseDouble(str[0]), Double.parseDouble(str[1])) ,
                                                     new point<Integer>(Integer.parseInt(str[2])  , Integer.parseInt(str[3])  )),
                                            new point<Double>(Double.parseDouble(str[4]), Double.parseDouble(str[5]))           ,
                                            Double.   parseDouble   (str[6])                                                    ,
                                            Direction.parseDirection(str[7])                                                    ,
                                            Boolean.  parseBoolean  (str[8])                                                    ,
                                            Boolean.  parseBoolean  (str[9])                                                    ,
                                            str[10]                                                                             ,
                                            _belong                                                                             ));
            }
            bRead.close();

        }catch(Exception e){

        }

        return char_obj_al;
    }

    /*
     * 変数セット
     * 引数：それぞれのデータ
     *
     */
    protected void set(rect        _rect   , point<Double> _accel, double  _hp         ,
                       Direction   _dir    , boolean       _is_gnd, boolean _isGravitied,
                       SpriteSheet _texture, Stage         _belong){
        accel         = _accel;
        hp            = _hp;
        dir           = _dir;
        is_gnd        = _is_gnd;
        is_gravitied  = _isGravitied;
        texture       = _texture;

        texture_state = 0;

        set(_rect.location, _rect.size);

        belong       = _belong;
    }
}
