import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
	public static void main(String[] args) {
		long startTime = 0, endTime = 0;
		int[] board;
		double runTime, successRate, avgCost = 0;
		int menuChoice, cost = 0, n, k = 0, loops, success;
		
		System.out.println("N-Queen Problem");
		do {
			printMainMenu();
			menuChoice = validMenuOption(1, 8, "Enter valid integer menu option: ");
			if(menuChoice >= 1 && menuChoice <= 4) {
				
				if(menuChoice == 1) {
					n = validMenuOption(4, 150, "Enter number of queens (min 4, max 150): ");
					board = new int[n];
					board = randomBoard(board);
					startTime = System.nanoTime();
					cost += Steepest.steepestHill(board);
					endTime = System.nanoTime();
				}
				else if(menuChoice == 2) {
					n = validMenuOption(4, 150, "Enter number of queens (min 4, max 150): ");
					board = new int[n];
					board = randomBoard(board);
					startTime = System.nanoTime();
					cost += Steepest.randomRestart(board);
					endTime = System.nanoTime();
				}
				else if(menuChoice == 3) {
					n = validMenuOption(4, 4000, "Enter number of queens (min 4, max 4000): ");
					board = new int[n];
					board = randomBoard(board);
					startTime = System.nanoTime();
					cost += MinConflicts.minConflicts(randomBoard(board));
					endTime = System.nanoTime();
				}
				else {
					n = validMenuOption(4, 120, "Enter number of queens (min 4, max 120): ");
					k = validMenuOption(6, 120, "Enter initial population (min 6, max 120): ");
					board = new int[n];
					startTime = System.nanoTime();
					cost += Genetic.genetic(k, n, board);
					endTime = System.nanoTime();
				}
				runTime = (endTime - startTime) / 1000000000.0;
				
				System.out.println("Solution: ");
				if(board == null) {
					System.out.println("DSfdsfsdfdsf");
				}
				printBoard(board);
				System.out.println("\n");
				System.out.println("Number of attacking queen pairs: " + value(board));
				System.out.println("Runtime: " + runTime + " seconds");
				if(menuChoice == 4) {
					System.out.println("Total search cost: " + cost + " generations");
				}
				else {
					System.out.println("Total search cost: " + cost + " queen moves");
				}
				System.out.println();
				cost = 0;
			}
			else if(menuChoice >= 5 && menuChoice <= 7) {
				
				if(menuChoice == 5) {
					System.out.println("Steepest-Ascent Hill Climbing Table");
					System.out.format("%5s %8s %10s %10s %17s %10s\n", "n", "Cases", "Successes", "Success %", "Avg. Runtime (s)", "Avg. Cost");
					loops = 10000;
				}
				else if(menuChoice == 6) {
					System.out.println("Min-Conflicts Table");
					System.out.format("%5s %8s %10s %10s %17s %10s\n", "n", "Cases", "Successes", "Success %", "Avg. Runtime (s)", "Avg. Cost");
					loops = 1000;
				}
				else {
					k = validMenuOption(6, 25, "Enter initial population (min 6, max 25): ");
					System.out.println("Genetic Table (k=" + k + ")");
					System.out.format("%5s %8s %10s %10s %17s %10s\n", "n", "Cases", "Successes", "Success %", "Avg. Runtime (s)", "Generations");
					loops = 101;
				}
				
				for(int i = 4; i <= 22; i++) {
					board = new int[i];
					success = 0;
					runTime = 0;
					avgCost = 0;
					
					for(int j = 0; j < loops; j++) {
						randomBoard(board);
						if(menuChoice == 5) {
							startTime = System.nanoTime();
							avgCost += Steepest.steepestHill(board);
							endTime = System.nanoTime();
						}
						else if(menuChoice == 6){
							startTime = System.nanoTime();
							avgCost += MinConflicts.minConflicts(board);
							endTime = System.nanoTime();
						}
						else {
							startTime = System.nanoTime();
							avgCost += Genetic.genetic(k, i, board);
							endTime = System.nanoTime();
						}
						
						runTime += endTime - startTime;
						if(value(board) == 0) {
							success++;
						}
					};
					successRate = success * 100.0 / loops ;
					runTime /= (loops * 1000000000.0);
					avgCost /= loops;
					System.out.format("%5d %8d %10d %10.2f %17.7f %10.2f\n", i, loops, success, successRate, runTime, avgCost);
				}
				
				if(menuChoice == 6) {
					loops = 10;
					for(int i = 100; i <= 1000; i += 100) {
						board = new int[i];
						success = 0;
						runTime = 0;
						avgCost = 0;
						
						for(int j = 0; j < loops; j++) {
							startTime = System.nanoTime();
							avgCost += MinConflicts.minConflicts(randomBoard(board));
							endTime = System.nanoTime();
							runTime += endTime - startTime;
							if(value(board) == 0) {
								success++;
							}
						};
						successRate = success * 100.0 / loops ;
						runTime /= (loops * 1000000000.0);
						avgCost /= loops;
						System.out.format("%5d %8d %10d %10.2f %17.7f %10.2f\n", i, loops, success, successRate, runTime, avgCost);
					}
				}
				System.out.println();
				
			}
			else if(menuChoice == 8) {
				break;
			}
		} while(true);
	}
	
	public static void printMainMenu() {
		System.out.println("1. Steepest-ascent hill climbing");
		System.out.println("2. Random-restart hill climbing");
		System.out.println("3. Min-conflicts");
		System.out.println("4. Genetic");
		System.out.println("5. Print steepest-ascent hill climbing search cost table");
		System.out.println("6. Print min-conflicts search cost table");
		System.out.println("7. Print genetic search cost table");
		System.out.println("8. Exit program");
		System.out.println();
	}
	
	public static int validMenuOption(int min, int max, String message) {
		Scanner s = new Scanner(System.in);
		int validInt;
		
		do {
			validInt = validInt(s, message);
			System.out.println();
			if(validInt >= min && validInt <= max) {
				return validInt;
			}
			else {
				System.out.println("Invalid option! Choose an integer inclusively between " + min + " and " + max + ".");
			}
		} while(true);
	}
	
	public static int validInt(Scanner s, String message) {	//Validates user input to accept integer values
		int i;
		do {
			System.out.print(message);
			try {
				i = s.nextInt();
				return i;
			}
			catch (InputMismatchException E) {
				System.out.println("Non-integer value!");
				s.nextLine();
			}
		} while(true);
	}
	
	public static int[] randomBoard(int[] board) {
		Random rand = ThreadLocalRandom.current();
		int length = board.length;
		for(int i = 0; i < length; i++) {
			board[i] = rand.nextInt(length);
		}
		return board;
	}
	
	public static int value(int[] board) {
		int length = board.length;
		int AtkQs = 0;
		
		for(int i = 0; i < length - 1; i++) {
			for(int j = i + 1; j < length; j++) {
				if(board[i] == board[j] || isDiagonal(i, board[i], j, board[j])) {	//Same y or same diagonal
					AtkQs++;
				}
			}
		}
		return AtkQs;
	}
	
	private static boolean isDiagonal(int ax, int ay, int bx, int by) {
		return Math.abs(ax - bx) == Math.abs(ay - by) &&
				((ax - ay) == (bx - by) ||
				(ax + ay) == (bx + by));
	}
	
	public static void printBoard(int[] board) {
		int length = board.length;
		for(int i = 0; i < length; i++) {
			System.out.print(board[i] + " ");
		}
	}
}