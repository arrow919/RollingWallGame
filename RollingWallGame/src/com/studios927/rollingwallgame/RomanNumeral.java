package com.studios927.rollingwallgame;

public class RomanNumeral {
	private static final char[] num = { 'ↂ', 'ↁ', 'M', 'D', 'C', 'L', 'X', 'V', 'I' };
	// or, as suggested by Andrei Fierbinteanu
	// private static final String[] R = {"X\u0305", "V\u0305", "M", "D", "C",
	// "L", "X", "V", "I"};
	private static final int MAX = 10000; // value of R[0], must be a power of
											// 10

	private static final int[][] DIGITS = { {}, { 0 }, { 0, 0 }, { 0, 0, 0 }, { 0, 1 }, { 1 }, { 1, 0 }, { 1, 0, 0 }, { 1, 0, 0, 0 }, { 0, 2 } };

	public static String int2roman(int number) {
		if (number < 0 || number >= MAX * 4)
			throw new IllegalArgumentException("int2roman: " + number + " is not between 0 and " + (MAX * 4 - 1));
		if (number == 0)
			return "N";
		StringBuilder sb = new StringBuilder();
		int i = 0, m = MAX;
		while (number > 0) {
			int[] d = DIGITS[number / m];
			for (int n : d)
				sb.append(num[i - n]);
			number %= m;
			m /= 10;
			i += 2;
		}
		return sb.toString();
	}

	enum Numeral {
		I(1), IV(4), V(5), IX(9), X(10), XL(40), L(50), XC(90), C(100), CD(400), D(500), CM(900), M(1000);
		int weigth;

		Numeral(int weigth) {
			this.weigth = weigth;
		}
	};

	public static String long2roman(long n) {

		if (n <= 0) {
			throw new IllegalArgumentException();
		}

		StringBuilder buf = new StringBuilder();

		final Numeral[] values = Numeral.values();
		for (int i = values.length - 1; i >= 0; i--) {
			while (n >= values[i].weigth) {
				buf.append(values[i]);
				n -= values[i].weigth;
			}
		}
		return buf.toString();
	}
}
