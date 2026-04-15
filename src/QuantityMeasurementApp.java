public class QuantityMeasurementApp {

    // Inner class to represent a Feet measurement
    static class Feet {
        private final double value;

        public Feet(double value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (this.getClass() != obj.getClass()) return false;
            Feet other = (Feet) obj;
            return Double.compare(this.value, other.value) == 0;
        }
    }

    // Inner class to represent an Inches measurement
    static class Inches {
        private final double value;

        public Inches(double value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (this.getClass() != obj.getClass()) return false;
            Inches other = (Inches) obj;
            return Double.compare(this.value, other.value) == 0;
        }
    }

    // Static method for Feet equality check
    public static boolean checkFeetEquality(double val1, double val2) {
        Feet feet1 = new Feet(val1);
        Feet feet2 = new Feet(val2);
        return feet1.equals(feet2);
    }

    // Static method for Inches equality check
    public static boolean checkInchesEquality(double val1, double val2) {
        Inches inch1 = new Inches(val1);
        Inches inch2 = new Inches(val2);
        return inch1.equals(inch2);
    }

    public static void main(String[] args) {
        System.out.println("=== Quantity Measurement App ===");
        System.out.println("--- UC2: Feet and Inches Measurement Equality ---\n");

        // --- Feet Tests ---
        System.out.println("[ Feet Equality Tests ]");

        System.out.println("testEquality_SameValue       : 1.0 ft == 1.0 ft  -> " + checkFeetEquality(1.0, 1.0));
        System.out.println("testEquality_DifferentValue  : 1.0 ft == 2.0 ft  -> " + checkFeetEquality(1.0, 2.0));

        Feet feetObj = new Feet(1.0);
        System.out.println("testEquality_NullComparison  : 1.0 ft == null    -> " + feetObj.equals(null));
        System.out.println("testEquality_NonNumericInput : 1.0 ft == \"abc\"   -> " + feetObj.equals("abc"));
        System.out.println("testEquality_SameReference   : 1.0 ft == itself  -> " + feetObj.equals(feetObj));

        // --- Inches Tests ---
        System.out.println("\n[ Inches Equality Tests ]");

        System.out.println("testEquality_SameValue       : 1.0 in == 1.0 in  -> " + checkInchesEquality(1.0, 1.0));
        System.out.println("testEquality_DifferentValue  : 1.0 in == 2.0 in  -> " + checkInchesEquality(1.0, 2.0));

        Inches inchObj = new Inches(1.0);
        System.out.println("testEquality_NullComparison  : 1.0 in == null    -> " + inchObj.equals(null));
        System.out.println("testEquality_NonNumericInput : 1.0 in == \"abc\"   -> " + inchObj.equals("abc"));
        System.out.println("testEquality_SameReference   : 1.0 in == itself  -> " + inchObj.equals(inchObj));
    }
}
