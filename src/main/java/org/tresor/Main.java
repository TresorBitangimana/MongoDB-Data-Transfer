package org.tresor;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.github.cdimascio.dotenv.Dotenv;
import org.bson.Document;

public class Main {
    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        //gets the connections string of both mongoDB clusters
        String ConnectionStringTransfer = dotenv.get("CONNECTION_STRING_TRANSFER");
        String ConnectionStringReceiver = dotenv.get("CONNECTION_STRING_RECEIVER");

        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        //connections to the transfer mongoDB cluster
        MongoClientSettings TransferSettings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(ConnectionStringTransfer))
                .serverApi(serverApi)
                .build();

        //connections to the receiver mongoDB cluster
        MongoClientSettings ReceiverSettings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(ConnectionStringReceiver))
                .serverApi(serverApi)
                .build();

        try (MongoClient transferClient = MongoClients.create(TransferSettings)) {
            try(MongoClient receiverClient = MongoClients.create(ReceiverSettings)){

                transferClient.getDatabase("admin").runCommand(new Document("ping", 1));
                receiverClient.getDatabase("admin").runCommand(new Document("ping", 1));

            }catch(MongoException e){
                System.out.println(e);
            }
        }catch(MongoException e){
            System.out.println(e);
        }

    }
}
