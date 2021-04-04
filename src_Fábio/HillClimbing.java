import java.util.ArrayList;
import java.util.Random;

public class HillClimbing {
	/** 
	 * Classe para resolução do Hill Climbing e com todas as funções auxiliares
	 * 
	 * mem - Conjunto de pontos gerados originalmente
	 * edgeConflicts - subconjunto referido globalmente de cruzamentos de arestas para qualquer candidato
	 * 					que está a ser analisado.
	 */
	
	public Solution firstImprovement(ArrayList<Solution> neighbours,Solution s){		
		int best = 0;
		long perimeter = s.getPerimiter();
		
		for(int i=1; i<neighbours.size(); i++) {
			long tmpPerimeter = neighbours.get(i).getPerimiter();
			if(tmpPerimeter < perimeter) {
				best = i;
				break;
			}
		}		
		return neighbours.get(best);
	}
	
	
	public Solution bestImprovementFirst(ArrayList<Solution> neighbours, Solution s){	
		/**
		 * Calcula o vizinho com melhor perímetro e retorna o seu índice
		 */
		int best = 0;
		long perimeter = s.getPerimiter();
		
		for(int i=0; i<neighbours.size(); i++) {
			long tmpPerimeter = neighbours.get(i).getPerimiter();
			if(tmpPerimeter < perimeter) {
				best = i;
				perimeter = tmpPerimeter;
			}
		}
		return neighbours.get(best);
	}
	
	public Solution lesserConflicts(ArrayList<Solution> neighbours) {
		/**
		 * Retorna o vizinho com menos conflitos e retorna o seu índice
		 */
		int best = 0;
		int conflicts = Integer.MAX_VALUE;
		
		for(int i=0; i<neighbours.size(); i++) {
			neighbours.get(i).conflicts();
			neighbours.get(i).resolveConflicts();
			int tmpConflicts = neighbours.get(i).totalConflicts;
			
			if(tmpConflicts < conflicts) {
				best = i;
				conflicts = tmpConflicts;
			}
		}
		return neighbours.get(best);
	}
	
	public Solution getNewRandom(int size, ArrayList<Solution> n) {
		Random rand = new Random();
		return n.get(rand.nextInt(size));		
	}
	
	public Solution chooseFunction(Solution s, ArrayList<Solution> n, int option) {
		switch(option) {
		case 1: return bestImprovementFirst(n,s);
		case 2: return firstImprovement(n,s);
		case 3: return lesserConflicts(n);
		case 4: return getNewRandom(n.size(), n);
		}
		return s;
	}
	
	public ArrayList<Point> hillClimbing(Solution s, Memory mem, int option) {
		
		int nConflicts = s.totalConflicts;
		long perimiter = s.getPerimiter();
		
		ArrayList<Solution> neighbours = new ArrayList<>();
		s.resolveConflicts();
		neighbours = s.neighbours;
		int count =0;
		
		while(nConflicts > 0) {
			count++;
			//System.out.println("\n"+count);
			System.out.println(nConflicts);
			//System.out.println(perimiter);
			
			Solution best = chooseFunction(s,neighbours, option);
			
			long curPerimiter = best.getPerimiter();
			
			if(curPerimiter < perimiter) {
				s = new Solution(best);
				s.conflicts();
				s.resolveConflicts();
				nConflicts = s.totalConflicts;
				neighbours = s.neighbours;
			}
			else break; 
		
		}

		s.changeSolFromEdges();

		System.out.println("conflicts: "+nConflicts);
		s.printSolution();
		System.out.println("\n"+"Iterações " +count);
		long perim = s.getPerimiter();
		System.out.println("Perimetro: " + perim);

		return s.returnPoints();
		
	}
	
}
