package luggage;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by myhellsing on 11/05/15.
 */
public class Transaction implements Serializable{

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

    public int getMonth(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.date);
        return calendar.get(Calendar.MONTH);
    }

    public int getYear(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.date);
        return calendar.get(Calendar.YEAR);
    }

    public boolean categoryFrom(List<String> lt){
        for (String s:lt){
            if (category !=null && category.contains(s.trim()))
                return true;
        }
        return false;

    }

    public String toString(){
        return this.name+"\t"+this.sum+"\t"+this.category;
    }
}
