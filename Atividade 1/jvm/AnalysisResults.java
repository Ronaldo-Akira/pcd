package jvm;

public class AnalysisResults {
    private int threads;
    private long time;
    private int livingCells;


    public AnalysisResults(int threads, long time, int livingCells){
        this.threads = threads;
        this.time = time;
        this.livingCells = livingCells;
    }

    @Override
    public String toString() {
        return "AnalysisResults{" +
                "threads=" + threads +
                ", time=" + time +
                ", livingCells=" + livingCells +
                '}';
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getLivingCells() {
        return livingCells;
    }

    public void setLivingCells(int livingCells) {
        this.livingCells = livingCells;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }
}
