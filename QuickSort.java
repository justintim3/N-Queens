public class QuickSort {
	private static int findpivot(int[][] A, int i, int j)
	  { return (i + j) / 2; }

	private static int partition(int[][] A, int l, int r, int[] pivot)
	{
		do
		{
			while (Genetic.fitness(pivot) < Genetic.fitness(A[++l]));
			while ((l < r) && Genetic.fitness(A[--r]) < Genetic.fitness(pivot));
			{
				int[] temp = A[l];
				A[l] = A[r];
				A[r] = temp;
			}
		} while (l < r);
	  return l;
	}

	private static void quicksort(int[][] A, int i, int j) {
		if (j <= i) return;
		int pivotindex = findpivot(A, i, j);
		int[] temp = A[pivotindex];
		A[pivotindex] = A[j];
		A[j] = temp;
		
		int k = partition(A, i - 1, j, A[j]);
		temp = A[k];
		A[k] = A[j];
		A[j] = temp;
		quicksort(A, i, k - 1);
		quicksort(A, k + 1, j);
	}

	public static void qsort(int[][] A, int n) {
		quicksort(A, 0, n - 1);
	}
}