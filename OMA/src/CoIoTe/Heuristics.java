package CoIoTe;

public interface Heuristics {
	
	public Solution getSolution();
	
	public void compute(int millis) throws InterruptedException;

}
