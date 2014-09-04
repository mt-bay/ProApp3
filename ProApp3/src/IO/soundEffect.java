package IO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.newdawn.slick.Sound;

import window.window;

import common.volume;

public class soundEffect {
    /* メンバ変数 */
    //自インスタンス
    private static soundEffect instance;
    //
    private ArrayList<Sound>  se_m;
    private ArrayList<String> name;
    public  volume            vol;

    /* コンストラクタ */
    private soundEffect(){
        vol = new volume(0);
        String script_path = "stage\\sound_effect\\se_list.txt";
        init(script_path);
    }

    /* メソッド */
    /*
     * インスタンス取得
     */
    public static synchronized soundEffect getInstance(){
        if(instance == null){
            if(instance == null){
                instance = new soundEffect();
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
        if(index < 0 || index >= se_m.size())
            return;

        if(se_m.get(index) == null)
            return;

        se_m.get(index).loop();

        return;
    }

    /*
     * index番の内容を停止
     * 引数  ：index
     * 戻り値：なし
     */
    public void stop(int index){
        if(index < 0 || index >= se_m.size())
            return;

        if(se_m.get(index) == null)
            return;

        if(se_m.get(index).playing())
            se_m.get(index).stop();

        return;
    }

    /*
     * 初期化
     * BGMリストは、1行ごとにファイルパスが記述されているものとする
     * 引数  ：BGMリストが記述されているファイル名
     *
     */
    private void init(String _file_path){
        se_m = new ArrayList<Sound>();
        name = new ArrayList<String>();

        try{
            String         script_path = (Paths.get(_file_path).getParent() == null)?
                                             "" : Paths.get(_file_path).getParent().toString() + "\\";

            BufferedReader bRead       = new BufferedReader(new FileReader(_file_path));
            String         str;

            Sound          snd_buf;


            while((str = bRead.readLine()) != null){
                str = window.file_path_corres(script_path + str);
                try{
                    snd_buf = new Sound(str);
                }
                catch(Exception e){
                    e.printStackTrace();
                    snd_buf = null;
                }

                if(snd_buf != null){
                    se_m.add(snd_buf);
                    name.add(window.get_file_name_prefix(str));
                }

            }

            bRead.close();
        }
        catch(Exception e){
            debugLog.getInstance().write_exception(e);
            e.printStackTrace();
        }
    }
}
