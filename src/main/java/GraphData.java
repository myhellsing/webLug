import luggage.data.Transaction;

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

    public ArrayList<GraphPoint> come(Transaction.TransactionType type){
        ArrayList<GraphPoint> out = new ArrayList<>();
        int lastMonth = Calendar.getInstance().get(Calendar.MONTH);
        double currentOutAtMonth = 0;
        for (Transaction t:transactions){
            if (t.type.equals(type) ){
                if ( lastMonth != t.getMonth()){
                    out.add(new GraphPoint(t.date, currentOutAtMonth));
                    lastMonth=t.getMonth();
                    currentOutAtMonth=0;
                }
                currentOutAtMonth+=t.sum;
            }

        }

        return out;
    };

    public ArrayList<GraphPoint> outCome(){
        return come(Transaction.TransactionType.INCOME);
    };
    public ArrayList<GraphPoint> inCome(){
        return come(Transaction.TransactionType.INCOME);
    };

}
