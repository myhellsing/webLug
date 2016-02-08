package luggage;

import GDriveData.driverToGoogleData;
import com.google.gdata.util.ServiceException;

import java.io.*;
import java.util.*;

/**
 * Created by myhellsing on 03/11/15.
 */
public class AnalysisCalc {
    public  ArrayList<Transaction> transactions = null;
    public  ArrayList<BalanceByMonth> balanceByMonths=null;
    public  String localCache="data/transactions.txt";

    public static void main(String[] args) {
        new AnalysisCalc().run();
    }

    public ArrayList<Transaction> getTransactions() {
        if (transactions == null) loadTransactions();
        return transactions;
    }

    public void loadTransactions(){
        if (getFromLocalCache()) return;
        driverToGoogleData rd = new driverToGoogleData();
        try {
            transactions = rd.getTransactions();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        balanceByMonths=rd.balanceByMonths;
        saveLocalCache();
    }

    public  boolean getFromLocalCache(){

        try {
            File f = new File(localCache);
            if (!f.exists()) return false;
            FileInputStream fileIn = new FileInputStream(f);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            transactions = (ArrayList<Transaction>) in.readObject();
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
            FileOutputStream fileOut = new FileOutputStream(localCache);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(transactions);
            out.close();
            fileOut.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void calcEveryMonthtransaction(){
        TreeSet<String> nameOfTransactions = new TreeSet<>();
        for (Transaction t : transactions){
            if (!nameOfTransactions.contains(t.name)) nameOfTransactions.add(t.name);
        }
        ArrayList<String> answer = new ArrayList<>();
        for (String name:nameOfTransactions){
            boolean wasIt = true;
            boolean wasItInMonth =  (transactions.get(6).name.compareTo(name) ==0 ) ? true:false;
            for (int i=7;i<transactions.size();i++){

                wasItInMonth = wasItInMonth || ((transactions.get(i).name.compareTo(name) ==0 ) ? true:false);
                if (transactions.get(i-1).getMonth() != transactions.get(i).getMonth()){
                    if (wasItInMonth) {

                        wasItInMonth= false;
                    }
                    else {
                        wasIt = false;
                    //    continue;
                    }
                }
            }
            if (wasIt){
                answer.add(name);
            }
            wasIt=true;
        }
        System.out.println("Категории, которые встречаются в каждом месяце");
        for (String n:answer){
            System.out.println(n);
        }


    }

    // сумма по названиям трат

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
        System.out.println("Searching for " + lt.get(0));
        for (Transaction t:transactions){
            if (t.categoryFrom(lt)){
                sortedTransactions.add(t);
                System.out.println(t.getYear()+": "+t);
            }
        }
        System.out.println("Found "+sortedTransactions.size());
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
        System.out.println(currentYear+": "+(-1)*allmoney+" by month "+((-1)*allmoney/12) );
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
        sumByNameOfTransactions(getTransactionsByYear(2015, getTransactionsByCategoryAliases(aliases)));
    }

    public void run(){
        loadTransactions();
       // calcEveryMonthtransaction();
        calcAuto();
    }

}
