package dao;

import static com.mongodb.client.model.Filters.eq;

import java.sql.SQLException;
import java.util.ArrayList;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import model.Employee;
import model.Product;

import org.bson.Document;

public class DaoImplMongoDB implements Dao {

	MongoCollection<Document> collection;
	Object id;

	@Override
	public void connect() {
		String uri = "mongodb://localhost:27017";
		MongoClientURI mongoClientURI = new MongoClientURI(uri);
		MongoClient mongoClient = new MongoClient(mongoClientURI);

		MongoDatabase mongoDatabase = mongoClient.getDatabase("shop");
		collection = mongoDatabase.getCollection("inventory");
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<Product> getInventory() throws SQLException {
		Document document = collection.find().first();
		ArrayList<Product> inventory = new ArrayList<>();

		document = collection.find(eq("_id", this.id)).first();
		System.out.println("document read by id " + document.toJson());

		// Read the first '5' documents
		Iterable<Document> documents = collection.find().limit(5);

		int count = 0;
		for (Document doc : documents) {
			System.out.println("document read on list " + (++count) + ": " + doc.toJson());
		}
		return inventory;
	}

	@Override
	public boolean writeInventory(ArrayList<Product> product) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addProduct(Product product) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateProduct(Product product) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteProduct(Product product) {
		// TODO Auto-generated method stub

	}

}
