package sk.fiit.macina.view;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class View extends JFrame{
	private JTextField[][] cells;
	private JPanel mapaPanel;

	public int pocetRiadkov;
	public int pocetStlpcov;
	
	public static final int CELL_SIZE = 40; 
	
	
	public View(int pocetRiadkov, int pocetStlpcov) {
		mapaPanel=new JPanel(new GridLayout(pocetRiadkov, pocetStlpcov));
		
		
		this.pocetRiadkov=pocetRiadkov;
		this.pocetStlpcov=pocetStlpcov;
		int canvas_width= CELL_SIZE * pocetStlpcov;
		int canvas_height= CELL_SIZE * pocetRiadkov;
		
		//NASTAVENIE JEDNOTLIVYCH BUNIEK
		cells = new JTextField[pocetRiadkov][pocetStlpcov]; 
	      for (int i = 0; i < pocetRiadkov; i++) {
	         for (int j = 0; j < pocetStlpcov; j++) {
	            cells[i][j] = new JTextField();
	            cells[i][j].setEditable(false);
	            cells[i][j].setHorizontalAlignment(JTextField.CENTER);
	            cells[i][j].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
	            mapaPanel.add(cells[i][j]); 
	         }
	      }
	    
		mapaPanel.setPreferredSize(new Dimension(canvas_width, canvas_height));
	
		this.getContentPane().add(mapaPanel);
		
		this.setVisible(true);
		this.setTitle("Zen garden solver");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation(100, 50);
		this.pack();
	}

	/**
	 * Funkcia na nastenie hodnoty prislusnej bunky.
	 * @param riadok
	 * @param stlpec
	 * @param hodnota
	 */
	public void setNumber(int riadok,int stlpec,int hodnota){
		if (hodnota == -1){
			cells[riadok][stlpec].setBackground(Color.YELLOW);
			cells[riadok][stlpec].setText("K");
		}else
		{
			cells[riadok][stlpec].setText(hodnota+"");
		}
		
	}
	
	

}
