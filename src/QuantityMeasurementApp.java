public class QuantityMeasurementApp {

    // Step 1: LengthUnit Enum with conversion factors relative to base unit (inches)
    enum LengthUnit {
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

    // Step 2: Generic Quantity class - Single class for all units (DRY Principle)
    static class Quantity {
        private final double value;
        private final LengthUnit unit;

        public Quantity(double value, LengthUnit unit) {
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
            // Reflexive property
            if (this == obj) return true;
            // Null check
            if (obj == null) return false;
            // Type check
            if (this.getClass() != obj.getClass()) return false;
            
            Quantity other = (Quantity) obj;
            // Use rounding to 5 decimal places for double precision consistency
            double thisBase = Math.round(this.toBaseUnit() * 100000.0) / 100000.0;
            double otherBase = Math.round(other.toBaseUnit() * 100000.0) / 100000.0;
            return Double.compare(thisBase, otherBase) == 0;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Quantity Measurement App ===");
        System.out.println("--- UC4: Extended Unit Support ---\n");

        // Example Outputs from description
        System.out.println("Input: Quantity(1.0, YARDS) and Quantity(3.0, FEET)");
        System.out.println("Output: Equal (" + new Quantity(1.0, LengthUnit.YARDS).equals(new Quantity(3.0, LengthUnit.FEET)) + ")\n");
        
        System.out.println("Input: Quantity(1.0, YARDS) and Quantity(36.0, INCHES)");
        System.out.println("Output: Equal (" + new Quantity(1.0, LengthUnit.YARDS).equals(new Quantity(36.0, LengthUnit.INCHES)) + ")\n");

        System.out.println("Input: Quantity(2.0, YARDS) and Quantity(2.0, YARDS)");
        System.out.println("Output: Equal (" + new Quantity(2.0, LengthUnit.YARDS).equals(new Quantity(2.0, LengthUnit.YARDS)) + ")\n");

        System.out.println("Input: Quantity(2.0, CENTIMETERS) and Quantity(2.0, CENTIMETERS)");
        System.out.println("Output: Equal (" + new Quantity(2.0, LengthUnit.CENTIMETERS).equals(new Quantity(2.0, LengthUnit.CENTIMETERS)) + ")\n");

        System.out.println("Input: Quantity(1.0, CENTIMETERS) and Quantity(0.393701, INCHES)");
        System.out.println("Output: Equal (" + new Quantity(1.0, LengthUnit.CENTIMETERS).equals(new Quantity(0.393701, LengthUnit.INCHES)) + ")\n");

        // Required Test Cases
        Quantity y1 = new Quantity(1.0, LengthUnit.YARDS);
        Quantity y2 = new Quantity(1.0, LengthUnit.YARDS);
        Quantity y3 = new Quantity(2.0, LengthUnit.YARDS);
        Quantity f3 = new Quantity(3.0, LengthUnit.FEET);
        Quantity i36 = new Quantity(36.0, LengthUnit.INCHES);
        Quantity f2 = new Quantity(2.0, LengthUnit.FEET);
        
        Quantity cm1 = new Quantity(1.0, LengthUnit.CENTIMETERS);
        Quantity inCmEq = new Quantity(0.393701, LengthUnit.INCHES);
        Quantity f1 = new Quantity(1.0, LengthUnit.FEET);

        System.out.println("[testEquality_YardToYard_SameValue]");
        System.out.println("Quantity(1.0, YARDS) equals Quantity(1.0, YARDS) -> " + y1.equals(y2));

        System.out.println("\n[testEquality_YardToYard_DifferentValue]");
        System.out.println("Quantity(1.0, YARDS) does not equal Quantity(2.0, YARDS) -> " + !y1.equals(y3));

        System.out.println("\n[testEquality_YardToFeet_EquivalentValue]");
        System.out.println("Quantity(1.0, YARDS) equals Quantity(3.0, FEET) -> " + y1.equals(f3));

        System.out.println("\n[testEquality_FeetToYard_EquivalentValue]");
        System.out.println("Quantity(3.0, FEET) equals Quantity(1.0, YARDS) -> " + f3.equals(y1));

        System.out.println("\n[testEquality_YardToInches_EquivalentValue]");
        System.out.println("Quantity(1.0, YARDS) equals Quantity(36.0, INCHES) -> " + y1.equals(i36));

        System.out.println("\n[testEquality_InchesToYard_EquivalentValue]");
        System.out.println("Quantity(36.0, INCHES) equals Quantity(1.0, YARDS) -> " + i36.equals(y1));

        System.out.println("\n[testEquality_YardToFeet_NonEquivalentValue]");
        System.out.println("Quantity(1.0, YARDS) does not equal Quantity(2.0, FEET) -> " + !y1.equals(f2));

        System.out.println("\n[testEquality_centimetersToInches_EquivalentValue]");
        System.out.println("Quantity(1.0, CENTIMETERS) equals Quantity(0.393701, INCHES) -> " + cm1.equals(inCmEq));

        System.out.println("\n[testEquality_centimetersToFeet_NonEquivalentValue]");
        System.out.println("Quantity(1.0, CENTIMETERS) does not equal Quantity(1.0, FEET) -> " + !cm1.equals(f1));

        System.out.println("\n[testEquality_MultiUnit_TransitiveProperty]");
        System.out.println("If A=B and B=C, then A=C -> " + (y1.equals(f3) && f3.equals(i36) && y1.equals(i36)));

        System.out.println("\n[testEquality_YardWithNullUnit]");
        try {
            new Quantity(1.0, null);
            System.out.println("Exception thrown -> false");
        } catch (IllegalArgumentException e) {
            System.out.println("Exception thrown -> true");
        }

        System.out.println("\n[testEquality_YardSameReference]");
        System.out.println("Quantity yard object equals itself -> " + y1.equals(y1));

        System.out.println("\n[testEquality_YardNullComparison]");
        System.out.println("Quantity yard object is not equal to null -> " + !y1.equals(null));

        System.out.println("\n[testEquality_CentimetersWithNullUnit]");
        try {
            new Quantity(1.0, null);
            System.out.println("Exception thrown -> false");
        } catch (IllegalArgumentException e) {
            System.out.println("Exception thrown -> true");
        }

        System.out.println("\n[testEquality_CentimetersSameReference]");
        System.out.println("Quantity cm object equals itself -> " + cm1.equals(cm1));

        System.out.println("\n[testEquality_CentimetersNullComparison]");
        System.out.println("Quantity cm object is not equal to null -> " + !cm1.equals(null));

        System.out.println("\n[testEquality_AllUnits_ComplexScenario]");
        Quantity y2Complex = new Quantity(2.0, LengthUnit.YARDS);
        Quantity f6Complex = new Quantity(6.0, LengthUnit.FEET);
        Quantity i72Complex = new Quantity(72.0, LengthUnit.INCHES);
        System.out.println("Quantity(2.0, YARDS) equals Quantity(6.0, FEET) equals Quantity(72.0, INCHES) -> " + (y2Complex.equals(f6Complex) && f6Complex.equals(i72Complex) && y2Complex.equals(i72Complex)));
    }
}
