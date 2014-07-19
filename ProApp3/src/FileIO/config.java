package FileIO;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

import org.newdawn.slick.Input;

/* 設定ファイルクラス
 * 静的変数のみを使用します
 */
public class config {
	/* メンバ変数 */
	//ファイル関係
	private static FileWriter fWrite;
	private static String     fName;
	
	//ボタン関係
	public  static int       attack;
	public  static int       jump;
	public  static int       left;
	public  static int       down;
	public  static int       up;
	public  static int       right;
	public  static int       ok;
	public  static int       cancel;
	/* コンストラクタ */
	
	/* デフォルトコンストラクタ
	 * fileからデータを読み込む(存在しない場合はfileにあたらしくデータを追加)
	 */
	public config(){
	}
	
	
	/* メソッド */
	/* 初期化
	 * 引数：なし 
	 */
	public void init(){
		try{
			//ファイル読み込み
			BufferedReader fRead = new BufferedReader(new FileReader(fName));
			
			
		}catch(FileNotFoundException e){
			//ファイル読み込みに失敗したら、コンフィグファイルを作成
			try{
				
				FileWriter fWrite = new FileWriter(fName); 
				buttonSet(Input.KEY_Z, Input.KEY_X, Input.KEY_LEFT, Input.KEY_DOWN, Input.KEY_UP, Input.KEY_RIGHT, Input.KEY_Z, Input.KEY_X);
				
			}catch(Exception e1){
				//未実装
				System.exit(1);
			}
		}
	}
	
	/* 使用ボタンセット
	 * 
	 */
	public void buttonSet(int _attack, int _jump,
			              int _left, int _down, int _up, int _right,
			              int _ok, int _cancel){
		
		attack = _attack;
		jump   = _jump;
		left   = _left;
		right  = _right;
		ok     = _ok;
		cancel = _cancel;
	}
}
