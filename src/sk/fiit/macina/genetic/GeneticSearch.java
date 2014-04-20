package sk.fiit.macina.genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sk.fiit.macina.garden.Garden;

/**
 * Trieda ktora predstavuje logiku genetickeho algoritmu.
 * @author Maci ThinkPad
 *
 */
public class GeneticSearch {
	public Garden garden;
	public int geneNumber;
	public int[] solution;
	public int celkovoPopulacii;
	
	int[][] population;
	
	//################KONSTANTY KTORE SA DAJU NASTAVOVAT###############################
	public static int CROSSOVER_METHOD = 3;				//1-UNIFORM, 2-SIMPLE, 3-TWO-POINT
	public static int SELECTION_METHOD = 3;				//1-ROULETTE SELECTION, 2-TOURNAMENT SELECTION(2 INDIVIDUALS), 3- TOURNAMENT SELECTION(3 INDIVIDUALS)
	public final int POPULATION_SIZE = 150;
	public final float CROSSOVER_RATE = 0.95f;
	public final int GENERATIONS=5000;
	public final float MUTATION_RATE_MIN = 0.02f;
	public final float MUTATION_RATE_MAX = 0.35f;
	//#################################################################################
	
	public GeneticSearch(Garden garden) {
		this.garden=garden;
		this.geneNumber=garden.getMaxGenome();
		this.population=new int[POPULATION_SIZE][geneNumber];
		this.solution=new int[geneNumber];
		
		genetic();
	}
	
	
	/**
	 * Hlavna funkcia genetickeho algoritmu.
	 */
	public void genetic(){
		int[] fitnessValues=new int[POPULATION_SIZE];
		long startTime=System.currentTimeMillis();
		
		//VYGENERUJ ZACIATOCNU POPULACIU
		for(int i=0;i<POPULATION_SIZE;i++){
			population[i]=generateChromozome();
		}
		
		int generationCounter=0;
		float mutationActuall=MUTATION_RATE_MIN;
		int maxPrevious=0; 
		//VYVOJ GENERACII
		while (generationCounter++ < GENERATIONS) {
			int[][] potomok = new int[POPULATION_SIZE][geneNumber];
			int max, max_i, min, sum = 0;
			
			//POHRAB ZAHRADU A VYPOCITAJ FITNESS FUNKCIU
			for (int i = 0; i < POPULATION_SIZE; i++) {
				fitnessValues[i] = garden.rakeGarden(population[i],false,false);
			}
			
			// VYPOCET MAX A MIN HODNOT FITNESS
			max = fitnessValues[0];
			max_i = 0;
			min = fitnessValues[0];

			for (int i = 0; i < POPULATION_SIZE; i++) {
				sum += fitnessValues[i];
				if (fitnessValues[i] > max) {
					max = fitnessValues[i];
					max_i = i;
				}
				if (fitnessValues[i] < min) {
					min = fitnessValues[i];
				}
			}
			System.out.printf("POP: %5d MAX: %4d MIN: %4d AVG: %4d MUT_RATE: %1.2f \n",generationCounter,max,min,sum / POPULATION_SIZE,mutationActuall);
			
			
			//AK SME NASLI RIESENIE KONIEC
			if ( max == garden.pocetNaPohrabanie ){
				garden.rakeGarden(population[max_i], true,false);
				System.arraycopy(population[max_i], 0, this.solution, 0, geneNumber);		//SKOPIRUJ CHROMOZOM RIESENIA DO POLA SOLUTION
				printChromosome(population[max_i]);
				this.celkovoPopulacii=generationCounter;
				long endTime=System.currentTimeMillis();
				System.out.println("\nCelkovy cas: "+(endTime-startTime)+" ms.");
				break;
			}
			
			//UPRAVA HODNOT MUTACIE AK SME UZ DLHO V LOKALNOM MAXIME
			if (max != maxPrevious || mutationActuall >= MUTATION_RATE_MAX ){
				maxPrevious=max;
				mutationActuall=MUTATION_RATE_MIN;
			}
			else {
				if (mutationActuall < MUTATION_RATE_MAX ){
					mutationActuall += 0.01f;
				}
			}
			
			// UPRAVA HODNOT FITNESS, OD VSETKYCH ODCITAM MIN FITNESS
			for (int i = 0; i < POPULATION_SIZE; i++) {
				fitnessValues[i] = fitnessValues[i] - min;
			}

			// VYTVORENIE NOVEJ POPULACIE
			for (int i = 0; i < POPULATION_SIZE; i += 2) {
				int individual1;	//JEDINEC 1
				int individual2;	//JEDINEC 2
				//VYBER JEDINCOV
				if (SELECTION_METHOD == 1) {
					//ROULETTE SELECTION
					individual1 = getFromRoulette(fitnessValues, sum);
					individual2 = getFromRoulette(fitnessValues, sum);
				} else if ( SELECTION_METHOD == 2 ){
					//TOURNAMENT SELECTION WITH 2 INDIVIDUALS IN TOURNAMENT
					individual1 = getFromTournament(fitnessValues, false);
					individual2 = getFromTournament(fitnessValues, false);
				}
				else{
					//TOURNAMENT SELECTION WITH 3 INDIVIDUALS IN TOURNAMENT
					individual1 = getFromTournament(fitnessValues, true);
					individual2 = getFromTournament(fitnessValues, true);
				}
				
				if (Math.random() < CROSSOVER_RATE) { 	// IDE SA KRIZIT
					if ( CROSSOVER_METHOD == 1 ) {
						// UNIFORM CROSSOVER
						for (int j = 0; j < geneNumber; j++) {
							if (Math.random() < 0.5) {
								potomok[i][j] = population[individual1][j];
								potomok[i + 1][j] = population[individual2][j];
							} else {
								potomok[i][j] = population[individual2][j];
								potomok[i + 1][j] = population[individual1][j];
							}
						}
					} else if ( CROSSOVER_METHOD == 2 ) {
						// SIMPLE CROSSOVER
						int indexLimit = (int) (Math.random() * geneNumber);
						for (int j = 0; j < geneNumber; j++) {
							if (j < indexLimit) {
								potomok[i][j] = population[individual1][j];
								potomok[i + 1][j] = population[individual2][j];
							} else {
								potomok[i][j] = population[individual2][j];
								potomok[i + 1][j] = population[individual1][j];
							}

						}
					} else {
						// TWO POINT CROSSOVER
						int indexLimit = (int) (Math.random() * geneNumber);
						int indexLimit2 = (int) (Math.random() * geneNumber);
						if (indexLimit > indexLimit2) {
							int pom = indexLimit;
							indexLimit = indexLimit2;
							indexLimit2 = pom;
						}
						for (int j = 0; j < geneNumber; j++) {
							if (j < indexLimit) {
								potomok[i][j] = population[individual1][j];
								potomok[i + 1][j] = population[individual2][j];
							} else if (j < indexLimit2) {
								potomok[i][j] = population[individual2][j];
								potomok[i + 1][j] = population[individual1][j];
							} else {
								potomok[i][j] = population[individual1][j];
								potomok[i + 1][j] = population[individual2][j];
							}

						}
					}
					
					// MUTACIA DETI
			        for(int child=0;child<2;child++){
                        for(int j=0;j<geneNumber;j++){
                            if(Math.random() < mutationActuall ){
                            	int mutationNumber=(int) (Math.random() * (garden.getObvod()-1)) +1;
                            	if ( Math.random() < 0.5){		//S PRAVDEPODOBNOSTOU 50% ZMEN NA ZAPORNE
                            		mutationNumber *= -1; 
                            	}
                            	int index=-1;
                            	for(int k=0;k<geneNumber;k++){		//ZISTI CI SA TAM NACHADZA TAKE CISLO
                            		if ( potomok[i+child][k] == mutationNumber){
                            			index=k;
                            			break;
                            		}
                            	}
                                
                            	if (index != -1){			//NACHADZA SA,TAK VYMEN HODNOTY NA INDEXOCH
                            		int pom=potomok[i+child][j];
                            		potomok[i+child][j]=potomok[i+child][index];
                            		potomok[i+child][index]=pom;
                            	}
                            	else{						//NENACHADZA SA, TAK ZMEN HODNOTU ZA VYGENEROVANU
                            		potomok[i+child][j]=mutationNumber;
                            	}
                            }
                        }
                    }
					

				} else { // NIE JE KRIZENIE, LEN SKOPIRUJ DO DALSEJ GENERACIE
					for (int j = 0; j < geneNumber; j++) {
						potomok[i][j] = population[individual1][j];
						potomok[i + 1][j] = population[individual2][j];
					}
				}
			}

			//ELITIZMUS- NAJLEPSIEHO JEDINCA PONECHAJ
			for (int i = 0; i < geneNumber; i++)
			{
				potomok[0][i] = population[max_i][i];
			}
			population = potomok;
		}
	}
	
	

