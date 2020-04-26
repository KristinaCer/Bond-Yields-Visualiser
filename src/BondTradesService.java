
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

/**
 * This class is responsible for the business logic: it reads files, stores data, and returns the stored data.
 **/

public class BondTradesService {
	
	private final BondReader reader = new BondReader(); 
	
	private Collection<BondTrade> data = new ArrayList<>();
	
	public void readFile(File file) throws IOException {

		InputStream stream = new FileInputStream(file);
		try {
			data = reader.read(stream); // storing the data (yield, amount, days to maturity).
		} finally {
			stream.close(); //closing the stream regardless an error occurs or not. 
		}
	}
	
	
	/** This method is used to return the BondTrade data, stored in the bond trade service:
	 **/
	
	public Collection<BondTrade> getBondTrades() {
		return data;
	}
	 
}
