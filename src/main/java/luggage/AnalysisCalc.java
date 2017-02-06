package luggage;

import GDriveData.driverToGoogleData;
import com.google.gdata.util.ServiceException;
import luggage.data.Category;
import luggage.data.MonthHistory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * Created by myhellsing on 03/11/15.
 */
public class AnalysisCalc {

    public ArrayList<MonthHistory> monthHistories;
    public  String localCache="data/transactions.txt";
    public Boolean quietMode =true;
    public SimpleDateFormat dateFormat = new SimpleDateFormat("MM.yyyy");

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
    public void calcEveryMonthtransaction(){
        TreeSet<Category> categories = new TreeSet<>();
        HashMap<Category,Double> answer= new HashMap<>();
        for (MonthHistory m : monthHistories){
            if (m.getSumByCategory()!= null){
                categories.addAll(m.getSumByCategory().keySet());
            }
        }
        for (Category c:categories){
            boolean wasIt = true;
            double sum = 0;
            for (MonthHistory m :monthHistories){
                if (!m.getSumByCategory().containsKey(c)) {
                    wasIt = false;
                    break;
                }
                sum+=m.getSumByCategory().get(c);
            }

            if (wasIt){
                answer.put(c, sum);
            }
        }
        System.out.println("Категории, которые встречаются в каждом месяце");
        for (Category c:answer.keySet()){
            System.out.println(c.name+"\t\n"+answer.get(c));
        }

    }

 /*   // сумма по названиям трат

    public void sumByNameOfTransactions(LinkedList<Transaction> trans){
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

    public void printMonthSummary(){
        double prewBalance = 0;
        System.out.println("Дата\tБаланс на начало\tПриход\tРасход\tПотери");
        for (MonthHistory m:monthHistories){

            System.out.println(dateFormat.format(m.date)+
                            "\t"+m.balanceAtBegin+
                            "\t"+m.getCurrentBalance()+
                            "\t"+m.getSummaryIncome()+
                            "\t"+m.getSummaryOutcome()+
                            "\t"+(Math.abs(prewBalance-m.balanceAtBegin))
            );
            prewBalance = m.getCurrentBalance();
        }
    }

    public void run(){
        loadMonthHistories();
        printMonthSummary();
      //  calcEveryMonthtransaction();
     //   calcAuto();
    }


}
