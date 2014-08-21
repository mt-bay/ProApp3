package gameObj;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

import window.window;
import IO.debugLog;

import common.point;
import common.rect;

/*
 * マップオブジェクト
 * 描画はマップチップ or 1枚イメージ
 */
public class mapObj {
    /* メンバ変数 */
    public               point<Integer> size_block;       //ブロックサイズ
    public               boolean[][]    is_collisionable; //接触判定を持つか(ブロックごとに管理)
    public               String         map_file_path;    //衝突データ導出に使ったファイルへのパス
    public               Stage          belong;           //どのステージに所属しているか

    //描画関係
    //public double         z;      //仮想Z軸(現在使用予定なし)
    private              Image         img;               //マップ用1枚絵
    private              SpriteSheet   ssheet;            //マップチップ
    private              boolean       use_img;           //描画の際、1枚絵を使うかどうか
    private              int[][]       use_chip;          //マップチップのどの要素を使うか


    /* コンストラクタ */
    /*
     * データ指定形コンストラクタ(汎用)
     * 引数  ：なし
     */
    public mapObj(point<Integer> _size_block, String      _csv_iscollisionable, boolean _use_img,
                  String         _img_or_spritesheet, Stage _belong){
        init(_size_block, _csv_iscollisionable,  _use_img, _img_or_spritesheet, _belong);

    }

    /* メソッド */
    /* 画面描画
     * 引数  ：なし or 描画倍率
     * 戻り値：なし
     */
    public void draw(){
        draw(1.0f);
    }
    public void draw(float _scale){
        if((use_img == false && ssheet == null) ||
           (use_img == true  && img    == null))
            return;

        //1枚絵を仕様する際
        if(use_img){
            point<Float> loc_f = belong.relative_camera_f(new point<Float>(0.0f, 0.0f));
            img.draw(loc_f.x, loc_f.y, _scale);
            return;
        }

        //マップチップを使用する際
        rect chip_loc;
        for(int i = 0; i < use_chip.length; i++){
            for(int j = 0; j < use_chip[i].length; j++){
                chip_loc = get_map_chip(j, i, _scale);

                if(window.comprise(chip_loc, belong)){
                    ssheet.getSubImage(0, use_chip[i][j]).
                           draw(chip_loc.location.x.floatValue(), chip_loc.location.y.floatValue(), _scale);
                }
            }
        }
        return;
    }


    /*
     * マップデータの地形部分と交差 or 内包 or 被内包の関係にあるか
     * 引数  ：対象の四角形
     * 戻り値：交差 or 内包 or 被内包の関係にあるか
     */
    public boolean is_contract(rect _obj){
        rect r;

        for(int i = 0; i < is_collisionable.length; i++){
            for(int j = 0; j < is_collisionable[i].length; j++){
                r = get_map_chip(j, i);
                if(r.is_contact(_obj))
                    return true;
            }
        }

        return false;
    }

    /* ファイル to マップオブジェクト
     * ファイルは、1行ごとに区切られている前提で、以下の内容で動作する
     * ブロックサイズ_x ブロックサイズ_y  csvファイルへのパス 一枚絵を使うか(falseでスプライトシート) 画像 or スプライトシートへのパス
     * <int>            <int>             <string>            <boolean>                               <string>
     */
    public static mapObj file_to_mapObj(String _file_path, Stage _belong){
        mapObj m_obj = null;
        try{
            BufferedReader bRead = new BufferedReader(new FileReader(_file_path));
            String[] str = bRead.readLine().split(" ");
            bRead.close();

            m_obj = new mapObj(new point<Integer>(Integer.parseInt(str[0]), Integer.parseInt(str[1])),
                               str[2]                                                                ,
                               Boolean.parseBoolean(str[3])                                          ,
                               str[4]                                                                ,
                               _belong                                                               );
        }catch(Exception e){
            debugLog.getInstance().write_exception(e, new Throwable());
        }
        return m_obj;
    }

    /* マップチップのrectを取得
     * 引数  ：取得するマップチップの番号
     * 戻り値：マップチップ番号に対応した位置情報を持つrect
     */
    private rect get_map_chip(int x, int y){
        return get_map_chip(x, y, 1.0f);
    }

    private rect get_map_chip(int x, int y, float _scale){
        return new rect(new point<Double >((double)size_block.x * (double)_scale  * x  , (double)size_block.y * (double)_scale * y),
                        new point<Integer>((int)((double)size_block.x * (double)_scale), (int)((double)size_block.y * (double)_scale)));
    }

    /*
     * 変数セット
     * 引数  ：それぞれのデータ
     * 戻り値：なし
     */
    private void init(point<Integer> _size_block, String _csv_iscollisionable, boolean _use_img,
                      String         _img_or_spritesheet, Stage _belong){
        try{
            size_block = new point<Integer>(_size_block);
            csv_to_mapchips(_csv_iscollisionable);
            use_img = _use_img;
            if(_img_or_spritesheet == ""){
                img    = null;
                ssheet = null;
            }
            else
            {
                img        = (_use_img)? new Image(_img_or_spritesheet) : null   ;
                ssheet     = (_use_img)? null                           : new SpriteSheet(_img_or_spritesheet, size_block.x, size_block.y);
            }

            belong = _belong;
        }catch(Exception e){
            debugLog.getInstance().write_exception(e, new Throwable());
        }

        return;
    }


    /*
     * CSVファイルからマップチップ + 接触判定の有無
     *   ・CSVでの値 / 2 = マップチップの番号
     *   ・CSVでの値が{奇数 = 接触判定有，偶数 = 接触判定無}
     * 引数  ：CSVファイルへのパス
     * 戻り値：なし
     */
    private void csv_to_mapchips(String _file_path){
        try{
            //インスタンス，変数宣言
            BufferedReader bRead = new BufferedReader(new FileReader(_file_path));
            int                 iBuf;
            String              line;
            ArrayList<String[]> elm  = new ArrayList<String[]>();

            //ファイル内容を要素ごとに分割
            while((line = bRead.readLine()) != null)
                elm.add(line.split(","));
            bRead.close();

            //配列長定義
            is_collisionable = new boolean[elm.size()][elm.get(0).length];
            use_chip         = new int    [elm.size()][elm.get(0).length];

            //配列に要素挿入
            for(int i = 0; i < elm.size(); i++){
                for(int j = 0; j < elm.get(i).length; j++){
                    iBuf = Integer.parseInt(elm.get(i)[j], 10);
                    is_collisionable[i][j] = ((iBuf % 2 == 1)? true : false);
                    use_chip        [i][j] =   iBuf / 2;
                }
            }
        }catch(Exception e){
            debugLog.getInstance().write_exception(e, new Throwable());
            return;
        }
    }

}
