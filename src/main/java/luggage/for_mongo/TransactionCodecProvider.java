package luggage.for_mongo;

import luggage.data.Transaction;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

/**
 * Created by myhellsing on 14/02/16.
 */
public class TransactionCodecProvider implements CodecProvider {

    @Override
    public <T> Codec<T> get(Class<T> aClass, CodecRegistry codecRegistry) {
        if (aClass == Transaction.class ){
            return (Codec<T>) new TransactionCodec(codecRegistry);
        }
        return null;
    }
}
