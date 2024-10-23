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

    /**
     * Converts a value from the current memory unit to the target memory unit.
     *
     * @param value      The value to be converted from the current memory unit.
     * @param targetUnit The target memory unit to which the value will be converted.
     * @return The converted value in the target memory unit.
     */
    public double to(final double value, final MemoryUnit targetUnit) {
        final double valueInBytes = value * this.bytes;
        return valueInBytes / targetUnit.bytes;
    }

    /**
     * Converts a value from the specified source memory unit to the current memory unit.
     *
     * @param value      The value to be converted from the source memory unit.
     * @param sourceUnit The source memory unit from which the value will be converted.
     * @return The converted value in the current memory unit.
     */
    public double from(final double value, final MemoryUnit sourceUnit) {
        final double valueInBytes = value * sourceUnit.bytes;
        return valueInBytes / this.bytes;
    }

    /**
     * Converts a long value from the current memory unit to the target memory unit.
     *
     * @param value      The long value to be converted from the current memory unit.
     * @param targetUnit The target memory unit to which the value will be converted.
     * @return The converted value in the target memory unit as a long.
     */
    public long to(final long value, final MemoryUnit targetUnit) {
        return (long) this.to((double) value, targetUnit);
    }

    /**
     * Converts a long value from the specified source memory unit to the current memory unit.
     *
     * @param value      The long value to be converted from the source memory unit.
     * @param sourceUnit The source memory unit from which the value will be converted.
     * @return The converted value in the current memory unit as a long.
     */
    public long from(final long value, final MemoryUnit sourceUnit) {
        return (long) this.from((double) value, sourceUnit);
    }
}