import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import CoIoTe.CostMatrixException;
import CoIoTe.GreedyHeuristic;
import CoIoTe.HeaderException;
import CoIoTe.Heuristics;
import CoIoTe.Instance;
import CoIoTe.InstanceFormatException;
import CoIoTe.InstanceReader;
import CoIoTe.Solution;

public class AssignmentMain {

	
	public static void main(String[] args) throws IOException {
		String inputPath = "";
		String outputPath = "";
		//String solPath = "";
		//Boolean test = false;
		Instance instance = null;
		File input = null;
		File output = null;
		for (int i = 0; i<args.length; i++) System.out.println(args[i]);
		for (int i = 0; i<args.length; i++) {
			if (args[i].equals("-i")) {
				inputPath = args[++i];
			}
			if (args[i].equals("-o")) {
				outputPath = args[++i];
			}
			//if (args[i].equals("-s")) {
			//	solPath = args[++i];
			//}
			//if (args[i].equals("-test")) {
			//	test = true;
			//}
		}
		
		if (inputPath.isEmpty() || outputPath.isEmpty()) {
			System.out.println("Usage : \n"
					+ "-i instance_filepath\n"
					+ "-o output_filepath\n"
					+ "-s solution_filepath(optional)\n"
					+ "-test (enables the feasibility check)\n");
			return;
		}
		
		
		input = new File(inputPath);
		output = new File(outputPath);
		
		
		if (!input.exists() || !input.isFile()) {
			System.out.println("The input file does not exist in the specified path\n");
			return;
		}
		
		try {
			InstanceReader reader = new InstanceReader(input);
			instance = reader.getInstance();
			Solution s = new Solution(instance);
			Heuristics h = new GreedyHeuristic(instance);
			long time = System.currentTimeMillis();
			h.compute(5000);
			s = h.getSolution();
			String out = input.getName().replaceAll(".txt", "") + ";" + (System.currentTimeMillis() - time)/1000.0 +";"+ s.toString() + "\n";
			System.out.println(out + " " + (System.currentTimeMillis() - time));
			if (!output.exists()) Files.createFile(Paths.get(outputPath));
			Files.write(Paths.get(outputPath), out.getBytes(), StandardOpenOption.APPEND);
			
		} catch (FileNotFoundException e) {
			System.out.println("The file specified cannot be found or opened\n");
			return;
		} catch (HeaderException h) {
			System.out.println(h);
		} catch (CostMatrixException c) {
			System.out.println(c);
		} catch (InstanceFormatException f) {
			System.out.println(f);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
		

	}

}
