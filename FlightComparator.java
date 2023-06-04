import java.util.Comparator;

public class FlightComparator implements Comparator<Flight>{

    @Override
    public int compare(Flight o1, Flight o2) {
                           
        int time1 =Math.min(o1.restrictionTime(),o1.getTime());
        int time2 =Math.min(o2.restrictionTime(),o2.getTime());

        if (time1==time2){
            if(o1.getTime()-time1==0 && o2.getTime()-time2==0){
                return o1.getName().compareTo(o2.getName());
                
            }if(o1.getTime()-time1!=0 && o2.getTime()-time2==0){
                if(o1.collision(o2)){
                    return 1;
                }return -1;
            }if(o1.getTime()-time1==0 && o2.getTime()-time2!=0){
                if(o2.collision(o1)){
                    return -1;
                }return 1;
            }else{
                return o1.getName().compareTo(o2.getName());
                
            }
        }
        else{
            return time1- time2;
        } 
    }
    
}
