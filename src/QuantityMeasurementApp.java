enum LengthUnit {
    FEET(1.0),
    INCHES(1.0 / 12.0),
    YARDS(3.0),
    CENTIMETERS(1.0 / 30.48);

    private final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double getConversionFactor() { return conversionFactor; }
    public double convertToBaseUnit(double value) { return value * this.conversionFactor; }
    public double convertFromBaseUnit(double baseValue) { return baseValue / this.conversionFactor; }
}

enum WeightUnit {
    KILOGRAM(1.0),
    GRAM(0.001),
    POUND(0.453592);

    private final double conversionFactor;

    WeightUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double getConversionFactor() { return conversionFactor; }
    public double convertToBaseUnit(double value) { return value * this.conversionFactor; }
    public double convertFromBaseUnit(double baseValue) { return baseValue / this.conversionFactor; }
}

public class QuantityMeasurementApp {

    public static class QuantityLength {
        private final double value;
        private final LengthUnit unit;

        public QuantityLength(double value, LengthUnit unit) {
            if (!Double.isFinite(value)) throw new IllegalArgumentException("Value must be a finite number");
            if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
            this.value = value;
            this.unit = unit;
        }

        public double getValue() { return value; }
        public LengthUnit getUnit() { return unit; }

        public QuantityLength convertTo(LengthUnit targetUnit) {
            if (targetUnit == null) throw new IllegalArgumentException("Target unit cannot be null");
            double baseValue = this.unit.convertToBaseUnit(this.value);
            double convertedValue = targetUnit.convertFromBaseUnit(baseValue);
            return new QuantityLength(convertedValue, targetUnit);
        }

        public QuantityLength add(QuantityLength other) {
            return add(other, this.unit);
        }

        public QuantityLength add(QuantityLength other, LengthUnit targetUnit) {
            if (other == null) throw new IllegalArgumentException("Cannot add a null quantity");
            if (targetUnit == null) throw new IllegalArgumentException("Target unit cannot be null");
            double baseValue1 = this.unit.convertToBaseUnit(this.value);
            double baseValue2 = other.unit.convertToBaseUnit(other.value);
            double sumBase = baseValue1 + baseValue2;
            double sumTarget = targetUnit.convertFromBaseUnit(sumBase);
            return new QuantityLength(sumTarget, targetUnit);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            QuantityLength other = (QuantityLength) obj;
            double epsilon = 1e-5;
            double thisBase = this.unit.convertToBaseUnit(this.value);
            double otherBase = other.unit.convertToBaseUnit(other.value);
            return Math.abs(thisBase - otherBase) < epsilon;
        }

        @Override
        public String toString() {
            return "Quantity(" + value + ", " + unit + ")";
        }
    }

    public static class QuantityWeight {
        private final double value;
        private final WeightUnit unit;

        public QuantityWeight(double value, WeightUnit unit) {
            if (!Double.isFinite(value)) throw new IllegalArgumentException("Value must be a finite number");
            if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
            this.value = value;
            this.unit = unit;
        }

        public double getValue() { return value; }
        public WeightUnit getUnit() { return unit; }

        public QuantityWeight convertTo(WeightUnit targetUnit) {
            if (targetUnit == null) throw new IllegalArgumentException("Target unit cannot be null");
            double baseValue = this.unit.convertToBaseUnit(this.value);
            double convertedValue = targetUnit.convertFromBaseUnit(baseValue);
            return new QuantityWeight(convertedValue, targetUnit);
        }

        public QuantityWeight add(QuantityWeight other) {
            return add(other, this.unit);
        }

        public QuantityWeight add(QuantityWeight other, WeightUnit targetUnit) {
            if (other == null) throw new IllegalArgumentException("Cannot add a null quantity");
            if (targetUnit == null) throw new IllegalArgumentException("Target unit cannot be null");
            double baseValue1 = this.unit.convertToBaseUnit(this.value);
            double baseValue2 = other.unit.convertToBaseUnit(other.value);
            double sumBase = baseValue1 + baseValue2;
            double sumTarget = targetUnit.convertFromBaseUnit(sumBase);
            return new QuantityWeight(sumTarget, targetUnit);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            QuantityWeight other = (QuantityWeight) obj;
            double epsilon = 1e-5;
            double thisBase = this.unit.convertToBaseUnit(this.value);
            double otherBase = other.unit.convertToBaseUnit(other.value);
            return Math.abs(thisBase - otherBase) < epsilon;
        }

