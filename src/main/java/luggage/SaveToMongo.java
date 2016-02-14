package luggage;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import luggage.data.Category;
import luggage.data.CategoryCodecProvider;
import luggage.data.Transaction;
import luggage.data.TransactionCodecProvider;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

import java.util.ArrayList;

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

    }




    public void addUser(String name){
        db.getCollection("User").insertOne(new Document().append("name", name));
    }

    // попробуем в лоб и посчитаем сколько времени это будет работать.
    public void printCandidateToAliases(){


    }

    public void printCategories(){

        MongoCollection<Category>  categoriesCollection = db.getCollection(Category.COLLECTION_NAME,Category.class);
        ArrayList<Category> categories = categoriesCollection.find().into(new ArrayList<Category>());
        for (Category c:categories){
            System.out.println(c.name);
        }


    }

    public void printTransactions(){
        MongoCollection<Transaction>  transCollection = db.getCollection(Transaction.COLLECTION_NAME,Transaction.class);
        ArrayList<Transaction> transactions = transCollection.find().into(new ArrayList<Transaction>());
        for (Transaction t:transactions){
            System.out.println(t.name);
        }

    }



    public void run(){
        init();
      //  addUser("Darya");
        saveTransactions();
      //  printCategories();

       printTransactions();

    }
    public static void main(String[] args) {
        new SaveToMongo().run();
    }
}
