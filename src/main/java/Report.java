public interface Report {
    void createReport(String header, String[][] batch);
    void nextBatch(String[][] batch);
    void getReport();
}
