 package model;

import java.text.DecimalFormat;
import javax.xml.bind.annotation.XmlElement;

public class Amount {
	private double value;	
	private String currency="€";
	
	private static final DecimalFormat df = new DecimalFormat("0.00");
	
	public Amount() {
		
	}
	
	public Amount(double value) {
		super();
		this.value = value;
	}

	@XmlElement
	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return df.format(value) + currency;
	}
	
	

	
}
