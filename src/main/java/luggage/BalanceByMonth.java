package luggage;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by myhellsing on 04/09/15.
 */
public class BalanceByMonth implements Serializable {

    public Date date;
    public double balance;

    public BalanceByMonth( Date date,double balance) {
        this.balance = balance;
        this.date = date;
    }
}
