package dao;

import static com.mongodb.client.model.Filters.eq;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import model.Employee;
import model.Product;

/**
 * MongoDB implementation of the Data Access Object.
 * This class handles all database operations for the shop application using MongoDB.
 */
public class DaoImplMongoDB implements Dao {

    // MongoDB client and database objects
    private MongoClient mongoClient;
    private MongoDatabase database;
    
    // Collection references
    private MongoCollection<Document> inventoryCollection;
    private MongoCollection<Document> historicalInventoryCollection;
    private MongoCollection<Document> userCollection;
    
    // Counter for product IDs - used to ensure unique IDs across all collections
    private static int idCounter = 0;

    /**
     * Connects to the MongoDB database and initializes collections
     */
    @Override
    public void connect() {
        try {
            String uri = "mongodb://localhost:27017";
            MongoClientURI mongoClientURI = new MongoClientURI(uri);
            mongoClient = new MongoClient(mongoClientURI);

            database = mongoClient.getDatabase("shop");
            inventoryCollection = database.getCollection("inventory");
            historicalInventoryCollection = database.getCollection("historical_inventory");
            userCollection = database.getCollection("user");

            // Initialize the ID counter by finding the highest ID in both collections
            if (idCounter == 0) {
                initializeIdCounter();
            }
        } catch (Exception e) {
            System.err.println("Error connecting to MongoDB: " + e.getMessage());
        }
    }

    /**
     * Initializes the ID counter based on the highest ID found in all collections
     * This ensures we don't create duplicate IDs even after database restarts
     */
    private void initializeIdCounter() {
        try {
            // Check inventory collection for highest ID
            try (MongoCursor<Document> cursor = inventoryCollection.find().iterator()) {
                while (cursor.hasNext()) {
                    Document doc = cursor.next();
                    if (doc.containsKey("id")) {
                        int currentId = doc.getInteger("id");
                        if (currentId > idCounter) {
                            idCounter = currentId;
                        }
                    }
                }
            }

            // Also check historical_inventory collection for highest ID
            try (MongoCursor<Document> cursor = historicalInventoryCollection.find().iterator()) {
                while (cursor.hasNext()) {
                    Document doc = cursor.next();
                    if (doc.containsKey("id")) {
                        int currentId = doc.getInteger("id");
                        if (currentId > idCounter) {
                            idCounter = currentId;
                        }
                    }
                }
            }
            
            System.out.println("ID counter initialized to: " + idCounter);

        } catch (Exception e) {
            System.err.println("Error initializing ID counter: " + e.getMessage());
        }
    }

