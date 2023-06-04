import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class ATC {
    private String name;
    private Queue<Flight> runningQueue;
    private Queue<Flight> waitingQueue;
    private int time = 0;
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Queue<Flight> getRunningQueue() {
        return this.runningQueue;
    }

    public void setRunningQueue(Queue<Flight> runningQueue) {
        this.runningQueue = runningQueue;
    }

    public Queue<Flight> getWaitingQueue() {
        return this.waitingQueue;
    }

    public void setWaitingQueue(Queue<Flight> waitingQueue) {
        this.waitingQueue = waitingQueue;
    }

    public int getTime() {
        return this.time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    
    public ATC(String name){
        this.name = name;
        this.runningQueue = new LinkedList<>();
        Comparator<Flight> comparator = new FlightComparator();
        this.waitingQueue = new PriorityQueue<>(10,comparator);
    }
}
