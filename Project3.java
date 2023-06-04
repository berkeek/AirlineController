import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class Project3{
    public static void main(String[] args) throws FileNotFoundException{
        System.err.println(args[0]);
        System.err.println(args[1]);
        //starting priority queue of flight objects
        Comparator<Flight> comparator = new FlightComparator();

        //current priority queue of flight objects
        Queue<Flight> currentQueue = new PriorityQueue<Flight>(10,comparator);

        //arraylist of acc objects
        ArrayList<ACC> accList = new ArrayList<>();

        //arraylist of used atc objects
        ArrayList<ATC> atcList = new ArrayList<>();
        
        File file = new File(args[0]);
        Scanner sc = new Scanner(file);

        FileOutputStream output = new FileOutputStream(args[1]);
        System.setOut(new PrintStream(output));

        int accNum = sc.nextInt();
        int fNum = sc.nextInt();
        sc.nextLine();
        //adds every acc to acclist 
        //adds every atc  to hastable of acc
        for (int i=0;i<accNum;i++){
            String[] line = sc.nextLine().split(" ");
            ACC obj = new ACC(line[0]);
            for (int j = 1;j<line.length;j++){
                obj.add(new ATC(line[j]));
            }
            accList.add(obj);
        }
        // sets flight objects properties and adds them to starting priority queue
        for (int i=0;i<fNum;i++){
            String[] line = sc.nextLine().split(" ");
            Flight obj = new Flight(line[1]);
            for (int j = 0;j<accList.size();j++){
                if (accList.get(j).getName().equals(line[2])){
                    //sets acc of flight
                    obj.setAcc(accList.get(j));
                    //sets arrival atc of flight
                    int arrslot = obj.getAcc().getIndex(line[3]);
                    ATC departure = obj.getAcc().getAirports()[arrslot];
                    obj.setDeparture(departure);
                    if (!atcList.contains(departure)){
                        atcList.add(departure);
                    }
                    //sets departure atc of flight
                    int depslot = obj.getAcc().getIndex(line[4]);
                    ATC arrival = obj.getAcc().getAirports()[depslot];
                    obj.setArrival(arrival);
                    if (!atcList.contains(arrival)){
                        atcList.add(arrival);
                    }
                    break;
                }
            }
            obj.getStageTimes()[0]=Integer.parseInt(line[0]);
            for (int j = 5;j<line.length;j++){
                obj.getStageTimes()[j-4]=Integer.parseInt(line[j]);
            }
            obj.getAcc().getStartqueue().add(obj);   
        }
        sc.close();
        

        for (int l=0;l<accList.size();l++){
            boolean flag = true;
            while (flag){
                if(!accList.get(l).getStartqueue().isEmpty()){
                    currentQueue.add(accList.get(l).getStartqueue().element());
                }
                Queue<Flight> accrunq = accList.get(l).getRunningQueue();
                if(!accrunq.isEmpty()){ 
                    currentQueue.add(accrunq.element());
                }
                Queue<Flight> accwaitq = accList.get(l).getWaitingQueue();
                if(!accwaitq.isEmpty()){
                    currentQueue.add(accwaitq.element());
                }
                for (int i=0;i<1000;i++){
                    ATC atc = accList.get(l).getAirports()[i];
                    if (atc!=null){
                        Queue<Flight> atcrunq = accList.get(l).getAirports()[i].getRunningQueue();
                        if(!atcrunq.isEmpty()){
                            currentQueue.add(atcrunq.element());
                        }
                        Queue<Flight> atcwaitq = accList.get(l).getAirports()[i].getWaitingQueue();
                        if(!atcwaitq.isEmpty()){
                            currentQueue.add(atcwaitq.element());
                        }
                    }
                }
                if (currentQueue.isEmpty()){
                    flag=false;
                    break;
                }
                Flight first = currentQueue.element();
                first = first.checkRunq();
                int timetopass = Math.min(first.getAcc().getRunqtime(), first.getTime());
                currentQueue.clear();

                //updates time
                first.updateTime(timetopass);

                if(first.getTime()==0){
                    Queue<Flight> currq = getQueue(first);
                    currq.remove();
                    first.resetRunqTime();
                    if (first.getStage()!=21){
                        // stage ++ and adds to next queue
                    first.stageUp();
                    }else{
                        first.setStage(-1);
                    }        
                }else{
                    if (first.inAccRunq()){
                        first.getQueue().remove();
                        first.resetRunqTime();
                        first.getQueue().add(first);
                    }
            
                }
            }    
        }      

        //prints the output
        for (int i = 0;i<accList.size();i++){
            ACC zort = accList.get(i);
            System.out.print(zort.getName()+" "+zort.getTime()+" ");
            for (int j = 0;j<1000;j++){
                ATC obj =zort.getAirports()[j];
                if (obj!=null){
                    System.out.print(obj.getName()+parseHash(j)+" ");
                }
            }
            System.out.println("");
        }
    }
    public static String parseHash(int index){
        if (index<10){
            return "00"+index;
        }else if (index<100){
            return "0"+index;
        }else{
            return ""+index;
        }
    }
    public  static Queue<Flight> getQueue(Flight f){
        if (f.getStage()>0){
            return f.getQueue();
        }else{
            return f.getAcc().getStartqueue();
        }
    }
}