package sample;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abedaigorou on 16/04/03.
 */
public class binReader
{
    private FileInputStream input;
    private byte[] inputData;

    public binReader(String fileName,int size)
    {
        inputData=new byte[size];
        try {
            input = new FileInputStream(fileName);
        }catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        try {
            input.read(inputData);
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public byte[] getData()
    {
        return inputData;
    }

    public Short[] getVoice()
    {
        List<Short> dataList=new ArrayList<Short>();
        for(int i=0;i<inputData.length;i+=2){
            dataList.add((short)readBytes(2,i,inputData));
        }
        return dataList.toArray(new Short[0]);
    }

    private int readBytes(int byteNum,int indexNum,byte[] data)//リトルエンディアンでindexNum+1番目からbyteNumバイト読み込む
    {
        indexNum+=byteNum-1;
        int bytes=0;
        int j=0;
        for (int i = 2*(byteNum-1); i >= 0; i -= 2)
            bytes += ((data[indexNum + j--]&0xff)* Math.pow(16, i));

        return (byteNum<3)?((short)bytes):(bytes);
    }

    public void stop()
    {
        try {
            input.close();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
