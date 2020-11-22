package DrivingAnalyzer;

public interface AnomalyDetector {
    public boolean putData();
    public AnomalyType detectAnomaly();
}
