package com.github.ufofvincent.backwarddigitsums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

/*
 * This program is the solution for the Backward Number Problem.
 * Finished by Vincent Han, at 20:29 (GMT-8) , November 18, 2017.
 * 
 * Methodology:
 * 
 * 1) Read the user input. Let the user input the value of n and s.
 * 2) Generate the equation that will be used later. It is found that the coefficients of the equation 
 * 		is the nth row of the Pascal Triangle. Hence, find the nth row  of the Pascal Triangle. This will be our coefficients.
 * 3) Initialize the array {1,2,3...n}. This array will be later permutated. It is expected that 
 * 		one of its permutation is our solution.
 * 4) The most important step: permutate the initialize array. All the permutations will be stored in the global variable permutations.
 * 5) Now, check all the permutations one by one to see whether or not it fits into the equation to give us s. If it does, this
 * 		permutation will be stored in the global variable results.
 * 6) The first element of results will be returned. This is our final value (yay!). If there are no elements in results, it means
 * 		that this pair of n and s has no solutions.
 * 
 * I'M SO HAPPY THAT I SOLVED THIS PROBLEM YAYYYYYY!!!!
 */

public class BackwardDigitSums {

	/*
	 * These are the global variables for n and s. S is the letter for the final
	 * sum.
	 */
	private static int n;
	private static int s;

	// This global variable will store all the permutations.
	private static int[][] permutations;

	// This global variable represents which slot will one permutation go to in
	// the array above.
	private static int placement = 0;

	/*
	 * This global variable will be the initial array in the form {1,2,3,4...n}.
	 * This array will be permutated.
	 */
	private static int[] initialArray;

	/*
	 * This array represents the coefficients of the equation. For example, in
	 * the equation
	 * 
	 * y = a + 3b + 3c +d,
	 * 
	 * this value would be {1,3,3,1}.
	 */
	private static int[] coefficients;

	/*
	 * This global variable stores all the arrays that satisfy the equation.
	 * From all these arrays, one will be selected as the lexicographically
	 * smallest, and will be our final result! The variable placement 2
	 * represent the slot which one result will go to.
	 */
	private static int[][] results;
	private static int placement2 = 0;

	// This method reads the user input, and sets the n and s value
	public static void readUserInput() {
		System.out.println(
				"Please input n and s respectively. N must be in (0,10]. Make sure that there is a space between the two integers.");
		Scanner sc = new Scanner(System.in);
		try {
			n = sc.nextInt();
			s = sc.nextInt();

			// n must be in (1,10]
			if (!(n > 1 && n <= 10)) {
				throw new RuntimeException();
			}
		} catch (RuntimeException e) {
			System.out.println("Input mismatch");
		} finally {
			sc.close();
		}
	}

	// This method initializes the array {1,2,3...n} and stores it into the
	// global variable initialArray.
	public static void initializeArray() {
		int[] arr = new int[n];

		for (int x = 1; x <= arr.length; x++) {
			arr[x - 1] = x;
		}

		initialArray = arr;
	}

	/*
	 * This method returns the corresponding equation in the form of an array of
	 * integers. This array of integers is the coefficients for the equation.
	 */
	public static void getEquation() {
		int[][] array = new int[n][n];

		for (int x = 0; x < n; x++) { // outer for, used to reach the first
										// dimension
			for (int y = 0; y < (x + 1); y++) { // inner for, used to reach the
												// second dimension
				if (y == 0) // if it is the first element, the element is
							// automatically 1
					array[x][y] = 1;
				else if (array[x - 1][y] == 0)
					array[x][y] = 1;
				else {
					/*
					 * Otherwise, the yth element of the nth array is equal to
					 * the y-1th element of the n-1 array + yth element of n-1
					 * array
					 */
					array[x][y] = array[x - 1][y - 1] + array[x - 1][y];
				}
			}
		}

		coefficients = array[n - 1];
	}

