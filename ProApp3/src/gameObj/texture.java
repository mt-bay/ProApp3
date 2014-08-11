package gameObj;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import window.window;

import common.point;
import common.rect;

/* テクスチャ用クラス
 * SpriteSheet管理用
 */
public class texture {
    private SpriteSheet    texture;

    private String         path;
    public  point<Integer> size;
    public  int            use_number;

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
    public void draw(point<Float> _location){
        draw(_location, 1.0f);
    }
    public void draw(point<Float> _location, float _scale){
        point<Float> l_relate = window.relative_camera_f(_location);
        if(window.comprise(new rect(l_relate.FtoD(), size))){
            texture.startUse();
            texture.getSubImage(0, use_number).draw(l_relate.x, l_relate.y);
            texture.endUse();
        }

        return;
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
            texture = new SpriteSheet(path, size.x, size.y);
        }catch(SlickException e){
            texture = null;
        }
    }
}
