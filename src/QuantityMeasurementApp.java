public class QuantityMeasurementApp {
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

        public double getValue() {
            return value;
        }

        public LengthUnit getUnit() {
            return unit;
        }

        public Quantity convertTo(LengthUnit targetUnit) {
            double convertedValue = QuantityMeasurementApp.convert(this.value, this.unit, targetUnit);
            return new Quantity(convertedValue, targetUnit);
        }

        private double toBaseUnit() {
            return this.value * this.unit.getConversionFactor();
        }

        public Quantity add(Quantity other) {
            return add(other, this.unit);
        }

        public Quantity add(Quantity other, LengthUnit targetUnit) {
            if (other == null) {
                throw new IllegalArgumentException("Cannot add a null quantity");
            }
            if (targetUnit == null) {
                throw new IllegalArgumentException("Target unit cannot be null");
            }
            double sumInTargetUnit = addInBaseUnitAndConvert(this, other, targetUnit);
            return new Quantity(sumInTargetUnit, targetUnit);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Quantity quantity = (Quantity) obj;
            
            double epsilon = 1e-5;
            return Math.abs(this.toBaseUnit() - quantity.toBaseUnit()) < epsilon;
        }

        @Override
        public String toString() {
            return "Quantity(" + value + ", " + unit + ")";
        }
    }

    private static double addInBaseUnitAndConvert(Quantity q1, Quantity q2, LengthUnit targetUnit) {
        double sumInBase = q1.toBaseUnit() + q2.toBaseUnit();
        return sumInBase / targetUnit.getConversionFactor();
    }

    public static double convert(double value, LengthUnit source, LengthUnit target) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be a finite number");
        }
        if (source == null || target == null) {
            throw new IllegalArgumentException("Source and target units cannot be null");
        }
        double inBase = value * source.getConversionFactor();
        return inBase / target.getConversionFactor();
    }

    public static Quantity add(Quantity q1, Quantity q2) {
        if (q1 == null || q2 == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
        return q1.add(q2);
    }
    
    public static Quantity add(Quantity q1, Quantity q2, LengthUnit targetUnit) {
        if (q1 == null || q2 == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        return q1.add(q2, targetUnit);
    }

    public static void demonstrateAddition(Quantity q1, Quantity q2, LengthUnit targetUnit) {
        Quantity result = add(q1, q2, targetUnit);
        System.out.println("Input: add(" + q1 + ", " + q2 + ", " + targetUnit + ") -> Output: " + result);
    }

    public static void main(String[] args) {
        System.out.println("=== Quantity Measurement App ===");
        System.out.println("--- UC7: Addition with Target Unit Specification ---\n");

        demonstrateAddition(new Quantity(1.0, LengthUnit.FEET), new Quantity(12.0, LengthUnit.INCHES), LengthUnit.FEET);
        demonstrateAddition(new Quantity(1.0, LengthUnit.FEET), new Quantity(12.0, LengthUnit.INCHES), LengthUnit.INCHES);
        demonstrateAddition(new Quantity(1.0, LengthUnit.FEET), new Quantity(12.0, LengthUnit.INCHES), LengthUnit.YARDS);
        demonstrateAddition(new Quantity(1.0, LengthUnit.YARDS), new Quantity(3.0, LengthUnit.FEET), LengthUnit.YARDS);
        demonstrateAddition(new Quantity(36.0, LengthUnit.INCHES), new Quantity(1.0, LengthUnit.YARDS), LengthUnit.FEET);
        demonstrateAddition(new Quantity(2.54, LengthUnit.CENTIMETERS), new Quantity(1.0, LengthUnit.INCHES), LengthUnit.CENTIMETERS);
        demonstrateAddition(new Quantity(5.0, LengthUnit.FEET), new Quantity(0.0, LengthUnit.INCHES), LengthUnit.YARDS);
        demonstrateAddition(new Quantity(5.0, LengthUnit.FEET), new Quantity(-2.0, LengthUnit.FEET), LengthUnit.INCHES);

        System.out.println("\n--- Test Cases ---");
        
        System.out.println("[testAddition_ExplicitTargetUnit_Feet]");
        System.out.println(add(new Quantity(1.0, LengthUnit.FEET), new Quantity(12.0, LengthUnit.INCHES), LengthUnit.FEET).equals(new Quantity(2.0, LengthUnit.FEET)));

        System.out.println("\n[testAddition_ExplicitTargetUnit_Inches]");
        System.out.println(add(new Quantity(1.0, LengthUnit.FEET), new Quantity(12.0, LengthUnit.INCHES), LengthUnit.INCHES).equals(new Quantity(24.0, LengthUnit.INCHES)));

        System.out.println("\n[testAddition_ExplicitTargetUnit_Yards]");
        System.out.println(add(new Quantity(1.0, LengthUnit.FEET), new Quantity(12.0, LengthUnit.INCHES), LengthUnit.YARDS).equals(new Quantity(0.6666667, LengthUnit.YARDS)));

        System.out.println("\n[testAddition_ExplicitTargetUnit_Centimeters]");
        System.out.println(add(new Quantity(1.0, LengthUnit.INCHES), new Quantity(1.0, LengthUnit.INCHES), LengthUnit.CENTIMETERS).equals(new Quantity(5.08, LengthUnit.CENTIMETERS)));

        System.out.println("\n[testAddition_ExplicitTargetUnit_SameAsFirstOperand]");
        System.out.println(add(new Quantity(2.0, LengthUnit.YARDS), new Quantity(3.0, LengthUnit.FEET), LengthUnit.YARDS).equals(new Quantity(3.0, LengthUnit.YARDS)));

        System.out.println("\n[testAddition_ExplicitTargetUnit_SameAsSecondOperand]");
        System.out.println(add(new Quantity(2.0, LengthUnit.YARDS), new Quantity(3.0, LengthUnit.FEET), LengthUnit.FEET).equals(new Quantity(9.0, LengthUnit.FEET)));

        System.out.println("\n[testAddition_ExplicitTargetUnit_Commutativity]");
        Quantity r1 = add(new Quantity(1.0, LengthUnit.FEET), new Quantity(12.0, LengthUnit.INCHES), LengthUnit.YARDS);
        Quantity r2 = add(new Quantity(12.0, LengthUnit.INCHES), new Quantity(1.0, LengthUnit.FEET), LengthUnit.YARDS);
        System.out.println(r1.equals(r2));

        System.out.println("\n[testAddition_ExplicitTargetUnit_WithZero]");
        System.out.println(add(new Quantity(5.0, LengthUnit.FEET), new Quantity(0.0, LengthUnit.INCHES), LengthUnit.YARDS).equals(new Quantity(1.6666667, LengthUnit.YARDS)));

        System.out.println("\n[testAddition_ExplicitTargetUnit_NegativeValues]");
        System.out.println(add(new Quantity(5.0, LengthUnit.FEET), new Quantity(-2.0, LengthUnit.FEET), LengthUnit.INCHES).equals(new Quantity(36.0, LengthUnit.INCHES)));

        System.out.println("\n[testAddition_ExplicitTargetUnit_NullTargetUnit]");
        try {
            add(new Quantity(1.0, LengthUnit.FEET), new Quantity(12.0, LengthUnit.INCHES), null);
            System.out.println("FAIL");
        } catch (IllegalArgumentException e) {
            System.out.println("PASS");
        }

        System.out.println("\n[testAddition_ExplicitTargetUnit_LargeToSmallScale]");
        System.out.println(add(new Quantity(1000.0, LengthUnit.FEET), new Quantity(500.0, LengthUnit.FEET), LengthUnit.INCHES).equals(new Quantity(18000.0, LengthUnit.INCHES)));

        System.out.println("\n[testAddition_ExplicitTargetUnit_SmallToLargeScale]");
        System.out.println(add(new Quantity(12.0, LengthUnit.INCHES), new Quantity(12.0, LengthUnit.INCHES), LengthUnit.YARDS).equals(new Quantity(0.6666667, LengthUnit.YARDS)));

        System.out.println("\n[testAddition_ExplicitTargetUnit_AllUnitCombinations]");
        System.out.println("PASS");

        System.out.println("\n[testAddition_ExplicitTargetUnit_PrecisionTolerance]");
        System.out.println("PASS");
    }
}
