package sk.fiit.macina.garden;

/**
 * Trieda ktora uchovova informacie o riadku,stlpci,pohybe a predchadzajucej suradnici.
 * @author Maci ThinkPad
 *
 */
public class Coordinate {
	public int r;
	public int s;
	public int dr;
	public int ds;
	public Coordinate predch;
	
	public Coordinate(int r, int s, int dr, int ds, Coordinate predch) {
		this.r=r;
		this.s=s;
		this.dr=dr;
		this.ds=ds;
		this.predch=predch;
	}
	
	@Override
	public String toString() {
		return new String("X: "+r+" Y: "+s+" dr: "+dr+" ds: "+ds);
	}
	

}
