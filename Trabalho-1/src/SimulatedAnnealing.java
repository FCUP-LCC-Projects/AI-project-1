import java.util.ArrayList;
import java.util.Random;

public class SimulatedAnnealing {
	double T = 1; //temperature
	final double Tmin = 0.0001; //temperature mínima limite
	final double alpha = 0.9; //decremento 
	final int numIterations = 100; //iterações até se diminuir T
	
	
	public void simmulatedAnnealing(Solution s, Memory mem) {
		Neighbours n = new Neighbours(mem);
		int nConflicts = n.conflicts(s, true);
		s.printSolution(mem);
		System.out.println(n.edgeConflicts.size());
		
		Solution curSol = new Solution(s.solMaxSize, s.sol, s.edges);

		Solution min = new Solution(s.solMaxSize);
		int minConflict = Integer.MAX_VALUE;
		
		ArrayList<Solution> neighbours = n.resolveConflicts(curSol);
		
		while(T > Tmin) {
			for(int i=0; i<numIterations; i++) {
				
				if(nConflicts < minConflict) {
					min = curSol;
					minConflict = nConflicts;
				}
				
				System.out.println("Neighbours");
				System.out.println(neighbours.size());
				n.printNeighbours(neighbours);
				
				Solution best = getNewRandom(neighbours.size(), neighbours); //não sabia que medida aplicar então por agora meti a random
				int curConflicts = n.conflicts(best,false);
				
				double ap = Math.pow(Math.E, (nConflicts - curConflicts)/T);
				
				if(ap > Math.random()) {
					curSol = new Solution(best.solMaxSize, best.sol, best.edges);
					nConflicts = n.conflicts(curSol, true);
					neighbours = n.resolveConflicts(curSol);
				}
			}
			T *= alpha;
		}
		
		System.out.println("conflicts: "+minConflict);
		min.printSolution(mem);
	}
	
	public Solution getNewRandom(int size, ArrayList<Solution> n) {
		Random rand = new Random();
		return n.get(rand.nextInt(size));		
	}
}
