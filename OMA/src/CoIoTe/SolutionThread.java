package CoIoTe;

import java.util.Map;

public class SolutionThread extends Thread {
	
	private Solution bestSol;
	private Instance instance;
	private Map<Integer, Solution> pop;
	private long millis;

	public SolutionThread(Instance instance, Map<Integer, Solution> pop, long millis) {
		this.instance = instance;
		this.bestSol = new Solution(instance);
		this.pop = pop;
		this.millis = millis;
	}
	
	public void run() {
		this.bestSol = new Solution(instance);
		this.bestSol.greedyRandomIt();
		long time = System.currentTimeMillis();
		for (int i = 0; i < 200000000 && System.currentTimeMillis() - time < millis; i++) {
			Solution s = new Solution(instance);
			s.greedyRandomIt();
			s.perturbateExcessExactly();
			if (s.getValue() < this.bestSol.getValue()) {
				this.bestSol = new Solution(instance, s);
				pop.put(s.getValue(), s);
			}
		}
		return;
	}
	
	public Solution getSolution() {
		return this.bestSol;
	}

}
