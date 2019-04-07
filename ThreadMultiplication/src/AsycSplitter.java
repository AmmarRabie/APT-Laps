@Deprecated
public class AsycSplitter implements Runnable {

	private Matrix s;
	private Matrix d1;
	private Matrix d2;

	public AsycSplitter(Matrix source, Matrix dest1, Matrix dest2) {
		this.s = source;
		this.d1 = dest1;
		this.d2 = dest2;
	}

	@Override
	public void run() {
		split();
	}

	public void split() {
		int cols1 = s.getNumCols() / 2;
		int cols2 = s.getNumCols() - cols1;
		d1.n = cols1;
		d2.n = cols2;
		for (int icol = 0; icol < cols1; icol++) {
			for (int irow = 0; irow < s.getNumRows(); irow++) {
				d1.set(irow, icol, s.get(irow, icol));
			}
		}

		for (int icol = 0; icol < cols2; icol++) {
			for (int irow = 0; irow < s.getNumRows(); irow++) {
				d2.set(irow, icol, s.get(irow, icol + cols1));
			}
		}
	}

}
