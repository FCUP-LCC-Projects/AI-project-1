import java.util.ArrayList;
import java.util.Random;

public class SimulatedAnnealing {
	double T; //temperature
	int numIterations = 5; //iterações até se diminuir T
	final double Tmin = 0.01; //temperature mínima limite
	final double alpha = 0.95; //decremento 
	
	
	public void simmulatedAnnealing(Solution s, Memory mem) {		
		Solution curSol = new Solution(s);
		curSol.conflicts();
		int curConflicts = curSol.totalConflicts;
		T = curConflicts;

		Solution min = new Solution(s.solMaxSize,mem);
		int minConflict = Integer.MAX_VALUE;

		curSol.resolveConflicts();
		
		ArrayList<Solution> neighbours = curSol.neighbours;
		int count = 0;
		
		while(T > Tmin) {
			//System.out.println("T: "+T);
			for(int i=0; i<numIterations && !neighbours.isEmpty(); i++) {
				count++;
				Solution best = getNewRandom(neighbours.size(), neighbours);
				best.conflicts();
				int bestConflicts = best.totalConflicts;
				//System.out.println("best conflicts: "+bestConflicts+" curConflicts: "+curConflicts);
				
				double delta = bestConflicts - curConflicts;
				if(delta < 0) {
					curSol = new Solution(best);

					curSol.conflicts();
					curConflicts = curSol.totalConflicts;
					
					
					
					curSol.resolveConflicts();
					neighbours = curSol.neighbours;
				}
				else {
					double ap = Math.pow(Math.E, (-1*delta)/T);
					//System.out.println("ap: "+ap);
					
				
					if(ap > Math.random()) {
						System.out.println("ap better than random");
						curSol = new Solution(best);
						
						curSol.conflicts();
						curConflicts = curSol.totalConflicts;
						
						
						
						curSol.resolveConflicts();
						neighbours = curSol.neighbours;
					}
				}
				
				if(curConflicts < minConflict) {
					min = new Solution(curSol);
					minConflict = curConflicts;
				}
			}
			if(minConflict == 0) break;
			T *= alpha;
		}
		
		System.out.println("iter: "+count);
		System.out.println("conflicts: "+minConflict);
		min.printSolution();
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
}
