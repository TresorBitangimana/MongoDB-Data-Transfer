# MongoDB Data Transfer

A Java program that transfers all databases and collections from one MongoDB cluster to another, preserving all documents.

## What It Does

- Connects to two MongoDB clusters (a source and a destination)
- Loops through all databases in the source cluster (skipping system databases: `admin`, `local`, `config`)
- For each database, copies all collections and their documents into the destination cluster
- Skips empty collections automatically

## Prerequisites

- Java 21+
- Maven
- Two MongoDB clusters (source and destination)
- A `.env` file with your MongoDB connection strings (see setup below)

## Setup

### 1. Clone the Repository

```bash
git clone https://github.com/TresorBitangimana/MongoDB-Data-Transfer.git
cd MongoDB-Data-Transfer
```

### 2. Create a `.env` File

In the root of the project, create a file named `.env` with the following two variables:

```env
CONNECTION_STRING_TRANSFER=your_source_cluster_connection_string
CONNECTION_STRING_RECEIVER=your_destination_cluster_connection_string
```

| Variable | Description |
|---|---|
| `CONNECTION_STRING_TRANSFER` | The connection string of the cluster you want to **copy data from** |
| `CONNECTION_STRING_RECEIVER` | The connection string of the cluster you want to **copy data to** |

#### Example `.env` File

```env
CONNECTION_STRING_TRANSFER=mongodb+srv://user:password@cluster0.mongodb.net/
CONNECTION_STRING_RECEIVER=mongodb+srv://user:password@cluster1.mongodb.net/
```

> You can find your connection string in MongoDB Atlas under **Database → Connect → Drivers**.

### 3. Add the `.env` File to `.gitignore`

Make sure your `.env` file is never committed to version control:

```bash
echo ".env" >> .gitignore
```

### 4. Install Dependencies

```bash
mvn clean install
```

### 5. Run the Program

```bash
mvn exec:java -Dexec.mainClass="org.tresor.Main"
```

Or run the `Main.java` file directly from IntelliJ by clicking the green **Run** button.

## Notes

- The program skips the `admin`, `local`, and `config` system databases automatically
- Collections are only created in the destination cluster if they contain documents
- If a collection already exists in the destination cluster with the same name, documents will be **appended** (not replaced) — duplicate `_id` errors may occur if the data already exists

## Dependencies

- [MongoDB Driver Sync](https://www.mongodb.com/docs/drivers/java/sync/current/)
- [dotenv-java](https://github.com/cdimascio/dotenv-java) — for loading the `.env` file