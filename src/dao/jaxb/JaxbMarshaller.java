package dao.jaxb;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import model.Product;
import model.ProductList;
import java.time.LocalDateTime;
import java.time.format.*;

public class JaxbMarshaller { //clase que convertirá objetos Java en archivos XML
	public boolean marshal(ProductList productList) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			JAXBContext context = JAXBContext.newInstance(ProductList.class);
			Marshaller marshaller = context.createMarshaller(); 
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); //Configura el marshaller para que el XML generado esté bien formateado
			System.out.println("marshalling... ");
			LocalDateTime today = LocalDateTime.now();
			marshaller.marshal(productList, new File("jaxb/inventory_"+today.format(formatter)+".xml"));  //Convierte el objeto ProductList a XML y lo guarda en un archivo específico
			return true;
		} catch (JAXBException e) {
			e.printStackTrace();
			return false;
		}
	}
	


}
