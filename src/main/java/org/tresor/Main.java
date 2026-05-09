package org.tresor;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.github.cdimascio.dotenv.Dotenv;

public class Main {
    static void main(String[] args) {

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

    }
}
