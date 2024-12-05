public class SumArray {
    private static int[] array = {1,2,3,4,5,6,7,8};

    private static int sum1 = 0;
    private static int sum2 = 0;
    
    public static void main(String[] args) {
        Thread thr1 = new Thread(new SumTask(0, array.length / 2));
        Thread thr2 = new Thread(new SumTask(array.length / 2, array.length));

        thr1.start();
        thr2.start();

        try {
            thr1.join();
            thr2.join();

        } catch (InterruptedException  e) {
            e.printStackTrace();
        }
        int totalSum = sum1 + sum2;
        System.out.println(totalSum);
    }

    static class SumTask implements Runnable {
        private int start;
        private int end;

        public SumTask(int start, int end){
            this.start = start;
            this.end = end;
        }
        
        @Override
        public void run() {
            int localSum = 0;
            for (int i = start; i < end; i++){
                localSum += array[i];
            }
            if (start == 0){
                sum1 = localSum;
            } else{
                sum2 = localSum;
            }
        }
    }
}
