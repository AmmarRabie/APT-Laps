import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import mpi.*;

public class HelloWorld {

	public static void main(String args[]) throws Exception {
		MPI.Init(args);

		int myRank = MPI.COMM_WORLD.Rank();
		int numProcess = MPI.COMM_WORLD.Size();
		System.out.println("Hi, I am <" + myRank + ">");

		int[] sendbuf;
		int[] recvbuf;
		int inputSize = 0; // any default value

		int[] inputSizeMes = new int[1];

		if (myRank == 0) { // root process
			// • Read array from a file
			// • Send each process its share of the array (the array should be divided among
			// the three processes)
			// • Calculate the sum of its share of elements
			// • Receive the summation result from each process
			// • Calculate the final sum and print it
			int[] theBigArray = readArray();
			inputSize = theBigArray.length;
			rankPrint("the size of the input is " + inputSize);

			// tell the world that the data is valid now sending its length
			inputSizeMes[0] = inputSize;
			MPI.COMM_WORLD.Bcast(inputSizeMes, 0, 1, MPI.INT, 0);
			
			// scattering the data
			sendbuf = theBigArray;
			int divideCount = inputSize / numProcess;
			recvbuf = new int[divideCount];
			MPI.COMM_WORLD.Scatter(sendbuf, 0, divideCount, MPI.INT, recvbuf, 0, divideCount, MPI.INT, 0);
			
			// local summation
			int localSum = sumArray(recvbuf);
			rankPrint("My local sum is " + localSum);
			
			// reducing
			int[] localSumBuf = new int[1];
			localSumBuf[0] = localSum;
			int[] result = new int[1];
			MPI.COMM_WORLD.Reduce(localSumBuf, 0, result, 0, 1, MPI.INT, MPI.SUM, 0);
			
			// sum remaining elements
			for(int i = 0; i < inputSize % numProcess;i++)
				result[0] += theBigArray[inputSize - 1 - i];
			
			// print the global sum
			rankPrint("final sum = " + result[0]);
		} else { // Other processes
			// • Receive its share of the elements of the array
			// • Print whether the elements are received or not (if not block till you
			// receive them)
			// • Calculate the local sum
			// • Send it to the root (process with rank 0)
			// • Print the local sum and exit

			// wait till the root prepare the data
			rankPrint("waiting");

			// wait till the input size come
			MPI.COMM_WORLD.Bcast(inputSizeMes, 0, 1, MPI.INT, 0);
			inputSize = inputSizeMes[0];

			rankPrint("started");

			int[] dummy = new int[inputSize];

			int divideCount = inputSize / numProcess;
			recvbuf = new int[divideCount];
			MPI.COMM_WORLD.Scatter(dummy, 0, divideCount, MPI.INT, recvbuf, 0, divideCount, MPI.INT, 0);

			// summation
			int localSum = sumArray(recvbuf);
			rankPrint("My local sum is " + localSum);

			// reducing
			int[] localSumBuf = new int[1];
			localSumBuf[0] = localSum;
			int[] result = new int[1];
			MPI.COMM_WORLD.Reduce(localSumBuf, 0, result, 0, 1, MPI.INT, MPI.SUM, 0);
		}
		rankPrint("Good bye :(");
		MPI.Finalize();
	}

	private static void rankPrint(String message) {
		int currRank = MPI.COMM_WORLD.Rank();
		System.out.println("Process " + currRank + ": saying '" + message + "'");
	}

	private static int[] readArray() {
		Scanner s;
		try {
			s = new Scanner(new File("input.txt"));
			ArrayList<Integer> numbers = new ArrayList<>();
			while (s.hasNextInt()) {
				int num = s.nextInt();
				numbers.add(num);
			}
			s.close();
			return numbers.stream().mapToInt(i -> i).toArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static int sumArray(int[] arr) {
		int sum = 0;
		for (int i = 0; i < arr.length; i++) {
			sum += arr[i];
		}
		return sum;
	}

}
