package jdcc.kernels.downloadmanager.statistics;

public class Size {
    /***
     * La quantità.
     */
    public float size;
    /***
     * L'unitò di misura.
     */
    public SizeUnitMeasurement unit;

    public Size() {
        size = 0;
        unit = SizeUnitMeasurement.BYTES;
    }

    public Size(float size, SizeUnitMeasurement unit) {
        this.size = size;
        this.unit = unit;
    }

    public Size to(SizeUnitMeasurement targetUnit) {
        float targetSize = toRawSize(targetUnit);
        return new Size(targetSize, targetUnit);
    }

    public float toRawSize(SizeUnitMeasurement targetUnit) {
        int sourceMagnitudeOrder = unit.getMagnitudeOrder();
        int targetMagnitudeOrder = targetUnit.getMagnitudeOrder();
        int magnitudeDifference = sourceMagnitudeOrder - targetMagnitudeOrder;
        float targetSize = size * (float) Math.pow(1024, magnitudeDifference);
        return targetSize;
    }
}
