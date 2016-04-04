package sample;

import javax.sound.sampled.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abedaigorou on 16/01/22.
 */
public class recorder
{
    public static final int bit=16;
    public static final int hz=8000;
    public static final int STEREO=1;

    private TargetDataLine target;
    private AudioInputStream stream;
    private byte[] voice=new byte[hz*bit/8*STEREO];
    private FileOutputStream output;

    private boolean isRunning=true;
    recorder()
    {
        try {
            //オーディオフォーマットの指定
            AudioFormat af = new AudioFormat(hz,bit,STEREO,true,false);
            //ターゲットデータラインを取得
            DataLine.Info info =new DataLine.Info(TargetDataLine.class,af);
            target=(TargetDataLine)AudioSystem.getLine(info);
            //ターゲットデータラインを開く
            target.open(af);

            //音声入力スタート
            target.start();

            //入力ストリームを取得
            stream=new AudioInputStream(target);


        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        try{
            output=new FileOutputStream("output.dat");
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run()
            {
                while(true) {
                    if(!isRunning)
                        return;
                    try {
                        stream.read(voice, 0, voice.length);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    public Short[] getVoice()
    {
        List<Short> dataList=new ArrayList<Short>();
        for(int i=0;i<voice.length-2;i+=2){
            dataList.add((short)readBytes(2,i,voice));
        }
        try {
            output.write(voice);
        }catch(IOException e)
        {
            e.printStackTrace();
        }

        return dataList.toArray(new Short[0]);
    }

    public byte[] getBVoice()
    {
        return voice;
    }

    public void stop()
    {
        isRunning=false;
        target.stop();
        target.close();
    }

    private int readBytes(int byteNum,int indexNum,byte[] data)//リトルエンディアンでindexNum+1番目からbyteNumバイト読み込む
    {
        indexNum--;
        indexNum+=byteNum;
        int bytes=0;
        int j=0;
        for (int i = 2*(byteNum-1); i >= 0; i -= 2)
            bytes += ((data[indexNum + j--])* Math.pow(16, i));

        return (byteNum<3)?((short)bytes):(bytes);
    }


}
