package FileIO;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.newdawn.slick.Input;

/* 設定ファイルクラス
 * 静的変数のみを使用します
 */
public class config {
	/* メンバ変数 */
	//ファイル関係
	private static String     fName  = "config.cfg";

	//ボタン関係
	public  static final int useBtn = 8;	//使用するボタン数
	public  static       int attack;
	public  static       int jump;
	public  static       int left;
	public  static       int down;
	public  static       int up;
	public  static       int right;
	public  static       int ok;
	public  static       int cancel;

	/* メソッド */
	/* 初期化
	 * 引数：なし
	 */
	static{
		try{
			//ファイル読み込み, ボタン割り当て
			readBtn();

		}catch(IOException e){
			//ファイル読み込みに失敗したら、コンフィグファイルを作成
			try{

				setBtn(Input.KEY_Z, Input.KEY_X, Input.KEY_LEFT, Input.KEY_DOWN, Input.KEY_UP, Input.KEY_RIGHT, Input.KEY_Z, Input.KEY_X);
				writeBtn();

			}catch(Exception e1){
				//未実装
				System.exit(1);
			}
		}
	}

	/* 使用ボタンセット
	 * 引数：それぞれのデータ
	 */
	public  static void setBtn(int _attack, int _jump  ,
	                           int _left  , int _down  , int _up, int _right,
	                           int _ok    , int _cancel){

		attack = _attack;
		jump   = _jump;
		left   = _left;
		down   = _down;
		up     = _up;
		right  = _right;
		ok     = _ok;
		cancel = _cancel;
	}

	/* 使用ボタンファイル書き込み
	 * 引数：なし
	 */
	public  static void writeBtn() throws IOException{
		BufferedWriter bWrite = new BufferedWriter(new FileWriter(fName));
		String         sWrite = String.format("%8X", attack) +
				                String.format("%8X", jump  ) +
				                String.format("%8X", left  ) +
				                String.format("%8X", down  ) +
				                String.format("%8X", up    ) +
				                String.format("%8X", right ) +
				                String.format("%8X", ok    ) +
				                String.format("%8X", cancel) ;

		bWrite.write(sWrite);
		bWrite.close();

		return;
	}

	/* 使用ボタンファイル読み込み
	 * 引数：なし
	 */
	private static void readBtn() throws IOException{
		BufferedReader bRead = new BufferedReader(new FileReader(fName));
		String         fst   = bRead.readLine();
		String[]       sBuf  = new String[useBtn];

		bRead.close();

		for(int i = 0; i < sBuf.length; i++)
			sBuf[i] = fst.substring(i * 8, (i + 1) * 8);


		//データ挿入
		attack = Integer.parseInt(sBuf[0], 16);
		jump   = Integer.parseInt(sBuf[1], 16);
		left   = Integer.parseInt(sBuf[2], 16);
		down   = Integer.parseInt(sBuf[3], 16);
		up     = Integer.parseInt(sBuf[4], 16);
		right  = Integer.parseInt(sBuf[5], 16);
		ok     = Integer.parseInt(sBuf[6], 16);
		cancel = Integer.parseInt(sBuf[7], 16);

		return;
	}


}
