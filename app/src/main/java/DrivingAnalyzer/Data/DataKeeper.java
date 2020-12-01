package DrivingAnalyzer.Data;

import com.example.driveranomalydetection.sensor.SensorDataBatch;
import com.example.driveranomalydetection.sensor.SensorDataBatchRow;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DataKeeper {
    private LinkedList<SimpleTimestampData> data = new LinkedList<>();
    private Integer dataLength = 0;
    private Integer lastStatisticUpdate = 0;
    private Double percentageUpdateThreshold = 0.8;

    //Statistics
    private Map<DataType,Double[]> mean = new HashMap<>();
    private Map<DataType,Double[]> std = new HashMap<>();

    /** TODO: Add to constructor which statistics are vital for the anomaly detection model not to make */
    public DataKeeper(){
        for(DataType dt:DataType.values()){
            this.mean.put(dt,new Double[dt.getDim()]);
            this.std.put(dt,new Double[dt.getDim()]);
        }
    }

    public int putData(SensorDataBatch sensorDataBatch){
        List<SensorDataBatchRow> sensorDataBatchRows = sensorDataBatch.getRows();

        for(SensorDataBatchRow s: sensorDataBatchRows){
            data.add(new SimpleTimestampData(s));
        }

        this.dataLength = this.data.size();

        if(this.dataLength*this.percentageUpdateThreshold > this.lastStatisticUpdate){
            this.updateStatistics();
        }
        return 0;
    }

    private void updateStatistics(){
        for(DataType dt:DataType.values()) {
            this.updateStatistics(dt);
        }
        this.lastStatisticUpdate = this.dataLength;
    }

    private void updateStatistics(DataType dt){
        int prevLen = this.lastStatisticUpdate;
        Double[] prevMean = this.mean.get(dt);
        Double[] prevStd = this.std.get(dt);

        int newLen = this.dataLength - this.lastStatisticUpdate;
        Double[] newMean = new Double[dt.getDim()];

        for(int i=lastStatisticUpdate;i<this.dataLength;i++){
            for(int j = 0;j<dt.getDim();j++){
                newMean[j] += this.data.get(i).timestampSensorDataMap.get(dt).logs[j];
            }
        }
        for(int j=0;j<dt.getDim();j++){
            newMean[j] /= newLen;
        }

        Double[] newStd = new Double[dt.getDim()];
        for(int i=lastStatisticUpdate;i<this.dataLength;i++) {
            for(int j = 0;j<dt.getDim();j++){
                newStd[j] += Math.pow(newMean[j] - this.data.get(i).timestampSensorDataMap.get(dt).logs[j],2);
            }
        }
        for(int j = 0;j<dt.getDim();j++){
            newStd[j] = Math.sqrt(newStd[j]/newLen);
        }

        Double meanToUpdate[] = new Double[dt.getDim()];
        Double stdToUpdate[] = new Double[dt.getDim()];

        for(int i=0;i<dt.getDim();i++){
            meanToUpdate[i] = (prevMean[i]*prevLen + newMean[i]*newLen)/(prevLen+newLen);
            stdToUpdate[i] = Math.sqrt(Math.pow(prevStd[i],2) * prevLen + Math.pow(newStd[i],2) * newLen);
        }

        for(int i=0;i<dt.getDim();i++){
            this.mean.put(dt,meanToUpdate);
            this.std.put(dt,stdToUpdate);
        }
    }

    private void calculateStatistics(){
        this.updateStatistics();
    }

    /** TODO: maybe add list of statistics needed  */
    public Map<String,Double[]> getStatistics(DataType dt){
        Map<String,Double[]> map = new HashMap<>();
        map.put("std",this.std.get(dt));
        map.put("mean",this.mean.get(dt));
        return map;
    }

    public Map<String,Map<DataType,Double[]>> getAllStatistics(){
        Map<String,Map<DataType,Double[]>> map = new HashMap<>();
        map.put("mean",this.mean);
        map.put("std",this.std);
        return map;
    }
}
