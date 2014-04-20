package sk.fiit.macina.garden;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import sk.fiit.macina.view.View;;

/**
 * Trieda reprezentujuca zahradku, funkcie na pohyb po zahradke...
 * @author Maci ThinkPad
 *
 */
public class Garden {
	public int n,m;
	public View view;
	public int[][] mapka;
	public int polObvod;
	public int pocet_kamenov;
	public int pocetNaPohrabanie;
	
	public static final int KAMEN = -1;
	public static final int PIESOK = 0;
	
	public Garden() {
		// TODO Auto-generated constructor stub
	}
	
	public Garden(File file) {	
		readFromFile(file);
		//vykresliMapu(mapka);
		this.pocetNaPohrabanie=(n*m)-pocet_kamenov;
		//iterujCezCisla();
		//vykresliMapu(mapka);
	}
	
	/**
	 * Zisti ci su suradnice v poli.
	 * @param riadok
	 * @param stlpec
	 * @return
	 */
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
			
			this.mapka=new int[n][m];
			this.polObvod=n+m;
			
			//vytvor mapku
			for(int i=0;i<n;i++){
			  for(int j=0; j< m; j++){
					mapka[i][j]=PIESOK;  
			  }
			}
			
			//na tychto dalsich miestach su kamene
			while (sc.hasNextLine()) {
				riadok = sc.nextLine();
				dvojica= riadok.split(" ");
				mapka[Integer.parseInt(dvojica[0])][Integer.parseInt(dvojica[1])]=KAMEN;
				pocet_kamenov++;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Metoda ktora vypise mapku.
	 * @param map
	 */
	public void printMap(int[][] mapa){
		for(int i=0;i<n;i++){
			for(int j=0;j<m;j++){
				System.out.printf("%4d",mapa[i][j]);
			}
		  System.out.print("\n");	
		}
	  System.out.print("\n");	
	}
	
	/**
	 * Funkcia vracia na zakladne cisla number v parametri instanciu triedy Coordinate-suradnica, teda mapovanie cisla na okraj zahradky.
	 * @param number
	 * @return
	 */
	public Coordinate getDirection(int number){
		//ZMEN CISLO NA KLADNE
		if (number < 0){			
			number=number*(-1);
		}
		
		if (number <= m){
			return new Coordinate(0, number-1, 1, 0, null);
		} else if ( number <= (polObvod) ){
			return new Coordinate(number-m-1, m-1 , 0, -1, null);
		} else if ( number <= (polObvod+n) ){
			return new Coordinate(number-polObvod-1, 0, 0, 1, null);
		} else {
			return new Coordinate(n-1, number-(polObvod+n)-1 , -1 , 0, null);
		}
	}
	
	
	/**
	 * Ak sme narazili, funkcia vrati novu instanciu Coordinate kadial pokracovat.
	 * @param suradnica
	 * @param gen
	 * @return
	 */
	public Coordinate getBumpDirection(Coordinate suradnica, int gen, int[][] mapa){
		Coordinate novaSur;
		novaSur=new Coordinate(suradnica.r,suradnica.s,suradnica.dr,suradnica.ds,suradnica);
		
		//LOGIKA
		if ( novaSur.dr != 0 ){			//NARAZILI SME A STUPALI SME PO RIADKOCH
			novaSur.dr=0;
			
			if ( ( (inBounds(novaSur.r, novaSur.s-1) && mapa[novaSur.r][novaSur.s-1]== PIESOK) || !inBounds(novaSur.r, novaSur.s-1) ) && ( (inBounds(novaSur.r, novaSur.s+1) && mapa[novaSur.r][novaSur.s+1]== PIESOK) || !inBounds(novaSur.r, novaSur.s+1)) ){			
				if ( gen < 0){										//OBE STRANY SU VOLNE, VYBER SI PODLA GENU
					novaSur.s -= 1;
					novaSur.ds = -1;
				}
				else {
					novaSur.s += 1;
					novaSur.ds = +1;
				}
			}
			else if ( (inBounds(novaSur.r, novaSur.s-1) && mapa[novaSur.r][novaSur.s-1]==PIESOK ) ){			//LEN DOLAVA
				novaSur.s -= 1;
				novaSur.ds = -1;
			}
			else if ( (inBounds(novaSur.r, novaSur.s+1) && mapa[novaSur.r][novaSur.s+1]==PIESOK) ){			//LEN DOPRAVA
				novaSur.s +=1;
				novaSur.ds=+1;
			}
			else{													//NIE JE NIC VOLNE,ZMAZ CESTU
				return null;
			}
		}
		else if ( novaSur.ds != 0 ){					//NARAZILI SME A STUPALI SME PO STLPOCH
			novaSur.ds=0;
			if ( ((inBounds(novaSur.r-1, novaSur.s) && mapa[novaSur.r-1][novaSur.s]== PIESOK) || !inBounds(novaSur.r-1, novaSur.s)) && ((inBounds(novaSur.r+1, novaSur.s) && mapa[novaSur.r+1][novaSur.s]== PIESOK) || (!inBounds(novaSur.r+1, novaSur.s))) ){			
				if ( gen < 0){							//OBE STRANY SU VOLNE, VYBER SI PODLA GENU
					novaSur.r -= 1;
					novaSur.dr =-1;
				}
				else{
					novaSur.r += 1;
					novaSur.dr = +1;
				}
			}
			else if ( (inBounds(novaSur.r-1, novaSur.s) && mapa[novaSur.r-1][novaSur.s]==PIESOK) ){			//LEN HORE
				novaSur.r -= 1;
				novaSur.dr = -1;
			}
			else if ( (inBounds(novaSur.r+1, novaSur.s) && mapa[novaSur.r+1][novaSur.s]==PIESOK) ){			//LEN DOLE
				novaSur.r += 1;
				novaSur.dr = +1;
			}
			else{											//NIE JE NIC VOLNE, ZMAZ CESTU
				return null;
			}
			
		}
	  
	  return novaSur;	
	}
	
	/**
	 * Pohrab mapku podla chromozomu a znac cestu.
	 * @param chromozome Chromozom podla ktoreho sa hrabe.
	 * @param vypis Ak je true, vykresli sa pohrabana zahradka.
	 */
	public int rakeGarden(int[] chromozome, boolean vypis, boolean gui){
		Coordinate suradnica;
		Coordinate novaSur;
		Coordinate predchSur;
		int poradiePrechodu=1;
		List<Coordinate> prechody = null;
		boolean uviaznutie=false;
		
		//SKOPIRUJ SI MAPKU, NEROB TO NA ORIGINALNEJ
		int[][] mapa=copyOriginalMap();
		

		for (int i = 0; i < chromozome.length; i++) {
			if (gui == true) {
				prechody = new ArrayList<Coordinate>();
			}

			suradnica = getDirection(chromozome[i]);

			// TEST CI MOZE VOJST DO ZAHRADKY
			if (mapa[suradnica.r][suradnica.s] == 0) {

				while (inBounds(suradnica.r, suradnica.s)) {

					// NARAZ
					if (mapa[suradnica.r][suradnica.s] != PIESOK) {
						predchSur = suradnica.predch;
						suradnica = getBumpDirection(suradnica.predch,
								chromozome[i], mapa);

						// AK SA VRATI NULL, TAK NASTALO UVIAZNUTIE
						if (suradnica == null) {
							// System.out.println("Cislo "+cisla[i]+" uviazlo");
							while (predchSur != null) {
								mapa[predchSur.r][predchSur.s] = 0;
								predchSur = predchSur.predch;
							}
							uviaznutie = true;
							break;
						}

						// AK SA VRATILA SURADNICA MIMO,TAK SME VYSLI VON ZO ZAHRADY, JE TO OK
						if (!inBounds(suradnica.r, suradnica.s)) {
							break;
						}
					}

					if (gui == true)
						prechody.add(suradnica);
					// ZAZNAC PRECHOD DO ZAHRADKY
					mapa[suradnica.r][suradnica.s] = poradiePrechodu;
					novaSur = new Coordinate(suradnica.r + suradnica.dr,
							suradnica.s + suradnica.ds, suradnica.dr,
							suradnica.ds, suradnica);
					suradnica = novaSur;
				}

				if (gui == true && !uviaznutie) {
					vypisGui(prechody, poradiePrechodu);
				}

				poradiePrechodu++;
				uviaznutie = false;

			}
		}
		
		//VYPIS MAPKY DO KONZOLY
		if ( vypis == true) {
			printMap(mapa);
		}
		
		return countRaked(mapa);
	}
	
	/**
	 * Vypis prechody postupne do GUI.
	 * @param prechody
	 * @param hodnota
	 */
	public void vypisGui(List<Coordinate> prechody, int hodnota){
		for(Coordinate suradnica : prechody){
			if (suradnica == null) System.out.println("TU");
			view.setNumber(suradnica.r, suradnica.s, hodnota);
			
			try{
	                Thread.sleep((int)200.0);
	            } catch (InterruptedException ex){
	            	System.out.println("Chyba thread sleep");
	            }
		}
		
	}
	
	
	/**
	 * Spocitaj pocet pohrabanych.
	 * @param mapa
	 * @return
	 */
	public int countRaked(int[][] mapa){
		int pocitadloNepohrabanych=0;
		for(int i=0;i<n;i++){
			for(int j=0;j<m;j++){
				if ( mapa[i][j] == PIESOK){
					pocitadloNepohrabanych++;
				}
			}
		}
	  
	  //float f= (float) (pocetNaPohrabanie-pocitadlo) / pocetNaPohrabanie;
	  return pocetNaPohrabanie-pocitadloNepohrabanych;
	}
	
	
	
	/**
	 * Metoda ktora skopiruje original mapu a vrati ju.
	 * @return
	 */
	public int[][] copyOriginalMap(){
		int [][] mapa = new int[mapka.length][];
		for(int i = 0; i < mapka.length; i++)
		{
		  int[] pom = mapka[i];
		  int   aLength = pom.length;
		  mapa[i] = new int[aLength];
		  System.arraycopy(pom, 0, mapa[i], 0, aLength);
		}
	 return mapa;
	}
	
	/**
	 * Metoda ktora vrati cislo= O/2 + pocet_kamenov
	 * @return
	 */
	public int getMaxGenome(){
		return polObvod + pocet_kamenov;
	}
	
	/**
	 * Metoda ktora vrati velkost obvodu zahradky.
	 * @return
	 */
	public int getObvod(){
		return 2*polObvod;
	}
	
	/**
	 * Metoda ktora vrati pocet policok ktore treba pohrabat.
	 * @return
	 */
	public int getPocetNaPohrabanie(){
		return pocetNaPohrabanie;
	}
	
	/**
	 * Metoda ktora zobrazi prazdnu mapku do GUI.
	 */
	public void printFullMap(){
		for(int i=0;i<n;i++){
			for(int j=0;j<m;j++){
				if ( mapka[i][j] == KAMEN ) view.setNumber(i, j, mapka[i][j]);
			}
		}
	}
	
	/**
	 * Prirad instanciu view.
	 * @param view
	 */
	public void setView(View view){
		this.view=view;
	}
	
}
