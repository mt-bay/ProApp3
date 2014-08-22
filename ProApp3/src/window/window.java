package window;

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
        if(o.LowerRight().x <                      0.0d ||
           o.UpperLeft() .x > (double)_camera.camera_location.x)
            return false;
        //y軸での判定
        if(o.LowerRight().y <                      0.0d ||
           o.UpperLeft() .y > (double)_camera.camera_location.y)
            return false;

        return true;
    }




}
