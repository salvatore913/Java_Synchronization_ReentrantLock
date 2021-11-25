import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CarShowroom {
    private final int PRODUCTION_TIME = 3000;
    private final int SELL_TIME = 1500;
    private final int BUYERS = 3;
    private final int CARS = 10;
    private final List<Car> cars = new ArrayList<>();
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public void sellCar() {
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + " зашел в автосалон");
            while (cars.isEmpty()) {
                System.out.println("Машин нет");
                condition.await();
            }
            Thread.sleep(SELL_TIME);
            System.out.println(Thread.currentThread().getName() + " уехал на новеньком авто");
            cars.remove(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void receiveCar() {
        for (int i = 0; i < CARS; i++) {
            try {
                Thread.sleep(PRODUCTION_TIME);
                cars.add(new Car());
                System.out.println("Производитель " + Thread.currentThread().getName() + " выпустил 1 авто");
                lock.lock();
                condition.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public void sellSeveralTimes() {
        for (int i = 0; i <= BUYERS; i++) {
            this.sellCar();
        }
    }
}

