package sk.fiit.macina.garden;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Garden {
	private int n,m;
	public int[][] mapka;
	public int polObvod;
	public int pocet_kamenov;
	
	static final int KAMEN = -1;
	static final int PIESOK = 0;
	
	public Garden(File file) {	
		readFromFile(file);
		vykresliMapu(mapka);
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
	public void vykresliMapu(int[][] mapa){
		for(int i=0;i<n;i++){
			for(int j=0;j<m;j++){
				System.out.printf("%4d",mapa[i][j]);
			}
		  System.out.print("\n");	
		}
	  System.out.print("\n");	
	}
	
	/**
	 * Funkcia vracia na zakladne cisla number v parametri instanciu triedy Coordinate-suradnica.
	 * @param number
	 * @return
	 */
	public Coordinate getDirection(int number){
		//ZMEN CISLO NA KLADNE
		if (number < 0){			
			number=number*(-1);
		}
		
		if (number < m){
			//rakeGarden(cislo ,0, number, 1, 0);
			return new Coordinate(0, number, 1, 0, null);
		} else if ( number < (polObvod) ){
			//rakeGarden(cislo, number-m, m-1 , 0 , -1);
			return new Coordinate(number-m, m-1 , 0, -1, null);
		} else if ( number < (polObvod+n) ){
			//rakeGarden(cislo, number-polObvod, 0, 0, 1);
			return new Coordinate(number-polObvod, 0, 0, 1, null);
		} else {
			//rakeGarden(cislo, n-1, number-(polObvod+n), -1 , 0);
			return new Coordinate(n-1, number-(polObvod+n) , -1 , 0, null);
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
		
		System.out.println("Naraz "+novaSur.toString());
		
		if ( novaSur.dr != 0 ){			//NARAZILI SME A STUPALI SME PO RIADKOCH
			novaSur.dr=0;
			
			if ( ( inBounds(novaSur.r, novaSur.s-1) && mapa[novaSur.r][novaSur.s-1]== PIESOK) && ( inBounds(novaSur.r, novaSur.s+1) && mapa[novaSur.r][novaSur.s+1]== PIESOK) ){			
				if ( gen < 0){										//OBE STRANY SU VOLNE, VYBER SI PODLA GENU
					novaSur.s -= 1;
					novaSur.ds = -1;
				}
				else {
					novaSur.s += 1;
					novaSur.ds = +1;
				}
			}
			else if ( !inBounds(novaSur.r, novaSur.s-1) || (inBounds(novaSur.r, novaSur.s-1) && mapa[novaSur.r][novaSur.s-1]==PIESOK) ){			//LEN DOLAVA
				novaSur.s -= 1;
				novaSur.ds = -1;
			}
			else if ( !inBounds(novaSur.r, novaSur.s+1) || (inBounds(novaSur.r, novaSur.s+1) && mapa[novaSur.r][novaSur.s+1]==PIESOK) ){			//LEN DOPRAVA
				novaSur.s +=1;
				novaSur.ds=+1;
			}
			else{													//NIE JE NIC VOLNE,ZMAZ CESTU
				return null;
			}
		}
		else if ( novaSur.ds != 0 ){					//NARAZILI SME A STUPALI SME PO STLPOCH
			novaSur.ds=0;
			if ( (inBounds(novaSur.r-1, novaSur.s) && mapa[novaSur.r-1][novaSur.s]== PIESOK) && (inBounds(novaSur.r+1, novaSur.s) && mapa[novaSur.r+1][novaSur.s]== PIESOK) ){			
				if ( gen < 0){							//OBE STRANY SU VOLNE, VYBER SI PODLA GENU
					novaSur.r -= 1;
					novaSur.dr =-1;
				}
				else{
					novaSur.r += 1;
					novaSur.dr = +1;
				}
			}
			else if ( !inBounds(novaSur.r-1, novaSur.s) || (inBounds(novaSur.r-1, novaSur.s) && mapa[novaSur.r-1][novaSur.s]==PIESOK) ){			//LEN HORE
				novaSur.r -= 1;
				novaSur.dr = -1;
			}
			else if ( !inBounds(novaSur.r+1, novaSur.s) || (inBounds(novaSur.r+1, novaSur.s) && mapa[novaSur.r+1][novaSur.s]==PIESOK) ){			//LEN DOLE
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
	 * Prechadzaj chromozomom a znac cestu.
	 */
	public void iterujCezCisla(int[] cisla){
		//int[] cisla = {25,0,-33,-30,-32,-34,-37,-36,5,-13,39,15,43,-41,16 };
		Coordinate suradnica;
		Coordinate novaSur;
		Coordinate predchSur;
		int poradiePrechodu=1;
		
		int[][] mapa=copyOriginalMap();
		
		
		
		
		for(int i=0;i<cisla.length;i++){
			suradnica=getDirection(cisla[i]);
			
			
			if ( mapa[suradnica.r][suradnica.s] == 0) {
				
				while (inBounds( suradnica.r, suradnica.s )) {
					
					if ( mapa[suradnica.r][suradnica.s] != PIESOK ){
						predchSur=suradnica.predch;
						suradnica=getBumpDirection(suradnica.predch, cisla[i], mapa);
						
						//AK SA VRATI NULL, TAK NASTALO UVIAZNUTIE
						if (suradnica == null){
							System.out.println("Cislo "+cisla[i]+" uviazlo");
							while (predchSur != null){
								mapa[predchSur.r][predchSur.s]=0;
								predchSur=predchSur.predch;
							}
							break;
						}
						
						//AK SA VRATILA SURADNICA MIMO,TAK SME VYSLI VON ZO ZAHRADY, OK
						if ( !inBounds(suradnica.r, suradnica.s)){
							break;
						}
					}
					
					mapa[suradnica.r][suradnica.s] = poradiePrechodu;
					novaSur=new Coordinate(suradnica.r + suradnica.dr, suradnica.s+suradnica.ds, suradnica.dr, suradnica.ds, suradnica);
					suradnica=novaSur;
				}
			 poradiePrechodu++;
			}
		}
		
		vykresliMapu(mapa);
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

}
