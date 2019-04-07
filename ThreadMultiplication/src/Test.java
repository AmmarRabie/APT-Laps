
// import labs.Matrix;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Test
 */
public class Test {
	static Matrix a, b, c;

	public static void main(String[] args) {
		System.out.println("Testing: Assignment 2(Threading in Java)...\n");

		Util.Log("getting input from user...");
		getUserInput();

		c = new Matrix(a.getNumRows(), b.getNumCols(), 0); // dummy matrix for storing the result of A*B

		Matrix b1 = new Matrix(b.getNumRows(), 2); // #cols should be determined in split stage and Overridden
		Matrix b2 = new Matrix(b.getNumRows(), 2);

		// [deprecated] splitting
//		Thread splitterThread = new Thread(new AsycSplitter(b, b1, b2));
//		splitterThread.start();
//		try {
//			splitterThread.join(); // wait split to finish work
//			Util.Log("Splitter has end his jop :)");
//			Util.Log("b1 and b2 after splitting");
//			b1.print();
//			System.out.println();
//			b2.print();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		
		// synchronous splitting
		new Splitter(b, b1, b2).split();

		// multiplying
		Thread t1 = startNewThread(b1, 0), t2 = startNewThread(b2, b1.getNumCols());
		try {
			t1.join();
			t2.join();

			Util.Log("A = ");
			a.print();
			Util.Log("B = ");
			b.print();
			Util.Log("c = A * B = ");
			c.print();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		printQuestionAnswer();
		
		Util.Log("main thread finished");
	}

	private static void getUserInput() {
		Util.Log("What is matrix 'A' dimention(enter number of rows then cols) ?");
		Scanner reader = new Scanner(System.in);
		int rows = reader.nextInt(), aCols = reader.nextInt();
		Util.Log("Now, Enter values of 'A' row by row");
		a = new Matrix(rows, aCols);
		ArrayList<Integer> values = new ArrayList<Integer>();
		for (int i = 0; i < rows * aCols; i++) {
			values.add(reader.nextInt());
		}
		a.setNumbers(values);
		Util.Log("Matrix A read is");
		a.print();
		values.clear();

		Util.Log("What is matrix 'B' number of cols ?");
		int bCols = reader.nextInt();
		Util.Log("Now, Enter values of 'B' row by row");
		b = new Matrix(aCols, bCols);
		for (int i = 0; i < aCols * bCols; i++) {
			values.add(reader.nextInt());
		}
		b.setNumbers(values);
		Util.Log("Matrix B read is");
		b.print();

		reader.close();
	}

	private static Thread startNewThread(Matrix splittedMatrix, int offset) {
		Thread t = new Thread(new Multiplier(a, splittedMatrix, c, offset));
		t.setName(String.valueOf(t.getId()));
		t.start();
		return t;
	}
	
	private static void printQuestionAnswer() {
		Util.Log("We have to join the multiplication threads before trying to print matrix 'C' so that we print the right values");
	}
}