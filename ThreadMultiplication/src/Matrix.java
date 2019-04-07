
import java.util.ArrayList;

// import labs.Addable;

/**
 * Matrix
 */
public class Matrix{
    protected int m, n;
    protected int[][] numbers;

    public Matrix() {
        this(1, 1, 0);
    }
    
    public Matrix(int rows, int cols) {
        this(rows, cols, 0);
    }

    public Matrix(int rows, int cols, int initializeWith) {
        this.n = cols;
        this.m = rows;
        this.numbers = new int[n][];
        for (int i = 0; i < n; i++) {
            numbers[i] = new int[m];
            for (int j = 0; j < m; j++)
                numbers[i][j] = initializeWith;
        }
    }

    public Matrix(Matrix source) {
		this.setFrom(source);
	}

	public boolean setNumbers(ArrayList<Integer> falattenNumbers) {
        int elementsCount = falattenNumbers.size();
        if (elementsCount != n * m)
            return false;
        // where to set = row*m + col
        int indexTaken = 0;
        for (int row = 0; row < m; row++)
            for (int col = 0; col < n; col++)
                set(row, col, falattenNumbers.get(indexTaken++));
        return true;
    }

    public void print() {
        for (int row = 0; row < m; row++) {
            for (int col = 0; col < n; col++) {
                System.out.printf("%d ", get(row, col));
            }
            System.out.println();
        }
    }

    public void transpose() {
        Matrix matrixTranspose = new Matrix(n, m);
        for (int row = 0; row < m; row++)
            for (int col = 0; col < n; col++)
            	matrixTranspose.set(col, row, get(row, col));
        setFrom(matrixTranspose);
    }

    public int get(int row, int col) {
        return numbers[col][row];
    }

    public void set(int row, int col, int value) {
        numbers[col][row] = value;
    }

    public Matrix add(Matrix value){
    	if(value.m != m || value.n != n)
    		throw new Error("matrix should be at the same lenght");
        Matrix result = new Matrix(n, m);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int newVal = value.get(j, i) + numbers[i][j];
                result.set(j, i, newVal);
            }
        }
        return result;
    }
    
    public int getNumRows() {return m;}
    public int getNumCols() {return n;}
    
    public void setFrom(Matrix source) {
    	this.n = source.n;
    	this.m = source.m;
    	this.numbers = source.numbers;
    }
}