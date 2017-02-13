import luggage.AnalysisCalc;
import luggage.data.Category;
import luggage.data.MonthHistory;
import luggage.data.Transaction;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by myhellsing on 12/02/17.
 */
public class AnalysisCalcTest {

    ArrayList<MonthHistory> monthHistories;
    AnalysisCalc analysisCalc;

    @Before
    public void createData(){
        monthHistories = new ArrayList<>();
        analysisCalc= new AnalysisCalc();
        Date date = null;
        try {
            date = AnalysisCalc.dateFormat.parse("03.2016");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal  = Calendar.getInstance();
        cal.setTime(date);

        ArrayList<Transaction> transaction1 =  new ArrayList<>();
        transaction1.add(new Transaction("Utkonos",100.0, new Category("Eat"), date, Transaction.TransactionType.OUTCOME));
        transaction1.add(new Transaction("Magnolia",120.0, new Category("Eat"), date, Transaction.TransactionType.OUTCOME));
        transaction1.add(new Transaction("Qlean", 500.0, new Category("Home"), date, Transaction.TransactionType.OUTCOME));
        transaction1.add(new Transaction("Internet",50.0, new Category("Communication"), date, Transaction.TransactionType.OUTCOME));
        transaction1.add(new Transaction("Dress",1000.0, new Category("Clothes"), date, Transaction.TransactionType.OUTCOME));
        transaction1.add(new Transaction("Present for colleague",100.0, new Category("Present"), date, Transaction.TransactionType.OUTCOME));

        MonthHistory m1= new MonthHistory(date);
        m1.balanceAtBegin=0;
        m1.transactions =  transaction1;

        cal.add(Calendar.MONTH, 1);
        date = cal.getTime();

        ArrayList<Transaction> transaction2 =  new ArrayList<>();
        transaction2.add(new Transaction("Utkonos",200.0, new Category("Eat"), date, Transaction.TransactionType.OUTCOME));
        transaction2.add(new Transaction("Magnolia",220.0, new Category("Eat"), date, Transaction.TransactionType.OUTCOME));
        transaction2.add(new Transaction("Qlean", 500.0, new Category("Home"), date, Transaction.TransactionType.OUTCOME));
        transaction2.add(new Transaction("Internet",50.0, new Category("Communication"), date, Transaction.TransactionType.OUTCOME));
        transaction2.add(new Transaction("Зарплата",25000.0, new Category("Приход"), date, Transaction.TransactionType.INCOME));
        transaction2.add(new Transaction("Аванс",10000.0, new Category("Приход"), date, Transaction.TransactionType.INCOME));

        MonthHistory m2= new MonthHistory(date);
        m2.balanceAtBegin=0;
        m2.transactions =  transaction2;

        cal.add(Calendar.MONTH,1);
        date = cal.getTime();

        ArrayList<Transaction> transaction3 =  new ArrayList<>();
        transaction3.add(new Transaction("Utkonos",200.0, new Category("Eat"), date, Transaction.TransactionType.OUTCOME));
        transaction3.add(new Transaction("Magnolia",200.0, new Category("Eat"), date, Transaction.TransactionType.OUTCOME));
        transaction3.add(new Transaction("Qlean", 500.0, new Category("Home"), date, Transaction.TransactionType.OUTCOME));
        transaction3.add(new Transaction("Internet",50.0, new Category("Communication"), date, Transaction.TransactionType.OUTCOME));
        transaction3.add(new Transaction("Iphone", 50000.0, new Category("Gadgets"), date, Transaction.TransactionType.OUTCOME));
        transaction3.add(new Transaction("Aqualor",50.0, new Category("Medicine"), date, Transaction.TransactionType.OUTCOME));


        MonthHistory m3= new MonthHistory(date);
        m3.balanceAtBegin=0;
        m3.transactions =  transaction3;

        monthHistories.add(m1);
        monthHistories.add(m2);
        monthHistories.add(m3);
    }


    @Test
    public void testEveryMonthTransaction(){
        HashMap<Category,Double> expected = new HashMap<>();
        expected.put(new Category("Eat"),1040.0);
        expected.put(new Category("Home"),1500.0);
        expected.put(new Category("Communication"),150.0);
        assertEquals(expected,analysisCalc.calcEveryMonthCategories(monthHistories));

    }

    @Test
    public void testGetSummaryByYearsAndType(){
        HashMap<Integer,Double> expected =  new HashMap<>();

        //checkOutCome
        expected.put(2016,53840.0);
        assertEquals(expected, analysisCalc.getSummaryByYearsAndType(monthHistories, Transaction.TransactionType.OUTCOME));

        //checkInCome
        expected.put(2016,35000.0);
        assertEquals(expected, analysisCalc.getSummaryByYearsAndType(monthHistories, Transaction.TransactionType.INCOME));
    }
}
