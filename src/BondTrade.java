 
public class BondTrade {
	
	/** The values are final so once the reader reads a file, 
	and the object is created, it is impossible to change these values. The object is immutable.
	It is done for the security purposes.**/
	
	public static final String YIELD_AXIS = "YIELD"; 
	public static final String DAYS_AXIS = "DAYS TO MATURITY";
	public static final String AMOUNT_AXIS = "AMOUNT";
	public static final String[] AXIS = {YIELD_AXIS, DAYS_AXIS, AMOUNT_AXIS}; 
	
	public BondTrade(double yield, long daysToMaturity, long amount) {
		this.yield = yield;
		this.daysToMaturity = daysToMaturity;
		this.amount = amount;
	}
	
    public final double yield;
    public final long daysToMaturity;
    public final long amount;
    

    public Double getByAxisLabel(String label) { 

		if (label.equals(YIELD_AXIS)) { //Return coordinate values based on the axis name.
			return yield;
		} else if (label.equals(AMOUNT_AXIS)) {
			return (double) amount;
		} else if (label.equals(DAYS_AXIS)) {
			return  (double) daysToMaturity;
		} else {
			throw new IllegalArgumentException("Incorrect axis label!");
		}
    }
     
    @Override
    public String toString() {
    	return "yield: "+yield+"\namount: "+amount+" \ndays: "+daysToMaturity;
    }
    
}
