
import java.util.Queue;

public class Flight {
    private String name;
    private ACC acc;
    private ATC arrival;
    private ATC departure;
    private int stage = 0;
    private int[] stageTimes = new int[22];
    public int prevtime=0;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ACC getAcc() {
        return this.acc;
    }

    public void setAcc(ACC acc) {
        this.acc = acc;
    }

    public ATC getArrival() {
        return this.arrival;
    }

    public void setArrival(ATC arrival) {
        this.arrival = arrival;
    }

    public ATC getDeparture() {
        return this.departure;
    }

    public void setDeparture(ATC departure) {
        this.departure = departure;
    }

    public int getStage() {
        return this.stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public int[] getStageTimes() {
        return this.stageTimes;
    }

    public void setStageTimes(int[] stageTimes) {
        this.stageTimes = stageTimes;
    }

    public Flight(String name){
        this.name = name;
    }

    public int getTime(){
        return stageTimes[stage];
    }

    public void setTime(int time){
        stageTimes[stage]=time;
    }

    public void stageUp(){
        if (this.stage==0 || this.stage==2 || this.stage==10 || this.stage==12 || this.stage==20){
            this.stage++;
            this.acc.getRunningQueue().add(this);
            return;
        }if (this.stage==1 || this.stage==11){
            this.stage++;
            this.acc.getWaitingQueue().add(this);
            return;
        }if (this.stage==3 || this.stage==5 || this.stage==7 || this.stage==9){
            this.stage++;
            this.departure.getRunningQueue().add(this);
            return;
        }if (this.stage==13 || this.stage==15 || this.stage==17 || this.stage==19){
            this.stage++;
            this.arrival.getRunningQueue().add(this);
            return;
        }if (this.stage==4 || this.stage==6 || this.stage==8){
            this.stage++;
            this.departure.getWaitingQueue().add(this);
            return;
        }if (this.stage==14 || this.stage==16 || this.stage==18){
            this.stage++;
            this.arrival.getWaitingQueue().add(this);
            return;
        }
    }
    public Queue<Flight> getQueue(){
        if (this.stage==1 || this.stage==3 || this.stage==11 || this.stage==13 || this.stage==21){
            return this.acc.getRunningQueue();
        }if (this.stage==2 || this.stage==12){
            return this.acc.getWaitingQueue();
        }if (this.stage==4 || this.stage==6 || this.stage==8 || this.stage==10){
            return this.departure.getRunningQueue();
        }if (this.stage==14 || this.stage==16 || this.stage==18 || this.stage==20){
            return this.arrival.getRunningQueue();
        }if (this.stage==5 || this.stage==7 || this.stage==9){
            return this.departure.getWaitingQueue();
        }if (this.stage==15 || this.stage==17 || this.stage==19){
            return this.arrival.getWaitingQueue();
        }return null;
    }

    public void updateTime(int timetopass){
        this.getAcc().setTime(this.getAcc().getTime()+timetopass);
        if(!this.getAcc().getRunningQueue().isEmpty()){
            this.getAcc().setRunqtime(this.getAcc().getRunqtime()-timetopass);
        }
        Queue <Flight> startq = this.getAcc().getStartqueue();
        for (Flight f : startq){
            f.setTime(f.getTime()-timetopass);
        }
        for (ATC atc : this.getAcc().getAirports()){
            if (atc!=null){
                for (Flight f : atc.getWaitingQueue()){
                    if (f!=null){
                        f.setTime(f.getTime()-timetopass);
                    }
                }
                Flight head = atc.getRunningQueue().peek();
                if (head!=null){
                    head.setTime(head.getTime()-timetopass);
                }
            }
        }

        for (Flight f : this.getAcc().getWaitingQueue()){
            if (f!=null){
                f.setTime(f.getTime()-timetopass);
            }
        }
        Flight head= this.getAcc().getRunningQueue().peek();
        if(head!=null){
            head.setTime(head.getTime()-timetopass);
        }
    }
    
    public void resetRunqTime(){
        if (this.stage==1 || this.stage==3 || this.stage==11 || this.stage==13 || this.stage==21){
            this.getAcc().setRunqtime(30);
        }
    }
    public boolean inAccRunq(){
        if (this.stage==1 || this.stage==3 || this.stage==11 || this.stage==13 || this.stage==21){
            return true;
        }return false;
    }

    public Flight checkRunq(){
        int mintime = this.getAcc().getRunqtime();
        Queue<Flight> from = this.getAcc().getRunningQueue();
        
        if (mintime<this.getTime()){
            if (!from.isEmpty()){
                return from.element();
            }
            return this;
        }return this;
    }
    public int getMintime(){
        int mintime = this.getAcc().getRunqtime();
        return mintime;
    }
    public int restrictionTime(){
        if (this.stage==1 || this.stage==3 || this.stage==11 || this.stage==13 || this.stage==21){
            return this.getAcc().getRunqtime();
        }return this.getTime();
    }
    public boolean collision(Flight f){
        if (this.stage==1 || this.stage==3 || this.stage==11 || this.stage==13 || this.stage==21){
            if (f.stage==0 || f.stage==2 || f.stage==10 || f.stage==12 || f.stage==20){
                return true;
            }return false;
        }return false;
    }

    @Override
    public String toString() {
        return "Flight [name=" + name + ", "+"operation time:" + stageTimes[stage]+" stage:"+stage+ "]";
    }
    
}
