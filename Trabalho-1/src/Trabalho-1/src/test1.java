import java.util.Random;
import java.util.Scanner;


public class test1 {
	
	public static void generatePoints(int n, int m, Memory mem) {
		/**
		 * Generate n random points with ranges between m and -m
		 */
		Random rand = new Random();
		int upperbound = m*2+1;
		
		for(int i=0; i<n; i++) { 
			int x = rand.nextInt(upperbound) - m; // a subtração é porque rand só retorna entre [0, upperbound]
			int y = rand.nextInt(upperbound) - m; // então é necessário a subtração para se obter números negativos
			if(mem.contains(x, y)) { i--; continue; } // ignora esta iteração se já contiver o ponto gerado
			mem.add(x, y, i);
		}
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int defaultOption =2;
		
		int n = sc.nextInt();
		int m = sc.nextInt();
		Memory mem = new Memory(n);
		generatePoints(n, m ,mem);
		/*
		for(int i=0; i<n; i++){
			int x,y;
			x = sc.nextInt();
			y = sc.nextInt();
			mem.add(x,y,i);
		}*/
		Solution solution = new Solution(n);

		solution.permutation(mem);

		solution.printEdges(mem);

		HillClimbing solve = new HillClimbing(mem);
		solve.hillClimbing(solution, defaultOption);
		
		sc.close();
	}
}

