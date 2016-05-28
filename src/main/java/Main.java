
/**
 * Created by myhellsing on 11/05/15.
 */

import com.google.gson.Gson;
import com.mitchellbosecke.pebble.loader.FileLoader;
import com.mitchellbosecke.pebble.loader.Loader;
import luggage.AnalysisCalc;
import luggage.BalanceByMonth;
import luggage.data.Transaction;
import spark.ModelAndView;
import spark.template.pebble.PebbleTemplateEngine;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;
import static spark.SparkBase.staticFileLocation;

public class Main {
    static Gson gson = new Gson();
    public static ArrayList<Transaction> transactions = null;
    public static ArrayList<BalanceByMonth> balanceByMonths=null;

    private static void loadTransactions() {
        AnalysisCalc analysisCalc = new AnalysisCalc();
        transactions = analysisCalc.getTransactions();
        balanceByMonths=analysisCalc.balanceByMonths;
    }

    public static void main(String[] args) {
        gson = new Gson();

        staticFileLocation("/public"); // Static files

        Loader loader = new FileLoader();
        loader.setPrefix("views/");

        get("/hello", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("message", "Hello World!");

            return new ModelAndView(attributes, "hello.pebble");
        }, new PebbleTemplateEngine(loader));

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