	/*
	 * This method requires two integer arrays as parameters. The first array
	 * serves as the coefficients of an equation, for example y = a + 3b + 3c +
	 * d
	 * 
	 * the value of the first parameter would be {1,3,3,1} The second array,
	 * therefore, is the values for each variable (a,b,c, and d).
	 * 
	 * The method returns the sum of the equation.
	 */
	private static int compute(int[] coefficients, int[] values) {
		int sum = 0;

		for (int x = 0; x < coefficients.length; x++) {
			sum += coefficients[x] * values[x];
		}

		return sum;
	}

	/*
	 * These methods will permute the given array. All the permutations will be
	 * placed into the global variable permutations.
	 */

	public static void permute(int[] arr) {
		permutations = new int[getFactorial(arr.length)][arr.length];

		permute(new int[] {}, arr);
	}

	private static void permute(int[] prefix, int[] remainder) {

		if (remainder.length == 0) {

			permutations[placement] = prefix;

			placement++;
			return;
		}

		int[] temp;
		int[] temp2;

		for (int i = 0; i < remainder.length; i++) {
			temp = combineArrays(prefix, remainder[i]);
			temp2 = combineArrays(subArray(remainder, 0, i), subArray(remainder, i + 1, remainder.length));

			permute(temp, temp2);
		}
	}

	private static int[] combineArrays(int[] arr1, int[] arr2) {
		int[] combinedArr = new int[arr1.length + arr2.length];

		for (int x = 0; x < arr1.length; x++) {
			combinedArr[x] = arr1[x];
		}

		for (int x = 0; x < arr2.length; x++) {
			combinedArr[x + arr1.length] = arr2[x];
		}

		return combinedArr;
	}

	private static int[] combineArrays(int[] arr1, int arr2) {

		int[] combinedArr = new int[arr1.length + 1];

		for (int x = 0; x < arr1.length; x++) {
			combinedArr[x] = arr1[x];
		}

		combinedArr[arr1.length] = arr2;

		return combinedArr;
	}

	private static int[] subArray(int[] arr, int start, int end) {

		int[] newArr = new int[end - start];

		if (start == end) {
			return new int[] {};
		}

		for (int x = 0; x < (end - start); x++) {
			newArr[x] = arr[x + start];
		}

		return newArr;
	}

	private static int getFactorial(int x) {
		int fact = 1;

		for (int i = 1; i <= x; i++) {
			fact = fact * i;
		}

		return fact;
	}
	
	
	/*
	 * This method will check all the permutations one by one to see whether or not it satisfies our condition.
	 * If one does, it will be stored into the global variable results.
	 */
	public static void checkAllPermutations() {
		// This ArrayList will contain all the solutions
		ArrayList<Integer[]> al = new ArrayList<>();

		for (int i = 0; i < permutations.length; i++) {
			if (compute(coefficients, permutations[i]) == s) {
				al.add(convertFromIntToInteger(permutations[i]));
			}
		}

		// Now, add all the elements of that ArrayList to the global variable
		// results.

		results = new int[al.size()][n];
		Iterator<Integer[]> it = al.iterator();

		for (int a = 0; it.hasNext(); a++) {
			results[a] = convertFromIntegerToInt(it.next());
		}
	}

	/*
	 * This method converts an int[] type to an Integer[] type (the other one vice versa), since
	 * checkAllPermutations() method needs this feature.
	 */
	private static Integer[] convertFromIntToInteger(int[] x) {
		Integer[] arr = new Integer[x.length];

		for (int i = 0; i < x.length; i++) {
			arr[i] = x[i];
		}

		return arr;
	}

	private static int[] convertFromIntegerToInt(Integer[] x) {
		int[] arr = new int[x.length];
		for (int i = 0; i < x.length; i++) {
			arr[i] = x[i];
		}

		return arr;
	}


	public static void main(String[] args) {
		readUserInput();
		getEquation();
		initializeArray();
		permute(initialArray);
		checkAllPermutations();

		if (results.length == 0) {
			System.out.println("There are no solutions for this pair of n and s");
			return;
		}
		System.out.println(Arrays.toString(results[0]));

	}

}
