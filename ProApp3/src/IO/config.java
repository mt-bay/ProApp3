package IO;


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

	//キーアサイン関係
	public  static final int maxKey = 10;	//使用するボタン数(最大)
	public  static       int attack;
	public  static       int jump;
	public  static       int highsp;
	public  static       int change;
	public  static       int left;
	public  static       int down;
	public  static       int up;
	public  static       int right;
	public  static       int ok;
	public  static       int cancel;

	//難易度関係


	/* メソッド */
	/* 初期化
	 */
	static{
		try{
			//ファイル読み込み, ボタン割り当て
			read();

		}catch(IOException e){
			//ファイル読み込みに失敗したら、コンフィグファイルを作成
			try{

				set(Input.KEY_Z   , Input.KEY_X   , Input.KEY_C , Input.KEY_V    ,
				    Input.KEY_LEFT, Input.KEY_DOWN, Input.KEY_UP, Input.KEY_RIGHT,
				    Input.KEY_Z   , Input.KEY_X   );
				write();

			}catch(Exception e1){
				//未実装
				System.exit(1);
			}
		}
	}

	/* 使用ボタンセット
	 * 引数：それぞれのデータ
	 */
	public  static void set(int _attack, int _jump  , int _highsp, int _change,
	                        int _left  , int _down  , int _up    , int _right ,
	                        int _ok    , int _cancel){

		attack = _attack;
		jump   = _jump;
		highsp = _highsp;
		change = _change;
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
	public  static void write() throws IOException{
		BufferedWriter bWrite = new BufferedWriter(new FileWriter(fName));
		//ファイルに書き込む内容
		String         sWrite = String.format("%08X", attack) +
		                        String.format("%08X", jump  ) +
		                        String.format("%08X", highsp) +
		                        String.format("%08X", change) +
		                        String.format("%08X", left  ) +
		                        String.format("%08X", down  ) +
		                        String.format("%08X", up    ) +
		                        String.format("%08X", right ) +
		                        String.format("%08X", ok    ) +
		                        String.format("%08X", cancel) ;

		bWrite.write(sWrite);
		bWrite.close();

		return;
	}

	/* 使用ボタンファイル読み込み
	 * 引数：なし
	 */
	private static void read() throws IOException{
		//ファイル入力
		BufferedReader bRead = new BufferedReader(new FileReader(fName));
		String         fst   = bRead.readLine();

		bRead.close();

		//データ用に文字列分割
		String[]       sBuf  = new String[maxKey];
		for(int i = 0; i < sBuf.length; i++)
			sBuf[i] = fst.substring(i * 8, (i + 1) * 8);

		//データ挿入
		//求：もっといい感じの代入方法
		set(Integer.parseInt(sBuf[0], 16), Integer.parseInt(sBuf[1], 16), Integer.parseInt(sBuf[2], 16),
		    Integer.parseInt(sBuf[3], 16), Integer.parseInt(sBuf[4], 16), Integer.parseInt(sBuf[5], 16),
		    Integer.parseInt(sBuf[6], 16), Integer.parseInt(sBuf[7], 16), Integer.parseInt(sBuf[8], 16),
		    Integer.parseInt(sBuf[9], 16));


		return;
	}


}
