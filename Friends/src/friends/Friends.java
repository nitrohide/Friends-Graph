package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {	
		if (g == null || p1 == null || p2 == null) {
			return null;
		}
		if (g.map.get(p1) == null || g.map.get(p2) == null) {
			return null;
		}
		boolean [] traversed = new boolean [g.members.length];
		boolean targetfound = false;
		ArrayList <Person> names = new ArrayList<>();
		ArrayList <String> shortestPath = new ArrayList<>();
		Queue <Person> list = new Queue<>();
		Person start = g.members[g.map.get(p1)];
		Person target = g.members[g.map.get(p2)];
		
		list.enqueue(start);
		while(!list.isEmpty()) {
			Person temp = list.dequeue();
			names.add(temp);
			if(target.equals(temp)) {
				targetfound = true;
				break;
			}
			int nameIndex = g.map.get(temp.name);
			traversed[nameIndex] = true;
			Friend adjacent = g.members[nameIndex].first;
			while(adjacent != null) {
				if(traversed[adjacent.fnum] == false) {
					list.enqueue(g.members[adjacent.fnum]);
						if(target.equals(g.members[adjacent.fnum])) {
							targetfound = true;
							break;
						}
				}
				adjacent = adjacent.next;
			}
		}		
		Stack <String> flip = new Stack<>();
		if (targetfound == true) {
			int minIndex;
			int i = names.size()-1;
			flip.push(names.get(i).name);
			while (i != 0) {
				Friend ptr = names.get(i).first; 
				minIndex = Integer.MAX_VALUE;
				while (ptr != null) {
					Person tempName = g.members[ptr.fnum];
					for (int j = 0; j<names.size(); j++) {
						if ( j < i && tempName == names.get(j) && j < minIndex){
							minIndex = j;
						}
					}
					ptr = ptr.next;
				}
				i = minIndex;
				flip.push(names.get(i).name);
			}
			while (!flip.isEmpty()) {
				shortestPath.add(flip.pop());
			}  
			return shortestPath;
		}
		return null;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		if (g==null) {
			return null;
		}
		if (school==null) {
			return null;
		}
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		ArrayList<String> add;
		ArrayList<Boolean> traversed = new ArrayList<Boolean>();
		ArrayList<Person> list;
		for(int i = 0; i < g.members.length; i++) {
			if(g.members[i].school == null) {
				traversed.add(true);
			}
			else if(!g.members[i].school.equals(school)) {
				traversed.add(true);
			}
			else {
				traversed.add(false);
			}
		}
		for (int i = 0; i < g.members.length; i++) {
			if(!traversed.get(i)) {
				list = new ArrayList<Person>();
				Queue<Person> names = new Queue<Person>();
				names.enqueue(g.members[i]);
				traversed.set(i, true);
				while(!names.isEmpty()) {
					Person temp = names.dequeue();
					list.add(temp);
					Friend ptr = temp.first;
					while(ptr != null) {
						if(!traversed.get(ptr.fnum)) {
							names.enqueue(g.members[ptr.fnum]);
							traversed.set(ptr.fnum, true);
						}
						ptr = ptr.next;
					}
				}
				if(!list.isEmpty()) {
					add = sift(list, school);
					if(add != null) {
						result.add(add);
					}
				}
			}
		}
		if(result.isEmpty()) {
			return null;
		}	
		return result;	
	}
	
	private static ArrayList<String> sift(ArrayList<Person> list, String school){
		ArrayList<String> result = new ArrayList<String>();
		if(list == null) {
			return null;
		}
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i).school != null) {
				if(list.get(i).school.equals(school)) {
					result.add(list.get(i).name);
				}
			}
		}	
		if(result.isEmpty()) {
			return null;
		}
		return result;
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {	
		if (g == null || g.members.length <= 2) 
			return null;
		ArrayList<ArrayList<ArrayList<Integer>>> list = new ArrayList<ArrayList<ArrayList<Integer>>>();
		ArrayList<String> connectors = new ArrayList<String>();
		ArrayList<Integer> counter = new ArrayList<Integer>();
		counter.add(1);
		for(int i = 0; i < g.members.length; i++) {
			ArrayList<ArrayList<Integer>> Input = new ArrayList<ArrayList<Integer>>();
			for(int j = 0; j < g.members.length; j++) {
				Input.add(null);
			}
			Input.add(counter);
			Input = BFS(Input, true, i, g);
			list.add(Input);
		}
		if(list.size() == 0) {
			return null;
		}
		for(int i = 0; i < list.size(); i++) {
			ArrayList<ArrayList<Integer>> Input = list.get(i);
			for(int j = g.members.length +1; j < Input.size(); j++) {
				if(test(g.members[Input.get(j).get(0)].name, connectors))
					connectors.add(g.members[Input.get(j).get(0)].name);
			}
		}
		if(connectors.size() == 0) {
			return null;
		}
		return connectors;
	}
	
	private static boolean test(String name, ArrayList<String> connectors) {
		if(connectors == null) {
			return true;
		}
		for(int i = 0; i < connectors.size(); i++) {
			if(connectors.get(i).compareTo(name) == 0) {
				return false;
			}
		}
		return true;
	}
	
	private static ArrayList<ArrayList<Integer>> BFS(ArrayList<ArrayList<Integer>> List, boolean first, int Vertex, Graph g){
		ArrayList<Integer> New = new ArrayList<Integer>();
		New.add(List.get(g.members.length).get(0));
		New.add(List.get(g.members.length).get(0));
		List.set(Vertex, New);
		List.get(g.members.length).set(0, List.get(g.members.length).get(0) + 1);
		Friend ptr = g.members[Vertex].first;
		while(ptr != null) {
			int ptrIn = ptr.fnum;
			if(List.get(ptrIn) != null) {
				if(List.get(Vertex).get(1) > List.get(ptrIn).get(0)) {
					List.get(Vertex).set(1, List.get(ptrIn).get(0));
				}
			}
			else {
				List = BFS(List, false, ptrIn, g);
				if(List.get(Vertex).get(0) > List.get(ptrIn).get(1)) {
					if(List.get(Vertex).get(1) > List.get(ptrIn).get(1)) {
						List.get(Vertex).set(1, List.get(ptrIn).get(1));
					}
				}
				else if(List.get(Vertex).get(0) <= List.get(ptrIn).get(1)) {
					if(!first) {
						ArrayList<Integer> add = new ArrayList<Integer>();
						add.add(Vertex);
						List.add(add);
					}
				}
			}
			ptr = ptr.next;
		}
		return List;
	}
}

