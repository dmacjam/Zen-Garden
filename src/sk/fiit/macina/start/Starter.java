package sk.fiit.macina.start;

import java.io.File;

import sk.fiit.macina.garden.Garden;

public class Starter {

	public static void main(String[] args) {
		File file=new File("test1.txt");
		Garden zahradka= new Garden(file);
	}
	

}
