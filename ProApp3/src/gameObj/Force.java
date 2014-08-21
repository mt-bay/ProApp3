package gameObj;


/*
 * オブジェクトの勢力を表す列挙体
 */
public enum Force {
    /* 列挙定数 */
    FRIEND , //プレイヤおよび味方
    ENEMY  , //敵
    NEUTRAL; //中立


    /* メソッド */
    /* 文字列 to force
     * "friend"  -> プレイヤおよび味方,
     * "enemy"   -> 敵
     * "neutral" -> 中立
     * 引数  ：基となる文字列
     * 戻り値：文字列を基にした列挙体
     */
    public static Force parceForce(String s){
        switch(s.toLowerCase()){
            case "friend"  :
                return FRIEND;
            case "enemy"   :
                return ENEMY;
            case "neutral" :
                return NEUTRAL;

            default :
                return NEUTRAL;
        }
    }

    /*
     * 引数の勢力が攻撃対象になるかどうか
     * 引数  ：調査する勢力
     * 戻り値：攻撃対象になるか
     */
    public boolean is_attackable(Force _target){
        if     (this == FRIEND){
            switch(_target){
                case ENEMY:
                    return true;

                case FRIEND:
                    return false;

                case NEUTRAL:
                    return true;

                default:
                    return true;
            }
        }
        else if(this == ENEMY){
            switch(_target){
                case ENEMY:
                    return false;

                case FRIEND:
                    return true;

                case NEUTRAL:
                    return true;

                default:
                    return true;

            }
        }
        else if(this == NEUTRAL){
            switch(_target){
                case ENEMY:
                    return true;

                case FRIEND:
                    return true;

                case NEUTRAL:
                    return true;

                default:
                    return true;

            }
        }

        return false;
    }


}
