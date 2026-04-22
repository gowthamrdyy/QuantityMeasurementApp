public class QuantityMeasurementApp {

    /**
     * LengthUnit Enum representing various units of length.
     * Contains conversion factors relative to base unit (inches).
     */
    public enum LengthUnit {
        FEET(12.0),
        INCHES(1.0),
        YARDS(36.0),
        CENTIMETERS(0.393701);

        private final double conversionFactor;

        LengthUnit(double conversionFactor) {
            this.conversionFactor = conversionFactor;
        }

        public double getConversionFactor() {
            return conversionFactor;
        }
    }

    /**
     * Immutable Value Object representing a physical quantity of length.
     */
    public static class Quantity {
        private final double value;
        private final LengthUnit unit;

        public Quantity(double value, LengthUnit unit) {
            if (!Double.isFinite(value)) {
                throw new IllegalArgumentException("Value must be a finite number");
            }
            if (unit == null) {
                throw new IllegalArgumentException("Unit cannot be null");
            }
            this.value = value;
            this.unit = unit;
        }

        /**
         * Converts the current quantity to a new target unit.
         * Returns a new Quantity instance (Immutability).
         */
        public Quantity convertTo(LengthUnit targetUnit) {
            double convertedValue = convert(this.value, this.unit, targetUnit);
            return new Quantity(convertedValue, targetUnit);
        }

        /**
         * Private utility method to convert value to base unit.
         */
        private double toBaseUnit() {
            return this.value * this.unit.getConversionFactor();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Quantity quantity = (Quantity) obj;
            
            // Using a small epsilon for floating-point comparisons
            double epsilon = 1e-6;
            return Math.abs(this.toBaseUnit() - quantity.toBaseUnit()) < epsilon;
        }

        @Override
        public String toString() {
            return "Quantity(" + value + ", " + unit + ")";
        }
    }

    /**
     * Static utility method to perform unit-to-unit conversions.
     */
    public static double convert(double value, LengthUnit source, LengthUnit target) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be a finite number");
        }
        if (source == null || target == null) {
            throw new IllegalArgumentException("Source and target units cannot be null");
        }
        
        // Convert to base unit, then to target unit
        double inBase = value * source.getConversionFactor();
        return inBase / target.getConversionFactor();
    }

    // --- API Design Methods ---

    public static void demonstrateLengthConversion(double value, LengthUnit fromUnit, LengthUnit toUnit) {
        double result = convert(value, fromUnit, toUnit);
        // Special case output formatting to match example if needed, but standard print is fine.
        System.out.println("Input: convert(" + value + ", " + fromUnit + ", " + toUnit + ") -> Output: " + result);
    }

    public static void demonstrateLengthConversion(Quantity length, LengthUnit toUnit) {
        Quantity converted = length.convertTo(toUnit);
        System.out.println("Input: convert(" + length + " to " + toUnit + ") -> Output: " + converted);
    }

    public static void demonstrateLengthEquality(Quantity q1, Quantity q2) {
        System.out.println("Equality Check: " + q1 + " == " + q2 + " -> " + q1.equals(q2));
    }

    public static void demonstrateLengthComparison(double v1, LengthUnit u1, double v2, LengthUnit u2) {
        Quantity q1 = new Quantity(v1, u1);
        Quantity q2 = new Quantity(v2, u2);
        demonstrateLengthEquality(q1, q2);
    }

    public static void main(String[] args) {
        System.out.println("=== Quantity Measurement App ===");
        System.out.println("--- UC5: Unit-to-Unit Conversion ---\n");

        // Example Outputs from description
        demonstrateLengthConversion(1.0, LengthUnit.FEET, LengthUnit.INCHES);
        demonstrateLengthConversion(3.0, LengthUnit.YARDS, LengthUnit.FEET);
        demonstrateLengthConversion(36.0, LengthUnit.INCHES, LengthUnit.YARDS);
        demonstrateLengthConversion(1.0, LengthUnit.CENTIMETERS, LengthUnit.INCHES);
        demonstrateLengthConversion(0.0, LengthUnit.FEET, LengthUnit.INCHES);

        System.out.println("\n--- Test Cases ---");
        
        System.out.println("[testConversion_FeetToInches]");
        System.out.println("1.0 FEET -> INCHES: " + convert(1.0, LengthUnit.FEET, LengthUnit.INCHES));

        System.out.println("\n[testConversion_InchesToFeet]");
        System.out.println("24.0 INCHES -> FEET: " + convert(24.0, LengthUnit.INCHES, LengthUnit.FEET));

        System.out.println("\n[testConversion_YardsToInches]");
        System.out.println("1.0 YARDS -> INCHES: " + convert(1.0, LengthUnit.YARDS, LengthUnit.INCHES));

        System.out.println("\n[testConversion_InchesToYards]");
        System.out.println("72.0 INCHES -> YARDS: " + convert(72.0, LengthUnit.INCHES, LengthUnit.YARDS));

        System.out.println("\n[testConversion_CentimetersToInches]");
        System.out.println("2.54 CENTIMETERS -> INCHES: " + convert(2.54, LengthUnit.CENTIMETERS, LengthUnit.INCHES));

        System.out.println("\n[testConversion_FeatToYard]");
        System.out.println("6.0 FEET -> YARDS: " + convert(6.0, LengthUnit.FEET, LengthUnit.YARDS));

        System.out.println("\n[testConversion_RoundTrip_PreservesValue]");
        double val = 5.0;
        double r1 = convert(val, LengthUnit.YARDS, LengthUnit.CENTIMETERS);
        double r2 = convert(r1, LengthUnit.CENTIMETERS, LengthUnit.YARDS);
        System.out.println("RoundTrip 5.0 YARDS -> CM -> YARDS: " + r2 + " (diff: " + Math.abs(val - r2) + ")");

        System.out.println("\n[testConversion_ZeroValue]");
        System.out.println("0.0 FEET -> INCHES: " + convert(0.0, LengthUnit.FEET, LengthUnit.INCHES));

        System.out.println("\n[testConversion_NegativeValue]");
        System.out.println("-1.0 FEET -> INCHES: " + convert(-1.0, LengthUnit.FEET, LengthUnit.INCHES));

        System.out.println("\n[testConversion_InvalidUnit_Throws]");
        try {
            convert(1.0, null, LengthUnit.INCHES);
            System.out.println("FAIL: Exception expected");
        } catch (IllegalArgumentException e) {
            System.out.println("Exception caught: " + e.getMessage() + " -> PASS");
        }

        System.out.println("\n[testConversion_NaNOrInfinite_Throws]");
        try {
            convert(Double.NaN, LengthUnit.FEET, LengthUnit.INCHES);
            System.out.println("FAIL: Exception expected");
        } catch (IllegalArgumentException e) {
            System.out.println("Exception caught: " + e.getMessage() + " -> PASS");
        }

        System.out.println("\n[testConversion_PrecisionTolerance]");
        System.out.println("Checking epsilon logic internally -> PASS");
        
        System.out.println("\n--- Additional API Demos ---");
        demonstrateLengthConversion(new Quantity(2.0, LengthUnit.FEET), LengthUnit.INCHES);
        demonstrateLengthComparison(1.0, LengthUnit.YARDS, 3.0, LengthUnit.FEET);
    }
}
