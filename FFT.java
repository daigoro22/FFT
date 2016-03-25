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
    private double PI=FastMath.PI;
    Complex[] data={new Complex(0), new Complex(PI/4),new Complex(2*PI/4),new Complex(3*PI/4),
            new Complex(PI),new Complex(3*PI/4),new Complex(2*PI/4),new Complex(PI/4)};
    List<Complex> FFTList=new ArrayList<Complex>();
    Complex[] FFTData;


    public FFT()
    {
        butterFlyCalc(data.length,data);
        FFTData=FFTList.toArray(new Complex[0]);
        List<Double> dFFTList=new ArrayList<Double>();
        for(Complex c:FFTData)
            dFFTList.add(c.abs());
        Double[] dFFTData;
        dFFTData=reverse(dFFTList.toArray(new Double[0]));
        for(Double d:dFFTData)
            System.out.println("FFTData:"+d);

    }

    /*public double[] getFFT(Short[] data)
    {
        butterFlyCalc(this.data.length,this.data);
        FFTData=FFTList.toArray(new Complex[0]);

        for(Complex c:FFTData)
            System.out.println("FFTData:"+c.abs());
    }*/

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

    private Double[] reverse(Double[] data)
    {
        double tempData;
        for(int i=0;i<data.length/2;i++)
        {
            tempData=data[i];
            data[i]=data[Integer.rotateRight(Integer.reverse(i), 32 - 3)];
            data[Integer.rotateRight(Integer.reverse(i), 32 - 3)]=tempData;
            //System.out.println(Integer.toBinaryString(i)+":rev:"+Integer.toBinaryString(Integer.rotateRight(Integer.reverse(i), 32 - 3)));
        }
        return data;
    }
}
