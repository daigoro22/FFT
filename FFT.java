package sample;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.util.FastMath;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abedaigorou on 16/03/21.
 */
public class FFT
{
    public static double PI=FastMath.PI;
    private Complex[] data;
    private List<Complex> FFTList=new ArrayList<Complex>();
    private Complex[] FFTData;
    private int bitsNum;


    public FFT(Short[] data,int bitsNum)
    {
        List<Complex> tempData=new ArrayList<Complex>();
        for(Short s:data)
            tempData.add(new Complex(s.doubleValue()));
        if(data.length%2!=0)
            tempData.remove(data.length-1);
        this.data=tempData.toArray(new Complex[0]);
        this.bitsNum=bitsNum;

    }
    public FFT(double[] data,int bitsNum)
    {
        List<Complex> tempData=new ArrayList<Complex>();
        for(double d:data)
            tempData.add(new Complex(d));
        this.data=tempData.toArray(new Complex[0]);
        this.bitsNum=bitsNum;
    }

    public Double[] getFFT()
    {
        butterFlyCalc(this.data.length,this.data);
        FFTData=FFTList.toArray(new Complex[0]);
        List<Double> data=new ArrayList<Double>();
        for(Complex c:FFTData)
            data.add(c.abs()/this.data.length);
        Double[] revData=data.toArray(new Double[0]);
        return reverse(revData,bitsNum);
    }

    private void butterFlyCalc(int length,Complex[] data)
    {
        int halfLen=length/2;
        Complex[] plusData=new Complex[halfLen];
        Complex[] minusData=new Complex[halfLen];


        for(int i=0;i<halfLen;i++) {
            plusData[i]=data[i].add(data[halfLen + i]);
            minusData[i] = new Complex(0, -2 * PI * i / length).exp();
            minusData[i] = minusData[i].multiply(data[i].subtract(data[halfLen + i]));
            if(length==2){
                FFTList.add(plusData[0]);
                FFTList.add(minusData[0]);
                return;
            }
        }
        butterFlyCalc(plusData.length,plusData);
        butterFlyCalc(minusData.length,minusData);
        //System.out.println(new Complex(0,-2*PI/8).exp());
    }

    private Double[] reverse(Double[] data,int bitsNum)
    {
        double tempData;
        for(int i=0;i<data.length/2;i++)
        {
            tempData=data[i];
            data[i]=data[Integer.rotateRight(Integer.reverse(i),32-bitsNum)];
            data[Integer.rotateRight(Integer.reverse(i),32-bitsNum)]=tempData;
            //System.out.println(Integer.toBinaryString(i)+":rev:"+Integer.toBinaryString(Integer.rotateRight(Integer.reverse(i), 32 - 3)));
        }
        return data;
    }
}
