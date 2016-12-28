package CoIoTe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

public class Solution {
	
	private Integer cellNo;
	private Integer typesNo;
	private Integer periodNo;
	public Integer[][][][] Xijmt;
	public Integer[][][] available;
	private Integer[][][] initialPositions;
	private CostMatrix[][] costs;
	private Integer[] types;
	public Integer[] demand;
	private Integer[] cells;
	private Integer value;
	private TreeMap<Double, List<Coordinates>>[] listPerCell;
	

	public Solution (Instance instance) {
		this.cellNo = instance.getCellNo();
		this.typesNo = instance.getTypesNo();
		this.periodNo = instance.getPeriodNo();
		this.Xijmt = new Integer[cellNo][cellNo][typesNo][periodNo];
		this.available = new Integer[cellNo][typesNo][periodNo];
		this.initialPositions = instance.getInitialPositions();
		this.costs = instance.getCosts();
		this.types = new Integer[typesNo];
		this.cells = instance.getCells();
		this.demand = new Integer[cellNo];

		this.listPerCell = instance.getListPerDest();
		this.value = 0;
		for(int i = 0; i < cellNo; i++) {
			demand[i] = cells[i];
			for(int m = 0; m < typesNo; m++) {
				types[m] = instance.getCustomerTypeActions(m);
				for (int t = 0; t < periodNo; t++) {
					available[i][m][t] = initialPositions[i][m][t];
					for(int j = 0; j < cellNo; j++) {
						Xijmt[i][j][m][t] = 0;
					}
				}
			}
		}

	}
	
	public Solution(Instance instance, Solution s) {
		this.cellNo = instance.getCellNo();
		this.typesNo = instance.getTypesNo();
		this.periodNo = instance.getPeriodNo();
		this.Xijmt = new Integer[cellNo][cellNo][typesNo][periodNo];
		this.available = new Integer[cellNo][typesNo][periodNo];
		this.initialPositions = instance.getInitialPositions();
		this.costs = instance.getCosts();
		this.types = new Integer[typesNo];
		this.cells = instance.getCells();
		this.demand = new Integer[cellNo];
		this.listPerCell = instance.getListPerDest();
		this.value = s.getValue();
		for(int i = 0; i < cellNo; i++) {
			demand[i] = s.demand[i];
			for(int m = 0; m < typesNo; m++) {
				types[m] = instance.getCustomerTypeActions(m);
				for (int t = 0; t < periodNo; t++) {
					available[i][m][t] = s.available[i][m][t];
					for(int j = 0; j < cellNo; j++) {
						Xijmt[i][j][m][t] = s.Xijmt[i][j][m][t];
					}
				}
			}
		}
	}
	
