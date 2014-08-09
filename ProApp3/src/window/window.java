package window;

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
    public static       point<Float>  camera_location = new point<Float>(0.0f, 0.0f);

    /* メソッド */
    /* カメラからの相対座標の導出
     * 引数  ：元データ
     * 戻り値：カメラからの相対座標
     */
    public static point<Double > relative_camera_d(point<Double > _obj){
        return new point<Double>(_obj.x - camera_location.x.doubleValue(),
                                 _obj.y - camera_location.y.doubleValue());
    }
    public static point<Float  > relative_camera_f(point<Float  > _obj){
        return new point<Float>(_obj.x - camera_location.x,
                                _obj.y - camera_location.y);
    }
    public static point<Integer> relative_camera_i(point<Integer> _obj){
        return new point<Integer>(_obj.x - camera_location.x.intValue(),
                                  _obj.y - camera_location.y.intValue());
    }

    /* カメラからの相対座標を持つrectの導出
     * 引数  ：元データ
     * 戻り値：カメラからの相対座標データを持つrect
     */
    public static rect relative_camera_rect(rect _obj){
        return new rect(relative_camera_d (_obj.location),
                        new point<Integer>(_obj.size    ));
    }

    /* ウィンドウの表示範囲に引数のRectが含まれるかどうか
     * 引数  ：対象の長方形
     * 戻り値：引数の長方形が含まれるかどうか
     */
    public static boolean comprise(rect _obj){
        //null対策
        if(_obj == null)
            return false;

        rect o = relative_camera_rect(_obj);
        //x軸での判定
        if(o.LowerRight().x <                      0.0d ||
           o.UpperLeft() .x > (double)camera_location.x)
            return false;
        //y軸での判定
        if(o.LowerRight().y <                      0.0d ||
           o.UpperLeft() .y > (double)camera_location.y)
            return false;

        return true;
    }




}
