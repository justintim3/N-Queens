import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Steepest {
	public static int randomRestart(int[] board) {
		int cost = 0;
		while(value(board) > 0) {
			cost += steepestHill(randomBoard(board));
		}
		return cost;
	}
	
	public static int steepestHill(int[] board) {
		int boardValue;
		int searchCost = 0;
		do {
			boardValue = value(board);
			board = minValue(board);
			searchCost++;
			if(boardValue == value(board)) {
				return searchCost;
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
	
	public static int[] minValue(int[] board) {
		int length = board.length;
		int maxAtkQs = (length * (length - 1)) / 2;
		int AtkQs = 0, minAtkQs = maxAtkQs, minX = 0, minY = 0, colAtkQs = 0;
		for(int i = 0; i < length; i++) {
			for(int j = 0; j < length - 1; j++) {	//Count attacking queens for the rest of the board
				for(int k = j + 1; k < length; k++) {
					if(i != j && i != k && (board[j] == board[k] || isDiagonal(j, board[j], k, board[k]))) {	//Same y or same diagonal
						AtkQs++;
					}
				}
			}
			for(int y = 0; y < board.length; y++) {	//Count attacking queens for the column
				for(int j = i + 1; j < length; j++) {
					if(y == board[j] || isDiagonal(i, y, j, board[j])) {	//Same y or same diagonal
						colAtkQs++;
					}
				}
				for(int j = i - 1; j >= 0; j--) {
					if(y == board[j] || isDiagonal(i, y, j, board[j])) {	//Same y or same diagonal
						colAtkQs++;
					}
				}
				if(AtkQs + colAtkQs < minAtkQs) {
					minAtkQs = AtkQs + colAtkQs;
					minX = i;
					minY = y;
				}
				colAtkQs = 0;
			}
			AtkQs = 0;
		}
		board[minX] = minY;
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
}
