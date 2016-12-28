package CoIoTe;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class InstanceReader {
	
	private File file;
	
	private Instance instance;

	public InstanceReader(File file) throws HeaderException, CostMatrixException, InstanceFormatException, IOException {
		super();
		long time = System.currentTimeMillis();
		this.setFile(file);
		Scanner sc = new Scanner(file);
		//BufferedReader br = new BufferedReader(new FileReader(file));
		this.instance = readHeader(sc);
		//this.instance = readHeader(br);
		System.out.print("Header read.\nReading cost matrix...\r");
		int percent = 1;
		int tot = this.instance.getPeriodNo()*this.instance.getTypesNo();
		for (int type = 0; type < instance.getTypesNo(); type++) {
			for (int period = 0; period < instance.getPeriodNo(); period++) {
				readCostMatrix(sc, instance);
				//readCostMatrix(br, instance);
				System.out.print("Reading cost matrix... " + (percent*100)/tot + "%\r");
				percent++;
			}
		}
		System.out.println("\nRead cost matrix. Time elapsed " + (System.currentTimeMillis() - time)/1000.0);

		readTaskForCell(sc, instance);
		//readTaskForCell(br, instance);
		for (int type = 0; type < instance.getTypesNo(); type++) {
			for (int period = 0; period < instance.getPeriodNo(); period++) {
				readInitialPositons(sc, instance);
				//readInitialPositons(br, instance);
				System.out.print("Reading initial positions...\r");
			}
		}
		System.out.println("\nRead initial positions. Time elapsed : " + (System.currentTimeMillis() - time)/1000.0);
		
		//instance.setRownum();
		//System.out.println("\nCost matrix indexed. Time elapsed " + (System.currentTimeMillis() - time)/1000.0);
		
		sc.close();
		System.gc();
		//br.close();
		
	}
	
	

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public Instance getInstance() {
		return this.instance;
	}
	
	
	private Instance readHeader(Scanner sc) throws HeaderException {
		String line;
		Integer cellNo, periodNo, typesNo;
		if (sc.hasNextLine()) {
			line = readFirstLine(sc);
			if (line == null) throw new HeaderException("The file is empty\n");
		} else throw new HeaderException("The file is empty\n");
		
		if (line.isEmpty()) throw new HeaderException("The header line in the file is empty\n");
		
		String[] header = line.split(" ");
		if (header.length != 3) throw new HeaderException("The header line in the file does not contain the correct number of parameters\n");
		
		cellNo = Integer.valueOf(header[0]);
		periodNo = Integer.valueOf(header[1]);
		typesNo =  Integer.valueOf(header[2]);

		line = readFirstLine(sc);
		if (line == null) throw new HeaderException("The header in the file has a wrong format\n");
		
		String[] tasksForType = line.split(" ");
		if (tasksForType.length != typesNo) throw new HeaderException("The header line in the file does not contain the correct number of parameters\n");
		
		Instance in = new Instance(cellNo, periodNo, typesNo);
		for (int type = 0; type < typesNo; type++) {
			in.setNumberOfTasks(type, Integer.valueOf(tasksForType[type]));
		}
		return in;
	}
	
	private void readCostMatrix(Scanner sc, Instance instance) throws CostMatrixException {
		String line;
		Integer type;
		Integer period;
		line = readFirstLine(sc);
		if (line == null) throw new CostMatrixException("The header in the cost matrix file line has a wrong format\n");
		String[] ij = line.split(" ");
		if (ij.length != 2) throw new CostMatrixException("The header in the cost matrix file line has a wrong format\n");
		type = Integer.valueOf(ij[0]);
		period = Integer.valueOf(ij[1]);
		
		Integer cellsNo = instance.getCellNo();
		for (int i = 0; i < cellsNo; i++) {
			line = sc.nextLine();
			String[] values = line.split(" ");
			for (int j = 0; j < cellsNo; j++) {
				instance.setCostMatrix(type, period, Integer.valueOf(i), Integer.valueOf(j), (int)Math.floor(Double.parseDouble(values[j])));
			}
		}
		
		return;
	}
	
	private void readTaskForCell(Scanner sc, Instance in) throws InstanceFormatException {
		String line = readFirstLine(sc);
		if (line == null) throw new InstanceFormatException("The instance file has a wrong format");
		String[] taskForCell = line.split(" ");
		for (int i = 0; i < in.getCellNo(); i++) {
			in.setTaskForCell(i, Integer.valueOf(taskForCell[i]));
		}
	}
	
	private void readInitialPositons(Scanner sc, Instance in) throws InstanceFormatException {
		String line = readFirstLine(sc);
		if (line == null) throw new InstanceFormatException("The instance file has a wrong format");
		String[] typeAndPeriod = line.split(" ");
		Integer type = Integer.valueOf(typeAndPeriod[0]);
		Integer period = Integer.valueOf(typeAndPeriod[1]);
		line = readFirstLine(sc);
		if (line == null) throw new InstanceFormatException("The instance file has a wrong format");
		String[] peopleInCell = line.split(" ");
		for (int i = 0; i < in.getCellNo(); i++)
			in.setPeopleTypeInCellAtTimePeriod(Integer.valueOf(peopleInCell[i]), type, i, period);
		
	}
	
	private String readFirstLine(Scanner sc) {
		while(sc.hasNextLine()) {
			String line = sc.nextLine();
			if (line.trim().length() != 0) {
				return line.trim();
			}
		}
		return null;
	}
	
	

}
