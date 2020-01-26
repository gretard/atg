package edu.ktu.atg.instrumentor.samples;

public class InstructionsSample {
	int x = 0;

	public int checkReturnValue() {
		return x;
	}

	public void checkVoid() {
		x++;
	}
	private void test() {
		x++;
	}
	
	private void testArr(int[] arr) {
		arr[0] = 1;
		x++;
	}
	public int checkBranch(int x) {
		if (x > 0) {
			return 0;
		}
		test();
		testArr(null);
		return this.x;
	}

	public int checkSiwthc(int x) {
		switch (x) {
		case 1:

			break;
		case 2:

			break;

		default:
			break;
		}
		return this.x;
	}
}
