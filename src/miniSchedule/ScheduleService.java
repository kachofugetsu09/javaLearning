package miniSchedule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.LockSupport;

public class ScheduleService {
    Trigger trigger = new Trigger();
    ExecutorService executorService = Executors.newFixedThreadPool(6);
    void  schedule(Runnable task,long delay){
        Job job = new Job(task,System.currentTimeMillis()+delay);
        job.setDelay(delay);
        trigger.queue.offer(job);
        trigger.wakeUp();
    }

    class Trigger{

        PriorityBlockingQueue<Job> queue = new PriorityBlockingQueue<>();
        Thread thread = new Thread(()->{
            while(true){
                while(queue.isEmpty()){
                    LockSupport.park();
                }
                Job peek = queue.peek();
                if(peek.getStartTime()<System.currentTimeMillis()){
                    peek = queue.poll();
                    executorService.execute(peek.getTask());
                    Job nextJob = new Job();
                    nextJob.setTask(peek.getTask());
                    nextJob.setDelay(peek.getDelay());
                    nextJob.setStartTime(System.currentTimeMillis()+peek.getDelay());
                    queue.offer(nextJob);

                }else{
                    LockSupport.parkUntil(peek.getStartTime());
                }
            }
        });

        {
            thread.start();
            System.out.println("trigger start");
        }

        void wakeUp(){
            LockSupport.unpark(thread);
        }
    }



}
