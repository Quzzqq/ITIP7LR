import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MaxElementInMatrix {
    private static final int ROWS = 4;
    private static final int COLUMNS = 5;
    private static int[][] matrix = {
        {3, 5, 1, 9, 2},
        {4, 8, 6, 7, 0},
        {12, 15, 10, 11, 13},
        {14, 3, 2, 1, 5}
    };
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(ROWS);
        Future<Integer>[] futures = new Future[ROWS];

        for (int i = 0; i < ROWS; i++) {
            final int rowIndex = i;
            futures[i] = executor.submit(() -> findMaxInRow(rowIndex));
        }
        int maxElement = Integer.MIN_VALUE;
        for (int i = 0; i < ROWS; i++) {
            try {
                int rowMax = futures[i].get();
                if (rowMax > maxElement) {
                    maxElement = rowMax;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
        System.out.println("Наибольший элемент в матрице: " + maxElement);
    }
    
    private static int findMaxInRow(int rowIndex) {
        int max = matrix[rowIndex][0];
        for (int j = 1; j < COLUMNS; j++) {
            if (matrix[rowIndex][j] > max) {
                max = matrix[rowIndex][j];
            }
        }
        return max;
    }
}