	public void exchangeCustomers() {
		for (int m = 0; m < typesNo; m++) {
			for (int j = 0; j < cellNo; j++) {
				for (int i = 0; i < cellNo; i++) {
					for (int t = 0; t < periodNo; t++) {
						if (Xijmt[i][j][m][t] != 0) {
							for (int i1 = 0; i1 < cellNo; i1++) {
								for (int j1 = 0; j1 < cellNo; j1++) {
									for (int t1 = 0; t1 < periodNo; t1++) {
										if (Xijmt[i1][j1][m][t1] != 0 && i1 != i && j1 != j) {
											if (costs[m][t].getCost(i, j) + costs[m][t1].getCost(i1, j1) > costs[m][t].getCost(i, j1) + costs[m][t1].getCost(i1, j)) {
												int n = Integer.min(Xijmt[i][j][m][t], Xijmt[i1][j1][m][t1]);
												Xijmt[i][j][m][t] -= n;
												Xijmt[i][j1][m][t] += n;
												Xijmt[i1][j1][m][t1] -= n;
												Xijmt[i1][j][m][t1] += n;
												this.computeValue();
												System.out.println(this.value);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	public void greedy2() {
		Random r = new Random();
		TreeMap<Integer, List<Integer>> sortedDest = new TreeMap<>();
		for (int j = 0; j < cellNo; j++) {
			if (sortedDest.containsKey(cells[j]))
				sortedDest.get(cells[j]).add(j);
			else {
				sortedDest.put(cells[j], new LinkedList<>());
				sortedDest.get(cells[j]).add(j);
			}
		}
		for (List<Integer> l : sortedDest.values())
		for (Integer j : l)
		for (Coordinates c : listPerCell[j].pollFirstEntry().getValue()) {
			if (c.i != c.j) {
				if (demand[c.j] > 0) {
					if (initialPositions[c.i][c.m][c.t] > 0) {
						boolean choice = r.nextBoolean();
						int taken;
						if (choice) {
							taken = Integer.min((int)Math.ceil(demand[c.j]/(double)types[c.m]), initialPositions[c.i][c.m][c.t]);
						} else {
							taken = Integer.min((int)Math.floor(demand[c.j]/(double)types[c.m]), initialPositions[c.i][c.m][c.t]);
						}
						Xijmt[c.i][c.j][c.m][c.t] = taken;
						demand[c.j] -= types[c.m] * taken;
						available[c.i][c.m][c.t] -= taken;
						value += taken * costs[c.m][c.t].getCost(c.i, c.j);
					}
				}
			}
		}
	}
	
	
	public void greedyRandomIt() {
		Random r = new Random();
		double val;
		
		ArrayList<Integer> sortedDest = new ArrayList<>();
		for (int j = 0; j < cellNo; j++) {
			sortedDest.add(j);
		}
		Collections.shuffle(sortedDest);	
		for(Integer j : sortedDest) {
		boolean notSatisfied = true;
		val = listPerCell[j].firstKey();
		while (notSatisfied) {
			for (Coordinates c : listPerCell[j].get(val)) {
				if (c.i != c.j && c.j == j && notSatisfied) {
						if (demand[c.j] > 0) {
							if (available[c.i][c.m][c.t] > 0) {
								boolean choice = r.nextBoolean();
								int taken;
								if (choice) {
									taken = Integer.min((int)Math.ceil(demand[c.j]/(double)types[c.m]), available[c.i][c.m][c.t]);
								} else {
									taken = Integer.min((int)Math.floor(demand[c.j]/(double)types[c.m]), available[c.i][c.m][c.t]);
								}
								Xijmt[c.i][c.j][c.m][c.t] += taken;
								demand[c.j] -= types[c.m] * taken;
								available[c.i][c.m][c.t] -= taken;
								value += taken * costs[c.m][c.t].getCost(c.i, c.j);
						}
					} else {
						notSatisfied = false;
					}
				}
			}
			if (notSatisfied) val = listPerCell[j].higherKey(val);
			}
		}
	}
	
	public int getValue() {
		return this.value;
	}
	
	
	public void computeValue() {
		this.value = 0;
		for (int i = 0; i < this.Xijmt.length; i++) {
			for (int j = 0; j < this.Xijmt[i].length; j++ ) {
				for (int m = 0; m < this.Xijmt[i][j].length; m++) {
					for (int t = 0; t < this.Xijmt[i][j][m].length; t++) {
						value += Xijmt[i][j][m][t] * costs[m][t].getCost(i, j);
					}
				}
			}
		}
	}

	
	public boolean isFeasible() {
		for (int i = 0; i < this.cellNo; i++) {
			if (demand[i] > 0) {
				System.out.println("Demand in cell " + i + " not satisfied - still " + demand[i] + " actions to perform");
				return false;
			}
			for (int m = 0; m < this.available[i].length; m++) {
				for (int t = 0; t < this.available[i][m].length; t++) {
					if (available[i][m][t] < 0) {
						System.out.println("Ran out of customers in cell " + i + " of type " + m + " at time period " + t);
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public void perturbateSlice(Integer percentage) {
		Random r = new Random();
		Integer cellsToPerturbate = (cellNo * percentage)/100;
		for (int k = 0; k < cellsToPerturbate; k++) {
			int j = r.nextInt(cellNo);
			
			
			for (int i = 0; i < this.Xijmt.length; i++) {
				for (int m = 0; m < this.Xijmt[i][j].length; m++) {
					for (int t = 0; t < this.Xijmt[i][j][m].length; t++) {
						if (Xijmt[i][j][m][t] != 0) {
							available[i][m][t] += Xijmt[i][j][m][t];
							demand[j] += types[m] * Xijmt[i][j][m][t];
							value -= Xijmt[i][j][m][t] * costs[m][t].getCost(i, j);
							Xijmt[i][j][m][t] = 0;
						}
					}
				}
			}
		}
	}
	
	public void perturbateSlice(int j) {
		for (int i = 0; i < this.Xijmt.length; i++) {
			for (int m = 0; m < this.Xijmt[i][j].length; m++) {
				for (int t = 0; t < this.Xijmt[i][j][m].length; t++) {
					if (Xijmt[i][j][m][t] != 0) {
						available[i][m][t] += Xijmt[i][j][m][t];
						demand[j] += types[m] * Xijmt[i][j][m][t];
						value -= Xijmt[i][j][m][t] * costs[m][t].getCost(i, j);
						Xijmt[i][j][m][t] = 0;
					}
				}
			}
		}
	}
	
	public void perturbateRandomPoint(Integer percentage) {
		Random r = new Random();
		Integer cellsToPerturbate = (cellNo * percentage)/100;
		int i = r.nextInt(cellNo);
		int j = r.nextInt(cellNo);
		int m = r.nextInt(typesNo);
		int t = r.nextInt(periodNo);
		for (int k = 0; k < cellsToPerturbate; k++) {
			while (Xijmt[i][j][m][t] == 0) {
				i = r.nextInt(cellNo);
				j = r.nextInt(cellNo);
				m = r.nextInt(typesNo);
				t = r.nextInt(periodNo);
			}
			if (Xijmt[i][j][m][t] != 0) {
				int remove = r.nextInt(Xijmt[i][j][m][t]);
				available[i][m][t] += remove;
				demand[j] += types[m] * remove;
				value -= remove * costs[m][t].getCost(i, j);
				Xijmt[i][j][m][t] -= remove;
			}
		}	
	}
	
	public void perturbateExcess() {
		Random r = new Random();
		for(int j = 0; j < cellNo; j++) {
			if(demand[j] < 0) {
				int i = r.nextInt(cellNo);
				int m = r.nextInt(typesNo);
				int t = r.nextInt(periodNo);
				while (Xijmt[i][j][m][t] == 0) {
					i = r.nextInt(cellNo);
					m = r.nextInt(typesNo);
					t = r.nextInt(periodNo);
				}
				if (Xijmt[i][j][m][t] != 0) {
					int remove = (int) Math.ceil(Math.abs(demand[j])/(double)types[m]);
					available[i][m][t] += remove;
					demand[j] += types[m] * remove;
					value -= remove * costs[m][t].getCost(i, j);
					Xijmt[i][j][m][t] -= remove;
				}
			}
		}
		for(int j = 0; j < cellNo; j++) System.out.print(demand[j] + " ");
		System.out.print("\n");
	}
	
	public void perturbateExcessExactly() {
		for(int j = 0; j < cellNo; j++) {
			boolean canBeRemoved1 = false;
			boolean canBeRemoved2 = false;
			boolean canBeRemoved3 = false;
			boolean canBeInserted2 = false;			
			boolean first = true;
			double val;
			int costd1 = 0;
			int costd2 = 0;
			int costd3 = 0;
			int costi2 = 10000;
			Coordinates d1 = new Coordinates(0, j, 0, 0, 0.0);
			Coordinates d2 = new Coordinates(0, j, 0, 0, 0.0);
			Coordinates d3 = new Coordinates(0, j, 0, 0, 0.0);
			Coordinates i2 = new Coordinates(0, j, 0, 0, 0.0);
			if(demand[j] == -1) {
				val = listPerCell[j].firstKey();
				while(val < listPerCell[j].lastKey()) {
					for (Coordinates c : listPerCell[j].get(val)) {					
							if (Xijmt[c.i][c.j][c.m][c.t] != 0 && c.m == 0) {
								if (c.getCost()>d1.getCost()) {
									canBeRemoved1 = true;
									d1 = new Coordinates(c);
								}
							}
							
						}
				val = listPerCell[j].higherKey(val);
				}
				if (canBeRemoved1) costd1 = costs[d1.m][d1.t].getCost(d1.i, d1.j);
				
				val = listPerCell[j].firstKey();
				while(val < listPerCell[j].lastKey()) {
					for (Coordinates c : listPerCell[j].get(val)) {
						val = listPerCell[j].ceilingEntry(val).getKey();
						if (Xijmt[c.i][c.j][c.m][c.t] != 0 && c.m == 1) {
							if (c.getCost()>d2.getCost()) {
								canBeRemoved2 = true;
								d2 = new Coordinates(c);					
							}
						}
					}
					val = listPerCell[j].higherKey(val);
				}
				if(canBeRemoved2) {
					costd2 = costs[d2.m][d2.t].getCost(d2.i, d2.j);
					val = listPerCell[j].firstKey();
					while(val < listPerCell[j].lastKey()/2) {
						for (Coordinates c : listPerCell[j].get(val)) {
							if (available[c.i][c.m][c.t] > 0 && c.m == 0) {
								if (c.getCost()<costi2) {								
									i2 = new Coordinates(c);
								}
							}
						}
						val = listPerCell[j].higherKey(val);
					}
					costi2 = costs[i2.m][i2.t].getCost(i2.i, i2.j);
					if(costi2 < costd2) canBeInserted2 = true;
				}
				if (!canBeInserted2 || costd1 > (costd2-costi2) && canBeRemoved1) {
					if (canBeRemoved1) {
					Xijmt[d1.i][d1.j][d1.m][d1.t]--;
					demand[j]++;
					available[d1.i][d1.m][d1.t]++;
					value -= costd1;
					}
				}
				else
					if (canBeInserted2 && (costd1 < (costd2-costi2) || !canBeRemoved1)){
						Xijmt[d2.i][d2.j][d2.m][d2.t]--;
						Xijmt[i2.i][i2.j][i2.m][i2.t]++;
						available[d2.i][d2.m][d2.t]++;
						available[i2.i][i2.m][i2.t]--;
						demand[j]++;
						value += (costi2 - costd2);
					}
			}
			if (demand[j] == -2) {
				val = listPerCell[j].firstKey();
				while(val < listPerCell[j].lastKey()) {
					for (Coordinates c : listPerCell[j].get(val)) {
						if (Xijmt[c.i][c.j][c.m][c.t] != 0 && c.m == 0) {
							if (c.getCost()>d2.getCost()) {
								canBeRemoved2 = true;
								d2 = new Coordinates(c);
							} else {
								if (c.getCost()>d1.getCost()) {
									canBeRemoved1 = true;
									d1 = new Coordinates(c);
								}
							}
						}
					
						if (d2.getCost()>d1.getCost() && canBeRemoved2) {
							if (first){
								canBeRemoved1 = true;
								canBeRemoved2 = false;
								first = false;
							}
							Coordinates tmp = new Coordinates(d2);
							d2 = new Coordinates(d1);
							d1 = tmp;
						}
					}
					val = listPerCell[j].higherKey(val);					
				}
				if (canBeRemoved1)	costd1 = costs[d1.m][d1.t].getCost(d1.i, d1.j);
				if (canBeRemoved2)	costd2 = costs[d2.m][d2.t].getCost(d2.i, d2.j);
				val = listPerCell[j].firstKey();
				while(val < listPerCell[j].lastKey()) {
					for (Coordinates c : listPerCell[j].get(val)) {
						if (Xijmt[c.i][c.j][c.m][c.t] != 0 && c.m == 1) {
							if (c.getCost()>d2.getCost()) {
								canBeRemoved3 = true;
								d3 = new Coordinates(c);					
							}
						}
					}
					val = listPerCell[j].higherKey(val);
				}
				if (canBeRemoved3) costd3 = costs[d3.m][d3.t].getCost(d3.i, d3.j);
				
				if((!canBeRemoved3 || (costd1+costd2) > costd3) && canBeRemoved1 && canBeRemoved2){
						Xijmt[d1.i][d1.j][d1.m][d1.t]--;
						Xijmt[d2.i][d2.j][d2.m][d2.t]--;
						demand[j] += 2;
						available[d1.i][d1.m][d1.t]++;
						available[d2.i][d2.m][d2.t]++;
						value -= costs[d1.m][d1.t].getCost(d1.i, d1.j);
						value -= costs[d2.m][d2.t].getCost(d2.i, d2.j);						
				}
		
				if ((!canBeRemoved3 || costd1 > costd3) && canBeRemoved1 && !canBeRemoved2) {
					Xijmt[d1.i][d1.j][d1.m][d1.t]--;
					demand[j] ++;
					available[d1.i][d1.m][d1.t]++;
					value -= costs[d1.m][d1.t].getCost(d1.i, d1.j);
				}
				
				if ((!canBeRemoved3 || costd2 > costd3) && !canBeRemoved1 && canBeRemoved2) {
					Xijmt[d2.i][d2.j][d2.m][d2.t]--;
					demand[j] ++;
					available[d2.i][d2.m][d2.t]++;
					value -= costs[d2.m][d2.t].getCost(d2.i, d2.j);				
				}
				if((canBeRemoved3 && ((!canBeRemoved1 && !canBeRemoved2) || costd3 > (costd1+costd2)))) {
					Xijmt[d3.i][d3.j][d3.m][d3.t]--;
					demand[j] ++;
					available[d3.i][d3.m][d3.t]++;
					value -= costs[d3.m][d3.t].getCost(d3.i, d3.j);
				}
			}
		}
		
		//for(int j = 0; j < cellNo; j++) System.out.print(demand[j]);
		//System.out.print("\n");
	}
	

	public void print() {
		for (int i = 0; i < this.Xijmt.length; i++) {
			for (int j = 0; j < this.Xijmt[i].length; j++ ) {
				for (int m = 0; m < this.Xijmt[i][j].length; m++) {
					for (int t = 0; t < this.Xijmt[i][j][m].length; t++) {
						if (this.Xijmt[i][j][m][t] != 0)
							System.out.println("I: " + i + " J: " + j + " M: " + m + " T: " + t + " Valore : " + this.Xijmt[i][j][m][t]);
					}
				}
			}
		}
	}
	
	public int getX(int i, int j, int m, int t) {
		return Xijmt[i][j][m][t];
	}

	public Integer[] getDemand() {
		return demand;
	}

	public Integer[][][] getAvailable() {
		return available;
	}

	public Integer[][][][] getSol() {
		return Xijmt;
	}

	
	
	public void printPeopleNo() {
		Integer[] people = new Integer[typesNo];
		for (int m = 0; m < typesNo; m++) {
			people[m] = new Integer(0);
			for (int i = 0; i < this.Xijmt.length; i++) {
				for (int j = 0; j < this.Xijmt[i].length; j++ ) {
					for (int t = 0; t < this.Xijmt[i][j][m].length; t++) {
						if (this.Xijmt[i][j][m][t] != 0) {
							//System.out.println("I: " + i + " J: " + j + " M: " + m + " T: " + t + " Valore : " + this.Xijmt[i][j][m][t]);
							people[m] += this.Xijmt[i][j][m][t];
						}
					}
				}
			}
		}
		for(int i = 0; i < typesNo; i++) System.out.println("Tipo " + i + " - " + people[i]);
		System.out.println(this.value);
	}

	

}