	/**
	 * Metoda ktora vrati vygenerovany chromozom.
	 * @return
	 */
	public int[] generateChromozome() {
		int[] chromozom = new int[geneNumber];
		List<Integer> rozsahCisel = new ArrayList<Integer>();

		// PRIDAJ DO LISTU CISLA OD 1 PO (OBVOD)
		for (int i = 1; i <= garden.getObvod(); i++) {
			rozsahCisel.add(i);
		}

		// NAHODNE ICH ROZHOD
		Collections.shuffle(rozsahCisel);

		// VYBER PRVYCH n DO geneNumber
		for (int i = 0; i < geneNumber; i++) {
			int cislo = rozsahCisel.get(i);

			if (Math.random() < 0.5) { // Z PRAVDEPODOBNOSTOU 50% ZMEN NA// ZAPORNE
				cislo = cislo * (-1);
			}

			chromozom[i] = cislo;
		}

		return chromozom;
	}
	
	
	/**
	 * Vrat jedinca z vyberu ruletou.
	 * @param fitness Pole hodnot fitness jednotlivych jedincov.
	 * @param sumFitness Suma fitness vsetkych jedincov v populacii.
	 * @return jeden vybrany jedinec.
	 */
	public int getFromRoulette(int[] fitness,int sumFitness){
		float point=(float) (Math.random() * sumFitness);
		int sum=0;
		for(int i=0;i<POPULATION_SIZE;i++){
            sum+= fitness[i];
            if(sum > point) return i;
        }
	    return 0;	
	}
	
	
	
