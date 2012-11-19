package gui;


import java.awt.BorderLayout;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import util.GraphData; 
import java.util.Iterator;
import java.awt.geom.*; 
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;


/**
 *  StockGraph
 * 
 *  This class generates the ChartPanel to be displayed
 * 
 *  @author mattvertescher
 */
public class StockGraph {
    
    private XYSeries priceSeries, fastSeries, slowSeries;
    private String name; 
    private int xLength = 100; 
    private int yLength = 50;
    private ChartPanel chartPanel;
    private DataTest dt;
    private GraphData graphData; 
    
    
    private float maxY, minY; 

    /**
     * StockGraph Constructor
     *
     * @author mattvertescher
     */
    public StockGraph(String title, GraphData gd) {
        name = title;
        
        //Sample Inputs
        priceSeries = new XYSeries("Stock Price");
        priceSeries.add(0.0, 0.0);
        priceSeries.add(100.0, 10.0);
        
        fastSeries = new XYSeries("Fast Series");
        fastSeries.add(0.0, 0.0);
        fastSeries.add(100.0, 10.0);
        
        slowSeries = new XYSeries("Slow Series");
        slowSeries.add(0.0, 0.0);
        slowSeries.add(100.0, 10.0);
        
        updateChart();
    
        dt = new DataTest();
        
        graphData = gd; 
    }
    
    /**
     * ChartPanel getter for the TradingServerFrame
     *
     * @return Chart Panel
     * @author mattvertescher
     */
    public ChartPanel getChartPanel() {
        
        return chartPanel;
    }
     
    /**
     * Updates all the data points for all the series 
     *
     * @author mattvertescher
     */
    public void updateAllSeries() {
        updateFast();
        updateSlow();
        updateStockPrice();       
        dt.increment++;
    }
    
    /**
     * Updates the ChartPanel
     *
     * @author mattvertescher
     */
    private void updateChart(){
        
        /*
        XYSeriesCollection collection = new XYSeriesCollection();
        collection.addSeries(priceSeries);
        collection.addSeries(fastSeries);
        collection.addSeries(slowSeries);
        XYDataset xyDataset = collection;

        JFreeChart chart = ChartFactory.createXYLineChart(name, "Time (Seconds)", "Price (USD)",
                xyDataset, PlotOrientation.VERTICAL, true, true, false);
       
        
        chartPanel = new ChartPanel(chart);
        chartPanel.setVisible(true);
        chartPanel.setSize(xLength,yLength);
        * 
        * */ 
        
        NumberAxis timeAxis = new NumberAxis("Time (Seconds)");
        timeAxis.setAutoRangeIncludesZero(false);
        NumberAxis priceAxis = new NumberAxis("Price (USD)");
        priceAxis.setAutoRangeIncludesZero(false);
        
        XYSeriesCollection collection = new XYSeriesCollection();
        collection.addSeries(priceSeries);
        collection.addSeries(fastSeries);
        collection.addSeries(slowSeries);
        XYDataset xyDataset = collection;
        
        XYPlot p = new XYPlot(xyDataset,timeAxis,priceAxis,new StandardXYItemRenderer());
        JFreeChart chart = new JFreeChart(name, JFreeChart.DEFAULT_TITLE_FONT, p, true);
        
        chartPanel = new ChartPanel(chart);
        
        chartPanel.setVisible(true);
        
        
    }
    
    /**
     * Updates the fast series
     *
     * @author mattvertescher
     */
    private void updateFast() {
       
        
        // Get first character of name
        char c = name.charAt(0); 
        Iterator<Float> iter = null; 
        switch (c){
            case 'E': 
                iter = graphData.getFastEMA(); 
                break; 
            case 'L': 
                iter = graphData.getFastLWMA(); 
                break;
            case 'S': 
                iter = graphData.getFastSMA(); 
                break;
            case 'T': 
                iter = graphData.getFastTMA(); 
                break;
            default: 
                System.out.println("Error when matching Stock Graph update to strategy"); 
                System.exit(1); 
        }
        
        if (iter == null)
            return; 
        
        // get size of each array
        int size = graphData.getSize(); 
        int time = graphData.getTime(); 
        
        float[] xPoints = new float[size]; 
        float[] yPoints = new float[size];
        for (int i = 0; i < size; i++){
            xPoints[i] = time-size+i; 
            
            assert iter.hasNext(); 
            yPoints[i] = iter.next(); 
        }
        
        updateSeries(fastSeries, xPoints, yPoints);
    }
    
    /**
     * Updates the slow series
     *
     * @author mattvertescher
     */
    private void updateSlow() {
      
        
        // Get first character of name
        char c = name.charAt(0); 
        Iterator<Float> iter = null; 
        switch (c){
            case 'E': 
                iter = graphData.getSlowEMA(); 
                break; 
            case 'L': 
                iter = graphData.getSlowLWMA(); 
                break;
            case 'S': 
                iter = graphData.getSlowSMA(); 
                break;
            case 'T': 
                iter = graphData.getSlowTMA(); 
                break;
            default: 
                System.out.println("Error when matching Stock Graph update to strategy"); 
                System.exit(1); 
        }
        
        if (iter == null)
            return; 
        
        // get size of each array
        int size = graphData.getSize(); 
        int time = graphData.getTime(); 
        
        float[] xPoints = new float[size]; 
        float[] yPoints = new float[size];
        for (int i = 0; i < size; i++){
            xPoints[i] = time-size+i; 
            
            assert iter.hasNext(); 
            yPoints[i] = iter.next(); 
        }

        updateSeries(slowSeries, xPoints, yPoints);
    }
    
    
    /**
     * Updates the stock price series
     *
     * @author mattvertescher
     */
    private void updateStockPrice() {
        
        Iterator<Float> iter = graphData.getPrice();  
        
        if (iter == null)
            return; 
        
        // get size of each array
        int size = graphData.getSize(); 
        int time = graphData.getTime(); 
        
        
        float[] xPoints = new float[size]; 
        float[] yPoints = new float[size];
        for (int i = 0; i < size; i++){
            xPoints[i] = time-size+i; 
            
            assert iter.hasNext(); 
            yPoints[i] = iter.next(); 
        }
        
        updateSeries(priceSeries, xPoints, yPoints);
    }
    
    
    /**
     * Loads new data points for a series 
     * 
     * @param series The series to update
     * @param X The new x points 
     * @param Y The new y points 
     *
     * @author mattvertescher
     */
    private void updateSeries(XYSeries series, float[] X, float[] Y) {
        int length = X.length; 
        series.clear();
        for (int i = 0; i < length; i++) 
            series.add(X[i],Y[i]); 
    }

    
}
