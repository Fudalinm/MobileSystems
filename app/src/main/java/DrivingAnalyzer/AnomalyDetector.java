package DrivingAnalyzer;
import java.util.List;

public interface AnomalyDetector {
    public boolean putData();
    public List<AnomalyType> detectAnomaly();
}
