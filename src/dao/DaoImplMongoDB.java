package dao;

import java.util.ArrayList;
import java.util.Date;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import model.Amount;
import model.Employee;
import model.Product;

public class DaoImplMongoDB implements Dao{
	
	MongoCollection<Document> collection;
	MongoDatabase mongoDatabase;
	ObjectId id;
	

	@Override
	public void connect() {
		String uri = "mongodb://localhost:27017";
		MongoClientURI mongoClientURI = new MongoClientURI(uri);
		MongoClient mongoClient = new MongoClient(mongoClientURI);
		
		mongoDatabase = mongoClient.getDatabase("shop");		
		System.out.println("conexión hecha");
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		collection = mongoDatabase.getCollection("users"); //pillo la colección
		Document document = collection.find().first(); //buscar el primero de toda la colección
		document = collection.find(Filters.and(Filters.eq("user", employeeId),Filters.eq("password", password))).first();
		if(document != null) {
			System.out.println("uwu");
			return new Employee(document.getInteger("user"),password, document.getString("password"));
			
		}else {
			return null;
		}
		
	}

	@Override
	public ArrayList<Product> getInventory() {
		ArrayList<Product> products = new ArrayList<Product>();
		
		collection = mongoDatabase.getCollection("inventory");
		Document document = collection.find().first();
		
		for(Document doc : collection.find()) {
			String name = doc.getString("name");
			boolean available = doc.getBoolean("available", true);
			int stock = doc.getInteger("stock", 0);
			
			Document wholesalerPriceDoc = (Document) doc.get("wholesalerPrice");
			double  wholesalerPriceValue =  wholesalerPriceDoc.getDouble("value");
			String  wholesalerPriceCurrency =  wholesalerPriceDoc.getString("currency");
			
			System.out.println("document read on list" + ":" + doc.toJson());
			Product product = new Product(name, new Amount( wholesalerPriceValue), available, stock);
			products.add(product);
		}
		return products;
	}

	@Override
	public boolean writeInventory(ArrayList<Product> inventory) {

		collection = mongoDatabase.getCollection("historical_inventory");
		ArrayList<Document> historyDocuments = new ArrayList<>();
		
		for (Product product : inventory) {
			Document historyDoc = new Document()
					.append("inProduct", product.getId())
					.append("name", product.getName())
					.append("price", product.getPrice())
					.append("stock", product.getStock())
					.append("timestamp", new Date());
			historyDocuments.add(historyDoc);
		}

		if (!historyDocuments.isEmpty()) {
			collection.insertMany(historyDocuments);
			System.out.println("Inventario exportado");
			return true;
		}
		
		return false;
	}

	@Override
	public boolean addProduct(String name, Amount price, int stock, boolean avaiblable) {
		collection = mongoDatabase.getCollection("inventory");
		
		Document productDoc = new Document()
				.append("name", name)
				.append("wholesalerPrice", new Document("value", price.getValue())
						.append("currency", "€"))
				.append("stock", stock)
				.append("available", avaiblable)
				.append("created_At", new Date());
		collection.insertOne(productDoc);
        System.out.println("Producto agregado correctamente: " + name);
        return true;
	}

	@Override
	public boolean addStockProduct(String name, int stock) {
		collection = mongoDatabase.getCollection("inventory");
		
		UpdateResult result = collection.updateOne(
				Filters.eq("name", name),
				Updates.inc("stock", stock)
		);
		
		if (result.getModifiedCount() > 0) {
	        System.out.println("Stock actualizado correctamente para el producto: " + name);
	        return true;
	    } else {
	        System.out.println("No se encontró el producto con el nombre: " + name);
	        return false;
	    }
	} 

	@Override
	public boolean deleteProduct(String name) {
		collection = mongoDatabase.getCollection("inventory");
		Document document = collection.find(Filters.regex("name", "^" + name + "$", "i")).first();
		if (document == null) {
			System.out.println("No se encontró ningún producto con el nombre: " + name);
			return false;
		} else {
			collection.deleteOne(document);
	        System.out.println("Producto eliminado correctamente (Nombre: " + name + ").");
	        return true;
	    }
	}
}
