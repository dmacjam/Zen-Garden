package sk.fiit.macina.genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sk.fiit.macina.garden.Garden;

public class GeneticSearch {
	public Garden garden;
	public int geneNumber;
	
	int[][] population;
	public final int POPULATION_SIZE = 500;
	public final float CROSSOVER_RATE = 0.95f;
	public final int GENERATIONS=500;
	public final float MUTATION_RATE_MIN = 0.05f;
	public final float MUTATION_RATE_MAX = 0.5f;
	
	
	public GeneticSearch(Garden garden) {
		this.garden=garden;
		this.geneNumber=garden.getMaxGenome();
		this.population=new int[POPULATION_SIZE][geneNumber];
		genetic();
	}
	
	
	/**
	 * Hlavna funkcia geneticky algoritmus.
	 */
	public void genetic(){
		int[] fitnessValues=new int[POPULATION_SIZE];
		
		//VYGENERUJ ZACIATOCNU POPULACIU
		for(int i=0;i<POPULATION_SIZE;i++){
			population[i]=generateChromozome();
		}
		
		int pocet=0;
		float mutationActuall=MUTATION_RATE_MIN;
		int maxPrevious=0; 
		int sequenceOfMax=0;
		//VELKY WHILE
		while (pocet++ < GENERATIONS) {
			int[][] newPopulation = new int[POPULATION_SIZE][geneNumber];
			int max, max_i, min, min_i, sum = 0;
			

			for (int i = 0; i < POPULATION_SIZE; i++) {
				fitnessValues[i] = garden.iterujCezCisla(population[i],false);
			}

			// VYPOCET MAX A MIN HODNOT FITNESS
			max = fitnessValues[0];
			max_i = 0;
			min = fitnessValues[0];
			min_i = 0;
			for (int i = 0; i < POPULATION_SIZE; i++) {
				sum += fitnessValues[i];
				if (fitnessValues[i] > max) {
					max = fitnessValues[i];
					max_i = i;
				}
				if (fitnessValues[i] < min) {
					min = fitnessValues[i];
					min_i = i;
				}
			}
			System.out.println("POP: "+pocet+" MAX: " + max + " MIN: " + min + " AVG: " + sum / POPULATION_SIZE+" MUT_RATE: "+mutationActuall);
			
			if ( max == garden.pocetNaPohrabanie ){
				garden.iterujCezCisla(population[max_i], true);
				printChromosome(population[max_i]);
				break;
			}
			
			
			if (max != maxPrevious || mutationActuall > MUTATION_RATE_MAX ){
				maxPrevious=max;
				sequenceOfMax=0;
				mutationActuall=MUTATION_RATE_MIN;
			}
			else {
				sequenceOfMax++;
				if (mutationActuall < MUTATION_RATE_MAX ){
					mutationActuall += 0.01f;
				}
			}
			
			// UPRAVA HODNOT FITNESS
			for (int i = 0; i < POPULATION_SIZE; i++) {
				fitnessValues[i] = fitnessValues[i] - min;
			}

			// VYTVORENIE NOVEJ POPULACIE
			for (int i = 0; i < POPULATION_SIZE; i += 2) {
				int individual1 = getRandomIndividual(fitnessValues, sum);
				int individual2 = getRandomIndividual(fitnessValues, sum);

				if (Math.random() < CROSSOVER_RATE) { 	// IDE SA KRIZIT
					for (int j = 0; j < geneNumber; j++) {
						if (Math.random() < 0.5) {
							newPopulation[i][j] = population[individual1][j];
							newPopulation[i + 1][j] = population[individual2][j];
						} else {
							newPopulation[i][j] = population[individual2][j];
							newPopulation[i + 1][j] = population[individual1][j];
						}
					}

					// MUTACIA
			        for(int child=0;child<2;child++){
                        for(int j=0;j<geneNumber;j++){
                            if(Math.random() < mutationActuall ){
                            	int mutationNumber=(int) (Math.random() * garden.getObvod());
                            	if ( Math.random() < 0.5){
                            		mutationNumber *= -1; 
                            	}
                            	int index=-1;
                            	for(int k=0;k<geneNumber;k++){
                            		if ( newPopulation[i+child][k] == mutationNumber){
                            			index=k;
                            		}
                            	}
                                
                            	if (index != -1){
                            		int pom=newPopulation[i+child][j];
                            		newPopulation[i+child][j]=newPopulation[i+child][index];
                            		newPopulation[i+child][index]=pom;
                            	}
                            	else{
                            		newPopulation[i+child][j]=mutationNumber;
                            	}
                            }
                        }
                    }
					

				} else { // NIE JE KRIZENIE
					for (int j = 0; j < geneNumber; j++) {
						newPopulation[i][j] = population[individual1][j];
						newPopulation[i + 1][j] = population[individual2][j];
					}
				}
			}

			//ELITIZMUS
			for (int i = 0; i < geneNumber; i++)
			{
				newPopulation[0][i] = population[max_i][i];
			}
			population = newPopulation;
		}
	}
	
	

	/**
	 * Metoda ktora vrati vygenerovany chromozom.
	 * @return
	 */
	public int[] generateChromozome(){
		int[] chromozom=new int[geneNumber];
		List<Integer> rozsahCisel = new ArrayList<Integer>();
		
		//PRIDAJ DO LISTU CISLA OD 0 PO (OBVOD)
		for(int i=0;i<garden.getObvod();i++){
			rozsahCisel.add(i);
		}
		
		//NAHDONE ICH ROZHOD
		Collections.shuffle(rozsahCisel);
		
		//VYBER PRVYCH n DO geneNumber
		for(int i=0;i<geneNumber;i++){
			int cislo=rozsahCisel.get(i);
			
			if (Math.random() < 0.5){		//Z PRAVDEPODOBNOSTOU 50% ZMEN NA ZAPORNE
				cislo=cislo*(-1);
			}
			
			chromozom[i]=cislo;
		}
		
//		System.out.println("Chromozom je: ");
//		
//		for(int i=0;i<geneNumber;i++){
//			System.out.print(chromozom[i]+" ");
//		}
	
		return chromozom;
	}
	
	
	
	public int getRandomIndividual(int[] fitness,int sumFitness){
		double point=Math.random() * sumFitness;
		int sum=0;
		for(int i=0;i<POPULATION_SIZE;i++){
            sum+= fitness[i];
            if(sum > point) return i;
        }
	    return 0;	
	}
	
	
	/**
	 * Vypise chromozom.
	 * @param chromozome
	 */
	public void printChromosome(int[] chromozome){
		for(int i=0;i<geneNumber;i++){
			System.out.printf("%4d",chromozome[i]);
		}
	}
	

}
