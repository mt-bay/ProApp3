package gameObj;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import org.newdawn.slick.SpriteSheet;

import window.window;
import IO.debugLog;

import common.point;
import common.rect;

public class dmgObj extends rect {
    /* メンバ変数 */
    public point<Double>  accel;        // 移動力
    public double         atk;          // charObj衝突時の体力変動値

    public SpriteSheet    texture;      //テクスチャ
    public String         texture_path; //テクスチャへのパス
    public int            use_texture;  //テクスチャのどこを使うか

    /* コンストラクタ */
    /*
     * デフォルトコンストラクタ 引数：なし
     */
    public dmgObj() {
        set(new rect(), new point<Double>(), 0.0, "");
    }

    /*
     * コピーコンストラクタ
     * 引数：コピー元
     */
    public dmgObj(dmgObj obj) {
        set(obj.location, obj.size, obj.accel, obj.atk, obj.texture_path);
    }

    /*
     * 値指定型コンストラクタ
     * 引数：それぞれのデータ
     */
    public dmgObj(rect _rect, point<Double> _accel, double _atk, String _texture_path) {
        set(_rect, _accel, _atk, _texture_path);
    }


    /* メソッド */
    /* 描画(作成中)
     * 引数  ：なし or 描画倍率
     * 戻り値：なし
     */
    public void draw(){
        draw(1.0f);
    }
    public void draw(float _scale){
        if(texture == null)
            return;
        if(window.comprise(this)){


        }
    }

    /* ファイルからArrayList<dmgObj>生成
     * 要素は1行1つで、要素1つごとに以下の内容を持っているものとする
     *
     * 座標_x   座標_y   サイズ_x サイズ_y 初期移動力_x 初期移動力_y 体力変動値 テクスチャへのファイルパス
     * <double> <double> <int>    <int>    <double>     <double>     <double>   <string>
     *
     * 引数  ：ファイルパス
     * 戻り値：生成されたダメージオブジェクトリスト
     */
    public static ArrayList<dmgObj> file_to_dmgObj_ArrayList(String _file_path){
        ArrayList<dmgObj> d_obj_al = new ArrayList<dmgObj>();

        try{
            BufferedReader bRead = new BufferedReader(new FileReader(_file_path));
            String         line  = "";
            String[]       str   = null;

            while((line = bRead.readLine()) != null){
                str = line.split(" ");
                d_obj_al.add(new dmgObj(new rect(new point<Double >(Double.parseDouble(str[0]), Double.parseDouble(str[1])) ,
                                                 new point<Integer>(Integer.parseInt  (str[2]), Integer.parseInt  (str[3]))),
                                        new point<Double>(Double.parseDouble(str[4]), Double.parseDouble(str[5]))           ,
                                        Double.parseDouble(str[6])                                                          ,
                                        str[7]                                                                            ));
            }

            bRead.close();
        }catch(Exception e){
            debugLog.getInstance().write_exception(e,
                                                   new Throwable().getStackTrace()[0].getClassName(),
                                                   new Throwable().getStackTrace()[0].getMethodName());
        }

        return d_obj_al;
    }


    /*
     * setter
     * 引数：それぞれのデータ
     */
    private void set(rect   _rect        , point<Double> _accel, double _atk,
                     String _texture_path){
        set(_rect.location, _rect.size, _accel, _atk, _texture_path);
    }
    private void set(point<Double> _location, point<Integer> _size        , point<Double> _accel,
                     double        _atk     , String         _texture_path){
        set(_location, _size);

        accel = new point<Double>(accel);
        atk   = _atk;
        texture_path = _texture_path;
        try{
            texture = new SpriteSheet(texture_path, size.x, size.y);
        }catch(Exception e){
            debugLog.getInstance().write_exception(e, new Throwable());
        }


    }

}
