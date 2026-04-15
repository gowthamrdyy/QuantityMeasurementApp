public class QuantityMeasurementApp {

    // Step 1: LengthUnit Enum with conversion factors relative to base unit (inches)
    enum LengthUnit {
        FEET(12.0),    // 1 foot = 12 inches
        INCH(1.0);     // 1 inch = 1 inch (base unit)

        private final double conversionFactor;

        LengthUnit(double conversionFactor) {
            this.conversionFactor = conversionFactor;
        }

        public double getConversionFactor() {
            return conversionFactor;
        }
    }

    // Step 2: Generic QuantityLength class - Single class for all units (DRY Principle)
    static class QuantityLength {
        private final double value;
        private final LengthUnit unit;

        public QuantityLength(double value, LengthUnit unit) {
            if (unit == null) {
                throw new IllegalArgumentException("Unit cannot be null");
            }
            this.value = value;
            this.unit = unit;
        }

        // Convert to base unit (inches) for comparison
        private double toBaseUnit() {
            return this.value * this.unit.getConversionFactor();
        }

        @Override
        public boolean equals(Object obj) {
            // Reflexive
            if (this == obj) return true;
            // Null check
            if (obj == null) return false;
            // Type check
            if (this.getClass() != obj.getClass()) return false;
            // Convert both to base unit and compare
            QuantityLength other = (QuantityLength) obj;
            return Double.compare(this.toBaseUnit(), other.toBaseUnit()) == 0;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Quantity Measurement App ===");
        System.out.println("--- UC3: Generic Quantity Class for DRY Principle ---\n");

        // testEquality_FeetToFeet_SameValue
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(1.0, LengthUnit.FEET);
        System.out.println("[testEquality_FeetToFeet_SameValue]");
        System.out.println("1.0 feet == 1.0 feet -> " + q1.equals(q2));

        // testEquality_InchToInch_SameValue
        QuantityLength q3 = new QuantityLength(1.0, LengthUnit.INCH);
        QuantityLength q4 = new QuantityLength(1.0, LengthUnit.INCH);
        System.out.println("\n[testEquality_InchToInch_SameValue]");
        System.out.println("1.0 inch == 1.0 inch -> " + q3.equals(q4));

        // testEquality_FeetToInch_EquivalentValue (1 foot = 12 inches)
        QuantityLength q5 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength q6 = new QuantityLength(12.0, LengthUnit.INCH);
        System.out.println("\n[testEquality_FeetToInch_EquivalentValue]");
        System.out.println("1.0 feet == 12.0 inch -> " + q5.equals(q6));

        // testEquality_InchToFeet_EquivalentValue (symmetry)
        System.out.println("\n[testEquality_InchToFeet_EquivalentValue]");
        System.out.println("12.0 inch == 1.0 feet -> " + q6.equals(q5));

        // testEquality_FeetToFeet_DifferentValue
        QuantityLength q7 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength q8 = new QuantityLength(2.0, LengthUnit.FEET);
        System.out.println("\n[testEquality_FeetToFeet_DifferentValue]");
        System.out.println("1.0 feet == 2.0 feet -> " + q7.equals(q8));

        // testEquality_InchToInch_DifferentValue
        QuantityLength q9  = new QuantityLength(1.0, LengthUnit.INCH);
        QuantityLength q10 = new QuantityLength(2.0, LengthUnit.INCH);
        System.out.println("\n[testEquality_InchToInch_DifferentValue]");
        System.out.println("1.0 inch == 2.0 inch -> " + q9.equals(q10));

        // testEquality_InvalidUnit (null unit passed to constructor)
        System.out.println("\n[testEquality_InvalidUnit]");
        try {
            QuantityLength qInvalid = new QuantityLength(1.0, null);
            System.out.println("No exception thrown — FAIL");
        } catch (IllegalArgumentException e) {
            System.out.println("Exception caught: " + e.getMessage() + " -> true");
        }

        // testEquality_SameReference
        System.out.println("\n[testEquality_SameReference]");
        System.out.println("1.0 feet == itself (same reference) -> " + q1.equals(q1));

        // testEquality_NullComparison
        System.out.println("\n[testEquality_NullComparison]");
        System.out.println("1.0 feet == null -> " + q1.equals(null));
    }
}
