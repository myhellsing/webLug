import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by myhellsing on 25/05/15.
 */
public class GraphData {

    public ArrayList<Transaction> transactions = null;

    public GraphData(ArrayList<Transaction> transactions) {
        this.transactions  = transactions;
    }

    public ArrayList<GraphPoint> come(int what){
        ArrayList<GraphPoint> out = new ArrayList<>();
        Calendar calendarCurrentMonth = null;
        double currentOutAtMonth = 0;
        for (int i=0;i<transactions.size();i++){
            if (transactions.get(i).sum*what <=0 ){
                Calendar calendarOfOutCome = Calendar.getInstance();
                calendarOfOutCome.setTime(transactions.get(i).date);
                if (calendarCurrentMonth == null) calendarCurrentMonth = Calendar.getInstance();
                if (calendarCurrentMonth.get(Calendar.MONTH) != calendarOfOutCome.get(Calendar.MONTH) ) {
                    out.add(new GraphPoint(calendarCurrentMonth.getTime(), currentOutAtMonth*what*(-1)));
                    calendarCurrentMonth = calendarOfOutCome;
                    calendarCurrentMonth.set(Calendar.DATE,1);
                    currentOutAtMonth=0;
                    System.out.println(calendarCurrentMonth.get(Calendar.MONTH) + " " +calendarCurrentMonth.get(Calendar.YEAR));
                }
                currentOutAtMonth+=transactions.get(i).sum;
            }

        }

        return out;
    };

    public ArrayList<GraphPoint> outCome(){
        return come(1);
    };
    public ArrayList<GraphPoint> inCome(){
        return come(-1);
    };

}
