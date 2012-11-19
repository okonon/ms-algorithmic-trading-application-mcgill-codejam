
package util;

/**
 * Smoothed version of the Simple Moving Average (SMA)
 *
 * @author Team Gredona
 */
public class TriangularMovingAverage extends Strategy {

    SimpleMovingAverage mySMA;

    public TriangularMovingAverage(SimpleMovingAverage SMA, GraphData g) {
        super(g);
        mySMA = SMA;
        type = "Triangular Moving Average";
        acronym = "TMA";
        typeInt = 3;
    }

    @Override
    protected float computeSlowMovingAverage() {
        int t = slowDataBuffer.size();
        int sumOfWeightingFactors = (t * (1 + t)) / 2;
        float sumOfWeightedPrices = 0;
        for (int index = 0; index < t; index++) {
            sumOfWeightedPrices += mySMA.getSlowBufferValue(index) * (index + 1);
        }
        
        float SMA = sumOfWeightedPrices / sumOfWeightingFactors;
        myGraphData.pushSlowTMA(SMA);
        return SMA;

    }

    @Override
    protected float computeFastMovingAverage() {
        int t = fastDataBuffer.size();
        int sumOfWeightingFactors = (t * (1 + t)) / 2;
        float sumOfWeightedPrices = 0;
        for (int index = 0; index < t; index++) {
            sumOfWeightedPrices += mySMA.getFastBufferValue(index) * (index + 1);
        }
        float FMA = sumOfWeightedPrices / sumOfWeightingFactors;
        myGraphData.pushFastTMA(FMA);
        return FMA;
    }
}
