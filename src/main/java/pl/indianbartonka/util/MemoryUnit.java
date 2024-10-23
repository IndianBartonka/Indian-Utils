package pl.indianbartonka.util;

/**
 * Enum representing different memory units.
 * <p>
 * Documents and math operations written by ChatGPT
 * </p>
 */
public enum MemoryUnit {

    BYTES(1),
    KILOBYTES(1024),
    MEGABYTES(1024 * 1024),
    GIGABYTES(1024 * 1024 * 1024);

    private final double bytes;

    MemoryUnit(final double bytes) {
        this.bytes = bytes;
    }

    public double to(final double value, final MemoryUnit targetUnit) {
        final double valueInBytes = value * this.bytes;
        return valueInBytes / targetUnit.bytes;
    }

    public double from(final double value, final MemoryUnit sourceUnit) {
        final double valueInBytes = value * sourceUnit.bytes;
        return valueInBytes / this.bytes;
    }

    public long to(final long value, final MemoryUnit unit) {
        return (long) this.to((double) value, unit);
    }

    public long from(final long value, final MemoryUnit unit) {
        return (long) this.from((double) value, unit);
    }
}