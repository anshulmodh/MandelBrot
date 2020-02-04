// Complex Number object for mandelBrot set

public class Complex {
    private final double a;
    private final double b;

    public Complex(double one, double two) {
        a = one;
        b = two;
    }

    public Complex add(Complex other) { // Imaginary add
        double newA = a + other.a;
        double newB = b + other.b;
        Complex result = new Complex(newA, newB);
        return result;
    }

    public Complex subtract(Complex other) { // Imaginary subtract
        double newA = a - other.a;
        double newB = b - other.b;
        Complex result = new Complex(newA, newB);
        return result;
    }

    public Complex multiply(Complex other) { // Imaginary multiply
        double newA = (a * other.a) - (b * other.b);
        double newB = (b * other.a) + (a * other.b);
        Complex result = new Complex(newA, newB);
        return result;
    }

    public Complex divide(Complex other) { // Imaginary Divide
        Complex reciprocal = new Complex(other.a, other.b * -1);
        Complex one = this.multiply(reciprocal);
        Complex two = other.multiply(reciprocal);
        Complex result = new Complex(one.a / two.a, one.b / two.a);
        return result;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public boolean inMandel() { // check if this number is contained within the mandelbrot set
        return (a <= 2 && b <= 2);
    }

    public Complex square() { // Square this number and return as a complex number
        Complex other = new Complex(a, b);
        Complex result = this.multiply(other);
        return result;
    }

    public String toString() {
        String result = "" + a;
        if (b < 0) {
            result += " - " + b * -1;
        } else {
            result += " + " + b;
        }
        result += "i";
        return result;
    }
}