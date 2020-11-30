package DrivingAnalyzer.Data;

import java.util.List;

public class SimpleSensorData {
    int dim;
    Float logs[];

    public SimpleSensorData(DataType dt, List<Float> logs){
        this.dim = dt.getDim();
        this.logs = new Float[dt.getDim()];
        for(int i=0;i<this.dim;i++){
            this.logs[i] = logs.get(i);
        }
    }
}
