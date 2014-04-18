package sk.fiit.macina.genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sk.fiit.macina.garden.Garden;

public class GeneticSearch {
	public Garden garden;
	public int geneNumber;
	
	public final int POPULATION_SIZE = 50;
	
	
	public GeneticSearch(Garden garden) {
		this.garden=garden;
		this.geneNumber=garden.getMaxGenome();
		generateChromozome();
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
		
		System.out.println("Chromozom je: ");
		
		for(int i=0;i<geneNumber;i++){
			System.out.print(chromozom[i]+" ");
		}
		
		garden.iterujCezCisla(chromozom);
		
		return chromozom;
	}
	

	

}
