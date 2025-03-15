package ThreadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class MyThreadPool {
    private final int corePoolSize;
    private final int maxSize;
    private final int timeout;
    private final TimeUnit timeUnit;
    private final BlockingQueue<Runnable> blockingQueue;
    private final RejectHandle rejectHandle;

    public MyThreadPool(int corePoolSize,
                        int maxSize,
                        int timeout,
                        TimeUnit timeUnit,
                        BlockingQueue<Runnable> blockingQueue,
                        RejectHandle rejectHandle) {
        this.corePoolSize = corePoolSize;
        this.maxSize = maxSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.blockingQueue = blockingQueue;
        this.rejectHandle = rejectHandle;
    }





    List<Thread> coreList = new ArrayList<>();
    List<Thread> supportList = new ArrayList<>();

    //TODO 有线程安全
    void execute(Runnable command) {
        if (coreList.size() < corePoolSize) {
            Thread thread = new CoreThread();
            coreList.add(thread);
            thread.start();
            blockingQueue.offer(command);
            return;
        }

        if (blockingQueue.offer(command)) {
            return;
        }

        if (coreList.size() + supportList.size() < maxSize) {
            Thread thread = new SupportThread();
            supportList.add(thread);
            thread.start();
            blockingQueue.offer(command);
            return;
        }

        rejectHandle.reject(command, MyThreadPool.this);
    }


    class CoreThread extends Thread{
        @Override
        public void run(){
                while(true){
                    try{
                        Runnable command = (Runnable) blockingQueue.take();
                        command.run();
                    }catch (InterruptedException e){
                        throw new RuntimeException();
                    }
                }
            }};


    class SupportThread extends Thread{
        @Override
        public void run(){
            while(true){
                try{
                    Runnable command = (Runnable) blockingQueue.poll(timeout, timeUnit);
                    if(command==null){
                        break;
                    }
                    command.run();
                }catch (InterruptedException e){
                    throw new RuntimeException();
                }
            }
            System.out.println(Thread.currentThread().getName()+"线程结束了");
        }
    }

}
