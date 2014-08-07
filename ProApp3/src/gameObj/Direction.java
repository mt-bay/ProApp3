package gameObj;


/* 方向列挙体 */
public enum Direction{
    /* 列挙定数 */
    LEFT,  //左
    RIGHT; //右

    /* メソッド */
    /* 文字列 to Direction
     * "left" -> 左, "right" -> 右
     * 引数  ：基となる文字列
     * 戻り値：文字列を基にした列挙体
     */
    public static Direction parseDirection(String s){
        switch(s.toLowerCase()){
            case "left"  :
                return LEFT;
            case "right" :
                return RIGHT;
            default      :
                return LEFT;
        }
    }
}