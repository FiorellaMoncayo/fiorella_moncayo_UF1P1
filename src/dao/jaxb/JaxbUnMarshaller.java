package dao.jaxb;

import javax.xml.bind.JAXBContext; //Maneja el contexto para la conversión
import javax.xml.bind.Unmarshaller; //Convierte XML en un objeto Java
import javax.xml.bind.JAXBException; //Excepción específica de JAXB

import model.Product;
import model.ProductList;
import java.io.File;

public class JaxbUnMarshaller { // Esta clase se encargará de convertir archivos XML en objetos Java

	public ProductList unmarshal() {
		ProductList products = null;
		try {
			File xmlFile = new File("jaxb/inputInventory.xml");
			if (!xmlFile.exists()) {
				System.err.println("Archivo no encontrado en: " + xmlFile.getAbsolutePath());
				return null;
			}
			JAXBContext context = JAXBContext.newInstance(ProductList.class); // Preparación para la conversión: Se crea
																				// un contexto JAXB que manejará la
																				// clase ProductList
			Unmarshaller unmarshaller = context.createUnmarshaller(); // Se crea un objeto Unmarshaller a partir del
																		// contexto. Este objeto realizará la conversión
			System.out.println("unmarshalling...");
			products = (ProductList) unmarshaller.unmarshal(new File("jaxb/inputInventory.xml")); // Convierte el archivo XML en un objeto de tipo ProductList
			
			for(Product product : products.getProducts()) {
			    System.out.println(product);
			}
			System.out.println("----------");
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		if (products == null || products.getProducts() == null) {
			System.err.println("El objeto deserializado es null o no contiene productos.");
			return null;
		}
		return products;

	}
}
