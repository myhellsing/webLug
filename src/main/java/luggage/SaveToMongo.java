package luggage;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoDatabase;
import luggage.data.Category;
import luggage.data.CategoryCodecProvider;
import luggage.data.Transaction;
import luggage.data.TransactionCodecProvider;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * Created by myhellsing on 09/02/16.
 */
public class SaveToMongo {

    // To directly connect to a single MongoDB server
// (this will not auto-discover the primary even if it's a member of a replica set)
    MongoClient mongoClient = null;
    // or MongoClient mongoClient = new MongoClient( "localhost" );
    // or MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

    MongoDatabase db = null;
    String dbName= "luggage-test";

    public void init(){
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                CodecRegistries.fromProviders(new TransactionCodecProvider(),new CategoryCodecProvider()),
                MongoClient.getDefaultCodecRegistry()
        );

        MongoClientOptions options =
                MongoClientOptions.builder()
                        .codecRegistry(codecRegistry)
                        .build();

        mongoClient =
                new MongoClient("localhost:27017", options);

        db = mongoClient.getDatabase(dbName);
    }

    public void saveTransactions()  {
        AnalysisCalc analysisCalc = new AnalysisCalc();
        ArrayList<Transaction> transactions = analysisCalc.getTransactions();
        db.getCollection(Transaction.COLLECTION_NAME,Transaction.class).insertMany(transactions);

        ArrayList<BalanceByMonth> balanceByMonths= analysisCalc.getBalanceByMonths();
      //  db.getCollection(Transaction.COLLECTION_NAME).find()
    }






    public void addUser(String name){
        db.getCollection("User").insertOne(new Document().append("name", name));
    }

    // попробуем в лоб и посчитаем сколько времени это будет работать.
    public void printCandidateToAliases(){


    }


    public void findCategoryAliases(){
        ArrayList<Transaction> transactions = new ArrayList<>();
        db.getCollection(Transaction.COLLECTION_NAME,Transaction.class).find().into(transactions);
        HashMap<String,TreeSet<String>> hm = new HashMap<>();
        for (Transaction t: transactions){
            if (t.category.name.compareTo(Category.UNKNOWN) == 0) continue;
            if (hm.containsKey(t.name)) {
                if (!hm.get(t.name).contains(t.category.name)){
                    hm.get(t.name).add(t.category.name);
                }
            }
            else{
                TreeSet<String> aliases = new TreeSet<>();
                aliases.add(t.category.name);
                hm.put(t.name, aliases);
            }
        }
        for (String s:hm.keySet()){
            if (hm.get(s).size()>1) {
                System.out.print("name: " + s);
                System.out.print("\t\t");
                for (String a : hm.get(s)) {
                    System.out.print(a + "\t");
                }
                System.out.println();
            }
        }

    }




    public void run(){
        init();
      //  addUser("Darya");
       saveTransactions();
     //   findCategoryAliases();


    }
    public static void main(String[] args) {
        new SaveToMongo().run();
    }
}
