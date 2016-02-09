package GDriveData;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import luggage.AnalysisCalc;
import luggage.Transaction;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by myhellsing on 09/02/16.
 */
public class SaveToMongo {

    // To directly connect to a single MongoDB server
// (this will not auto-discover the primary even if it's a member of a replica set)
    MongoClient mongoClient = new MongoClient();
    // or MongoClient mongoClient = new MongoClient( "localhost" );
    // or MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

    MongoDatabase database = mongoClient.getDatabase("luggage-test");


    public void saveTransactions()  {
        AnalysisCalc analysisCalc = new AnalysisCalc();
        ArrayList<Transaction> transactions = analysisCalc.getTransactions();
        ArrayList<Document> transDocs =new ArrayList<>();
        HashMap<String,ObjectId> categories = new HashMap<>();
        ArrayList<Document> categoryDocs =new ArrayList<>();
        for (Transaction t :transactions){
            ObjectId category_id= new ObjectId();
             if (categories.containsKey(t.category.name) ){
                 category_id = categories.get(t.category.name);
             }
            else {
                 categories.put(t.category.name, category_id);
                 categoryDocs.add(t.category.createBSON().append("_id",category_id));
             }
            transDocs.add(t.createBSON().append("category_id", category_id));
        }
        database.getCollection("Transactions").drop();
        database.getCollection("Transactions").insertMany(transDocs);
        database.getCollection("Category").drop();
        database.getCollection("Category").insertMany(categoryDocs);
    }




    public void addUser(String name){
        database.getCollection("User").insertOne(new Document().append("name", name));
    }


    public void run(){
      //  addUser("Darya");
        saveTransactions();
    }
    public static void main(String[] args) {
        new SaveToMongo().run();
    }
}
