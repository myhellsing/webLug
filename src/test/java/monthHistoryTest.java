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
 * Created by myhellsing on 06/02/17.
 */
public class monthHistoryTest {

    MonthHistory monthHistory;
    ArrayList<MonthHistory> monthHistories;
    Date dateFrom;
    Date dateTo;
    @Before
    public void loadMontHistorySeptember2016(){
        AnalysisCalc analysisCalc = new AnalysisCalc();
        monthHistories = analysisCalc.getMonthHistories();
        for (MonthHistory m:monthHistories){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(m.date);
            if (calendar.get(Calendar.MONTH) ==8 && calendar.get(Calendar.YEAR)==2016 ){
                monthHistory=m;
            }
        }
        try {
            dateTo= analysisCalc.dateFormat.parse("01.2017");
            dateFrom= analysisCalc.dateFormat.parse("01.2016");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHelloWorld(){
        String hello="HelloWorld";
        assertEquals("HelloWorld",hello);
    }

    @Test
    public void testBalanceAtBegin(){
        assertEquals(569483,monthHistory.balanceAtBegin,0.01);
    }
    @Test
    public void testGetCurrentBalance(){
        assertEquals(586216,monthHistory.getCurrentBalance(),0.01);
    }

    @Test
    public void testGetSummaryIncome(){
        assertEquals(380704,monthHistory.getSummaryIncome(),0.01);
    }
    @Test
    public void testGetSummaryOutcome(){
        assertEquals(363971,monthHistory.getSummaryOutcome(),0.01);
    }
    @Test
    public void testTransactionSize(){
        assertEquals(60,monthHistory.transactions.size());
    }

    @Test
    public void testAllIncomeByMonth(){
        ArrayList<Double> incomeExpected = new ArrayList<Double>(Arrays.asList
                (564521.0, 767124.0, 966720.0,380704.0,149604.0,358263.0,329427.0,629770.0,231543.0,276582.0,329106.0,137700.0));
        ArrayList<Double> incomeActual = new ArrayList<>();
        monthHistories.forEach(( m ) -> { if (m.date.after(dateFrom) && m.date.before(dateTo)) incomeActual.add(m.getSummaryIncome()); } );
        Collections.reverse(incomeActual);
        assertEquals(incomeExpected, incomeActual);

    }
    @Test
    public void testAllOutComeByMonth(){
        ArrayList<Double> outcomeExpected = new ArrayList<Double>(Arrays.asList
                (285724.0, 438368.0, 1176954.0,363971.0,1150741.0,170924.0,176384.0,300503.0,142771.0,516748.0,318299.0,430436.0));
        ArrayList<Double> outcomeActual = new ArrayList<>();
        monthHistories.forEach(( m ) -> { if (m.date.after(dateFrom) && m.date.before(dateTo)) outcomeActual.add(m.getSummaryOutcome()); } );
        Collections.reverse(outcomeActual);
        assertEquals(outcomeExpected,outcomeActual);

    }

    @Test
    public void testgetSumByCategory(){
        ArrayList<Transaction> transaction1 =  new ArrayList<>();
        transaction1.add(new Transaction("Utkonos",100.0, new Category("Eat"), new Date(), Transaction.TransactionType.OUTCOME));
        transaction1.add(new Transaction("Magnolia",120.0, new Category("Eat"), new Date(), Transaction.TransactionType.OUTCOME));
        transaction1.add(new Transaction("Qlean", 500.0, new Category("Home"), new Date(), Transaction.TransactionType.OUTCOME));
        transaction1.add(new Transaction("Internet",50.0, new Category("Communication"), new Date(), Transaction.TransactionType.OUTCOME));
        transaction1.add(new Transaction("Dress",1000.0, new Category("Clothes"), new Date(), Transaction.TransactionType.OUTCOME));
        transaction1.add(new Transaction("Shirts",300.0, new Category("Clothes"), new Date(), Transaction.TransactionType.OUTCOME));

        MonthHistory m1= new MonthHistory(new Date());
        m1.balanceAtBegin=0;
        m1.transactions =  transaction1;

        HashMap<Category,Double> expected = new HashMap<>();

        expected.put(new Category("Eat"),220.0);
        expected.put(new Category("Home"),500.0);
        expected.put(new Category("Communication"),50.0);
        expected.put(new Category("Clothes"),1300.0);


        HashMap<Category,Double> actual = m1.getSumByCategory();

        assertEquals(expected,actual);
    }

}
