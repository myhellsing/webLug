package luggage.for_mongo;

import luggage.data.Category;
import luggage.data.Transaction;
import org.bson.*;
import org.bson.codecs.*;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;

import java.util.Date;
/**
 * Created by myhellsing on 14/02/16.
 */
public class TransactionCodec implements CollectibleCodec<Transaction> {
    private CodecRegistry codecRegistry;


    public TransactionCodec(CodecRegistry codecRegistry){
        this.codecRegistry=codecRegistry;
    }
    @Override
    public Transaction decode(BsonReader reader, DecoderContext decoderContext)
    {

        reader.readStartDocument();

        ObjectId id = reader.readObjectId("_id");
        String name = reader.readString("name");
        Double sum = reader.readDouble("sum");
        Date date = new Date(reader.readDateTime("date"));
        reader.readName("category");
        Category category= codecRegistry.get(Category.class).decode(reader,decoderContext);
        String type= reader.readString("type");
        reader.readEndDocument();
        return new Transaction(id,name,sum,date,category,type);
    }

    @Override
    public void encode(BsonWriter writer, Transaction transaction, EncoderContext encoderContext)
    {
        writer.writeStartDocument();
        writer.writeObjectId("_id",transaction.objectId);
        writer.writeString("name",transaction.name);
        writer.writeDouble("sum",transaction.sum);
        writer.writeDateTime("date",transaction.date.getTime());
        if (transaction.category == null || transaction.category.name == null){
            transaction.category= new Category(Category.UNKNOWN);
        }
        writer.writeName("category");
        encoderContext.encodeWithChildContext(codecRegistry.get(Category.class),writer,transaction.category);
        writer.writeString("type",transaction.type);
        writer.writeEndDocument();
    }

    @Override
    public Class<Transaction> getEncoderClass()
    {
        return Transaction.class;
    }

    @Override
    public Transaction generateIdIfAbsentFromDocument(Transaction transaction)
    {
        if (!documentHasId(transaction))
        {
            transaction.objectId = new ObjectId();
        }
        return transaction;
    }

    @Override
    public boolean documentHasId(Transaction transaction)
    {
        return transaction.objectId != null;
    }

    @Override
    public BsonValue getDocumentId(Transaction transaction)
    {
        if (!documentHasId(transaction))
        {
            throw new IllegalStateException("The document does not contain an _id");
        }

        return new BsonString(transaction.objectId.toString());
    }

}