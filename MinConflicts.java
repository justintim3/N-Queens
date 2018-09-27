import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.LinkedList;

public class MinConflicts {
	public static int minConflicts(int[] board) {
		LinkedList<Integer> conflicters = new LinkedList<Integer>();
		LinkedList<Integer> minConflicts = new LinkedList<Integer>();
		Random rand = ThreadLocalRandom.current();
		int randCol, randMinConflict, searchCost = 0, maxSteps = averageCost(board.length) * 8;
		
		while(value(board) > 0) {
			conflicters(board, conflicters);
			randCol = conflicters.get(rand.nextInt(conflicters.size()));
			findMinConflicts(board, randCol, minConflicts);
			randMinConflict = minConflicts.get(rand.nextInt(minConflicts.size()));
			board[randCol] = randMinConflict;
			searchCost++;
			if(searchCost > maxSteps) {
				return searchCost;
			}
			conflicters.clear();
			minConflicts.clear();
		}
		return searchCost;
	}
	
	private static void conflicters(int[] board, LinkedList<Integer> conflicters) {
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board.length; j++) {
				if(i != j && (board[i] == board[j] || isDiagonal(i, board[i], j, board[j]))) {
					conflicters.add(i);
				}
			}
		}
	}
	
	private static boolean isDiagonal(int ax, int ay, int bx, int by) {
		return Math.abs(ax - bx) == Math.abs(ay - by) &&
				((ax - ay) == (bx - by) ||
				(ax + ay) == (bx + by));
	}
	
	public static void findMinConflicts(int[] board, int col, LinkedList<Integer> minConflicts) {
		int length = board.length, AtkQs = 0, colAtkQs = 0, minAtkQs = (length * (length - 1)) / 2;
		
		for(int j = 0; j < length - 1; j++) {	//Count attacking queens for the rest of the board
			for(int k = j + 1; k < length; k++) {
				if(col != j && col != k && (board[j] == board[k] || isDiagonal(j, board[j], k, board[k]))) {	//Same y or same diagonal
					AtkQs++;
				}
			}
		}
		for(int y = 0; y < length; y++) {	//Count attacking queens for the column
			for(int j = col + 1; j < length; j++) {
				if(y == board[j] || isDiagonal(col, y, j, board[j])) {	//Same y or same diagonal
					colAtkQs++;
				}
			}
			for(int j = col - 1; j >= 0; j--) {
				if(y == board[j] || isDiagonal(col, y, j, board[j])) {	//Same y or same diagonal
					colAtkQs++;
				}
			}
			if(AtkQs + colAtkQs < minAtkQs) {
				minAtkQs = AtkQs + colAtkQs;
				minConflicts.clear();
				minConflicts.add(y);
			}
			else if(AtkQs + colAtkQs == minAtkQs) {
				minConflicts.add(y);
			}
			
			colAtkQs = 0;
		}
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
	
	private static int averageCost(int n) {
		return (int)(.5725 * n + 77.371);
	}
}
