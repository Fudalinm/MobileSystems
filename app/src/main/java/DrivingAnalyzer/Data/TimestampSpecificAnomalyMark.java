package DrivingAnalyzer.Data;

import java.util.Map;

import DrivingAnalyzer.AnomalyType;

public class TimestampSpecificAnomalyMark {
    private long timestamp;
    private boolean fAnyAnomaly;
    private Map<DataType, AnomalyType> map;

    public TimestampSpecificAnomalyMark(long timestamp,Map<DataType,AnomalyType> map){
        this.timestamp = timestamp;
        boolean fa = false;
        for (AnomalyType a:map.values()){
            if (a == AnomalyType.Yes){
                fa = true;
                break;
            }
        }
        this.fAnyAnomaly = fa;
        this.map = map;
    }
}
