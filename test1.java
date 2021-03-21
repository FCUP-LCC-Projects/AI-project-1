import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class test1 {
	
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
	
	
	
	//nada de mais aqui, só serviu para testar o gerador de pontos e o greedy
	//ainda nao testei a permutation nem nada
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
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

		solution.permutation(mem);

		ArrayList<Edge> conflicts = solution.getAllConflicts();

		printEdges(solution);
		System.out.println();

		printConflicts(conflicts, mem);


		


		
	

	





		
		sc.close();
	}
}
