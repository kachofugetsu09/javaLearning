package ThreadPool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        MyThreadPool myThreadPool = new MyThreadPool(2,
                4,
                1,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(4),
                new ThrowRejectHandle());

        // 提交比核心线程能处理的更多的任务
        for(int i=0; i<6; i++) {
            final int taskNum = i;
            myThreadPool.execute(()->{
                try {
                    Thread.sleep(500);
                    System.out.println("任务 " + taskNum + " 由线程 " + Thread.currentThread().getName() + " 执行");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        System.out.println("主线程未被阻塞");
    }


}
