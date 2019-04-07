
public class Multiplier implements Runnable {

	private Matrix a;
	private Matrix b;
	private Matrix c;
	private int offset;

	public Multiplier(Matrix a, Matrix b, Matrix c, int colOffset) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.offset = colOffset;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		multiply();
	}

	public void multiply() {
		for (int irowA = 0; irowA < a.getNumRows(); irowA++) {
			for (int icolB = 0; icolB < b.getNumCols(); icolB++) {
				int sum = 0;
				for (int i = 0; i < a.getNumCols(); i++) {
					sum += a.get(irowA, i) * b.get(i, icolB);
				}
				c.set(irowA, icolB + offset, sum);
			}
		}
		Util.Log("Thread Multiplication finished");
	}

}
