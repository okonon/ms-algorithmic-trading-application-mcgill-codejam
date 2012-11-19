package gui;


import java.util.Calendar;
import java.util.Date;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;


/**
 *  Data representation for the managers schedule 
 * 
 * @author mattvertescher
 */
public class GanttSchedule extends JPanel {

   /**
    *  Gantt Schedule Constructor 
    * 
    * @author mattvertescher
    */
    public GanttSchedule() {
        
        final IntervalCategoryDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset);

        // add the chart to a panel...
        final ChartPanel chartPanel = new ChartPanel(chart);
        
        chartPanel.setSize(1125, 625);
        this.add(chartPanel);
        
    }

    
    /**
     * Creates a dataset for a Gantt chart.
     *
     * @return The dataset.
     */
    public static IntervalCategoryDataset createDataset() {

        final TaskSeries taskSeries1 = new TaskSeries("Shift 1");


        taskSeries1.add(new Task("Manager 1", new SimpleTimePeriod(time(9,00), time(11,00))));
        taskSeries1.add(new Task("Manager 2", new SimpleTimePeriod(time(9,00), time(11,00))));
        taskSeries1.add(new Task("Manager 3", new SimpleTimePeriod(time(9,30), time(11,30))));
        taskSeries1.add(new Task("Manager 4", new SimpleTimePeriod(time(11,00), time(13,00))));
        taskSeries1.add(new Task("Manager 5", new SimpleTimePeriod(time(13,00), time(15,00))));
        taskSeries1.add(new Task("Manager 6", new SimpleTimePeriod(time(13,30), time(15,30))));
        taskSeries1.add(new Task("Manager 7", new SimpleTimePeriod(time(14,00), time(16,00))));


        final TaskSeries taskSeries2 = new TaskSeries("Shift 2");

        taskSeries2.add(new Task("Manager 1", new SimpleTimePeriod(time(11,30), time(13,30))));
        taskSeries2.add(new Task("Manager 2", new SimpleTimePeriod(time(11,30), time(13,30))));
        taskSeries2.add(new Task("Manager 3", new SimpleTimePeriod(time(12,00), time(14,00))));
        taskSeries2.add(new Task("Manager 4", new SimpleTimePeriod(time(13,30), time(15,30))));
        taskSeries2.add(new Task("Manager 5", new SimpleTimePeriod(time(15,30), time(17,30))));
        taskSeries2.add(new Task("Manager 6", new SimpleTimePeriod(time(16,00), time(18,00))));
        taskSeries2.add(new Task("Manager 7", new SimpleTimePeriod(time(16,30), time(18,30))));

        final TaskSeriesCollection collection = new TaskSeriesCollection();
        collection.add(taskSeries1);
        collection.add(taskSeries2);

        return collection;
    }

     /**
     * Utility method for creating <code>Date</code> objects.
     *
     * @param day  the date.
     * @param month  the month.
     * @param year  the year.
     *
     * @return a date.
     */
    private static Date time(final int hour, final int minutes) {
        
        final Calendar calendar = Calendar.getInstance();
        calendar.set(0, 0, 0, hour, minutes, 0);
        final Date result = calendar.getTime();
        
        return result;
    }
        
        
    /**
     * Creates a chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return The chart.
     */
    private JFreeChart createChart(final IntervalCategoryDataset dataset) {
        final JFreeChart chart = ChartFactory.createGanttChart(
            "Manager Schedule",  // chart title
            "Managers",              // domain axis label
            "Time (Hour)",              // range axis label
            dataset,             // data
            true,                // include legend
            true,                // tooltips
            false                // urls
        );    
       //chart.getCategoryPlot().getDomainAxis().setMaxCategoryLabelWidthRatio(10.0f);
        return chart;    
    }
    

}
