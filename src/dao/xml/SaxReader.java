package dao.xml;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import model.Amount;
import model.Product;

public class SaxReader extends DefaultHandler {
	ArrayList<Product> products = new ArrayList();
	Product product;
	StringBuilder buffer = new StringBuilder(); // para poder ir leyendo los elementos simples (con texto)

	public ArrayList<Product> getProducts() {
		return products;
	}

	@Override
	public void startDocument() throws SAXException {
		this.products = new ArrayList<Product>();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		switch (qName) {
		case "product":
			String productName = attributes.getValue("name");
			product = new Product(productName, new Amount(0), true, 0);
			products.add(product);
			// this.product = new Product();
			break;
		case "name":
		case "wholesalerPrice":
		case "stock":
			buffer.delete(0, buffer.length()); // Limpiar buffer al inicio de cada elemento
			break;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		switch (qName) {
		case "name":
			product.setName(buffer.toString());
			break;
		case "wholesalerPrice":
			double priceValue = Double.parseDouble(buffer.toString());
			product.setWholesalerPrice(new Amount(priceValue));
			break;
		case "stock":
			product.setStock(Integer.parseInt(buffer.toString()));
			break;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		buffer.append(ch, start, length); // Guardamos el texto en el buffer
	}

	@Override
	public void endDocument() throws SAXException {
		printDocument();
	}

	private void printDocument() {
		for (Product p : products) {
			System.out.println(p.toString());
		}

	}

}
