package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;

import model.Amount;
import model.Employee;
import model.Product;
import model.Sale;

public class DaoImplFile implements Dao {
	//private ArrayList<Sale> sales;

	public void connect() {
		// TODO Auto-generated method stub

	}

	public void disconnect() {
		// TODO Auto-generated method stub

	}

	public Employee getEmployee(int employeeId, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<Product> getInventory() {

		ArrayList<Product> inventoryLoaded = new ArrayList<Product>();

		// locate file, path and name
		File f = new File(System.getProperty("user.dir") + File.separator + "files/inputInventory.txt");

		try {
			// wrap in proper classes
			FileReader fr;
			fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);

			// read first line
			String line = br.readLine();

			// process and read next line until end of file
			while (line != null) {
				// split in sections
				String[] sections = line.split(";");

				String name = "";
				double wholesalerPrice = 0.0;
				int stock = 0;

				// read each sections
				for (int i = 0; i < sections.length; i++) {
					// split data in key(0) and value(1)
					String[] data = sections[i].split(":");

					switch (i) {
					case 0:
						// format product name
						name = data[1];
						break;

					case 1:
						// format price
						wholesalerPrice = Double.parseDouble(data[1]);
						break;

					case 2:
						// format stock
						stock = Integer.parseInt(data[1]);
						break;

					default:
						break;
					}
				}
				// add product to inventory
				inventoryLoaded.add(new Product(name, new Amount(wholesalerPrice), true, stock));

				// read next line
				line = br.readLine();
			}
			fr.close();
			br.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return inventoryLoaded;
	}

	public boolean writeInventory(ArrayList<Product> inventory) {
		boolean isWritten = false;

		// define file name based on date
		LocalDate myObj = LocalDate.now();
		String fileName = "inventory_" + myObj.toString() + ".txt";

		// locate file, path and name
		File f = new File(System.getProperty("user.dir") + File.separator + "files" + File.separator + fileName);

		try {
			// wrap in proper classes
			FileWriter fw;
			fw = new FileWriter(f, true);
			PrintWriter pw = new PrintWriter(fw);

			// write line by line
			int counterSale = 1;
			for (Product product : inventory) {
				// format first line TO BE -> 1;Client=PERE;Date=29-02-2024 12:49:50;
				StringBuilder firstLine = new StringBuilder(
						counterSale + product.getId() +";Product:" + product.getName() + ";Stock:" + product.getStock());
				pw.write(firstLine.toString());
				fw.write("\n");
				
				counterSale++;

				// format second line TO BE ->
				// 1;Products=Manzana,20.0€;Fresa,10.0€;Hamburguesa,60.0€;
				// build products line
				StringBuilder productLine = new StringBuilder();
			/*	for (Product product : sale.getProducts()) {
					productLine.append(product.getName() + "," + product.getPublicPrice() + ";");
				}
				StringBuilder secondLine = new StringBuilder(counterSale + ";" + "Products=" + productLine + ";");
				pw.write(secondLine.toString());
				fw.write("\n");

				// format third line TO BE -> 1;Amount=93.60€;
				StringBuilder thirdLine = new StringBuilder(counterSale + ";" + "Amount=" + sale.getAmount() + ";");
				pw.write(thirdLine.toString());
				fw.write("\n");

				// increment counter sales
				counterSale++;
			*/
			}
			
	        String totalProductsLine = "Numero total de productos:" + inventory.size() + ";";
	        pw.write(totalProductsLine);
	        fw.write("\n");
			
			
			// close files
			pw.close();
			fw.close();

			isWritten = true;

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return isWritten;
	}

	@Override
	public boolean addProduct(String name, Amount price, int stock, boolean avaiblable) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addStockProduct(String name, int stock) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteProduct(String name) {
		// TODO Auto-generated method stub
		return false;
	}

}