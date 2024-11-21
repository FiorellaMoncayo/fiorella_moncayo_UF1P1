package dao.jaxb;

import java.io.File;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import model.Product;
import model.ProductList;

public class JaxbMarshaller { //clase que convertirá objetos Java en archivos XML
	public void marshal(ProductList productList) {
		try {
			//ProductList x = new ProductList(productList);
			JAXBContext context = JAXBContext.newInstance(ProductList.class);
			Marshaller marshaller = context.createMarshaller(); 
			//marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); //Configura el marshaller para que el XML generado esté bien formateado
			System.out.println("marshalling... ");
			marshaller.marshal(productList, new File("xml/inputInventory.xml"));  //Convierte el objeto ProductList a XML y lo guarda en un archivo específico
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	


}
