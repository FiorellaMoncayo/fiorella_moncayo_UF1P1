package dao.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.Source;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import model.Amount;
import model.Product;

public class DomWriter {
	private Document document;

	private ArrayList<Product> secondInventory;

	public DomWriter(ArrayList<Product> inventory) {
		try {
			secondInventory = inventory;
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.newDocument();
		} catch (ParserConfigurationException e) {
			System.out.println("Error generating document");
		}
	}

	public boolean generateDocument() {
		// PAREND NODE
		Element products = document.createElement("products");
		//products.setAttribute("total", String.valueOf(secondInventory.size()));
		products.setAttribute("total", String.valueOf(Product.getTotalProducts()));
		document.appendChild(products);

		// CHILD NODES
		for (Product productData : secondInventory) {

			Element product = document.createElement("product");
			product.setAttribute("id", String.valueOf(productData.getId()));
			products.appendChild(product);

			Element name = document.createElement("name");
			name.setTextContent(productData.getName());
			product.appendChild(name);

			Element price = document.createElement("price");
			price.setAttribute("currency", "€");
			price.setTextContent(String.valueOf(productData.getPublicPrice().getValue())); 
																							
			product.appendChild(price);

			Element stock = document.createElement("stock");
			stock.setTextContent(String.valueOf(productData.getStock()));
			product.appendChild(stock);

		}

	/*	if (generateXml()) {
			return true;
		} else {
			return false;
		} */
		
		return generateXml();

	}

	public boolean generateXml() {
		boolean generated = false;

		try {
			String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			String fileName = "inventory_" + date + ".xml";

			File file = new File("files/" + fileName);
			FileWriter fw = new FileWriter(file);
			PrintWriter pw = new PrintWriter(fw);

			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();

			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

			Source source = new DOMSource(document);
			Result result = new StreamResult(pw);

			transformer.transform(source, result);
			System.out.println("XML document saved as: " + fileName);

			generated = true;

		 } catch (IOException e) {
		        System.out.println("Error al crear el archivo: " + e.getMessage());
		    } catch (TransformerException e) {
		        System.out.println("Error al transformar el documento: " + e.getMessage());
		    } catch (Exception e) {
		        System.out.println("Error inesperado: " + e.getMessage());
		    }
		return generated;
	}
}
