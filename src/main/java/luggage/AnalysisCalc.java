package luggage;

import GDriveData.driverToGoogleData;
import com.google.gdata.util.ServiceException;

import java.io.*;
import java.util.ArrayList;
import java.util.TreeSet;

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
        driverToGoogleData rd = new driverToGoogleData("https://spreadsheets.google.com/feeds/spreadsheets/0Aoa5WkgCFdrudEhzcnE0bU83QksteENZS3puSTZJRUE");
        try {
            transactions = rd.getTransactions();
            balanceByMonths=rd.balanceByMonths;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        saveLocalCache();
    }

    public  boolean getFromLocalCache(){

        try {
            FileInputStream fileIn = new FileInputStream(localCache);
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

    public void run(){
        loadTransactions();
        calcEveryMonthtransaction();
    }

}
