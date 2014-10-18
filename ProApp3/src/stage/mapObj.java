package stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

import window.Main;
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
    public               boolean[][]   is_collisionable; //接触判定を持つか(ブロックごとに管理)
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
    public void draw(Graphics g){
        if((use_img == false && ssheet == null) ||
           (use_img == true  && img    == null))
            return;

        //1枚絵を使用する際
        point<Float> loc_f;
        if(use_img){
            loc_f = belong.relative_camera_f(new point<Float>(0.0f, 0.0f));
            g.drawImage(img, loc_f.x, loc_f.y);
            return;
        }

        //マップチップを使用する際
        rect chip_loc;
        for(int i = 0; i < use_chip.length; i++){
            for(int j = 0; j < use_chip[i].length; j++){

                chip_loc = get_map_chip(j, i);

                if(window.comprise(chip_loc, belong)){
                    loc_f = belong.relative_camera_f(chip_loc.location.DtoF());


                    g.drawImage(ssheet.getSubImage(use_chip[i][j], 0).getScaledCopy(window.SCALE), loc_f.x * window.SCALE, loc_f.y * window.SCALE);

                    if(Main._DEBUG){
                        g.setColor(new Color((is_collisionable[i][j])? 0x000000 : 0xffffff));
                        g.drawRect(loc_f.x * window.SCALE, loc_f.y * window.SCALE, size_block.x * window.SCALE, size_block.y * window.SCALE);
                    }

                    ssheet.getSubImage(use_chip[i][j], 0);
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
    public boolean is_collision(rect _obj){
    	point<Integer> chip_number = new point<Integer>();
        rect r;
        
        for(double d = _obj.UpperLeft().x; d <= _obj.UpperRight().x; d += size_block.x){
        	chip_number.x = (int)(d) / size_block.x;
        	for(double e = _obj.UpperLeft().y; e <= _obj.LowerLeft().y; e += size_block.y){
        		chip_number.y = (int)(e) / size_block.y;
        		r = get_map_chip(chip_number.x, chip_number.y);
        		if(is_collisionable[chip_number.y][chip_number.x] &&
        		   r.is_collision(_obj)                            ){
        			return true;
        		}
        	}
        }
        
        chip_number.x = _obj.UpperRight().x.intValue() / size_block.x;
        chip_number.y = _obj.UpperRight().y.intValue() / size_block.y;
        r = get_map_chip(chip_number.x, chip_number.y);
        if(is_collisionable[chip_number.y][chip_number.x] &&
        	r.is_collision(_obj)                          ){
        	return true;
        }
        
        chip_number.x = _obj.LowerLeft().x.intValue() / size_block.x;
        chip_number.y = _obj.LowerLeft().y.intValue() / size_block.y;
        r = get_map_chip(chip_number.x, chip_number.y);
        if(is_collisionable[chip_number.y][chip_number.x] &&
        	r.is_collision(_obj)                          ){
        	return true;
        }
        
        chip_number.x = _obj.LowerRight().x.intValue() / size_block.x;
        chip_number.y = _obj.LowerRight().y.intValue() / size_block.y;
        r = get_map_chip(chip_number.x, chip_number.y);
        if(is_collisionable[chip_number.y][chip_number.x] &&
        	r.is_collision(_obj)                          ){
        	return true;
        }

        return false;
    }

    /*
     * 対象の四角形がマップ内に内包されているか
     * 引数  ：対象の四角形
     * 戻り値：内包されているか
     */
    public boolean is_field_conntraction(rect _obj){
        point<Integer> f_s = get_field_size();
        if(_obj.UpperLeft(). x > 0.0d                &&
           _obj.UpperLeft(). y > 0.0d                &&
           _obj.LowerRight().x < f_s.x.doubleValue() &&
           _obj.LowerRight().y < f_s.x.doubleValue() )
            return true;
        else
            return false;
    }

    /*
     * マップ全体のサイズを返す
     * 引数  ：なし
     * 戻り値：マップ全体のサイズ
     */
    public point<Integer> get_field_size(){
        return new point<Integer>(size_block.x * is_collisionable[0].length,
                                  size_block.y * is_collisionable.length);
    }

    /* ファイル to マップオブジェクト
     * ファイルは、1行ごとに区切られている前提で、以下の内容で動作する
     * ブロックサイズ_x ブロックサイズ_y  csvファイルへのパス 一枚絵を使うか(falseならばブロックを描画) 画像 or スプライトシートへのパス
     * <int>            <int>             <string>            <boolean>                                 <string>
     */
    public static mapObj file_to_mapObj(String _file_path, Stage _belong){
        mapObj m_obj       = null;
        String script_path = ((Paths.get(window.file_path_corres(_file_path)).getParent() == null)?
                                  "" : Paths.get(window.file_path_corres(_file_path)).getParent().toString() + "\\");

        try{
            BufferedReader bRead = new BufferedReader(new FileReader(window.file_path_corres(_file_path)));
            String[] str = bRead.readLine().split(" ");
            bRead.close();

            m_obj = new mapObj(new point<Integer>(Integer.parseInt(str[0]), Integer.parseInt(str[1])),
                               window.file_path_corres(script_path + str[2])                         ,
                               Boolean.parseBoolean(str[3])                                          ,
                               window.file_path_corres(script_path + str[4])                         ,
                               _belong                                                               );
        }catch(Exception e){
            debugLog.getInstance().write_exception(e);
            debugLog.getInstance().write("    filename : " + _file_path);
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
                ssheet     = (_use_img)? null                           : new SpriteSheet(new Image(_img_or_spritesheet), size_block.x, size_block.y);
            }

            belong = _belong;
        }catch(Exception e){
            debugLog.getInstance().write_exception(e);
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
            debugLog.getInstance().write_exception(e);
            return;
        }
    }

}
