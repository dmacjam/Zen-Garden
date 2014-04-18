package sk.fiit.macina.start;

import java.io.File;

import sk.fiit.macina.garden.Garden;
import sk.fiit.macina.genetic.GeneticSearch;

public class Starter {

	public static void main(String[] args) {
		File file=new File("test1.txt");
		Garden zahradka= new Garden(file);
		GeneticSearch gs=new GeneticSearch(zahradka);
	}
	

}
