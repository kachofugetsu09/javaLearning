package miniSchedule;

public class Job implements Comparable<Job>{
    private Runnable task;
    private long startTime;

    private long delay;

        public Job(Runnable task, long startTime) {
            this.task = task;
            this.startTime = startTime;
        }

    public Job() {

    }

    public Job setTask(Runnable task){
            this.task = task;
            return this;
        }

        public long getStartTime(){
            return startTime;
        }
        public Runnable getTask(){
            return task;
        }

        public long getDelay(){
            return delay;
        }

    @Override
    public int compareTo(Job o) {
        return Long.compare(this.startTime,o.startTime);
    }

    public void setDelay(long delay) {
            this.delay = delay;
    }

    public void setStartTime(long l) {
            this.startTime = l;
    }
}
