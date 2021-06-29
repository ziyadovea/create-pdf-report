import java.util.List;

public interface Report {
    void createReport(List<String> header, List<String> batch);
    void nextBatch(List<String> batch);
    void getReport();
}
