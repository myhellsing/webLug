import luggage.Transaction;

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
        int lastMonth = Calendar.getInstance().get(Calendar.MONTH);
        double currentOutAtMonth = 0;
        for (Transaction t:transactions){
            if (t.sum*what <=0 ){
                if ( lastMonth != t.getMonth()){
                    out.add(new GraphPoint(t.date, currentOutAtMonth*what*(-1)));
                    lastMonth=t.getMonth();
                    currentOutAtMonth=0;
                }
                currentOutAtMonth+=t.sum;
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
