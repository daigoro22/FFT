package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class Main extends Application {

    private NumberAxis x=new NumberAxis();
    private NumberAxis y=new NumberAxis();
    private XYChart.Series Series=new XYChart.Series();
    private XYChart.Series appSeries=new XYChart.Series();
    private LineChart<Number,Number> lineChart=new LineChart<Number,Number>(x,y);
    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setScene(new Scene(lineChart, 1900, 1800));
        lineChart.getData().add(Series);
        lineChart.getData().add(appSeries);
        WAV wav=new WAV("am49.wav");
        wav.readData();
        wav.readHeader();
        Short[] sdata=wav.getSoundData();
        FFT fft;
        primaryStage.show();
        fft=new FFT(sdata,13);
        Double[] FFTdata=fft.getFFT();
        for(int i=0;i<FFTdata.length/2;i++) {
            Series.getData().add(new XYChart.Data(i,FFTdata[i]));
            System.out.println("FFT["+i+"]:" + FFTdata[i]);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
