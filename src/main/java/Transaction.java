import java.util.Date;

/**
 * Created by myhellsing on 11/05/15.
 */
public class Transaction {

    public String name;
    public Double sum;
    public String category;
    public Date date;

    public Transaction(String name, double sum, String category, Date date) {
        this.name = name;
        this.sum = sum;
        this.category = category;
        this.date = date;
    }

    public String toString(){
        return this.name+"\t"+this.sum+"\t"+this.category;
    }
}
