public class Polynomial {
	double[] coefficients;
	public Polynomial() {
		coefficients = new double[1];
	}
	public Polynomial(double[] c) { 
		coefficients = new double[c.length];
		for (int i = 0; i < c.length; i++) {
			coefficients[i]=c[i];
		}
	}
	public Polynomial add(Polynomial p) {
		double[] c = new double[Math.max(p.coefficients.length, coefficients.length)];
		double x, y;
		for (int i = 0; i < Math.max(p.coefficients.length, coefficients.length); i++) {
			if (i < p.coefficients.length) {
				x = p.coefficients[i];
			} else {
				x = 0;
			}
			if (i < coefficients.length) {
				y = coefficients[i];
			} else {
				y = 0;
			}
			c[i] = x + y; 
		}
		Polynomial new_p = new Polynomial(c);
		return new_p;
	}
	public double evaluate(double x) {
		double res = 0;
		for (int i = 0; i < coefficients.length; i++) {
			res += coefficients[i]*(Math.pow(x, i));
		}
		return res;
	}
	public boolean hasRoot(double x) {
		return evaluate(x) == 0;
	}
}