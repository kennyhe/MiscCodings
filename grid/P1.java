import java.util.ArrayList;
import java.util.Scanner;


public class P1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner sc = null;
		
		try {
			sc = new Scanner(System.in);
			
			while (sc.hasNext()) {
				String line = sc.nextLine();
				parse(line);
			}
		} catch (Exception e) {
			Dbg.warning("Error when read from stand input.");
			e.printStackTrace();
		} finally {
			sc.close();
		}
	}
	
	
	private static void parse(String s) {
		String regex;
		if (s.matches("\\s*")) // Blank row
			return;
		
		if (s.matches("\\s*#.*")) // Comments
			return;
		
		// Dimension
		if (s.matches("dimension\\s+\\d+\\s*.*")) {
			System.out.println("\n\nCommand: " + s);
			if (changedDim) {
				System.out.println("You have already set the dimension. Can not change it again.");
			} else {
				changedDim = true;
				Scanner scan = new Scanner(s);
				scan.skip("dimension\\s+");
				GlobalSettings.setDimension(scan.nextInt());
				scan.close();
			}
			return;
		}
		
		
		// Add node
		regex = "\\s*[aA]dd[nN]ode\\s*\\(\\s*(0\\.\\d+\\s*\\,\\s*){" + (GlobalSettings.getDimension() - 1) +"}0\\.\\d+\\s*\\).*";
//		Dbg.verbose(regex);
		if (s.matches(regex)) {
			System.out.println("\n\nCommand: " + s);
			if (changedDim) {
				double[] d = new double[GlobalSettings.getDimension()];
				Scanner scan = new Scanner(s);
				scan.useDelimiter("\\s*[\\,\\)]\\s*");
				scan.skip("\\s*[aA]dd[nN]ode\\s*\\(\\s*");
				int i = 0;
				while (scan.hasNextDouble()) {
					d[i++] = scan.nextDouble();
				}
				scan.close();
				nf.addNode(d);
				System.out.println(nf.printNodes());
			} else {
				System.out.println("You still have not set the dimension. Please set the dimension first.");
			}
			
			return;
		}
		
		
		// RemoveNode
		regex = "\\s*[rR]emove[nN]ode\\s*\\(\\s*(0\\.\\d+\\s*\\,\\s*){" + (GlobalSettings.getDimension() - 1) +"}0\\.\\d+\\s*\\).*";
		if (s.matches(regex)) {
			System.out.println("\n\nCommand: " + s);
			if (changedDim) {
				double[] d = new double[GlobalSettings.getDimension()];
				Scanner scan = new Scanner(s);
				scan.useDelimiter("\\s*[\\,\\)]\\s*");
				scan.skip("\\s*[Rr]emove[nN]ode\\s*\\(\\s*");
				int i = 0;
				while (scan.hasNextDouble()) {
					d[i++] = scan.nextDouble();
				}
				scan.close();
				nf.removeNode(d);
				System.out.println(nf.printNodes());
			} else {
				System.out.println("You still have not set the dimension. Please set the dimension first.");
			}
			
			return;
		}
		

		// Add item
		regex = "\\s*[iI]nsert[iI]tem\\s*\\(\\s*(0\\.\\d+\\s*\\,\\s*){" + (GlobalSettings.getDimension() ) +"}\"[^\"]+\"\\s*\\).*";
		if (s.matches(regex)) {
			System.out.println("\n\nCommand: " + s);
			if (changedDim) {
				double[] d = new double[GlobalSettings.getDimension()];
				Scanner scan = new Scanner(s);
				scan.useDelimiter("\\s*[\\,\\)]\\s*");
				scan.skip("\\s*[iI]nsert[iI]tem\\s*\\(\\s*");
				int i = 0;
				while (scan.hasNextDouble()) {
					d[i++] = scan.nextDouble();
				}
				String name = scan.next();
				name  = name.replaceAll("\"", "");
				scan.close();
				nf.insertItem(d, name);
				System.out.println(nf.printItems());
			} else {
				System.out.println("You still have not set the dimension. Please set the dimension first.");
			}
			
			return;
		}
		
		
		// DeleteItem
		regex = "\\s*[dD]elete[iI]tem\\s*\\(\\s*(0\\.\\d+\\s*\\,\\s*){" + (GlobalSettings.getDimension() ) +"}\".+\"\\s*\\).*";
		if (s.matches(regex)) {
			System.out.println("\n\nCommand: " + s);
			if (changedDim) {
				double[] d = new double[GlobalSettings.getDimension()];
				Scanner scan = new Scanner(s);
				scan.useDelimiter("\\s*[\\,\\)]\\s*");
				scan.skip("\\s*[dD]elete[iI]tem\\s*\\(\\s*");
				int i = 0;
				while (scan.hasNextDouble()) {
					d[i++] = scan.nextDouble();
				}
				String name = scan.next();
				name  = name.replaceAll("\"", "");
				scan.close();
				nf.deleteItem(d, name);
				System.out.println(nf.printItems());
			} else {
				System.out.println("You still have not set the dimension. Please set the dimension first.");
			}
			
			return;
		}
		
		
		// FindItem
		regex = "\\s*[fF]ind\\s*\\(\\s*(0\\.\\d+\\s*\\,\\s*){" + (GlobalSettings.getDimension() ) +"}\".+\"\\s*\\).*";
		if (s.matches(regex)) {
			System.out.println("\n\nCommand: " + s);
			if (changedDim) {
				double[] d = new double[GlobalSettings.getDimension()];
				Scanner scan = new Scanner(s);
				scan.useDelimiter("\\s*[\\,\\)]\\s*");
				scan.skip("\\s*[fF]ind\\s*\\(\\s*");
				int i = 0;
				while (scan.hasNextDouble()) {
					d[i++] = scan.nextDouble();
				}
				String name = scan.next();
				name  = name.replaceAll("\"", "");
				scan.close();
				ArrayList<Node> path = nf.findItem(d, name);
				if (path == null) {
					System.out.println("Can't locate the item. Please check whether the name is correct.");
					return;
				}
				System.out.println("The path to the destination:");
				for (Node tmp: path) {
					System.out.print("==>");
					System.out.print(tmp.toString());
				}
			} else {
				System.out.println("You still have not set the dimension. Please set the dimension first.");
			}
			
			return;
		}
		
		

		System.out.println("Syntax error: " + s);
	}

	static NodeFactory nf = new P2pNodeFactory();
	static boolean changedDim = false;
}
