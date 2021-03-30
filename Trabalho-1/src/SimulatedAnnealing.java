import java.util.ArrayList;
import java.util.Random;

public class SimulatedAnnealing {
	double T; //temperature
	int numIterations = 10; //iterações até se diminuir T
	final double Tmin = 0.01; //temperature mínima limite
	final double alpha = 0.95; //decremento 
	
	
	public void simmulatedAnnealing(Solution s, Memory mem) {
		Neighbours n = new Neighbours(mem);
		
		Solution curSol = new Solution(s.solMaxSize, s.sol, s.edges);
		int curConflicts = n.conflicts(curSol, true);
		T = curConflicts;

		Solution min = new Solution(s.solMaxSize);
		int minConflict = Integer.MAX_VALUE;
		
		ArrayList<Solution> neighbours = n.resolveConflicts(curSol);
		int count = 0;
		
		while(T > Tmin) {
			//System.out.println("T: "+T);
			for(int i=0; i<numIterations && !neighbours.isEmpty(); i++) {
				count++;
				Solution best = getNewRandom(neighbours.size(), neighbours);
				int bestConflicts = n.conflicts(best,false);
				//System.out.println("best conflicts: "+bestConflicts+" curConflicts: "+curConflicts);
				
				if(bestConflicts < curConflicts) {
					curSol = new Solution(best.solMaxSize, best.sol, best.edges);

					curConflicts = n.conflicts(best, true);
					
					//System.out.println("second best conflicts: "+bestConflicts+" curConflicts: "+curConflicts);
					
					neighbours = n.resolveConflicts(curSol);
				}
				else {
					double ap = Math.pow(Math.E, (double)(curConflicts - bestConflicts)/T);
					//System.out.println("ap: "+ap);
					
				
					if(ap > Math.random()) {
						//System.out.println("ap better than random");
						curSol = new Solution(best.solMaxSize, best.sol, best.edges);
						
						curConflicts = n.conflicts(best, true);	
						
						//System.out.println("second best conflicts: "+bestConflicts+" curConflicts: "+curConflicts);
						
						neighbours = n.resolveConflicts(curSol);
					}
				}
				
				if(curConflicts < minConflict) {
					min = curSol;
					minConflict = curConflicts;
				}
			}
			if(minConflict == 0) break;
			T *= alpha;
		}
		
		System.out.println("iter: "+count);
		System.out.println("conflicts: "+minConflict);
		min.printSolution(mem);
	}
	
	public Solution getNewRandom(int size, ArrayList<Solution> n) {
		Random rand = new Random();
		return n.get(rand.nextInt(size));		
	}
}
