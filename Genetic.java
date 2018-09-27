import java.util.Hashtable;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Genetic {
	public static int genetic(int k, int n, int[] goal) {
		Random rand = ThreadLocalRandom.current();
		int[][] population = new int[k][], selections, children;
		int cost = 0;
		
		if(k % 2 == 0) {
			selections = new int[k][];
		}
		else {
			selections = new int[k - 1][];
		}
		int[] fitness = new int[k];
		children = new int[selections.length][];
		
		population = initialize(k, n);
		
		do {
			cost++;
			QuickSort.qsort(population, population.length);
			selections = selection(population, selections, fitness);
			
			for(int i = 0; i < selections.length; i += 2) {
				children[i] = reproduce(selections[i], selections[i + 1]);
				if(isGoal(children[i])) {
					copyArray(children[i], goal);
					return cost;
				}
				children[i + 1] = reproduce(selections[i + 1], selections[i]);
				if(isGoal(children[i + 1])) {
					copyArray(children[i + 1], goal);
					return cost;
				}
			}
			
			for(int i = 0; i < children.length; i++) {
				if(rand.nextInt(2) == 1) {
					children[i] = mutate(children[i]);
					if(isGoal(children[i])) {
						copyArray(children[i], goal);
						return cost;
					}
				}
			}
			population = children;
		}
		while(true);
	}
	
	public static int[][] initialize(int k, int n) {
		int[][] population = new int[k][];
		Hashtable<int[], int[]> ht = new Hashtable<int[], int[]>();
		for(int i = 0; i < k; i++) {	//Initialize population
			int[] board = randomBoard(n);
			
			if(!ht.containsKey(board)) {
				ht.put(board, board);
				population[i] = board;
			}
			else {
				i--;
			}
		}
		return population;
	}
	
	public static int fitness(int[] board) {
		int length = board.length;
		int nonAtkQs = 0;
		int[] state = board;
		
		for(int i = 0; i < length - 1; i++) {
			for(int j = i + 1; j < length; j++) {
				if(state[i] != state[j] && !isDiagonal(i, state[i], j, state[j])) {	//Not same y and same diagonal
					nonAtkQs++;
				}
			}
		}
		return nonAtkQs;
	}
	
	private static boolean isDiagonal(int ax, int ay, int bx, int by) {
		return Math.abs(ax - bx) == Math.abs(ay - by) &&
				((ax - ay) == (bx - by) ||
				(ax + ay) == (bx + by));
	}
	
	public static int[] randomBoard(int n){
		Random rand = ThreadLocalRandom.current();
		int[] board = new int[n];
		for(int i = 0; i < n; i++) {
			board[i] = rand.nextInt(n);
		}
		return board;
	}
	
	private static int[][] selection(int[][] population, int[][] selections, int[] fitness) {
		Random rand = ThreadLocalRandom.current();
		int sum = 0, randInt, index, index2;
		int length = population.length;
		
		for(int i = 0; i < length / 2; i++) {
			fitness[i] = fitness(population[i]);
			sum += fitness[i];
		}
			
		for(int i = 0; i < selections.length; i += 2) {
			randInt = rand.nextInt(sum) + 1;
			index = 0;
			
			do {
				randInt -= fitness[index];
				if(randInt <= 0) {
					break;
				}
				index++;
			} while(true);
			selections[i] = population[index];
			
			randInt = rand.nextInt(sum - fitness[index]) + 1;
			index2 = 0;
			
			do {
				if(index2 != index) {
					randInt -= fitness[index2];
					if(randInt <= 0) {
						break;
					}
				}
				index2++;
			} while(true);
			selections[i + 1] = population[index2];
		}
		return selections;
	}
	
	private static int[] reproduce(int[] a, int[] b) {
		Random rand = ThreadLocalRandom.current();
		int length = a.length;
		int randInt;
		int[] child = new int[length];
		
		randInt = rand.nextInt(length - 1);
		for(int i = 0; i <= randInt; i++) {
			child[i] = a[i];
		}
		for(int i = randInt + 1; i < length; i++) {
			child[i] = b[i];
		}
		
		return child;
	}
	
	private static int[] mutate(int[] a) {
		Random rand = ThreadLocalRandom.current();
		int length = a.length;
		int randInt = rand.nextInt(length);
		
		a[randInt] = rand.nextInt(length);
		return a;
	}
	
	private static boolean isGoal(int[] board) {
		int length = board.length;
		return fitness(board) == (length * (length - 1)) / 2;
	}
	
	private static void copyArray(int[] source, int[] dest) {
		if(source.length == dest.length) {
			for(int i = 0; i < source.length; i++) {
				dest[i] = source[i];
			}
		}
	}
}