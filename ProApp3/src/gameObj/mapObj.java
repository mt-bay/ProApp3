package gameObj;

import java.io.BufferedReader;
import java.io.FileReader;
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
    public point<Integer> size_block;        //ブロックサイズ
    public boolean[][]    is_collisionable;  //接触判定を持つか(ブロックごとに管理)

    //描画関係
    //public double         z;      //仮想Z軸(現在使用予定なし)
    private Image         img;               //マップ用1枚絵
    private SpriteSheet   ssheet;            //マップチップ
    private int[][]       use_chip;          //マップチップのどの要素を使うか

    //デバッグ用データ
    private static final debugLog     dLog = debugLog.getInstance();

    /* コンストラクタ */
    /*
     * データ指定形コンストラクタ(汎用)
     * 引数  ：なし
     */
    public mapObj(point<Integer> _size_block, String      _csv_iscollisionable, boolean _use_img,
                  String         _img_or_spritesheet){
        set(_size_block, _csv_iscollisionable,  _use_img, _img_or_spritesheet);

    }

    /* メソッド */



    /* ファイル to マップオブジェクト
     * ファイルは、1行ごとに区切られている前提で
     * ブロックサイズ_x ブロックサイズ_y  csvファイルへのパス 一枚絵を使うか(falseでスプライトシート) 画像 or スプライトシートへのパス
     * <int>            <int>             <string>            <boolean>                               <string>
     */
    public static mapObj file_to_mapObj(String _file_path){
        mapObj m_obj = null;
        try{
            BufferedReader bRead = new BufferedReader(new FileReader(_file_path));
            String[] str = bRead.readLine().split(" ");
            bRead.close();

            m_obj = new mapObj(new point<Integer>(Integer.parseInt(str[0]), Integer.parseInt(str[1])),
                               str[2]                                                                ,
                               Boolean.parseBoolean(str[3])                                          ,
                               str[4]                                                                );
        }catch(Exception e){
            dLog.write_exception(e                                    ,
                    new Throwable().getStackTrace()[0].getClassName() ,
                    new Throwable().getStackTrace()[0].getMethodName());
        }

        return m_obj;
    }

    /*
     * 変数セット
     * 引数  ：それぞれのデータ
     * 戻り値：なし
     */
    private void set(point<Integer> _size_block, String _csv_iscollisionable, boolean _use_img,
                     String         _img_or_spritesheet){
        try{
            size_block = new point<Integer>(_size_block);
            if(_img_or_spritesheet == ""){
                img    = null;
                ssheet = null;
            }
            else
            {
                img        = (_use_img)? new Image(_img_or_spritesheet) : null   ;
                ssheet     = (_use_img)? null : new SpriteSheet(_img_or_spritesheet, size_block.x, size_block.y);
            }
            csv_to_mapchips(_csv_iscollisionable);
        }catch(Exception e){
            dLog.write_exception(e                                                 ,
                                 new Throwable().getStackTrace()[0].getClassName() ,
                                 new Throwable().getStackTrace()[0].getMethodName());
        }


        return;
    }

    /*
     * CSV to マップチップ + 接触判定の有無
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
            dLog.write_exception(e, new Throwable().getStackTrace()[0].getClassName(), new Throwable().getStackTrace()[0].getMethodName());
            return;
        }
    }
}
