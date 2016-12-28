package CoIoTe;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GreedyHeuristic implements Heuristics{
	
	private Instance instance;
	private Solution best;

	public GreedyHeuristic(Instance instance) {
		this.instance = instance;
		this.best = new Solution(instance);
	}

	@Override
	public Solution getSolution() {
		return best;
	}

	@Override
	public void compute(int millis) throws InterruptedException {
		this.best.greedyRandomIt();
		this.best.perturbateExcessExactly();
		NavigableMap<Integer, Solution> pop = generate(4, millis - instance.getCellNo()*4);
		Solution s;
		try {
			s = pop.firstEntry().getValue();
		} catch (Exception e) {
			s = this.best;
		}
		if (s.getValue() < this.best.getValue())
			this.best = new Solution(instance, s);


	}
	

	private NavigableMap<Integer, Solution> generate(int size, int millis) throws InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(size);
		SolutionThread[] threads = new SolutionThread[size];
		ConcurrentNavigableMap<Integer, Solution> pop = new ConcurrentSkipListMap<Integer, Solution>();
		for (int i = 0; i < size; i++) {
			threads[i] = new SolutionThread(instance, pop, millis - instance.getCellNo()*4);
			executor.execute(threads[i]);
		}
		executor.awaitTermination(millis - instance.getCellNo()*2, TimeUnit.MILLISECONDS);
		executor.shutdownNow();
		return pop;
	}

}
