import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class test1 {
	
	public static Point[] generatePoints(int n, int m) {
		Point[] points = new Point[n];
		/**
		 * Generate n random points with ranges between m and -m
		 */
		Random rand = new Random();
		int upperbound = m*2+1;
		
		for(int i=0; i<n; i++) { //adiciona condição para confirmar que n ha pontos repetidos
			int x = rand.nextInt(upperbound) - m; // a subtração é porque rand só retorna entre [0, upperbound]
			int y = rand.nextInt(upperbound) - m; // então é necessário a subtração para se obter números negativos
			points[i] = new Point(x,y);
		}

		return points;
	}
	

	
	
	
	//nada de mais aqui, só serviu para testar o gerador de pontos e o greedy
	//ainda nao testei a permutation nem nada
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		//int n = sc.nextInt();
		//int m = sc.nextInt();
		Point[] mem = new Point[5];
		//generatePoints(n, m, mem);
		Point a = new Point(1,3);
		Point b = new Point(2,1);
		Point c = new Point(3,5);
		Point d = new Point(5,4);
		Point e = new Point(6,2);
		mem[0] =a;
		mem[1] = b;
		mem[2] = c;
		mem[3] = d;
		mem[4] = e;
		
		Solution sol = new Solution(mem);
		sol.greedy();



		
		sc.close();
	}
}
