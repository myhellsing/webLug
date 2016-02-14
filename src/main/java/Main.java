
/**
 * Created by myhellsing on 11/05/15.
 */

import com.google.gson.Gson;
import luggage.AnalysisCalc;
import luggage.BalanceByMonth;
import luggage.data.Transaction;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static spark.Spark.get;
import static spark.SparkBase.staticFileLocation;

public class Main {
    static Gson gson = new Gson();
    public static ArrayList<Transaction> transactions = null;
    public static ArrayList<BalanceByMonth> balanceByMonths=null;

    private static void loadTransactions() {
        AnalysisCalc analysisCalc = new AnalysisCalc();
        transactions = analysisCalc.getTransactions();
    }

    public static void main(String[] args) {
        gson = new Gson();

        staticFileLocation("/public"); // Static files
        get("/hello", (req, res) -> "Hello World");

        get("/load", (req,res) -> {
            loadTransactions();
            System.out.println("Load done. Load "+ transactions.size()+" elements");
            return transactions.size();
        });

        get("/transactions", (req,res)->{
            return transactions;
        });


        get("/income", (req,res) -> {
            if (transactions == null) loadTransactions();
            GraphData graphData = new GraphData(transactions);
            return graphData.inCome();

        }, gson::toJson);
        get("/outcome", (req,res) -> {
            if (transactions == null) loadTransactions();
            GraphData graphData = new GraphData(transactions);
            return graphData.outCome();

        }, gson::toJson);

        get("/balance",(req,res) -> {
            if (transactions == null) loadTransactions();
            return balanceByMonths;

        }, gson::toJson);

        get("/tryHtml", (req,res) ->{
            return new String(Files.readAllBytes(Paths.get("views/test.html")));

        });
        get("/balanceHtml", (req,res) ->{
            return new String(Files.readAllBytes(Paths.get("views/balance.html")));

        });
    }


}
