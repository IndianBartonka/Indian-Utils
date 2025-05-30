package pl.indianbartonka.util;

import pl.indianbartonka.util.annotation.Since;
import pl.indianbartonka.util.annotation.UtilityClass;

/**
 * Enum representing different memory units.
 * <p>
 * Documents and math operations written by ChatGPT
 * </p>
 */
@UtilityClass
public enum MemoryUnit {

    BYTES(1),
    KILOBYTES(1000),
    MEGABYTES(1000 * 1000),
    GIGABYTES(1000 * 1000 * 1000),
    KIBIBYTES(1024),
    MEBIBYTES(1024 * 1024),
    GIBIBYTES(1024 * 1024 * 1024);

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

    /**
     * Converts an int value from the current memory unit to the target memory unit.
     *
     * @param value      The int value to be converted from the current memory unit.
     * @param targetUnit The target memory unit to which the value will be converted.
     * @return The converted value in the target memory unit as an int.
     */
    @Since("0.0.9.3")
    public int to(final int value, final MemoryUnit targetUnit) {
        return (int) this.to((double) value, targetUnit);
    }

    /**
     * Converts an int value from the specified source memory unit to the current memory unit.
     *
     * @param value      The int value to be converted from the source memory unit.
     * @param sourceUnit The source memory unit from which the value will be converted.
     * @return The converted value in the current memory unit as an int.
     */
    @Since("0.0.9.3")
    public int from(final int value, final MemoryUnit sourceUnit) {
        return (int) this.from((double) value, sourceUnit);
    }
}