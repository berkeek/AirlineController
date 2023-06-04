import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
public class ACC {
    private String name;
    private ATC[] airports;
    private Queue<Flight> runningQueue;
    private Queue<Flight> waitingQueue;
    private int time = 0;
    private int runqtime = 30;
    private Queue<Flight> startqueue;

    public Queue<Flight> getStartqueue() {
        return this.startqueue;
    }

    public void setStartqueue(Queue<Flight> startqueue) {
        this.startqueue = startqueue;
    }

    public int getRunqtime() {
        return this.runqtime;
    }

    public void setRunqtime(int runqtime) {
        this.runqtime = runqtime;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ATC[] getAirports() {
        return this.airports;
    }

    public void setAirports(ATC[] airports) {
        this.airports = airports;
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

    public int getTime()
    {
        return this.time ;
    }

    public void setTime(int time)
    {
        this.time =time;
    }

    public ACC(String name){
        this.name = name;
        this.airports = new ATC[1000];
        this.runningQueue = new LinkedList<>();
        Comparator<Flight> comparator = new FlightComparator();
        this.waitingQueue = new PriorityQueue<>(10,comparator);
        this.startqueue = new PriorityQueue<>(10,comparator);
    }
    public int hashing(String name){
        int  total = 0;
        for (int i = 0;i<3;i++){
            total += (int) name.charAt(i) * Math.pow(31, i);
        }
        return total%1000;
    }
    public int getIndex(String name){
        int hash = hashing(name);
        for (int i =0;i<1000;i++){
            if (airports[(hash+i)%1000].getName().equals(name)){
                return (hash+i)%1000;
            }
        }
        return -1;
    }
    public void add(ATC airport){
        int hash = hashing(airport.getName());
        for (int i =0;i<1000;i++){
            if (airports[(hash+i)%1000]==null){
                airports[(hash+i)%1000]=airport;
                return;
            }
        }
    }
}
