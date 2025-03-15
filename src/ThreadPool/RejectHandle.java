package ThreadPool;

public interface RejectHandle {
    void reject(Runnable rejectCommand,MyThreadPool myThreadPool);
}
