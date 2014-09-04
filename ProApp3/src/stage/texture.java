package stage;

import java.io.File;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

import window.window;
import IO.debugLog;

import common.point;
import common.rect;

/* テクスチャ用クラス
 * SpriteSheet管理用
 */
public class texture {
    private SpriteSheet    texture_m;

    private String         path;
    public  point<Integer> size;
    public  int            use_number;
    public  Direction      direction_m;

    /* コンストラクタ */
/*    private texture(){
        init("", new point<Integer>(0, 0));
    }*/
    /* コピーコンストラクタ
     * 引数  ：元データ
     */
    public texture(texture _obj){
         init(_obj.path, _obj.size);
    }
    /* ファイルパスを受け取ってテクスチャ読み込みするコンストラクタ
     * 引数  ：テクスチャへのファイルパス, サイズ
     */
    public texture(String _path, point<Integer> _size){
        init(_path, _size);
    }

    /* メソッド */
    /* 描画
     * 引数：テクスチャの現在位置 (, 描画倍率)
     */
    public void draw(Graphics g, point<Float> _location, Stage _camera){
        draw(g, _location, _camera, 1.0f);
    }
    public void draw(Graphics g,point<Float> _location, Stage _camera, float _scale){
        if(texture_m == null)
            return;

        if(!window.comprise(new rect(_location.FtoD(), size), _camera)){
            return;
        }

        point<Float> l_relate = _camera.relative_camera_f(_location);
        g.drawImage(texture_m.getSubImage(use_number, 0), l_relate.x, l_relate.y);

        return;
    }


    /* ファイルパスgetter
     * 引数  ：なし
     * 戻り値：ファイルパス
     */
    public String get_file_path(){
        return path;
    }

    /* 初期化
     * 引数  ：必要なデータ
     * 戻り値：なし
     */
    private void init(String _path, point<Integer> _size){
        path       = _path;
        size       = _size;
        use_number = 0;
        try{

            texture_m = (new File(path).exists())?
                    new SpriteSheet(new Image(_path), size.x, size.y) :
                    null                                              ;
        }catch(Exception e){
            e.printStackTrace();
            debugLog.getInstance().write_exception(e);
            debugLog.getInstance().write("    filename : " + _path);
            texture_m = null;
        }
    }
}