        @Override
        public String toString() {
            return "Quantity(" + value + ", " + unit + ")";
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Quantity Measurement App ===");
        System.out.println("--- UC9: Weight Measurement ---\n");

        System.out.println("Equality Comparisons:\n");
        System.out.println("Input: Quantity(1.0, KILOGRAM).equals(Quantity(1.0, KILOGRAM)) -> Output: " + new QuantityWeight(1.0, WeightUnit.KILOGRAM).equals(new QuantityWeight(1.0, WeightUnit.KILOGRAM)));
        System.out.println("Input: Quantity(1.0, KILOGRAM).equals(Quantity(1000.0, GRAM)) -> Output: " + new QuantityWeight(1.0, WeightUnit.KILOGRAM).equals(new QuantityWeight(1000.0, WeightUnit.GRAM)));
        System.out.println("Input: Quantity(2.0, POUND).equals(Quantity(2.0, POUND)) -> Output: " + new QuantityWeight(2.0, WeightUnit.POUND).equals(new QuantityWeight(2.0, WeightUnit.POUND)));
        System.out.println("Input: Quantity(1.0, KILOGRAM).equals(Quantity(~2.20462, POUND)) -> Output: " + new QuantityWeight(1.0, WeightUnit.KILOGRAM).equals(new QuantityWeight(2.20462, WeightUnit.POUND)));
        System.out.println("Input: Quantity(500.0, GRAM).equals(Quantity(0.5, KILOGRAM)) -> Output: " + new QuantityWeight(500.0, WeightUnit.GRAM).equals(new QuantityWeight(0.5, WeightUnit.KILOGRAM)));
        System.out.println("Input: Quantity(1.0, POUND).equals(Quantity(~453.592, GRAM)) -> Output: " + new QuantityWeight(1.0, WeightUnit.POUND).equals(new QuantityWeight(453.592, WeightUnit.GRAM)));

        System.out.println("\nUnit Conversions:\n");
        System.out.println("Input: Quantity(1.0, KILOGRAM).convertTo(GRAM) -> Output: " + new QuantityWeight(1.0, WeightUnit.KILOGRAM).convertTo(WeightUnit.GRAM));
        System.out.println("Input: Quantity(2.0, POUND).convertTo(KILOGRAM) -> Output: " + new QuantityWeight(2.0, WeightUnit.POUND).convertTo(WeightUnit.KILOGRAM));
        System.out.println("Input: Quantity(500.0, GRAM).convertTo(POUND) -> Output: " + new QuantityWeight(500.0, WeightUnit.GRAM).convertTo(WeightUnit.POUND));
        System.out.println("Input: Quantity(0.0, KILOGRAM).convertTo(GRAM) -> Output: " + new QuantityWeight(0.0, WeightUnit.KILOGRAM).convertTo(WeightUnit.GRAM));

        System.out.println("\nAddition Operations (Implicit Target Unit):\n");
        System.out.println("Input: Quantity(1.0, KILOGRAM).add(Quantity(2.0, KILOGRAM)) -> Output: " + new QuantityWeight(1.0, WeightUnit.KILOGRAM).add(new QuantityWeight(2.0, WeightUnit.KILOGRAM)));
        System.out.println("Input: Quantity(1.0, KILOGRAM).add(Quantity(1000.0, GRAM)) -> Output: " + new QuantityWeight(1.0, WeightUnit.KILOGRAM).add(new QuantityWeight(1000.0, WeightUnit.GRAM)));
        System.out.println("Input: Quantity(500.0, GRAM).add(Quantity(0.5, KILOGRAM)) -> Output: " + new QuantityWeight(500.0, WeightUnit.GRAM).add(new QuantityWeight(0.5, WeightUnit.KILOGRAM)));

        System.out.println("\nAddition Operations (Explicit Target Unit):\n");
        System.out.println("Input: Quantity(1.0, KILOGRAM).add(Quantity(1000.0, GRAM), GRAM) -> Output: " + new QuantityWeight(1.0, WeightUnit.KILOGRAM).add(new QuantityWeight(1000.0, WeightUnit.GRAM), WeightUnit.GRAM));
        System.out.println("Input: Quantity(1.0, POUND).add(Quantity(453.592, GRAM), POUND) -> Output: " + new QuantityWeight(1.0, WeightUnit.POUND).add(new QuantityWeight(453.592, WeightUnit.GRAM), WeightUnit.POUND));
        System.out.println("Input: Quantity(2.0, KILOGRAM).add(Quantity(4.0, POUND), KILOGRAM) -> Output: " + new QuantityWeight(2.0, WeightUnit.KILOGRAM).add(new QuantityWeight(4.0, WeightUnit.POUND), WeightUnit.KILOGRAM));

        System.out.println("\nCategory Incompatibility:\n");
        System.out.println("Input: Quantity(1.0, KILOGRAM).equals(Quantity(1.0, FOOT)) -> Output: " + new QuantityWeight(1.0, WeightUnit.KILOGRAM).equals(new QuantityLength(1.0, LengthUnit.FEET)));

        System.out.println("\n--- Test Cases ---");
        System.out.println("[testEquality_KilogramToKilogram_SameValue]");
        System.out.println(new QuantityWeight(1.0, WeightUnit.KILOGRAM).equals(new QuantityWeight(1.0, WeightUnit.KILOGRAM)));
        
        System.out.println("\n[testEquality_KilogramToKilogram_DifferentValue]");
        System.out.println(!new QuantityWeight(1.0, WeightUnit.KILOGRAM).equals(new QuantityWeight(2.0, WeightUnit.KILOGRAM)));
        
        System.out.println("\n[testEquality_KilogramToGram_EquivalentValue]");
        System.out.println(new QuantityWeight(1.0, WeightUnit.KILOGRAM).equals(new QuantityWeight(1000.0, WeightUnit.GRAM)));

        System.out.println("\n[testEquality_GramToKilogram_EquivalentValue]");
        System.out.println(new QuantityWeight(1000.0, WeightUnit.GRAM).equals(new QuantityWeight(1.0, WeightUnit.KILOGRAM)));

        System.out.println("\n[testEquality_WeightVsLength_Incompatible]");
        System.out.println(!new QuantityWeight(1.0, WeightUnit.KILOGRAM).equals(new QuantityLength(1.0, LengthUnit.FEET)));

        System.out.println("\n[testEquality_NullComparison]");
        System.out.println(!new QuantityWeight(1.0, WeightUnit.KILOGRAM).equals(null));

        System.out.println("\n[testEquality_SameReference]");
        QuantityWeight q = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        System.out.println(q.equals(q));

        System.out.println("\n[testEquality_NullUnit]");
        try {
            new QuantityWeight(1.0, null);
            System.out.println("FAIL");
        } catch(IllegalArgumentException e) {
            System.out.println("PASS");
        }

        System.out.println("\n[testEquality_TransitiveProperty]");
        boolean aEqb = new QuantityWeight(1.0, WeightUnit.KILOGRAM).equals(new QuantityWeight(1000.0, WeightUnit.GRAM));
        boolean bEqc = new QuantityWeight(1000.0, WeightUnit.GRAM).equals(new QuantityWeight(1.0, WeightUnit.KILOGRAM));
        boolean aEqc = new QuantityWeight(1.0, WeightUnit.KILOGRAM).equals(new QuantityWeight(1.0, WeightUnit.KILOGRAM));
        System.out.println(aEqb && bEqc && aEqc);

        System.out.println("\n[testEquality_ZeroValue]");
        System.out.println(new QuantityWeight(0.0, WeightUnit.KILOGRAM).equals(new QuantityWeight(0.0, WeightUnit.GRAM)));

        System.out.println("\n[testEquality_NegativeWeight]");
        System.out.println(new QuantityWeight(-1.0, WeightUnit.KILOGRAM).equals(new QuantityWeight(-1000.0, WeightUnit.GRAM)));

        System.out.println("\n[testEquality_LargeWeightValue]");
        System.out.println(new QuantityWeight(1000000.0, WeightUnit.GRAM).equals(new QuantityWeight(1000.0, WeightUnit.KILOGRAM)));

        System.out.println("\n[testEquality_SmallWeightValue]");
        System.out.println(new QuantityWeight(0.001, WeightUnit.KILOGRAM).equals(new QuantityWeight(1.0, WeightUnit.GRAM)));

        System.out.println("\n[testConversion_PoundToKilogram]");
        System.out.println(new QuantityWeight(2.20462, WeightUnit.POUND).convertTo(WeightUnit.KILOGRAM).equals(new QuantityWeight(1.0, WeightUnit.KILOGRAM)));

        System.out.println("\n[testConversion_KilogramToPound]");
        System.out.println(new QuantityWeight(1.0, WeightUnit.KILOGRAM).convertTo(WeightUnit.POUND).equals(new QuantityWeight(2.20462, WeightUnit.POUND)));

        System.out.println("\n[testConversion_SameUnit]");
        System.out.println(new QuantityWeight(5.0, WeightUnit.KILOGRAM).convertTo(WeightUnit.KILOGRAM).equals(new QuantityWeight(5.0, WeightUnit.KILOGRAM)));

        System.out.println("\n[testConversion_ZeroValue]");
        System.out.println(new QuantityWeight(0.0, WeightUnit.KILOGRAM).convertTo(WeightUnit.GRAM).equals(new QuantityWeight(0.0, WeightUnit.GRAM)));

        System.out.println("\n[testConversion_NegativeValue]");
        System.out.println(new QuantityWeight(-1.0, WeightUnit.KILOGRAM).convertTo(WeightUnit.GRAM).equals(new QuantityWeight(-1000.0, WeightUnit.GRAM)));

        System.out.println("\n[testConversion_RoundTrip]");
        System.out.println(new QuantityWeight(1.5, WeightUnit.KILOGRAM).convertTo(WeightUnit.GRAM).convertTo(WeightUnit.KILOGRAM).equals(new QuantityWeight(1.5, WeightUnit.KILOGRAM)));

        System.out.println("\n[testAddition_SameUnit_KilogramPlusKilogram]");
        System.out.println(new QuantityWeight(1.0, WeightUnit.KILOGRAM).add(new QuantityWeight(2.0, WeightUnit.KILOGRAM)).equals(new QuantityWeight(3.0, WeightUnit.KILOGRAM)));

        System.out.println("\n[testAddition_CrossUnit_KilogramPlusGram]");
        System.out.println(new QuantityWeight(1.0, WeightUnit.KILOGRAM).add(new QuantityWeight(1000.0, WeightUnit.GRAM)).equals(new QuantityWeight(2.0, WeightUnit.KILOGRAM)));

        System.out.println("\n[testAddition_CrossUnit_PoundPlusKilogram]");
        System.out.println(new QuantityWeight(2.20462, WeightUnit.POUND).add(new QuantityWeight(1.0, WeightUnit.KILOGRAM)).equals(new QuantityWeight(4.40924, WeightUnit.POUND)));

        System.out.println("\n[testAddition_ExplicitTargetUnit_Kilogram]");
        System.out.println(new QuantityWeight(1.0, WeightUnit.KILOGRAM).add(new QuantityWeight(1000.0, WeightUnit.GRAM), WeightUnit.GRAM).equals(new QuantityWeight(2000.0, WeightUnit.GRAM)));

        System.out.println("\n[testAddition_Commutativity]");
        QuantityWeight add1 = new QuantityWeight(1.0, WeightUnit.KILOGRAM).add(new QuantityWeight(1000.0, WeightUnit.GRAM));
        QuantityWeight add2 = new QuantityWeight(1000.0, WeightUnit.GRAM).add(new QuantityWeight(1.0, WeightUnit.KILOGRAM));
        System.out.println(add1.equals(add2));

        System.out.println("\n[testAddition_WithZero]");
        System.out.println(new QuantityWeight(5.0, WeightUnit.KILOGRAM).add(new QuantityWeight(0.0, WeightUnit.GRAM)).equals(new QuantityWeight(5.0, WeightUnit.KILOGRAM)));

        System.out.println("\n[testAddition_NegativeValues]");
        System.out.println(new QuantityWeight(5.0, WeightUnit.KILOGRAM).add(new QuantityWeight(-2000.0, WeightUnit.GRAM)).equals(new QuantityWeight(3.0, WeightUnit.KILOGRAM)));

        System.out.println("\n[testAddition_LargeValues]");
        System.out.println(new QuantityWeight(1e6, WeightUnit.KILOGRAM).add(new QuantityWeight(1e6, WeightUnit.KILOGRAM)).equals(new QuantityWeight(2e6, WeightUnit.KILOGRAM)));
    }
}