    /**
     * Authenticates an employee based on ID and password
     * @param employeeId The employee ID
     * @param password The employee password
     * @return Employee object if authentication succeeds, null otherwise
     */
    @Override
    public Employee getEmployee(int employeeId, String password) {
        connect();
        try {
            Document userDoc = userCollection.find(new Document("employeeId", employeeId)).first();

            if (userDoc != null && password.equals(userDoc.getString("password"))) {
                String name = userDoc.getString("name");
                Employee employee = new Employee(employeeId, password);
                employee.setName(name);
                return employee;
            }
        } catch (Exception e) {
            System.err.println("Error authenticating employee: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Closes the MongoDB connection
     */
    @Override
    public void disconnect() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    /**
     * Retrieves the entire inventory from MongoDB
     * Ensures all products have proper IDs assigned
     * @return ArrayList of Products from the inventory
     * @throws SQLException if an error occurs
     */
    @Override
    public ArrayList<Product> getInventory() throws SQLException {
        ArrayList<Product> inventory = new ArrayList<>();
        connect();
        try {
            System.out.println("Loading inventory from MongoDB...");
            FindIterable<Document> documents = inventoryCollection.find();

            // Process each document with correct ID handling
            for (Document doc : documents) {
                String name = doc.getString("name");

                double value = 0;
                try {
                    Document wholesalerPriceDoc = doc.get("wholesalerPrice", Document.class);
                    if (wholesalerPriceDoc != null) {
                        try {
                            value = wholesalerPriceDoc.getDouble("value");
                        } catch (Exception e) {
                            Object valueObj = wholesalerPriceDoc.get("value");
                            if (valueObj != null) {
                                value = Double.parseDouble(valueObj.toString());
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error processing wholesalerPrice for product " + name + ": " + e.getMessage());
                }

                boolean available = doc.getBoolean("available", true);
                int stock = doc.getInteger("stock", 0);

                int id;
                if (doc.containsKey("id")) {
                    id = doc.getInteger("id");
                    // Ensure the counter is at least as high as this ID
                    if (id > idCounter) {
                        idCounter = id;
                    }
                } else {
                    // Increment and assign a new ID
                    id = ++idCounter;
                    System.out.println("Assigning new ID to product in inventory: " + name + " -> ID: " + id);

                    // Update the document with the assigned ID
                    Document update = new Document();
                    update.append("id", id);

                    UpdateResult result = inventoryCollection.updateOne(eq("_id", doc.getObjectId("_id")),
                            new Document("$set", update));

                    if (result.getModifiedCount() > 0) {
                        System.out.println("MongoDB document updated with new ID: " + id);
                    } else {
                        System.err.println("Failed to update MongoDB document with ID");
                    }
                }

                Product product = new Product(id, name, value, available, stock);
                inventory.add(product);
                System.out.println("Loaded product: " + name + " with ID: " + id);
            }
        } catch (Exception e) {
            System.err.println("Error retrieving inventory from MongoDB: " + e.getMessage());
            e.printStackTrace();
            throw new SQLException("Failed to retrieve inventory from MongoDB", e);
        }

        return inventory;
    }

    /**
     * Exports the current inventory to the historical_inventory collection
     * Maintains product IDs and adds a creation timestamp
     * First clears the historical collection to ensure it represents the current state
     * @param products List of products to export
     * @return true if the export is successful, false otherwise
     */
    @Override
    public boolean writeInventory(ArrayList<Product> products) {
        connect();
        try {
            // First, clear the historical inventory to ensure it reflects current state
            historicalInventoryCollection.drop();
            database.createCollection("historical_inventory");
            historicalInventoryCollection = database.getCollection("historical_inventory");
            
            Date currentDate = new Date();
            System.out.println("Exporting inventory to historical collection...");

            for (Product product : products) {
                // Use the product's existing ID, which should already be assigned
                int productId = product.getId();

                // If the product doesn't have an ID, assign a new one
                if (productId <= 0) {
                    productId = ++idCounter;
                    product.setId(productId);
                }

                // Create the document with the exact structure shown in the image
                Document document = new Document();
                document.append("_id", new ObjectId());
                document.append("id", productId); // Top-level id field
                document.append("name", product.getName());

                // Create the wholesalerPrice nested object with correct structure
                Document wholesalerPrice = new Document();
                wholesalerPrice.append("value", product.getWholesalerPrice().getValue());
                wholesalerPrice.append("currency", product.getWholesalerPrice().getCurrency());
                document.append("wholesalerPrice", wholesalerPrice);

                document.append("available", product.isAvailable());
                document.append("stock", product.getStock());
                document.append("created_at", currentDate); // Add creation timestamp

                historicalInventoryCollection.insertOne(document);
                System.out.println(
                        "Product exported to historical inventory: " + product.getName() + " with ID: " + productId);
            }

            System.out.println("Export complete. " + products.size() + " products exported to historical inventory.");
            return true;
        } catch (Exception e) {
            System.err.println("Error writing inventory to historical collection: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Adds a new product to the inventory
     * Ensures proper ID assignment and document structure
     * @param product The product to add
     */
    @Override
    public void addProduct(Product product) {
        connect();
        try {
            // Check if product already exists
            Document existingProduct = inventoryCollection.find(eq("name", product.getName())).first();
            if (existingProduct != null) {
                System.err.println("Product with name " + product.getName() + " already exists.");
                return;
            }

            // Assign a new ID using our counter if needed
            if (product.getId() <= 0) {
                product.setId(++idCounter);
            }

            // Create document with the correct structure including id field
            Document document = new Document();
            document.append("_id", new ObjectId());
            document.append("id", product.getId());
            document.append("name", product.getName());
            
            // Create the wholesalerPrice nested object with correct structure
            Document wholesalerPrice = new Document();
            wholesalerPrice.append("value", product.getWholesalerPrice().getValue());
            wholesalerPrice.append("currency", product.getWholesalerPrice().getCurrency());
            document.append("wholesalerPrice", wholesalerPrice);
            
            document.append("available", product.isAvailable());
            document.append("stock", product.getStock());

            inventoryCollection.insertOne(document);
            System.out.println("Product added to MongoDB inventory: " + product.getName() + " with ID: " + product.getId());
        } catch (Exception e) {
            System.err.println("Error adding product to MongoDB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Updates a product's stock in the inventory
     * Uses ID as the filter for reliable updates
     * Ensures immediate visibility of changes in the UI
     * @param product The product with updated stock
     */
    @Override
    public void updateProduct(Product product) {
        connect();
        try {
            System.out.println("Updating stock for product ID: " + product.getId());
            
            // First, verify the product exists
            Document existingProduct = inventoryCollection.find(eq("id", product.getId())).first();
            if (existingProduct == null) {
                System.out.println("Product not found in MongoDB: ID=" + product.getId());
                return;
            }
            
            // Update only the stock field
            UpdateResult result = inventoryCollection.updateOne(
                eq("id", product.getId()),
                set("stock", product.getStock())
            );
            
            if (result.getModifiedCount() > 0) {
                System.out.println("Product stock updated in MongoDB: ID=" + product.getId() + 
                                  ", New stock=" + product.getStock());
                
                // Verify the update happened correctly
                Document updatedDoc = inventoryCollection.find(eq("name", product.getName())).first();
                if (updatedDoc != null) {
                    int newStock = updatedDoc.getInteger("stock");
                    System.out.println("Verification: Stock value in database is now: " + newStock);
                    
                    // The product object should be updated in memory as well
                    product.setStock(newStock);
                }
            } else {
                System.out.println("No changes made to the database. Stock might already be at the target value.");
            }
        } catch (Exception e) {
            System.err.println("Error updating product stock in MongoDB: " + e.getMessage());
            e.printStackTrace();
        }
        
    }
	/**
     * Deletes a product from the inventory
     * Uses ID as the filter for more reliable deletion
     * @param product The product to delete
     */
    @Override
    public void deleteProduct(Product product) {
        connect();
        try {
            System.out.println("Attempting to delete product with ID: " + product.getId());
            
            // Delete the product by ID instead of by name
            DeleteResult result = inventoryCollection.deleteOne(eq("id", product.getId()));

            if (result.getDeletedCount() > 0) {
                System.out.println("Product deleted from MongoDB: ID=" + product.getId() + 
                                  ", Name=" + product.getName());
            } else {
                System.out.println("No products were deleted from MongoDB. Product ID may not exist: " + 
                                  product.getId());
            }
            
            // Verify the deletion
            Document verifyDoc = inventoryCollection.find(eq("id", product.getId())).first();
            if (verifyDoc != null) {
                System.err.println("WARNING: Product still exists after deletion attempt!");
            } else {
                System.out.println("Verification: Product successfully deleted.");
            }
        } catch (Exception e) {
            System.err.println("Error deleting product from MongoDB: " + e.getMessage());
            e.printStackTrace();
        }
    }
}