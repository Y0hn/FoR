import java.awt.Color;

import javax.swing.JPanel;

/**
 * Write a description of class Nepriatel here.
 * 
 * @author y0hn
 * @version v0.2
 */
public class Nepriatel {
    private static final int MAX_POCET_ZIVOTOV = 1; 
    private static final int RYCHLOST = 5; 
    private static final double RYCHLOST_UTOKU = 1;
    private static final int POSKODENIE_UTOKU = 1;
    public static final Vektor2D VELKOST = new Vektor2D(50, 50);
    
    private Telo telo;
    private long buduciUtok;

    /**
     * Vytvori nepriatela s Telom
     * @param rozmerTela pozicia a velkost nepriatela
     */
    public Nepriatel(Vektor2D pozicia) {
        Rozmer2D rozmer = new Rozmer2D(pozicia, VELKOST);
        this.telo = new Telo(MAX_POCET_ZIVOTOV, rozmer, RYCHLOST, POSKODENIE_UTOKU);
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
    public void zruzGrafiku() {
        JPanel grafika = this.telo.getGrafika();
        grafika.setBounds(Rozmer2D.ZERO.vytvorRectangle());
        grafika.getParent().remove(grafika);
    }
    /**
     * Obnovi spravanie nepiratela
     * @param aM aktualna Miestnost
     * @param hrac objekt Hraca
     * @return PRAVDA ak Hrac stratil posledny zivot
     */
    public boolean tik(Miestnost aM, Hrac hrac, double deltaCasu) {
        boolean znicilTeloHraca = false;

        Vektor2D smer = this.telo.getRozmer().getPozicia();
        smer = smer.rozdiel(hrac.getTelo().getRozmer().getPozicia());
        smer = smer.sucinSoSkalarom(-1);
        this.telo.setPohybVektor(smer);
        
        if (this.telo.tik(aM, hrac, deltaCasu) && this.buduciUtok <= System.currentTimeMillis()) {
            znicilTeloHraca = this.zautoc(hrac);
            this.buduciUtok = System.currentTimeMillis() + Math.round(1 / RYCHLOST_UTOKU * Math.pow(10, 3)); 
        }
        return znicilTeloHraca;
    }
    /**
     * Zautoci na Telo Hraca (uberie mu zdravie) 
     * @param hrac Objekt Hraca
     * @return PRAVDA ak Hrac stratil posledny zivot
     */
    private boolean zautoc(Hrac hrac) {
        return !hrac.getTelo().zmenZdravie(-this.telo.getPoskodenie());
    }
}