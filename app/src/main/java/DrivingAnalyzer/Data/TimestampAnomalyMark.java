package DrivingAnalyzer.Data;

import DrivingAnalyzer.AnomalyType;

public class TimestampAnomalyMark {
    private long timestamp;
    private AnomalyType anomalyType;

    public TimestampAnomalyMark(long t, AnomalyType at){
        this.timestamp = t;
        this.anomalyType = at;
    }
}
