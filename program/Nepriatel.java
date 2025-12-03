import java.awt.Color;

import javax.swing.JPanel;

/**
 * Write a description of class Nepriatel here.
 * 
 * @author y0hn
 * @version v0.1
 */
public class Nepriatel {
    private static final int MAX_POCET_ZIVOTOV = 10; 
    private static final int RYCHLOST = 5; 
    //private static final double RYCHLOST_UTOKU = 0.5;
    public static final Vektor2D VELKOST = new Vektor2D(50, 50);
    
    private Telo telo;
    //private long buduciUtok;
    //private Rozmer2D rozmerCiela;

    /**
     * Vytvori nepriatela s Telom
     * @param rozmerTela pozicia a velkost nepriatela
     */
    public Nepriatel(Vektor2D pozicia) {
        Rozmer2D rozmer = new Rozmer2D(pozicia, VELKOST);
        this.telo = new Telo(MAX_POCET_ZIVOTOV, rozmer, RYCHLOST);
        //this.buduciUtok = 0;
    }
    /**
     * Vrati Telo Nepriatela
     * @return reprazentacia Nepriatela vo Svete
     */
    public Telo getTelo() {
        return this.telo;
    }
    /**
     * Nastav grafiku pre nepiratela
     * @param grafika
     */
    public void setGrafika(JPanel grafika) {
        grafika.setBounds(this.telo.getRozmer().vytvorRectangle());
        grafika.setBackground(Color.RED);
        this.telo.setGrafika(grafika, Color.RED);
    }
    /**
     * Obnovi spravanie nepiratela
     * @param aM aktualna Miestnost
     * @param hrac objekt Hraca
     */
    public void tik(Miestnost aM, Hrac hrac) {
        
        Vektor2D smer = this.telo.getRozmer().getPozicia();
        smer = smer.rozdiel(hrac.getTelo().getRozmer().getPozicia());
        smer = smer.skalarnySucin(-1);
        this.telo.setPohybVektor(smer);
        
        this.telo.tik(aM, hrac);
    }
}