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

	public static String mainPath = "/mnt/D/TA/CMP(N)306 Advanced Programming/2018/Labs/Lab 5/";

	public static void main(String args[]) throws Exception {
		MPI.Init(args);

		int myRank = MPI.COMM_WORLD.Rank();
		System.out.println("Hi, I am <" + myRank + ">");

		int[] sendbuf;
		int[] recvbuf;
		int size = 3;

		if (myRank == 0) { // root process
			// • Read array from a file
			// • Send each process its share of the array (the array should be divided among
			// the three processes)
			// • Calculate the sum of its share of elements
			// • Receive the summation result from each process
			// • Calculate the final sum and print it
			int[] theBigArray = readArray();
			size = theBigArray.length;

			sendbuf = theBigArray;
			recvbuf = new int[1];

			MPI.COMM_WORLD.Scatter(sendbuf, 0, size/3, MPI.INT, recvbuf, 0, 1, MPI.INT, 0);
			for (int i = 0; i < size; i++) {
				System.out.println("I SENT" + sendbuf[i]);
			}

			for (int i = 0; i < 1; i++) {
				System.out.println("I found" + recvbuf[i]);
			}
		} else { // Other processes
			// • Receive its share of the elements of the array
			// • Print whether the elements are received or not (if not block till you
			// receive them)
			// • Calculate the local sum
			// • Send it to the root (process with rank 0)
			// • Print the local sum and exit
//			int[] dummy = new int[4]; // Although it is not needed, it needs to match the total number of sent
//										// messages
//			int[] Scatter_recvbuf = new int[2];
//			// the sendpart have to match the original send, so does the rec (should match
//			// the send)
//			MPI.COMM_WORLD.Scatter(dummy, 0, 2, MPI.INT, Scatter_recvbuf, 0, 2, MPI.INT, 0);
//			for (int i = 0; i < 2; i++) {
//				System.out.println("I am receiving from " + Scatter_recvbuf[i]);
//				Scatter_recvbuf[i] = Scatter_recvbuf[i] * 10;
//			}
		}
		System.out.println("Process " + myRank + " exiting ...");
		MPI.Finalize();
	}

	private static int[] readArray() {
		Scanner s;
		try {
			s = new Scanner(new File("input.txt"));
			ArrayList<Integer> numbers = new ArrayList<>();
			while (s.hasNextInt()) {
				int num = s.nextInt();
				numbers.add(num);
				s.close();
				return numbers.stream().mapToInt(i -> i).toArray();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
