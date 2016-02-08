package GDriveData;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

/**
 * Created by myhellsing on 09/02/16.
 */
public class SaveToMongo {

    // To directly connect to a single MongoDB server
// (this will not auto-discover the primary even if it's a member of a replica set)
    MongoClient mongoClient = new MongoClient();
    // or MongoClient mongoClient = new MongoClient( "localhost" );
    // or MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

    MongoDatabase database = mongoClient.getDatabase("luggage");




}
