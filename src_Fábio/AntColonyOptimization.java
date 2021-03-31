import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class AntColonyOptimization{
        double [][] pheromone;
        double evaporation;
        double q=1.0;
        Memory mem;
        int points;
        double alpha=1;
        double beta =1;

        public AntColonyOptimization(Memory m, double evap, double bet, double alph){
            mem = m;
            points = mem.memSize;
            pheromone = new double[mem.memSize][mem.memSize];
            evaporation = evap;
            beta = bet;
            alpha = alph;
            buildPheromone();

        }

        private void buildPheromone(){
            for(int i=0; i<points; i++){
                for(int j=0; j<points;j++){
                    pheromone[i][j] =(double)1.0;
                }
            }
        }

        private void updatePheromone(Solution [] sols){
            int i,j;
            for(i=0; i<points; i++){
                for(j=0; j<points; j++){
                    if(i!=j){
                        double t = getTotalPheromone(sols, i, j);
                        double val = pheromone[i][j] * (1- evaporation);
                        pheromone[i][j] = val +t;
                    }
                }
            }
        }

        private double getTotalPheromone(Solution [] sols, int i, int j){

            double pheromoneTotal=0;
            for(int k=0; k<sols.length; k++){
                Solution curr = sols[k];
                int conflicts = curr.totalConflicts;

                
                for(int l =0; l<curr.solMaxSize-1; l++){
                    if(curr.sol[l] == i && curr.sol[l+1] ==j){
                        pheromoneTotal += q/(conflicts+1);
                        break; 
                    }
                }
                if(curr.sol[curr.solMaxSize-1] == i && curr.sol[0] == j){
                    pheromoneTotal +=q/(conflicts+1);
                }
            }
            return pheromoneTotal;
        }

        public int euclidean( int i, int j) {

            return (mem.points[j].x - mem.points[i].x)*(mem.points[j].x - mem.points[i].x) +
                    (mem.points[j].y - mem.points[i].y)*(mem.points[j].y - mem.points[i].y);
        }

        public int probability(int i, boolean[] available){
            ArrayList<Double>  path = new ArrayList<Double>();
            ArrayList<Double>  prob = new ArrayList<Double>();
            ArrayList<Double>  cumulativeSum = new ArrayList<Double>();
            
            //Guardar o indice original dos pontos, para no fim retornar o indice do próximo ponto escolhido
            ArrayList<Integer> pointIndex = new ArrayList<Integer>();
            double total=0;
            /*
            Calcular o peso de cada caminho possivel desde i, usando a feromona e a distancia
            Acumular o total do peso, para poder usar na divisão do cálculo da probabilidade
            de escolher um caminho */

            for(int j=0; j<points;j++){
                if(j != i && available[j]){
                    double p = Math.pow(pheromone[i][j], alpha);
                    double d = Math.pow(1.0/euclidean(i,j), beta);
                    path.add(p*d);
                    total +=(p*d);
                    pointIndex.add(j);
                }
            }
            /*
            Cálculo da probabilidade de escolher cada caminho, utilizando os valores
            já calculados */

            for(int j=0; j<pointIndex.size(); j++){
                prob.add(path.get(j) / total);
            }

            /*Fazer a soma cumulativa das probabilidades para poder escolher */
            for(int j =0;j<pointIndex.size(); j++){
                double tmp=0;
                for(int k=j; k<pointIndex.size(); k++){
                    tmp += prob.get(k);
                }
                cumulativeSum.add(tmp);
            }
            cumulativeSum.add(0.0);

            Random rand = new Random();
            double r = rand.nextDouble();
            int next=-1;
            /* Ver onde encaixa o número gerado aleatoriamente para o próximo vizinho */
            
            for(int j=0; j<cumulativeSum.size()-1;j++){
                if(r <= cumulativeSum.get(j) && r > cumulativeSum.get(j+1)){
                    next = j;
                    break;
                }
            }

            return pointIndex.get(next);
        }

        public void findBestPath(int maxItrs, int ants){
            Solution[] solutions = new Solution[ants];            
            
            for(int i=0; i<maxItrs; i++){
                System.out.println(i);
                
                for(int j=0; j<ants; j++){
                    Solution tmp = buildNewSolution();
                    solutions[j] = tmp;
                }
                updatePheromone(solutions);
            }
            int counter =0;
            int bestConflicts = Integer.MAX_VALUE/2;
            long bestPerimeter = Integer.MAX_VALUE/2;
            int bestIndex=0;

            
            for(int i=0; i<ants; i++){
                
                int conflicts = solutions[i].totalConflicts;
                long perimeter = solutions[i].getPerimiter();

                if(conflicts < bestConflicts && perimeter < bestPerimeter){
                    bestConflicts = conflicts;
                    bestPerimeter = perimeter;
                    bestIndex =i;
                    counter=0;
                }
                if(conflicts == bestConflicts && perimeter == bestPerimeter){
                    counter++;
                }
                
            }
            
            System.out.println("----------------Melhor caminho encontrado---------------");
            System.out.println("Conflitos: " + bestConflicts);
            System.out.println("Perimetro: " + bestPerimeter);
            System.out.println("Percentagem de formigas que escolheu melhor caminho:" + ((double)counter/(double)ants) *100 + "%");
            
            solutions[bestIndex].printSolution();
            
        }

        private Solution buildNewSolution(){
            boolean[] available = new boolean[points];
            Arrays.fill(available,true);
            Random rand = new Random();
            int start = rand.nextInt(points);
            available[start] = false;
            Solution s = new Solution(points,mem);
            s.solAdd(start);
            
            for(int i=1; i<points; i++){
                int next = probability(start, available);
                available[next] = false;
                s.solAdd(next);
                s.edgeAdd(new Edge(start,next, euclidean(start,next)));
                start = next;
            }

            //Fechar caminho
            int last = s.solGetLast();
            int first = s.solGetFirst();
            int distance = euclidean(s.solGetFirst(), s.solGetLast());
            s.edgeAdd(new Edge(last, first, distance));
            s.conflicts();
            return s;
        }

      





} 