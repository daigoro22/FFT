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

    private Short[] rData;
    player player=new player();

    private recorder recorder=new recorder();

    private binReader reader=new binReader("output6.dat",16000);

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setScene(new Scene(lineChart, 1900, 1800));
        lineChart.getData().add(Series);
        lineChart.getData().add(appSeries);

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

        player.start();
        player.setVoice(recorder.getBVoice());

        /*WAV wav=new WAV("am49.wav");
        //WAV wav=new WAV("am50.wav");
        //WAV wav=new WAV("am55.wav");
        //WAV wav=new WAV("400Hz.WAV");
        wav.readData();
        wav.readHeader();
        Short[] sdata=wav.getSoundData();*/

        //rData=reader.getVoice();

        FFT fft;
        primaryStage.show();
        //fft=new FFT(sinData,12,smpf);
        //fft=new FFT(sdata,WAV.dataInf.samplingRate);
        fft=new FFT(rData, recorder.hz);
        fft.FFTcalc();


        /*for(int i=0;i<fft.data.length;i++){
            Series.getData().add(new XYChart.Data(i,fft.data[i].getReal()));
            System.out.println("data["+i+"]:"+fft.data[i].getReal());
        }*/

        for(int i=0;i<fft.FFTLength/4;i++) {
            Series.getData().add(new XYChart.Data(((double) recorder.hz / (fft.FFTLength)) /2* i, fft.getFFTData(i)));
            if(fft.getFFTData(i)>70)
                System.out.println(((double) recorder.hz / (fft.FFTLength)) * i);
        }

        /*for(int i=0;i<fft.data.length;i++) {
            Series.getData().add(new XYChart.Data(i, fft.data[i].getReal()));
            System.out.println("data["+i+"]:"+fft.data[i].getReal());
        }*/

        /*for(int i=0;i<fft.FFTLength/2;i++) {
            Series.getData().add(new XYChart.Data(((double)WAV.dataInf.samplingRate/fft.FFTLength)*i,fft.getFFTData(i)));
            //Series.getData().add(new XYChart.Data((double)smpf/sinData.length*i, fft.getFFTData(i)));
            if(fft.getFFTData(i)>500)
                System.out.println(((double)WAV.dataInf.samplingRate/fft.FFTLength)*i);
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
        reader.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
