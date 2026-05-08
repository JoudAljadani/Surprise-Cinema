public class DashStat {
    private String label;
    private int count;

    public DashStat(String label, int count) {
        this.label = label;
        this.count = count;
    }

    public String getLabel() {
        return label;
    }

    public int getCount() {
        return count;
    }
}
