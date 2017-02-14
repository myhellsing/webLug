package luggage.data;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Трата
 * Created by myhellsing on 11/05/15.
 */
public class Transaction  implements Serializable{

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("MM.yyyy");

    public enum TransactionType {INCOME,OUTCOME }; //"INCOME" (приход) or "OUTCOME" (расход)

    public static String COLLECTION_NAME ="Transaction";


    public ObjectId objectId;  //for mongo, id объекта
    public String name;
    public Double sum;
    public Category category;  //категория
    public Date date;   //дата
    public TransactionType type;  //"INCOME" (приход) or "OUTCOME" (расход)


    /**
     *  Конструктор
     * @param name название
     * @param sum сумма
     * @param category категория
     * @param date дата
     * @param type тип Transaction.INCOME or  Transaction.OUTCOME
     */
    public Transaction(String name, double sum, Category category, Date date, TransactionType type) {
        this.name = name;
        this.sum = sum;
        this.category = category;
        this.date = date;
        this.type=type;
    }

    //for mongo
    public Transaction(ObjectId objectId, String name, Double sum, Date date, Category category, TransactionType type) {
        this(name,sum,category,date,type);
        this.objectId = objectId;

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

    public boolean isCategoryFrom(List<String> lt){
        for (String s:lt){
            if (category !=null && category.name != null &&  category.name.contains(s.trim()))
                return true;
        }
        return false;

    }

    public String toString(){
        return String.format("%20s\t%15.0f\t%20s\t%s",this.name,this.sum,this.category.name, dateFormat.format(date));
    }

    // for mongo
    public Document createBSON(){
        return new Document().append("name", name).append("sum",sum).append("date",date);
    }

}
