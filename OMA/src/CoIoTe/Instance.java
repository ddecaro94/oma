package CoIoTe;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Instance {
	int id = 0;
	private Integer cellNo;
	private Integer periodNo;
	private Integer typesNo;
	private Integer[] customerTypeActions;
	private Integer[] cells; //for each cell the number of requested actions
	private CostMatrix[][] costs; //the cost matrix for each type and each step
	private Integer[][][] initialPositions; //the number of people in each cell, for each type, for each period
	private TreeMap<Double, List<Coordinates>>[] listPerCell;
	
	@SuppressWarnings("unchecked")
	public Instance(Integer cellNo, Integer periodNo, Integer typesNo) {
		super();
		this.cellNo = cellNo;
		this.periodNo = periodNo;
		this.typesNo = typesNo;
		this.cells = new Integer[cellNo];
		this.customerTypeActions = new Integer[typesNo];
		this.costs = new CostMatrix[typesNo][periodNo];
		this.initialPositions = new Integer[cellNo][typesNo][periodNo];
		this.listPerCell = new TreeMap[cellNo];
		for (int t = 0; t < periodNo; t++) {
			for (int m = 0; m < typesNo; m++) {
				costs[m][t] = new CostMatrix(cellNo);
			}
		}
		for (int i = 0; i < cellNo; i++) {
			listPerCell[i] = new TreeMap<Double, List<Coordinates>>();
		}
		
	}
	
	public Integer getCellNo() {
		return cellNo;
	}

	public Integer getPeriodNo() {
		return periodNo;
	}

	public Integer getTypesNo() {
		return typesNo;
	}

	public void setNumberOfTasks(Integer type, Integer tasks) {
		this.customerTypeActions[type] = tasks;
	}
	
	public void setCostMatrix(Integer type, Integer period, Integer from, Integer to, Integer value) {
		this.costs[type][period].setCost(from, to, value);
		double val = value/(double)customerTypeActions[type];
		if (from.compareTo(to)!=0) {
			Coordinates c = new Coordinates(from, to, type, period, val);
			if (listPerCell[to].containsKey(val))
				listPerCell[to].get(val).add(c);
			else {
				List<Coordinates> l = new ArrayList<Coordinates>();
				l.add(c);
				listPerCell[to].put(val, l);
			}
		}
	}
	
	public void setTaskForCell(Integer cell, Integer tasks) {
		this.cells[cell] = tasks;
	}
	
	public void setPeopleTypeInCellAtTimePeriod(Integer people, Integer type, Integer cell, Integer timePeriod) {
		this.initialPositions[cell][type][timePeriod] = people;
		
	}
	
	/*public void print() {
		for (int t = 0; t < periodNo; t++) {
			System.out.println("Period of time " + t + "\n");
			for (int m = 0; m < typesNo; m++) {
				System.out.println("Customers of type " + m + "\n");
				this.costs[m][t].print();
			}
		}
		
		System.out.println("Number of tasks to do in each cell:");
		for (int i = 0; i < cellNo; i++)
			System.out.print(cells[i] + " ");
		System.out.println("\n");
		System.out.println("Initial positions: \n");
		
		for (int t = 0; t < periodNo; t++) {
			System.out.println("Period of time " + t + "\n");
			for (int c = 0; c < cellNo; c++) {			
				System.out.println("Cella " + c + ": ");
				for (int m = 0; m < typesNo; m++) {
					System.out.print("\t Tipo " + m + ": " +initialPositions[c][m][t] + " "  + "\n");
				}
			}
		}
		
	}*/

	public Integer[][][] getInitialPositions() {
		return initialPositions;
	}

	public Integer[] getCells() {
		return cells;
	}

	public CostMatrix[][] getCosts() {
		return costs;
	}

	public Integer getCustomerTypeActions(Integer type) {
		return this.customerTypeActions[type];
	}


	public TreeMap<Double, List<Coordinates>>[] getListPerDest() {
		return listPerCell;
	}

	/*public void printLowestCosts() {
		for (int j = 0; j < cellNo; j++) {
			if (cells[j] != 0) {
				System.out.println("Cella " +j);
				for (Coordinates c : listPerCell[j].headSet(new Coordinates(0, 0, 0, 0, 0, 5.0))) {
					if (initialPositions[c.i][c.m][c.t] != 0) System.out.println("\tDa " +c.i + " costo " + c.getCost() + " tipo " + c.m + " tempo " + c.t + " persone " + initialPositions[c.i][c.m][c.t]);
				}
			}
		}
	}*/

	
	
	
}
