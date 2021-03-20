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
	
	public static Point subPoint(Point a, Point b) {
		/**
		 * Subtrai as coordenadas de dois pontos
		 */
		return new Point(a.x - b.x, a.y - b.y, -1);
	}
	
	public static int crossProduct(Point a, Point b) {
		/**
		 * Produto vetorial de dois pontos
		 */
		return (a.x * b.y) - (a.y * b.x); 
	}
	
	public static int halfStraddle(Point a, Point b, Point c) {
		/**
		 * Metade da operação de Straddle de dois pontos
		 * (saber se um ponto interseta outro)
		 */
		return crossProduct(subPoint(c,a),subPoint(b,a));
	}
	
	public static int straddleTest(Point a, Point b, Point c, Point d) {
		/**
		 * Retorna a posição relativa de um ponto a outro
		 * (saber se um ponto interseta outro)
		 */
		return halfStraddle(c,d,a)*halfStraddle(c,d,b);
	}
	
	public static boolean inBox(Point a, Point b, Point c) {
		/**
		 * Retorna se um ponto coincide noutro (?)
		 */
		return Math.min(a.x,b.x) <= c.x && c.x >= Math.max(a.x,b.x)
				&& Math.min(a.y, b.y) <= c.y && c.y >= Math.max(a.y,b.y);
	}
	
	public static boolean edgeIntersect(Memory mem, Edge i, Edge j) {
		/**
		 * Retorna se duas arestas se intersetam
		 */
		if(straddleTest(mem.points[i.origin], mem.points[i.dest], mem.points[j.origin], mem.points[j.dest])< 0
				&& straddleTest(mem.points[j.origin], mem.points[j.dest], mem.points[i.origin], mem.points[i.dest])< 0)
			return true;
		else if(halfStraddle(mem.points[i.origin],mem.points[i.dest],mem.points[j.origin]) == 0)
			if(inBox(mem.points[i.origin], mem.points[i.dest], mem.points[j.origin]))
				return true;
		else if(halfStraddle(mem.points[i.origin],mem.points[i.dest],mem.points[j.dest]) == 0)
			if(inBox(mem.points[i.origin], mem.points[i.dest], mem.points[j.dest]))
				return true;
		else if(halfStraddle(mem.points[j.origin],mem.points[j.dest],mem.points[i.origin]) == 0)
			if(inBox(mem.points[j.origin], mem.points[j.dest], mem.points[i.origin]))
				return true;
		else if(halfStraddle(mem.points[j.origin],mem.points[j.dest],mem.points[i.dest]) == 0)
			if(inBox(mem.points[j.origin], mem.points[j.dest], mem.points[i.dest]))
				return true;
		return false;
			
	}
	
	//nada de mais aqui, só serviu para testar o gerador de pontos e o greedy
	//ainda nao testei a permutation nem nada
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		int n = sc.nextInt();
		int m = sc.nextInt();
		Memory mem = new Memory(n);
		generatePoints(n, m, mem);
		
		Solution sol = new Solution(n);
		
		sc.close();
	}
}
