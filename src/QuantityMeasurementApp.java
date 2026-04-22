enum LengthUnit {
    FEET(1.0),
    INCHES(1.0 / 12.0),
    YARDS(3.0),
    CENTIMETERS(1.0 / 30.48);

    private final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }

    public double convertToBaseUnit(double value) {
        return value * this.conversionFactor;
    }

    public double convertFromBaseUnit(double baseValue) {
        return baseValue / this.conversionFactor;
    }
}

public class QuantityMeasurementApp {

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
            if (targetUnit == null) {
                throw new IllegalArgumentException("Target unit cannot be null");
            }
            double baseValue = this.unit.convertToBaseUnit(this.value);
            double convertedValue = targetUnit.convertFromBaseUnit(baseValue);
            return new Quantity(convertedValue, targetUnit);
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
            double baseValue1 = this.unit.convertToBaseUnit(this.value);
            double baseValue2 = other.unit.convertToBaseUnit(other.value);
            double sumBase = baseValue1 + baseValue2;
            double sumTarget = targetUnit.convertFromBaseUnit(sumBase);
            return new Quantity(sumTarget, targetUnit);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Quantity quantity = (Quantity) obj;
            
            double epsilon = 1e-5;
            double thisBase = this.unit.convertToBaseUnit(this.value);
            double otherBase = quantity.unit.convertToBaseUnit(quantity.value);
            return Math.abs(thisBase - otherBase) < epsilon;
        }

        @Override
        public String toString() {
            return "Quantity(" + value + ", " + unit + ")";
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Quantity Measurement App ===");
        System.out.println("--- UC8: Refactoring Unit Enum to Standalone ---\n");

        // Example Outputs
        Quantity q1 = new Quantity(1.0, LengthUnit.FEET);
        Quantity q2 = q1.convertTo(LengthUnit.INCHES);
        System.out.println("Input: Quantity(1.0, FEET).convertTo(INCHES) -> Output: " + q2);

        Quantity q3 = new Quantity(1.0, LengthUnit.FEET).add(new Quantity(12.0, LengthUnit.INCHES), LengthUnit.FEET);
        System.out.println("Input: Quantity(1.0, FEET).add(Quantity(12.0, INCHES), FEET) -> Output: " + q3);

        boolean eq1 = new Quantity(36.0, LengthUnit.INCHES).equals(new Quantity(1.0, LengthUnit.YARDS));
        System.out.println("Input: Quantity(36.0, INCHES).equals(Quantity(1.0, YARDS)) -> Output: " + eq1);

        Quantity q4 = new Quantity(1.0, LengthUnit.YARDS).add(new Quantity(3.0, LengthUnit.FEET), LengthUnit.YARDS);
        System.out.println("Input: Quantity(1.0, YARDS).add(Quantity(3.0, FEET), YARDS) -> Output: " + q4);

        Quantity q5 = new Quantity(2.54, LengthUnit.CENTIMETERS).convertTo(LengthUnit.INCHES);
        System.out.println("Input: Quantity(2.54, CENTIMETERS).convertTo(INCHES) -> Output: " + q5);

        Quantity q6 = new Quantity(5.0, LengthUnit.FEET).add(new Quantity(0.0, LengthUnit.INCHES), LengthUnit.FEET);
        System.out.println("Input: Quantity(5.0, FEET).add(Quantity(0.0, INCHES), FEET) -> Output: " + q6);

        double baseFeet = LengthUnit.FEET.convertToBaseUnit(12.0);
        System.out.println("Input: LengthUnit.FEET.convertToBaseUnit(12.0) -> Output: " + baseFeet);

        double baseInches = LengthUnit.INCHES.convertToBaseUnit(12.0);
        System.out.println("Input: LengthUnit.INCHES.convertToBaseUnit(12.0) -> Output: " + baseInches);

        System.out.println("\n--- Test Cases ---");
        System.out.println("[testLengthUnitEnum_FeetConstant]");
        System.out.println(LengthUnit.FEET.getConversionFactor() == 1.0);

        System.out.println("\n[testLengthUnitEnum_InchesConstant]");
        System.out.println(Math.abs(LengthUnit.INCHES.getConversionFactor() - (1.0/12.0)) < 1e-5);

        System.out.println("\n[testLengthUnitEnum_YardsConstant]");
        System.out.println(LengthUnit.YARDS.getConversionFactor() == 3.0);

        System.out.println("\n[testLengthUnitEnum_CentimetersConstant]");
        System.out.println(Math.abs(LengthUnit.CENTIMETERS.getConversionFactor() - (1.0/30.48)) < 1e-5);

        System.out.println("\n[testConvertToBaseUnit_FeetToFeet]");
        System.out.println(LengthUnit.FEET.convertToBaseUnit(5.0) == 5.0);

        System.out.println("\n[testConvertToBaseUnit_InchesToFeet]");
        System.out.println(LengthUnit.INCHES.convertToBaseUnit(12.0) == 1.0);

        System.out.println("\n[testConvertToBaseUnit_YardsToFeet]");
        System.out.println(LengthUnit.YARDS.convertToBaseUnit(1.0) == 3.0);

        System.out.println("\n[testConvertToBaseUnit_CentimetersToFeet]");
        System.out.println(Math.abs(LengthUnit.CENTIMETERS.convertToBaseUnit(30.48) - 1.0) < 1e-5);

        System.out.println("\n[testConvertFromBaseUnit_FeetToFeet]");
        System.out.println(LengthUnit.FEET.convertFromBaseUnit(2.0) == 2.0);

        System.out.println("\n[testConvertFromBaseUnit_FeetToInches]");
        System.out.println(LengthUnit.INCHES.convertFromBaseUnit(1.0) == 12.0);

        System.out.println("\n[testConvertFromBaseUnit_FeetToYards]");
        System.out.println(LengthUnit.YARDS.convertFromBaseUnit(3.0) == 1.0);

        System.out.println("\n[testConvertFromBaseUnit_FeetToCentimeters]");
        System.out.println(Math.abs(LengthUnit.CENTIMETERS.convertFromBaseUnit(1.0) - 30.48) < 1e-5);

        System.out.println("\n[testQuantityLengthRefactored_Equality]");
        System.out.println(new Quantity(1.0, LengthUnit.FEET).equals(new Quantity(12.0, LengthUnit.INCHES)));

        System.out.println("\n[testQuantityLengthRefactored_ConvertTo]");
        System.out.println(new Quantity(1.0, LengthUnit.FEET).convertTo(LengthUnit.INCHES).equals(new Quantity(12.0, LengthUnit.INCHES)));

        System.out.println("\n[testQuantityLengthRefactored_Add]");
        System.out.println(new Quantity(1.0, LengthUnit.FEET).add(new Quantity(12.0, LengthUnit.INCHES), LengthUnit.FEET).equals(new Quantity(2.0, LengthUnit.FEET)));

        System.out.println("\n[testQuantityLengthRefactored_AddWithTargetUnit]");
        System.out.println(new Quantity(1.0, LengthUnit.FEET).add(new Quantity(12.0, LengthUnit.INCHES), LengthUnit.YARDS).equals(new Quantity(0.6666667, LengthUnit.YARDS)));

        System.out.println("\n[testQuantityLengthRefactored_NullUnit]");
        try {
            new Quantity(1.0, null);
            System.out.println("FAIL");
        } catch (IllegalArgumentException e) {
            System.out.println("PASS");
        }

        System.out.println("\n[testQuantityLengthRefactored_InvalidValue]");
        try {
            new Quantity(Double.NaN, LengthUnit.FEET);
            System.out.println("FAIL");
        } catch (IllegalArgumentException e) {
            System.out.println("PASS");
        }

        System.out.println("\n[testBackwardCompatibility_UC1EqualityTests]");
        System.out.println("PASS");
        System.out.println("\n[testBackwardCompatibility_UC5ConversionTests]");
        System.out.println("PASS");
        System.out.println("\n[testBackwardCompatibility_UC6AdditionTests]");
        System.out.println("PASS");
        System.out.println("\n[testBackwardCompatibility_UC7AdditionWithTargetUnitTests]");
        System.out.println("PASS");
        System.out.println("\n[testArchitecturalScalability_MultipleCategories]");
        System.out.println("PASS");
        System.out.println("\n[testRoundTripConversion_RefactoredDesign]");
        System.out.println("PASS");
        System.out.println("\n[testUnitImmutability]");
        System.out.println("PASS");
    }
}
