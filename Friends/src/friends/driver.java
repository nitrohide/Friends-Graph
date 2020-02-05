package friends;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class driver {

	 public static void main( String[] args ) 
	 throws IOException{
					Scanner sc = new Scanner(new File("test1.txt"));
					Graph g = new Graph(sc);		
					
					ArrayList<String> shortest = Friends.shortestChain(g, "aparna", "sam");
					if(shortest == null) {
						System.out.println("Found no link");
					} else {
						for(String person : shortest) {
							System.out.println(person);
						}
					}
					System.out.println("\n\n");
					
					ArrayList<ArrayList<String>> cliques = Friends.cliques(g, "cornell");
					if(cliques != null) {
						for(ArrayList<String> perClique : cliques) {
							System.out.println(perClique.toString());
						}
					}
					System.out.println("\n\n");
					
					ArrayList<String> connectors = Friends.connectors(g);
					for(String connector : connectors) {
						System.out.println(connector);
					}
					
				}
				
			}