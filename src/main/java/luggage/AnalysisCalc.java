package luggage;

import GDriveData.driverToGoogleData;
import com.google.gdata.util.ServiceException;
import luggage.data.Category;
import luggage.data.MonthHistory;
import luggage.data.Transaction;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by myhellsing on 03/11/15.
 */
public class AnalysisCalc {

    public ArrayList<MonthHistory> monthHistories;
    public  String localCache="data/transactions.txt";
    public Boolean quietMode =true;
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("MM.yyyy");

    public static void main(String[] args) {
        new AnalysisCalc().run();
    }

    public ArrayList<MonthHistory> getMonthHistories() {
        if (monthHistories == null) loadMonthHistories();
        return monthHistories;
    }

    public void loadMonthHistories(){
        if (getFromLocalCache()) return;
        driverToGoogleData rd = new driverToGoogleData();
        try {
            monthHistories = rd.getMonthHistories();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        saveLocalCache();
    }

    /**
     * Достаем данные из локального файла, путь к которому указан в localCache
     * @return
     */
    public  boolean getFromLocalCache(){

        try {
            File f = new File(localCache);
            if (!f.exists()) return false;
            FileInputStream fileIn = new FileInputStream(f);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            monthHistories = (ArrayList<MonthHistory>) in.readObject();
            in.close();
            fileIn.close();

            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Сохраняем данные в локальный файл по пути, указанному в localCache
     */
    public  void saveLocalCache (){
        try {
            //кешируем траты
            FileOutputStream fileOut = new FileOutputStream(localCache);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(monthHistories);
            out.close();
            fileOut.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  Список ежемесячных трат
     */
    public HashMap<Category,Double> calcEveryMonthCategories(ArrayList<MonthHistory> monthHistories){
        TreeSet<Category> categories = new TreeSet<>();
        HashMap<Category,Double> everyMonthCategories= new HashMap<>();
        for (MonthHistory m : monthHistories){
            if (m.getSumByCategory()!= null){
                categories.addAll(m.getSumByCategory().keySet());
            }
        }
        for (Category c:categories){
            boolean wasIt = true;
            double sum = 0;
            for (MonthHistory m :monthHistories){
                //Так как не всегда мы записывали категории, добавим костыль, что проверять будем на наличие, только
                //если категорий больше 3.
                if (m.getSumByCategory().size() >3 && !m.getSumByCategory().containsKey(c)) {
                    wasIt = false;
                    break;
                }
                sum+=(m.getSumByCategory().containsKey(c)) ? m.getSumByCategory().get(c) : 0;
            }

            if (wasIt){
                everyMonthCategories.put(c, sum);
            }
        }
        return everyMonthCategories;
    }


 /*
  // сумма по названиям трат
    public void summaryByNameOfTransactions(LinkedList<Transaction> trans){
        HashMap<String,Double> hm= new HashMap<>();
        for (Transaction t :trans){
            if (hm.containsKey(t.name)){
                Double sum = hm.get(t.name)+t.sum;
                hm.put(t.name,sum);
            }
            else
                hm.put(t.name,t.sum);
        }
        for (String s:hm.keySet()){
            System.out.println(s + " " + Math.abs(hm.get(s)));
        }
    }


    public LinkedList<Transaction> getTransactionsByCategoryAliases(List<String> lt){
        LinkedList<Transaction> sortedTransactions =new LinkedList<>();
        if (!quietMode) System.out.println("Searching for " + lt.get(0));
        for (Transaction t:transactions){
            if (t.isCategoryFrom(lt)){
                sortedTransactions.add(t);
                if (!quietMode) System.out.println(t.getYear()+": "+t);
            }
        }
        if (!quietMode) System.out.println("Found "+sortedTransactions.size());
        return sortedTransactions;
    }

    public LinkedList<Transaction> getTransactionsByYear(int year, LinkedList<Transaction> trans){
        LinkedList<Transaction> sortedTransactions = new LinkedList<>();
        for (Transaction t:trans){
            if (t.getYear() == year)
                sortedTransactions.add(t);
        }
        return sortedTransactions;
    }

    public void printMoney(int currentYear, long allmoney){
        System.out.println(currentYear+": "+allmoney+" by month "+(allmoney/12) );
    }

    public void calcAuto(){
        List<String> aliases = new LinkedList<>();
        aliases.add("машина");
        aliases.add("авто");

        long allmoney =0;
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (Transaction t:getTransactionsByCategoryAliases(aliases)){
            if (currentYear!=t.getYear()){
                printMoney(currentYear, allmoney);
                currentYear=t.getYear();
                allmoney=0;

            }
            else{
                if (!t.name.contains("каско"))
                    allmoney+=t.sum;

            }

        }
        printMoney(currentYear,(allmoney));

        //2015 по статьям
        System.out.println("2015");
        sumByNameOfTransactions(getTransactionsByYear(2015, getTransactionsByCategoryAliases(aliases)));
        System.out.println("2016");
        sumByNameOfTransactions(getTransactionsByYear(2016, getTransactionsByCategoryAliases(aliases)));
    }
*/

    /**
     *  Считаем сумму по годам в зависимости от типа - Расход или Доход
     * @param monthHistories  список трат по месяцам
     * @param type тип -  INCOME или OUTCOME
     * @return
     */

    public HashMap<Integer,Double> getSummaryByYearsAndType(ArrayList<MonthHistory> monthHistories, Transaction.TransactionType type){
        HashMap<Integer,Double> summary = new HashMap<>();
        double sum =0;
        for (MonthHistory m:monthHistories){
            Calendar cal = Calendar.getInstance();
            cal.setTime(m.date);
            int year = cal.get(Calendar.YEAR);
            sum= (summary.containsKey(year) ? summary.get(year):0);
            summary.put(year, m.getSummaryByType(type) + sum);
        }
        return summary;
    }



    public void printInOutComeByYears(){
        System.out.println("Год \t\t Приход \t\t Расход \t\t Разница");
        HashMap<Integer,Double> income = getSummaryByYearsAndType(monthHistories, Transaction.TransactionType.INCOME);
        HashMap<Integer,Double> outcome = getSummaryByYearsAndType(monthHistories, Transaction.TransactionType.OUTCOME);
        ArrayList<Integer> years =new ArrayList<>();
        years.addAll(income.keySet());
        Collections.sort(years);

        double incomeAll=0;
        double outcomeAll=0;
        for (int year:years){
            System.out.printf("%d\t%,15d\t%,15d\t%,15d\n",
                    year,
                    Math.round(income.get(year)),
                    Math.round(outcome.get(year)),
                    Math.round(income.get(year) - outcome.get(year)));
            incomeAll+=income.get(year);
            outcomeAll+=outcome.get(year);
        }
        System.out.printf("Итого\t%,15d\t%,15d\t%,15d\n",
                Math.round(incomeAll),
                Math.round(outcomeAll),
                Math.round(incomeAll-outcomeAll));


    }


    public void printMonthSummary(){
        double prewBalance = 0;
        System.out.println("Дата\t  Баланс на начало\tБаланс на конец\t\tПриход\t\tРасход\t\t\tПотери");
        for (MonthHistory m:monthHistories){

            System.out.printf("%s \t %,10d \t %,10d \t %,10d \t %,10d \t %,10d \n",
                    dateFormat.format(m.date),
                    Math.round(m.balanceAtBegin),
                    Math.round(m.getCurrentBalance()),
                    Math.round(m.getSummaryIncome()),
                    Math.round(m.getSummaryOutcome()),
                    Math.round((Math.abs(prewBalance - m.balanceAtBegin)))
            );
            prewBalance = m.getCurrentBalance();
        }
    }


    public void printToConsoleEveryMonthCategories(){
        HashMap<Category,Double>  everyMonthCategories = calcEveryMonthCategories(monthHistories);
        System.out.println("Категории, которые встречаются в каждом месяце");
        for (Category c:everyMonthCategories.keySet()){
            System.out.printf("%15s\t %15.0f\n", c.name, everyMonthCategories.get(c));
        }
    }

    /**
     * Доход и расход за все года.
     */
    public void printAllIncomeAndOutcome(){
        double income =0;
        double outcome =0;
        int cntMonths = 0;
        for (MonthHistory m: monthHistories){
            income+=m.getSummaryIncome();
            outcome+=m.getSummaryOutcome();
        }
        System.out.printf("Приход:\t %15.0f\n",income);
        System.out.printf("Расход:\t %15.0f\n",outcome);
        System.out.printf("Кол-во месяцев(лет):\t %d(%d)\n",monthHistories.size(), (monthHistories.size()/12));
    }

    public void run(){
        loadMonthHistories();
       // printMonthSummary();
        //printToConsoleEveryMonthCategories();
       // printAllIncomeAndOutcome();
        printInOutComeByYears();

        System.out.println("--------------------------------------------------------");
        printMonthSummary();

      //  calcEveryMonthCategories();
     //   calcAuto();


    }


}
