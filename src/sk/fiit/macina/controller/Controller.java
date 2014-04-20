package sk.fiit.macina.controller;

import java.io.File;

import javax.swing.JPanel;

import sk.fiit.macina.garden.Garden;
import sk.fiit.macina.genetic.GeneticSearch;
import sk.fiit.macina.view.View;

	
public class Controller {
	public Garden zahradka;
	public GeneticSearch gs;
	public View view;
	public JPanel drawing;
	
	public static final String fileName="test1.txt";
	
	
	public Controller() {
		File file=new File(fileName);
		this.zahradka= new Garden(file);
		

		this.gs=new GeneticSearch(zahradka);
		
		
		//this.view=new View(zahradka.n,zahradka.m);
		//zahradka.setView(view);
		//zahradka.printFullMap();
		//gs.printSolutionGui();
	}
	
}
