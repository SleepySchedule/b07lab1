import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public class Polynomial {
	double[] coefficients;
	int[] exponents;
	public Polynomial() {
		// Create the 0 polynomial (empty arrays because only zero coefficients)
		coefficients = new double[0];
		exponents = new int[0];
	}
	public Polynomial(double[] c, int[] e) {
		// Create a polynomial
		coefficients = c;
		exponents = e;
	}
	public Polynomial(File path) throws FileNotFoundException {
		// file contains one line, no whitespace
		Scanner f = new Scanner(path);
		String s = f.nextLine();
		int added_chars = 0;
		for (int i = 0; i + added_chars < s.length(); i++) {
			if (s.charAt(i+added_chars) == '-') {
				s = s.substring(0, i+added_chars) + "+" + s.substring(i+added_chars);
				added_chars++;
			}
		}
		String[] terms = s.split("[+]");
		coefficients = new double[terms.length];
		exponents = new int[terms.length];
		for (int i = 0; i < terms.length; i++) {
			String[] term = terms[i].split("x");
			if (term.length == 1) {
				exponents[i] = 0;
			} else {
				exponents[i] = Integer.parseInt(term[1]);
			}
			coefficients[i] = Double.parseDouble(term[0]);
		}
		f.close();
	}
	public void saveToFile(String s) throws FileNotFoundException {
		// save polynomial to file s
		PrintStream f = new PrintStream(s);
		String poly = polyToString();
		f.print(poly);
		f.close();
	}
	public Polynomial add(Polynomial p) {
		// add polynomial p with self
		int p_deg = p.getDegree();
		int deg = this.getDegree();
		int high_deg = Math.max(p_deg, deg);
		double[] c = new double[high_deg + 1];

		int p_coeff_len = p.coefficients.length;
		int coeff_len = coefficients.length;
		int coeff_max_len = Math.max(p_coeff_len, coeff_len);
		for (int i = 0; i < coeff_max_len; i++) {
			// add coefficient to corresponding exponent
			if (i < p_coeff_len) {
				double x = p.coefficients[i];
				int y = p.exponents[i];
				c[y] += x;
			}
			if (i < coeff_len) {
				double x = coefficients[i];
				int y = exponents[i];
				c[y] += x;
			}
		}

		// remove zero coefficients and corresponding exponents
		int zero_count = countZero(c);
		double[] c2 = new double[c.length - zero_count];
		int[] e2 = new int[c.length - zero_count];
		int cur = 0;
		for (int i = 0; i < c.length; i++) {
			if (c[i] != 0) {
				c2[cur] = c[i];
				e2[cur] = i;
				cur++;
			}
		}
		Polynomial new_p = new Polynomial(c2, e2);
		return new_p;
	}
	public double evaluate(double x) {
		double res = 0;
		for (int i = 0; i < coefficients.length; i++) {
			res += coefficients[i]*(Math.pow(x, exponents[i]));
		}
		return res;
	}
	public boolean hasRoot(double x) {
		return evaluate(x) == 0;
	}
	public Polynomial multiply(Polynomial p) {
		// multiply polynomial p with self
		// highest degree is sum of the highest degrees for both polynomials
		// for c, c[i] is the coefficient for the ith degree term
		double[] c = new double[getDegree() + p.getDegree() + 1];

		// multiply distributively, adding the value to the appropriate exponent (sum of exponents)
		for (int i = 0; i < p.coefficients.length; i++) {
			double x = p.coefficients[i];
			int a = p.exponents[i];
			for (int j = 0; j < coefficients.length; j++) {
				double y = coefficients[j];
				int b = exponents[j];
				c[a + b] += x * y;
			}
		}

		// remove zero coefficients and corresponding exponents
		int zero_count = countZero(c);
		double[] c2 = new double[c.length - zero_count];
		int[] e2 = new int[c.length - zero_count];
		int cur = 0;
		for (int i = 0; i < c.length; i++) {
			if (c[i] != 0) {
				c2[cur] = c[i];
				e2[cur] = i;
				cur++;
			}
		}
		Polynomial new_p = new Polynomial(c2, e2);
		return new_p;
	}
	public static int countZero(double[] c) {
		// return the number of zero coefficients in c
		int count = 0;
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 0) {
				count++;
			}
		}
		return count;
	}
	public int getDegree() {
		// return the degree of the polynomial
		// Precondition: exponents are ordered in ascending order
		return exponents[exponents.length - 1];
	}
	public String polyToString() {
		// converts current polynomial into string format
		String s = new String();
		for (int i = 0; i < coefficients.length; i++) {
			if (coefficients[i] > 0 && !s.isEmpty()) {
				s += "+";
			}
			s += coefficients[i] + "x" + exponents[i];
		}
		return s;
	}
	@Override
	public String toString() {
		// print each term separated by whitespace
		String s = new String();
		for (int i = 0; i < coefficients.length; i++) {
			s += coefficients[i] + "x^" + exponents[i] + " ";
		}
		return s;
	}
}