import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class driver {
	
	public static void generatePoints(int n, int m, Memory mem) {
		/**
		 * Generate n random points with ranges between m and -m
		 */
		Random rand = new Random();
		int upperbound = m*2+1;
		
		for(int i=0; i<n; i++) { //adiciona condição para confirmar que n ha pontos repetidos
			int x = rand.nextInt(upperbound) - m; // a subtração é porque rand só retorna entre [0, upperbound]
			int y = rand.nextInt(upperbound) - m; // então é necessário a subtração para se obter números negativos
			mem.add(x, y, i);
		}
	}

	public static void printConflicts(ArrayList<Edge> conflicts,Memory mem){
		for(int i=0; i<conflicts.size(); i+=2){
			Edge a,b;
			a = conflicts.get(i);
			b = conflicts.get(i+1);
			Point start1, end1, start2, end2;

			start1 = mem.points[a.origin];
			end1 = mem.points[a.dest];

			start2 = mem.points[b.origin];
			end2 = mem.points[b.dest];

			System.out.println(start1 + " " + end1 + " " + start2 + " " + end2);
		}

	}

	public static void printEdges(Solution solution){
		Memory mem = solution.mem;
		
		for(int i=0; i<solution.solMaxSize; i++){
			Edge a = solution.edges[i];
			Point start,end;
			start = mem.points[a.origin];
			end = mem.points[a.dest];
			
			System.out.println(start + " " + end);
		}
	}

	public static  Solution firstImprovement(Solution solution, ArrayList<Edge> conflicts){
		
		Solution temp = new Solution(solution.mem, solution.edges);
		
		int perimeter = Integer.MAX_VALUE;

		for(int i=0; i<conflicts.size(); i+=2){
			Edge a = conflicts.get(i);
			Edge b = conflicts.get(i+1);
			temp.twoExchange(a, b);
			int tempPerimeter = temp.getPerimiter();
			if(tempPerimeter < perimeter){
				break;
			}
			temp = new Solution(solution.mem, solution.edges);
		}
		return temp;
	}

	public static Solution bestImprovementFirst(Solution solution, ArrayList<Edge> conflicts){
		
		int perimeter = solution.getPerimiter();
		
		Solution best = new Solution(solution.mem, solution.edges);
		Solution temp = new Solution(solution.mem, solution.edges);

		for(int i=0; i<conflicts.size(); i+=2){
			Edge a = conflicts.get(i);
			Edge b = conflicts.get(i+1);
			temp.twoExchange(a, b);
			System.out.println("\n");
			int tempPerimeter = temp.getPerimiter();
			if(tempPerimeter < perimeter){
				best = new Solution(temp.mem, temp.edges);
				perimeter = tempPerimeter;
			}
			temp = new Solution(solution.mem, solution.edges);
		}
		return best;
	}

	public static  Solution getNextNeighbor(Solution solution,ArrayList<Edge> conflicts, int option ){
		Solution next = new Solution();
		switch(option){
			case 1:
				next = bestImprovementFirst(solution, conflicts);
				break;
			case 2:
				next = firstImprovement(solution, conflicts);
				break;
		}
		return next;
	}

	public static Solution hillClimbing(Solution solution, ArrayList<Edge> conflicts, int option){
	
		if(conflicts.size() ==0){
			return solution;
		}
		Solution temp = getNextNeighbor(solution, conflicts, option);

		if(temp.getPerimiter() < solution.getPerimiter()){
			 return hillClimbing(temp, temp.getAllConflicts(), option);
		}

		return solution;
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int defaultOption =1;
		
		int n = sc.nextInt();
		//int m = sc.nextInt();
		Memory mem = new Memory(n);

		for(int i=0; i<n; i++){
			int x,y;
			x = sc.nextInt();
			y = sc.nextInt();
			mem.add(x,y,i);
		}
		Solution solution = new Solution(n,mem);

		solution.greedy(mem);

		printEdges(solution);

		Solution finalResult = hillClimbing(solution, solution.getAllConflicts(),defaultOption);
		System.out.println("\nSolução final\n");
		
		printEdges(finalResult);
		ArrayList<Edge> conflicts = finalResult.getAllConflicts();
		System.out.println("\n Conflitos= " + conflicts.size());
		
		sc.close();
	}
}
