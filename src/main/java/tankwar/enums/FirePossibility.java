package tankwar.enums;

public enum FirePossibility {
    LOW(0.9D), HIGH(0.85);
    
    private final double value;

    FirePossibility(double value) {
        this.value = value;
    }

    public double value() {
        return value;
    }
}
