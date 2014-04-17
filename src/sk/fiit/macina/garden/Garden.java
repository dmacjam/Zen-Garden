package sk.fiit.macina.garden;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Garden {
	public int n,m;
	public StringBuilder mapa;
	public int polObvod;
	
	static final char KAMEN = 'K';
	static final char PIESOK = '.';
	
	public Garden(File file) {
		mapa=new StringBuilder();	
		readFromFile(file);
		vykresliMapu(mapa.toString());
		iterujCezCisla();
		vykresliMapu(mapa.toString());
	}

	/**
	 * Regexp na pekny vypis mapky.
	 * @param state
	 * @return
	 */
	public String prettify(String state) {
		String EVERY_NTH = "(?<=\\G.{N})".replace("N", String.valueOf(m));
		return state.replaceAll(EVERY_NTH, "\n");
	}
	
	/**
	 * Prepocet z dvoch rozmerov na jeden.
	 * @param riadok Riadok v 2D tabulke.
	 * @param stlpec Stlpec v 2D tabulke.
	 * @return 1D hodnota.
	 */
	public int prepocet(int riadok,int stlpec){
		return riadok*m+stlpec;
	}
	
	public boolean inBounds(int riadok,int stlpec){
		if ( (riadok >= 0 && riadok < n) && (stlpec >=0 && stlpec < m) ){
			return true;
		}
		else return false;
	}
	
	
	/**
	 * Nacita mapku zo suboru a inicializuje atribut triedy Garden mapa.
	 * @param file Subor ktory sa ma nacitat.
	 */
	public void readFromFile(File file){
		try (Scanner sc = new Scanner(file)) {
			String riadok=sc.nextLine();
			String[] dvojica = riadok.split(" ");
			n=Integer.parseInt(dvojica[0]);
			m=Integer.parseInt(dvojica[1]);
			
			this.polObvod=n+m;
			
			//vytvor mapku
			for(int i=0;i<n*m;i++){
				mapa.append(PIESOK);
			}
			
			//na tychto dalsich miestach su kamene
			while (sc.hasNextLine()) {
				riadok = sc.nextLine();
				dvojica= riadok.split(" ");
				mapa.setCharAt(prepocet(Integer.parseInt(dvojica[0]), Integer.parseInt(dvojica[1])), KAMEN);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Metoda ktora vypise mapku.
	 * @param map
	 */
	public void vykresliMapu(String map){
		System.out.println(prettify(map));
	}
	
	public void getDirection(int number, char cislo){
		if (number < m){
			rakeGarden(cislo ,0, number, 1, 0);
		} else if ( number < (polObvod) ){
			rakeGarden(cislo, number-m, m-1 , 0 , -1);
		} else if ( number < (polObvod+n) ){
			rakeGarden(cislo, number-polObvod, 0, 0, 1);
		} else {
			rakeGarden(cislo, n-1, number-(polObvod+n), -1 , 0);
		}
	}
	
	/**
	 * 
	 * @param cisla
	 */
	public void rakeGarden(char cislo, int r,int s,int dr, int ds){
		while ( inBounds(r, s) ){
			mapa.setCharAt(prepocet(r, s), cislo);
			r=r+dr;
			s=s+ds;
		}
	}
	
	public void iterujCezCisla(){
		int[] cisla = {1, 13, 43, 31};
		
		for(int i=0;i<cisla.length;i++){
			getDirection(cisla[i], Character.forDigit(15, 10) );
		}
	}

}
