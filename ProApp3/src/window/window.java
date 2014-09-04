package window;

import java.io.File;

import stage.Stage;

import common.point;
import common.rect;

/* ウィンドウ関係の処理や定数を放り込んでおくクラス
 * スタティッククラス
 */
public class window {
    /* 定数 */
    public static final point<Integer> SIZE           = new point<Integer>(800, 600);
    public static final int            FPS            = 30;

    /* メンバ変数 */

    /* ウィンドウの表示範囲に引数のRectが含まれるかどうか
     * 引数  ：対象の長方形, 表示中のステージ
     * 戻り値：引数の長方形が含まれるかどうか
     */
    public static boolean comprise(rect _obj, Stage _camera){
        //null対策
        if(_obj == null)
            return false;

        rect o = _camera.relative_camera_rect(_obj);
        //x軸での判定
        if(o.LowerRight().x <                       0.0d ||
           o.UpperLeft() .x > window.SIZE.x.doubleValue())
            return false;
        //y軸での判定
        if(o.LowerRight().y <                       0.0d ||
           o.UpperLeft() .y > window.SIZE.y.doubleValue())
            return false;

        return true;
    }

    /*
     * ファイルパスを各OSの形式に合った形に変更する
     * 引数  ：元のファイルパス
     * 戻り値：各OSの形式に変換したファイルパス
     */
    public static String file_path_corres(String _file_path){
        String return_str = "";
        for(int i = 0; i < _file_path.length(); i++){
            if((_file_path.charAt(i) == '/' ) ||
               (_file_path.charAt(i) == '\\')){

                return_str += File.separator;
            }
            else{
                return_str += _file_path.charAt(i);
            }
        }
        return return_str;
    }

    /*
     * ファイルパスから、拡張子を除いたファイル名を取得する
     * 引数  ：ファイルパス
     * 戻り値：ファイル名
     */
    public static String get_file_name_prefix(String _file_path){
        File   f         = new File(_file_path);
        String fname     = f.getName();
        int    ext_point = fname.lastIndexOf(".");

        if(ext_point != -1)
            fname = fname.substring(0, ext_point);

        return fname;
    }

}
