package miniSchedule;

public class Main {
    public static void main(String[] args)  throws InterruptedException{
        ScheduleService scheduleService = new ScheduleService();
        scheduleService.schedule(()->{
            System.out.println(1);
        },100);

        Thread.sleep(100);
        scheduleService.schedule(()->{
            System.out.println(2);
        },200);
    }
}
