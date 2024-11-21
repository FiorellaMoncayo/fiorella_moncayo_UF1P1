package dao;

import java.util.ArrayList;
import java.io.File;
import model.Employee;
import model.Product;
import model.ProductList;
import dao.jaxb.JaxbUnMarshaller;
import dao.jaxb.JaxbMarshaller;
import javax.xml.bind.JAXBException;

public class DaoImplJaxb implements Dao{
	//objetos para manejar la conversión
	private JaxbUnMarshaller unMarshaller = new JaxbUnMarshaller(); //convierte XML en objetos Java
    private JaxbMarshaller marshaller = new JaxbMarshaller(); //Convierte objetos Java en XML

	@Override
	public void connect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Product> getInventory() { //método para leer un archivo XML y convertirlo en un objeto ProductList
		//ArrayList<Product> productos = unMarshaller.unmarshal().getProducts();
		//return productos;
		JaxbUnMarshaller unmarshaller = new JaxbUnMarshaller();
		ProductList productList = unMarshaller.unmarshal();
		
		if(productList == null) {
			return null;
		}
		
		return new ArrayList<>(productList.getProducts());
 	}

	@Override
	public boolean writeInventory(ArrayList<Product> products) { 
		try {
			ProductList productList = new ProductList(products);
			JaxbMarshaller jaxbMarshaller = new JaxbMarshaller();
			jaxbMarshaller.marshal(productList);
			System.out.println("El inventario se ha guardado correctamente en el archivo XML.");
	        return true;
		} catch (Exception e) {
			System.err.println("Error al guardar el inventario en el archivo XML:");
			e.printStackTrace();
			return false;
		}
		
	}

}
