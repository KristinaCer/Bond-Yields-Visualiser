import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;

public class BondReader {
	
	private final int COLUMN_COUNT = 3;
	
	public Collection<BondTrade> read(InputStream stream) throws IOException { 
		Collection<BondTrade> result = new ArrayList<>();  
		
		
		 Reader reader = new InputStreamReader(stream);
		 BufferedReader  br = new BufferedReader(reader); 
		 try {  
			 
			 br.readLine();
			 
			 String line;
			 while ((line = br.readLine()) != null) { 
				 result.add(parseLine(line));
			 }
			 
			 
		 } finally {
			 reader.close();
			 br.close();
		 } 
		 
		 return result;
	}
	
	/**
	 * The method parseLine takes a string (a line of data from the csv file in our case) and creates an object from it.
	 **/
	
	private BondTrade parseLine(String line) {
		String[] args = line.split(",");
		
		if (args.length != COLUMN_COUNT) { //3 values from 3 different columns must be retrieved after splitting the data line.
			throw new IllegalStateException("Could not parse datapoint  -> " + line);
		}
		
		double yield = Double.parseDouble(args[0]); //parseDouble and paseLong methods show NumberFormatException (it is included in the method) if an error occurs; no need to handle an exception in this case.
		long daysToMaturity = Long.parseLong(args[1]);
		long amount = Long.parseLong(args[2]);
		
		return new BondTrade(yield, daysToMaturity, amount);
		
	}
	
} 