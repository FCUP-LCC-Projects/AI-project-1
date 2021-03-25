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
	
	public Solution firstImprovement(ArrayList<Solution> neighbours){		
		int best = 0;
		int perimeter = neighbours.get(best).getPerimiter();
		
		for(int i=1; i<neighbours.size(); i++) {
			int tmpPerimeter = neighbours.get(i).getPerimiter();
			if(tmpPerimeter < perimeter) {
				best = i;
				break;
			}
		}		
		return neighbours.get(best);
	}
	
	
	public Solution bestImprovementFirst(ArrayList<Solution> neighbours){	
		/**
		 * Calcula o vizinho com melhor perímetro e retorna o seu índice
		 */
		int best = 0;
		int perimeter = neighbours.get(best).getPerimiter();
		
		for(int i=1; i<neighbours.size(); i++) {
			int tmpPerimeter = neighbours.get(i).getPerimiter();
			if(tmpPerimeter < perimeter) {
				best = i;
				perimeter = tmpPerimeter;
			}
		}
		return neighbours.get(best);
	}
	
	public Solution lesserConflicts(ArrayList<Solution> neighbours, Neighbours n) {
		/**
		 * Retorna o vizinho com menos conflitos e retorna o seu índice
		 */
		int best = 0;
		int conflicts = n.conflicts(neighbours.get(best),false);
		
		for(int i=0; i<neighbours.size(); i++) {
			int tmpConflicts = n.conflicts(neighbours.get(i), false);
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
	
	public Solution chooseFunction(Solution s, Neighbours nhbd, ArrayList<Solution> n, int option) {
		switch(option) {
		case 1: return bestImprovementFirst(n);
		case 2: return firstImprovement(n);
		case 3: return lesserConflicts(n, nhbd);
		case 4: return getNewRandom(n.size(), n);
		}
		return s;
	}
	
	public void hillClimbing(Solution s, Memory mem, int option) {
		Neighbours n = new Neighbours(mem);
		int nConflicts = n.conflicts(s, true);
		int perimiter = s.getPerimiter();
		
		ArrayList<Solution> neighbours = new ArrayList<>();
		neighbours = n.resolveConflicts(s);
		int count =0;
		
		while(nConflicts > 0 && (!neighbours.isEmpty() || !n.edgeConflicts.isEmpty())) {
			count++;
			System.out.println("\n"+count);
			System.out.println(nConflicts);
			
			Solution best = chooseFunction(s, n, neighbours, option);
			int curPerimiter = best.getPerimiter();
			
			if(curPerimiter < perimiter) {
				s = new Solution(best.solMaxSize, best.sol, best.edges);
				nConflicts = n.conflicts(s, true);
				neighbours = n.resolveConflicts(s);
				perimiter = curPerimiter;
			}
			else if(option == 4) neighbours.remove(best);
			else break; 
			
	
		}

		System.out.println("conflicts: "+nConflicts);
		s.printSolution(mem);
		System.out.println("\n"+"Iterações " +count);
		
	}
	
}
