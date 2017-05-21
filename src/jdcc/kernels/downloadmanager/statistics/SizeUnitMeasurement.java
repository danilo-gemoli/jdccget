package jdcc.kernels.downloadmanager.statistics;

public enum SizeUnitMeasurement {
    BYTES("B", 0),
    KILO_BYTES("KB", 1),
    MEGA_BYTES("MB", 2),
    GIGA_BYTES("GB", 3);

    private String signature;
    private int magnitudeOrder;

    SizeUnitMeasurement(String signature, int magnitudeOrder) {
        this.signature = signature;
        this.magnitudeOrder = magnitudeOrder;
    }

    public int getMagnitudeOrder() {
        return magnitudeOrder;
    }

    @Override
    public String toString() {
        return signature;
    }
}
