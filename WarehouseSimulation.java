import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class Warehouse {
    private static final int MAX_WEIGHT = 150;
    private int currentWeight = 0;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public void load(int weight) throws InterruptedException {
        lock.lock();
        try {
            if (currentWeight + weight > MAX_WEIGHT) {
                System.out.println(Thread.currentThread().getName() + " не может загрузить " + weight + " кг. Текущий вес: " + currentWeight + " кг.");
                return;
            }

            currentWeight += weight;
            System.out.println(Thread.currentThread().getName() + " загрузил " + weight + " кг. Текущий вес: " + currentWeight + " кг.");

            if (currentWeight == MAX_WEIGHT) {
                unload();
            }
        } finally {
            lock.unlock();
        }
    }

    private void unload() throws InterruptedException {
        System.out.println("Грузчики отправляются на разгрузку с весом: " + currentWeight + " кг.");
        Thread.sleep(1000);
        currentWeight = 0;
        System.out.println("Разгрузка завершена. Грузчики готовы к новой загрузке.");
        condition.signalAll();
    }
}

class Loader extends Thread {
    private final Warehouse warehouse;
    private final Random random = new Random();

    public Loader(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 5; i++) {
                int weight = random.nextInt(50) + 1;
                warehouse.load(weight);
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class WarehouseSimulation {
    public static void main(String[] args) {
        Warehouse warehouse = new Warehouse();

        // Создаем 3 грузчика
        Loader loader1 = new Loader(warehouse);
        Loader loader2 = new Loader(warehouse);
        Loader loader3 = new Loader(warehouse);

        loader1.start();
        loader2.start();
        loader3.start();

        try {
            loader1.join();
            loader2.join();
            loader3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Все грузчики завершили работу.");
    }
}