	/**
	 * Vrat jedinca z vyberu turnajom.
	 * @param fitness Pole hodnot fitness jednotlivych jedincov.
	 * @param three True ak je turnaj pre troch, inak false je turnaj pre dvoch.
	 * @return jeden vybrany jedinec.
	 */
	public int getFromTournament(int[] fitness, boolean three){
		int individual1=(int) (Math.random()*POPULATION_SIZE);
		int individual2=(int) (Math.random()*POPULATION_SIZE);
		
		int max=fitness[individual1] > fitness[individual2] ? individual1 : individual2;
		
		if (three == true){
			int individual3=(int) (Math.random()*POPULATION_SIZE);
			max= fitness[individual3] > fitness[max] ? individual3 : max;
		}
		return max;
	}
	
	
	/**
	 * Vypise chromozom.
	 * @param chromozome ten ktory sa ma vypisat.
	 */
	public void printChromosome(int[] chromozome){
		for(int i=0;i<geneNumber;i++){
			System.out.printf("%4d",chromozome[i]);
		}
	}
	
	/**
	 * Vypis animaciu do GUI.
	 */
	public void printSolutionGui(){
		garden.rakeGarden(solution, false,true);
	}
	
	public int getCelkovoPopulacii(){
		return celkovoPopulacii;
	}
	

}
