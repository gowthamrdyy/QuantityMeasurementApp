public class QuantityMeasurementApp {

    // Inner class to represent a Feet measurement
    static class Feet {
        private final double value;

        public Feet(double value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            // Reflexive: same reference
            if (this == obj) return true;
            // Null check
            if (obj == null) return false;
            // Type check
            if (this.getClass() != obj.getClass()) return false;
            // Cast and compare using Double.compare() for floating-point safety
            Feet other = (Feet) obj;
            return Double.compare(this.value, other.value) == 0;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Quantity Measurement App ===");
        System.out.println("--- UC1: Feet Measurement Equality ---\n");

        // testEquality_SameValue
        Feet feet1 = new Feet(1.0);
        Feet feet2 = new Feet(1.0);
        System.out.println("[testEquality_SameValue]");
        System.out.println("1.0 ft equals 1.0 ft: " + feet1.equals(feet2));

        // testEquality_DifferentValue
        Feet feet3 = new Feet(2.0);
        System.out.println("\n[testEquality_DifferentValue]");
        System.out.println("1.0 ft equals 2.0 ft: " + feet1.equals(feet3));

        // testEquality_NullComparison
        System.out.println("\n[testEquality_NullComparison]");
        System.out.println("1.0 ft equals null: " + feet1.equals(null));

        // testEquality_NonNumericInput
        System.out.println("\n[testEquality_NonNumericInput]");
        System.out.println("1.0 ft equals \"abc\" (String): " + feet1.equals("abc"));

        // testEquality_SameReference
        System.out.println("\n[testEquality_SameReference]");
        System.out.println("1.0 ft equals itself (same reference): " + feet1.equals(feet1));
    }
}
