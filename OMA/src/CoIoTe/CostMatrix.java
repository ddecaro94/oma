package CoIoTe;

public class CostMatrix {
	private Integer cellsNo;
	private Integer[][] costs;

	public CostMatrix(Integer cellsNo) {
		this.cellsNo = cellsNo;
		this.costs = new Integer[cellsNo][cellsNo];
	}
	public void setCost(Integer from, Integer to, Integer value) {
		if (from.compareTo(to) != 0) {
			this.costs[from][to] = value;
		} else {
			this.costs[from][to] = Integer.MAX_VALUE;
		}
	}
	public int getCost(Integer from, Integer to) {
		return this.costs[from][to];
	}
	
	public void print() {
		for (int i = 0; i < this.cellsNo; i ++) {
			for (int j = 0; j < this.cellsNo; j++) {
				System.out.print(this.costs[i][j] + " ");
			}
			System.out.println("\n");
		}
	}

}
