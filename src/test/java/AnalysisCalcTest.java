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
        Date date = null;
        try {
            date = Transaction.dateFormat.parse("03.2016");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Month 1
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

        //Month 2
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

        //Month 3
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

        //Month 4
        cal.add(Calendar.MONTH,1);
        date = cal.getTime();
        ArrayList<Transaction> transaction4 =  new ArrayList<>();
        transaction4.add(new Transaction("Utkonos",200.0, new Category("Eat"), date, Transaction.TransactionType.OUTCOME));
        transaction4.add(new Transaction("Magnolia",220.0, new Category("Eating"), date, Transaction.TransactionType.OUTCOME));
        transaction4.add(new Transaction("Qlean", 500.0, new Category("House"), date, Transaction.TransactionType.OUTCOME));
        transaction4.add(new Transaction("Cup", 100.0, new Category("Home"), date, Transaction.TransactionType.OUTCOME));
        transaction4.add(new Transaction("Internet", 500.0, new Category("Communication"), date, Transaction.TransactionType.OUTCOME));
        transaction4.add(new Transaction("Something",100.0, new Category(Category.UNKNOWN), date, Transaction.TransactionType.OUTCOME));
        MonthHistory m4= new MonthHistory(date);
        m4.balanceAtBegin=0;
        m4.transactions =  transaction4;


        monthHistories.add(m1);
        monthHistories.add(m2);
        monthHistories.add(m3);
        monthHistories.add(m4);

        analysisCalc= new AnalysisCalc(monthHistories);
    }


    @Test
    public void testEveryMonthTransaction(){
        HashMap<Category,Double> expected = new HashMap<>();
        expected.put(new Category("Eat"),1240.0);
        expected.put(new Category("Home"),1600.0);
        expected.put(new Category("Communication"),650.0);

        HashMap<Category,Double> actual = analysisCalc.calcEveryMonthCategories();
        assertEquals(expected.size(),actual.size());
        assertEquals(expected,actual);

    }

    @Test
    public void testGetSummaryByYearsAndType(){
        HashMap<Integer,Double> expected =  new HashMap<>();

        //checkOutCome
        expected.put(2016,55460.0);
        assertEquals(expected, analysisCalc.getSummaryByYearsAndType(Transaction.TransactionType.OUTCOME));

        //checkInCome
        expected.put(2016,35000.0);
        assertEquals(expected, analysisCalc.getSummaryByYearsAndType( Transaction.TransactionType.INCOME));
    }

    @Test
    public void testGetSummaryByNameOfTransactions(){
        LinkedHashMap<String,Double> expected =  new LinkedHashMap<>();

        expected.put("Utkonos",700.0);
        expected.put("Magnolia",760.0);
        expected.put("Qlean",2000.0);
        expected.put("Internet",650.0);
        expected.put("Dress",1000.0);
        expected.put("Present for colleague",100.0);
        expected.put("Зарплата",25000.0);
        expected.put("Аванс",10000.0);
        expected.put("Iphone",50000.0);
        expected.put("Aqualor",50.0);
        expected.put("Something",100.0);
        expected.put("Cup",100.0);



        LinkedHashMap<String,Double> actual  = analysisCalc.getSummaryByNameOfTransactions();
        assertEquals(expected.size(),actual.size());
        assertEquals(expected,actual);

        //проверим, что отсортировалось по убыванию
        long max =0;
        for (String s:actual.keySet()){
            max=Math.round(actual.get(s));
            break;
        }
        assertEquals(50000,max);

    }

    @Test
    public void testGetSummaryByCategoryName(){
        LinkedHashMap<Category,Double> expected =  new LinkedHashMap<>();

        expected.put(new Category("Gadgets"),50000.0);
        expected.put(new Category("Приход"),35000.0);
        expected.put(new Category("Home"),1600.0);
        expected.put(new Category("Eat"),1240.0);
        expected.put(new Category("Clothes"),1000.0);
        expected.put(new Category("Communication"),650.0);
        expected.put(new Category("House"),500.0);
        expected.put(new Category("Eating"),220.0);
        expected.put(new Category("Present"),100.0);
        expected.put(new Category(Category.UNKNOWN),100.0);
        expected.put(new Category("Medicine"),50.0);



        LinkedHashMap<Category,Double> actual = analysisCalc.getSummaryByCategoryName();

        assertEquals(expected.size(),actual.size());
        assertEquals(expected,actual);

    }

    @Test
    public void testGetTransactionsByCategory(){
        LinkedList<Transaction> expected = new LinkedList<>();
        expected.add(monthHistories.get(0).transactions.get(0));
        expected.add(monthHistories.get(0).transactions.get(1));
        expected.add(monthHistories.get(1).transactions.get(0));
        expected.add(monthHistories.get(1).transactions.get(1));
        expected.add(monthHistories.get(2).transactions.get(0));
        expected.add(monthHistories.get(2).transactions.get(1));
        expected.add(monthHistories.get(3).transactions.get(0));

        assertEquals(expected,analysisCalc.getTransactionsByCategory(new Category("Eat")));
    }


    @Test
    public void testGenerateCategoryAliases(){

        ArrayList<Category> expected = new ArrayList<>();
        expected.add(new Category("Eat"));
        expected.get(0).aliases.add("Eating");
        expected.add(new Category("Home"));
        expected.get(1).aliases.add("House");

        expected.add(new Category("Communication"));
        expected.add(new Category("Clothes"));
        expected.add(new Category("Present"));
        expected.add(new Category("Gadgets"));
        expected.add(new Category("Medicine"));

        AnalysisCalc analysisCalc1=new AnalysisCalc(monthHistories);
        ArrayList<Category> actual =analysisCalc1.generateCategoryAliases();
        assertEquals(expected.size(),actual.size());

        Collections.sort(expected);
        Collections.sort(actual);


        for (Category expectedCategory:expected){
             if (actual.contains(expectedCategory)){
                 Category actualCategory = actual.get(actual.indexOf(expectedCategory));
                 assertEquals(expectedCategory.name,actualCategory.name);
                 assertEquals(expectedCategory.aliases,actualCategory.aliases);
             }
        }
    }

    @Test
    public void testGetTransactionsByCategoryAndAliases(){
        ArrayList<Transaction> expected = new ArrayList<>();
        Category category =  new Category("Eat");

        for (MonthHistory m:monthHistories){
            for (Transaction t:m.transactions){
                if (t.category.compareTo(category)==0){
                    expected.add(t);
                }
            }
        }

        assertEquals(expected,analysisCalc.getTransactionsByCategory(category));

        //TODO проверить еще случай с алиасами категорий

    }

}
