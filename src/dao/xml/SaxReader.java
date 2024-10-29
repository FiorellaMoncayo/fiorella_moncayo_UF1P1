package dao.xml;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import model.Amount;
import model.Product;

public class SaxReader extends DefaultHandler{
	ArrayList <Product> products = new ArrayList();
	Product product;
	private StringBuilder buffer = new StringBuilder(); //para poder ir leyendo lso elementos simples (con texto)

	
	//double price;
	//int stock;
	
	
	//método de acceso para leer el ArrayList desde el main:

	public ArrayList<Product> getProducts(){
		return products;
	}
	
	/*public void setProducts(ArrayList<Product> products) {
		this.products= products;
	}*/
	
	/*@Override
	public void startDocument() throws SAXException{
		this.products = new ArrayList <>();
	}*/
	
	@Override
	public void startElement(String uri,String localName, String qName, Attributes attributes) throws SAXException{
		switch(qName) {
		case "inventory":
			break;
		case "product":
			break;
		case "name":
			String productName = attributes.getValue("name");
			product = new Product(productName, new Amount(0), true, 0);
			products.add(product);
			
			
			
		
			this.product = new Product();
			break;
		case "wholesalerPrice":
			break;
		case "stock":
			break;
		}
	}
	
	
	@Override
	public void endElement(String uri,String localName, String qName) throws SAXException{
		switch(qName) {
		case "inventory":
			break;
		case "product":
			break;
		case "name":
			break;
		case "wholesalerPrice":
			break;
		case "stock":
			break;
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		buffer.append(ch, start, length); //guardamos el texto en el buffer
	} 
	
	
	

}
