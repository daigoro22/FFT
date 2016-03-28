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
    double[] sinData=new double[5000];
    private int smpf=5000;

    int f=200;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setScene(new Scene(lineChart, 1900, 1800));
        lineChart.getData().add(Series);
        lineChart.getData().add(appSeries);
        WAV wav=new WAV("am49.wav");

        for(int i=0;i<sinData.length;i++)
            sinData[i]=F((double)i/smpf);
        //WAV wav=new WAV("am50.wav");
        //WAV wav=new WAV("am55.wav");
        //WAV wav=new WAV("400Hz.WAV");
        wav.readData();
        wav.readHeader();
        Short[] sdata=wav.getSoundData();
        FFT fft;
        primaryStage.show();
        //fft=new FFT(data,3);
        fft=new FFT(sdata);
        Double[] FFTdata=fft.getFFT();

        /*for(int i=0;i<fft.data.length;i++) {
            Series.getData().add(new XYChart.Data(i, fft.data[i].getReal()));
            System.out.println("data["+i+"]:"+fft.data[i].getReal());
        }*/

        for(int i=0;i<FFTdata.length/2;i++) {
            //Series.getData().add(new XYChart.Data(((double)WAV.dataInf.samplingRate/sdata.length)*i,FFTdata[i]));
            //Series.getData().add(new XYChart.Data((double) 1 / ((double) sinData.length / smpf) * i, FFTdata[i]));
            Series.getData().add(new XYChart.Data(i, FFTdata[i]));
            System.out.println("FFT[" + i + "]:" + FFTdata[i]);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

    private double F(double x)
    {
        return FastMath.sin(2*PI*f*x);
    }
}
