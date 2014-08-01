package gameObj;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

import IO.debugLog;
import common.point;

/*
 * マップオブジェクト
 * 描画はマップチップ or 1枚イメージ
 */
public class mapObj {
    /* メンバ変数 */
    public point<Integer> size_block;
    public boolean[][]    is_collisionable;
    //public double         z;      //仮想Z軸(現在使用予定なし)
    public point<Integer> size_map; //マップサイズ
    private Image         img;      //マップ用1枚絵
    private SpriteSheet   ssheet;   //マップチップ
    private int[][]       use_chip; //マップチップのどの要素を使うか

    //デバッグ用データ
    private debugLog     dLog;

    /* コンストラクタ */
    /*
     * デフォルトコンストラクタ
     * 引数  ：なし
     */
    public mapObj(){
        set(new point<Integer>(), "", new point<Integer>(), null, null, false);
    }

    /*
     * データ指定形コンストラクタ(Image使用)
     * 引数  ：なし
     */
    public mapObj(point<Integer> _size_block, String _csv_iscollisionable, point<Integer> _size_map,
                  Image          _img){
        set(_size_block, _csv_iscollisionable, _size_map, _img, null, true);
    }
    /*
     * データ指定形コンストラクタ(SpriteSheet使用)
     * 引数  ：なし
     */
    public mapObj(point<Integer> _size_block, String       _csv_iscollisionable, point<Integer> _size_map,
                  SpriteSheet  _ssheet){
        set(_size_block, _csv_iscollisionable, _size_map, null, _ssheet, false);

    }


    /* メソッド */
    /*
     * 変数セット
     * 引数：それぞれのデータ
     */
    private void set(point<Integer> _size_block, String       _csv_iscollisionable, point<Integer> _size_map,
                     Image          _img       , SpriteSheet  _ssheet             , boolean        _use_img){
        size_block = _size_block;
        size_map   = _size_map;
        img        = (_use_img)? _img : null   ;
        ssheet     = (_use_img)? null : _ssheet;
        CSVtoMchips(_csv_iscollisionable);

        dLog       = debugLog.getInstance();
        return;
    }

    private void CSVtoMchips(String filename){
        try{
            //インスタンス，変数宣言
            BufferedReader bRead = new BufferedReader(new FileReader(filename));
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
            dLog.write_exception(e, new Throwable().getStackTrace()[0].getClassName(), new Throwable().getStackTrace()[0].getMethodName());
            return;
        }
    }
}
