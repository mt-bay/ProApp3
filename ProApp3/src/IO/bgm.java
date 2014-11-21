package IO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.newdawn.slick.Music;

import window.window;

import common.volume;

public class bgm {
    /* メンバ変数 */
    //自インスタンス
    private static bgm        instance;
    //
    private ArrayList<Music>  bgm_m;
    private ArrayList<String> name;
    public  volume            vol;

    /* コンストラクタ */
    private bgm(){
        vol = new volume(0);
        String script_path = "stage\\BGM\\bgm_list.txt";
        init(script_path);
    }

    /* メソッド */
    /*
     * インスタンス取得
     */
    public static synchronized bgm getInstance(){
        if(instance == null){
            if(instance == null){
                instance = new bgm();
            }
        }
        return instance;
    }

    /*
     * index番の内容を再生
     * 引数  ：index
     * 戻り値：なし
     */
    public void play(int index){
        if(index < 0 || index >= bgm_m.size())
            return;

        if(bgm_m.get(index) == null)
            return;

        bgm_m.get(index).loop();

        return;
    }

    /*
     * index番の内容を停止
     * 引数  ：index
     * 戻り値：なし
     */
    public void stop(int index){
        if(index < 0 || index >= bgm_m.size())
            return;

        if(bgm_m.get(index) == null)
            return;

        if(bgm_m.get(index).playing())
            bgm_m.get(index).stop();

        return;
    }

    /*
     * 初期化
     * BGMリストは、1行ごとにファイルパスが記述されているものとする
     * 引数  ：BGMリストが記述されているファイル名
     *
     */
    private void init(String _file_path){
        bgm_m = new ArrayList<Music>();
        name  = new ArrayList<String>();

        try{
            String         script_path = (Paths.get(window.file_path_corres(_file_path)).getParent() == null)?
                                             "" : Paths.get(window.file_path_corres(_file_path)).getParent().toString() + "\\";

            BufferedReader bRead       = new BufferedReader(new FileReader(window.file_path_corres(_file_path)));
            String         str;

            while((str = bRead.readLine()) != null){
                str = window.file_path_corres(script_path + str);


                bgm_m.add(new Music(str));
                name.add(window.get_file_name_prefix(str));
            }

            bRead.close();
        }
        catch(Exception e){
            debugLog.getInstance().write_exception(e);
            e.printStackTrace();
        }
    }



}
