package org.tresor;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.github.cdimascio.dotenv.Dotenv;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        //configures dotenv to not throw an error if the dot env file is missing
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

        //try catch block, create the client, throws an error if not possible
        //automatically closes the connection with the mongoDB client after use.
        try (MongoClient transferClient = MongoClients.create(TransferSettings)) {
            try(MongoClient receiverClient = MongoClients.create(ReceiverSettings)){

                //list to store all the Databases from the transfer cluster
                List<MongoDatabase> transferDatabases = new ArrayList<>();

                //loops through the cluster and gets all the databases names and use them
                //to store the actual databases in the transferDatabases Arraylist
                for(String TransferDatabaseName : transferClient.listDatabaseNames()){
                    if(TransferDatabaseName.equals("admin") ||
                            TransferDatabaseName.equals("local") ||
                            TransferDatabaseName.equals("config")){
                        continue;
                    }else{
                        transferDatabases.add(transferClient.getDatabase(TransferDatabaseName));

                    }
                }


                //loops through the transferDatabases ArrayList
                //creates a newReceiverDataBase with the receiverClient
                //creates a TransferCollections ArrayList to store all the collections
                //loops through each database in transferDatabases to get their collections
                //loops through each collection
                //creates a new collection newReceiverCollection with the newReceiverDatabase
                //creates an Arraylist docs that stores all the documents in the transferCollections
                //finally adds the documents in the newReceiverCollection using insertMany()
                for(MongoDatabase transferDatabase : transferDatabases){
                    MongoDatabase newReceiverDatabase = receiverClient.getDatabase(transferDatabase.getName());
                    System.out.println("Created receiver database: "+ newReceiverDatabase.getName());

                    //list to store all the collection
                    List<MongoCollection<Document>> transferCollections = new ArrayList<>();

                    for(String transferCollectionName : transferDatabase.listCollectionNames()){
                        transferCollections.add(transferDatabase.getCollection(transferCollectionName));
                        System.out.println("Added Collection: "+transferDatabase
                                .getCollection(transferCollectionName)
                                .getNamespace()
                                .getCollectionName() + "To "+newReceiverDatabase.getName()+ "database");
                    }
                    for(MongoCollection<Document> collection : transferCollections){
                        MongoCollection<Document> newReceiverCollection = newReceiverDatabase.getCollection(collection.getNamespace().getCollectionName());
                        List<Document> docs = collection.find().into(new ArrayList<>());
                        if(!docs.isEmpty()){
                            newReceiverCollection.insertMany(docs);
                            System.out.println("Added all document to"+ collection.getNamespace().getCollectionName() + "collection");
                        }
                    }
                }


            }catch(MongoException e){
                System.out.println(e);
            }
        }catch(MongoException e){
            System.out.println(e);
        }

    }
}
