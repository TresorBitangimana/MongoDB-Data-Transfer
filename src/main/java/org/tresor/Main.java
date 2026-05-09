package org.tresor;

public class Main {

    Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();

    //gets the connections string of both mongoDB clusters
    String ConnectionStringTransfer = dotenv.get("CONNECTION_STRING_TRANSFER");
    String ConnectionStringReceiver = dotenv.get("CONNECTION_STRING_RECEIVER");

    static void main() {

        System.out.println("hello World!");

    }
}
