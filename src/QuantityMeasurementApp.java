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
            if (other == null) {
                throw new IllegalArgumentException("Cannot add a null quantity");
            }
            // Add in base unit then convert back to this unit
            double sumInBase = this.toBaseUnit() + other.toBaseUnit();
            double sumInThisUnit = sumInBase / this.unit.getConversionFactor();
            return new Quantity(sumInThisUnit, this.unit);
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
        if (q1 == null || q2 == null || targetUnit == null) {
             throw new IllegalArgumentException("Arguments cannot be null");
        }
        double sumInBase = q1.toBaseUnit() + q2.toBaseUnit();
        double sumInTargetUnit = sumInBase / targetUnit.getConversionFactor();
        return new Quantity(sumInTargetUnit, targetUnit);
    }

    public static void demonstrateAddition(Quantity q1, Quantity q2) {
        Quantity result = add(q1, q2);
        System.out.println("Input: add(" + q1 + ", " + q2 + ")");
        System.out.println("Output: " + result + "\n");
    }

    public static void main(String[] args) {
        System.out.println("=== Quantity Measurement App ===");
        System.out.println("--- UC6: Addition of Two Length Units ---\n");

        // Example Outputs
        demonstrateAddition(new Quantity(1.0, LengthUnit.FEET), new Quantity(2.0, LengthUnit.FEET));
        demonstrateAddition(new Quantity(1.0, LengthUnit.FEET), new Quantity(12.0, LengthUnit.INCHES));
        demonstrateAddition(new Quantity(12.0, LengthUnit.INCHES), new Quantity(1.0, LengthUnit.FEET));
        demonstrateAddition(new Quantity(1.0, LengthUnit.YARDS), new Quantity(3.0, LengthUnit.FEET));
        demonstrateAddition(new Quantity(36.0, LengthUnit.INCHES), new Quantity(1.0, LengthUnit.YARDS));
        demonstrateAddition(new Quantity(2.54, LengthUnit.CENTIMETERS), new Quantity(1.0, LengthUnit.INCHES));
        demonstrateAddition(new Quantity(5.0, LengthUnit.FEET), new Quantity(0.0, LengthUnit.INCHES));
        demonstrateAddition(new Quantity(5.0, LengthUnit.FEET), new Quantity(-2.0, LengthUnit.FEET));

        System.out.println("--- Test Cases ---");

        System.out.println("[testAddition_SameUnit_FeetPlusFeet]");
        System.out.println(add(new Quantity(1.0, LengthUnit.FEET), new Quantity(2.0, LengthUnit.FEET)).equals(new Quantity(3.0, LengthUnit.FEET)));

        System.out.println("\n[testAddition_SameUnit_InchPlusInch]");
        System.out.println(add(new Quantity(6.0, LengthUnit.INCHES), new Quantity(6.0, LengthUnit.INCHES)).equals(new Quantity(12.0, LengthUnit.INCHES)));

        System.out.println("\n[testAddition_CrossUnit_FeetPlusInches]");
        System.out.println(add(new Quantity(1.0, LengthUnit.FEET), new Quantity(12.0, LengthUnit.INCHES)).equals(new Quantity(2.0, LengthUnit.FEET)));

        System.out.println("\n[testAddition_CrossUnit_InchPlusFeet]");
        System.out.println(add(new Quantity(12.0, LengthUnit.INCHES), new Quantity(1.0, LengthUnit.FEET)).equals(new Quantity(24.0, LengthUnit.INCHES)));

        System.out.println("\n[testAddition_CrossUnit_YardPlusFeet]");
        System.out.println(add(new Quantity(1.0, LengthUnit.YARDS), new Quantity(3.0, LengthUnit.FEET)).equals(new Quantity(2.0, LengthUnit.YARDS)));

        System.out.println("\n[testAddition_CrossUnit_CentimeterPlusInch]");
        System.out.println(add(new Quantity(2.54, LengthUnit.CENTIMETERS), new Quantity(1.0, LengthUnit.INCHES)).equals(new Quantity(5.08, LengthUnit.CENTIMETERS)));

        System.out.println("\n[testAddition_Commutativity]");
        Quantity r1 = add(new Quantity(1.0, LengthUnit.FEET), new Quantity(12.0, LengthUnit.INCHES));
        Quantity r2 = add(new Quantity(12.0, LengthUnit.INCHES), new Quantity(1.0, LengthUnit.FEET));
        System.out.println("Commutative equality: " + r1.equals(r2)); // Note: equals compares the normalized base units!

        System.out.println("\n[testAddition_WithZero]");
        System.out.println(add(new Quantity(5.0, LengthUnit.FEET), new Quantity(0.0, LengthUnit.INCHES)).equals(new Quantity(5.0, LengthUnit.FEET)));

        System.out.println("\n[testAddition_NegativeValues]");
        System.out.println(add(new Quantity(5.0, LengthUnit.FEET), new Quantity(-2.0, LengthUnit.FEET)).equals(new Quantity(3.0, LengthUnit.FEET)));

        System.out.println("\n[testAddition_NullSecondOperand]");
        try {
            add(new Quantity(1.0, LengthUnit.FEET), null);
            System.out.println("FAIL");
        } catch (IllegalArgumentException e) {
            System.out.println("PASS");
        }

        System.out.println("\n[testAddition_LargeValues]");
        System.out.println(add(new Quantity(1e6, LengthUnit.FEET), new Quantity(1e6, LengthUnit.FEET)).equals(new Quantity(2e6, LengthUnit.FEET)));

        System.out.println("\n[testAddition_SmallValues]");
        System.out.println(add(new Quantity(0.001, LengthUnit.FEET), new Quantity(0.002, LengthUnit.FEET)).equals(new Quantity(0.003, LengthUnit.FEET)));
    }
}
