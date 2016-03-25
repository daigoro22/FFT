package sample;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abedaigorou on 16/03/08.
 */
public class WAV
{
    private InputStream wavIn;
    private List<Integer> datalList=new ArrayList<Integer>();
    private List<Byte> byteList=new ArrayList<Byte>();
    private List<Short> soundList=new ArrayList<Short>();
    private Integer[] data;
    private Byte[] byteData;
    private Short[] soundData;

    public WAV(String fileName)
    {
        try {
            wavIn = new BufferedInputStream(new FileInputStream(fileName));
        }catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public Integer[] readData()
    {
        try {
            int d;
            while((d=wavIn.read())!=-1)
                datalList.add(d);

        }catch(IOException e)
        {
            e.printStackTrace();
        }
        data= datalList.toArray(new Integer[0]);
        return data;
    }

    public Byte[] readByteData()
    {
        try {
            for(int d=wavIn.read();d!=-1;d=wavIn.read())
                byteList.add((byte) d);

        }catch(IOException e)
        {
            e.printStackTrace();
        }
        byteData= byteList.toArray(new Byte[0]);

        return byteData;
    }

    public void readHeader()//RIFF,WAVE,fmt のヘッダを読み込む
    {
        do {
            int c;
            if ((c = checkHeader("RIFF")) != -1) {//RIFFヘッダ確認
                dataInf.fileSize=readBytes(4, c)+8;
                System.out.println("ファイルサイズ:"+ dataInf.fileSize+"byte");
            } else
                break;

            if(checkHeader("WAVE")!=-1)
                System.out.println("WAVEヘッダ確認");
            else
                break;

            if((c=checkHeader("fmt "))!=-1) {
                System.out.println("fmt ヘッダ確認");
                dataInf.fmtBytes = readBytes(4, c);
                System.out.println("fmtチャンクサイズ:"+ dataInf.fmtBytes+"byte");

                dataInf.fmtID=readBytes(2, c += 4);
                System.out.println("フォーマットID:"+ dataInf.fmtID);

                dataInf.ch=readBytes(2, c += 2);
                System.out.println("チャンネル数:"+ dataInf.ch);

                dataInf.samplingRate=readBytes(4,c+=2);
                System.out.println("サンプリングレート:"+ dataInf.samplingRate);

                dataInf.dataSpeed=readBytes(4,c+=4);
                System.out.println("データ速度:"+ dataInf.dataSpeed);

                dataInf.blockSize=readBytes(2,c+=4);
                System.out.println("ブロックサイズ(Byte/Sample*チャンネル数):"+ dataInf.blockSize);

                dataInf.bitParSample=readBytes(2,c+=2);
                System.out.println("サンプルあたりのビット数(bit/sample):"+ dataInf.bitParSample);

            }
            else
                break;

            if((c=checkHeader("data"))!=-1) {
                dataInf.dataPoint=c;
                System.out.println("dataヘッダ確認");
                dataInf.soundDataSize=readBytes(4, c);
                System.out.println("波形データのバイト数:"+ dataInf.soundDataSize);
                for(int i=0;i< dataInf.soundDataSize;i+=2) {
                    //soundList.add((short)readBytesB(2, i + c + 4));
                    soundList.add((short)readBytes(2, i + c + 4));
                    //System.out.println("readBytes:" + (i + c + 4));
                }
                    //soundList.add(data[c + i].shortValue());
                soundData=soundList.toArray(new Short[0]);
            }
            else
                break;



        }while(false);
    }

    private int readBytes(int byteNum,int indexNum)//リトルエンディアンでindexNum+1番目からbyteNumバイト読み込む
    {
        indexNum+=byteNum;
        int bytes=0;
        int j=0;
        for (int i = 2*(byteNum-1); i >= 0; i -= 2)
            bytes += (data[indexNum + j--] * Math.pow(16, i));

        return (byteNum<3)?((short)bytes):(bytes);
    }

    private int readBytesB(int byteNum,int indexNum)//ビッグエンディアンでindexNum+1番目からbyteNumバイト読み込む
    {
        indexNum++;
        int bytes=0;
        int j=0;
        for(int i=2*(byteNum-1);i>=0;i-=2) {
            if(indexNum+i<=data.length)
                bytes += (data[indexNum + j++] * Math.pow(16, i));
        }

        return (byteNum<3)?((short)bytes):(bytes);
    }

    private int checkHeader(String cd)//data配列を走査してヘッダを見つける
    {
        int count = 0;
        for(int d=0;d<data.length;d++) {
            if (cd.charAt(count) == data[d])
                count++;
            else
                count=0;
            if(count == cd.length())
                return d;
        }
        return -1;
    }


    public Short[] getSoundData()
    {
        System.out.println("波形データの数:"+soundData.length);
        return soundData;
    }

    public static class dataInf
    {
        public static int fileSize;
        public static int fmtBytes;
        public static int fmtID;
        public static int ch;
        public static int samplingRate;
        public static int dataSpeed;
        public static int blockSize;
        public static int bitParSample;
        public static int soundDataSize;

        public static int dataPoint;
    }
}
