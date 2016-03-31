package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import org.apache.commons.math3.util.FastMath;

public class Main extends Application {

    private NumberAxis x=new NumberAxis();
    private NumberAxis y=new NumberAxis();
    private XYChart.Series Series=new XYChart.Series();
    private XYChart.Series appSeries=new XYChart.Series();
    private LineChart<Number,Number> lineChart=new LineChart<Number,Number>(x,y);
    private double PI= FastMath.PI;

    double[] data={0,PI/4,2*PI/4,3*PI/4,
            PI,3*PI/4,2*PI/4,PI/4};

    double[] teisuuData={1,1,1,1,1,1,1,1};
    double[] sinData=new double[8192];
    private int smpf=3500;
    private Short[] rData;

    private recorder recorder=new recorder();

    int f=1500;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setScene(new Scene(lineChart, 1900, 1800));
        lineChart.getData().add(Series);
        lineChart.getData().add(appSeries);
        //WAV wav=new WAV("am49.wav");
        for(int i=0;i<sinData.length;i++) {
            if(i<2000)
                sinData[i] = F((double) i / smpf);
            else
                sinData[i]=0;
        }

        for(int i=6;i>0;i--)//五秒待ち後1秒録音　
        {
            if(i>1) {
                System.out.println(i-1);
            }
            else{
                rData=recorder.getVoice();
                System.out.println("getVoice:"+rData.length);
            }
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /*//WAV wav=new WAV("am50.wav");
        WAV wav=new WAV("am55.wav");
        //WAV wav=new WAV("400Hz.WAV");
        wav.readData();
        wav.readHeader();
        Short[] sdata=wav.getSoundData();*/

        FFT fft;
        primaryStage.show();
        //fft=new FFT(sinData,12,smpf);
        //fft=new FFT(sdata,WAV.dataInf.samplingRate);
        fft=new FFT(rData, sample.recorder.hz);
        fft.FFTcalc();


        /*for(int i=0;i<rData.length;i++){
            Series.getData().add(new XYChart.Data(i,fft.data[i].getReal()));
        }*/

        for(int i=0;i<fft.FFTLength/2;i++){
            Series.getData().add(new XYChart.Data(((double)recorder.hz/fft.FFTLength)*i,fft.getFFTData(i)));
        }
        /*for(int i=0;i<fft.data.length;i++) {
            Series.getData().add(new XYChart.Data(i, fft.data[i].getReal()));
            System.out.println("data["+i+"]:"+fft.data[i].getReal());
        }*/

        /*for(int i=0;i<fft.FFTLength/4;i++) {
            Series.getData().add(new XYChart.Data(((double)WAV.dataInf.samplingRate/fft.FFTLength) *2*i,fft.getFFTData(i)));
            //Series.getData().add(new XYChart.Data((double)smpf/sinData.length*i, fft.getFFTData(i)));
            if(fft.getFFTData(i)>500)
                System.out.println((double)WAV.dataInf.samplingRate/fft.FFTLength*2*i);
        }*/

        /*for(int i=0;i<sinData.length/2;i++)
        {
            Series.getData().add(new XYChart.Data((double)smpf/sinData.length*i, fft.getFFTData(i)));
            if(fft.getFFTData(i)>0.1)
                System.out.println((double)smpf/sinData.length*i);
        }*/
    }

    @Override
    public void stop()
    {
        recorder.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private double F(double x)
    {
        return FastMath.sin(2*PI*f*x);
    }

}
