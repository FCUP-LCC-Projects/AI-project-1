import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;


public class Main {
	
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
	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner sc = new Scanner(System.in);

		System.out.println("Input de pontos\n");
		System.out.println("1 - Gerar Input aleatório");
		System.out.println("2 - Escolher ficheiro de input");
		
		int input = sc.nextInt();
		Memory mem = new Memory(0);
		int n=0;
		int m=0;
		if(input ==1){
			System.out.println("Número de pontos:");
			n = sc.nextInt();
			System.out.println("Valor máximo das coordenadas");
			m = sc.nextInt();
			mem = new Memory(n);
			generatePoints(n, m, mem);
		}
		if(input ==2){
			sc.nextLine();
			System.out.println("Nome do ficheiro:");
			String file = sc.nextLine();
			File inputFile = new File(file);
			Scanner myReader = new Scanner(inputFile);
			n = myReader.nextInt();
			mem = new Memory(n);
			for(int i=0; i<n; i++){
				int x,y;
				x = myReader.nextInt();
				y = myReader.nextInt();
				mem.add(x,y,i);
			}
			myReader.close();
		}
		
		Solution solution = new Solution(n);

		System.out.println("Algoritmo a ser usado");
		System.out.println("1- Hill Climbing");
		System.out.println("2- Simulated Annealing");
		System.out.println("3- Ant Colony Optimization");
		
		int algorithm = sc.nextInt();

		if(algorithm == 3){
			boolean restart = true;
			while(restart){

				int maxItrs,ants;
				double alpha,beta,evap;
				System.out.println("Número máximo de iterações");
				maxItrs = sc.nextInt();
				System.out.println("Número de formigas");
				ants = sc.nextInt();
				System.out.println("Peso a atribuir à feromona na probabilidade");
				alpha = sc.nextDouble();
				System.out.println("Peso a atribuir à distância na probabilidade");
				beta = sc.nextDouble();
				System.out.println("Valor da evaporação da feromona");
				evap = sc.nextDouble();
				AntColonyOptimization aco = new AntColonyOptimization(mem, evap,beta,alpha);
				aco.findBestPath(maxItrs, ants);
				System.out.println("Tentar com novos parâmetros? (s/n)");
				String str = sc.next();
				if(str.equals("n")) restart=false; 
			}
			sc.close();
			return;
		}
		
		System.out.println("Método de determinação de candidato");
		System.out.println("1 - Permutation");
		System.out.println("2 - Nearest Neighbour");
		int selection = sc.nextInt();
		if(selection == 1)
			solution.permutation(mem);
		if(selection == 2)
			solution.greedy(mem);
		
		HillClimbing climb = new HillClimbing();

		System.out.println("Escolha do candidato");
		System.out.println("1 - Best Improvement first");
		System.out.println("2 - First Improvement");
		System.out.println("3 - Less Conflicts");
		System.out.println("4 - Random");

		int option =sc.nextInt();
		
		climb.hillClimbing(solution, mem, option);
		
		sc.close();

	}
}

