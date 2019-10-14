package edu.ktu.atg.example;

public class Calculator {
	private double result = 0;

	public Calculator add(double v) {
		result += v;
		return this;
	}

	public Calculator divide(double v) {
		if (v != 0) {
			result /= v;
		}
		return this;
	}

	public double getResult() {
		return result;
	}
}
