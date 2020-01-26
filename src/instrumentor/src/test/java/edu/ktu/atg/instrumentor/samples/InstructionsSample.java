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
	public void check(int a, Object c, boolean b, Integer a2) {
		if (c instanceof String) {
			System.out.println("OK");
			return;
		}
		if (a > 10) {
			return;
		}
		if (b) {
			System.out.println("Bool");
			return;
		}
		if (a2> 100) {
			return;
		}
		if (c == null) {
			System.out.println("NULL");
			return;
		}
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
