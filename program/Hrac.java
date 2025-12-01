import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Drzi informacie o hracovi vo Svete
 * 
 * @author y0hn 
 * @version v0.4
 */
public class Hrac {
    private static final int MAX_ZIVOT_HRACA = 10;
    private static final Vektor2D VELKOST_HRACA = new Vektor2D(50, 50);
    private static final double RYCHLOST_HRACA = 10;
    private static final double RYCHLOST_STRELBY = 2;

    private Telo telo;
    private boolean[] pohybVSmere;
    private boolean[] strelbaVSmere;
    private JPanel grafika;
    private ArrayList<Strela> strely;
    private long buducaStrela;

    /**
     * Vytvori hraca vo svete
     * @param svet Svet v ktorom hrac zacina hru 
     */
    public Hrac() {
        Rozmer2D rozmer = new Rozmer2D(Displej.getStred(), VELKOST_HRACA);
        this.telo = new Telo(MAX_ZIVOT_HRACA, rozmer, Smer.DOLE.getVektor2D(), RYCHLOST_HRACA);

        this.pohybVSmere = new boolean[Smer.values().length];
        this.strelbaVSmere = new boolean[Smer.values().length];
        for (int i = 0; i < this.pohybVSmere.length; i++) {
            this.pohybVSmere[i] = false;
        }
        this.strely = new ArrayList<Strela>();
    }
    /**
     * Vrati Telo Hraca
     * @return reprazentacia Hraca vo Svete
     */
    public Telo getTelo() {
        return this.telo;
    }
    /**
     * Nastavi grafiku hraca
     * @param grafika JPanel
     */
    public void setGrafika(JPanel grafika) {
        this.grafika = grafika;
    }
    /**
     * Ziska grafiku hraca
     * @return JPanel hraca
     */
    public JPanel getGrafika() {
        return this.grafika;
    }
    /**
     * Odstani Strelu z obnovovacieho listu
     * @param strela
     */
    public void odstranStrelu(Strela strela) {
        this.strely.remove(strela);
    }

    /**
     * Obnovi vlastnosti Hraca a jeho komponenty.
     * Ziska a pracuje informacie zo vstupov.
     * @param aktMiest sucastna Miestnost
     */
    public void tik(Miestnost aktMiest) {
        Vektor2D v = this.ziskajSmerovyVektor2D(this.pohybVSmere);
        this.telo.setPohybVektor(v);
        this.telo.tik(aktMiest);

        Vektor2D s = this.ziskajSmerovyVektor2D(this.strelbaVSmere);
        if (this.buducaStrela <= System.nanoTime()) {
            this.vystrel(s);
            this.buducaStrela = System.nanoTime() + Math.round(1 / RYCHLOST_STRELBY * Math.pow(10, 9));
        }

        // Vykresli zmenu
        if (this.grafika != null && !v.equals(Vektor2D.ZERO)) {
            this.grafika.setLocation(this.telo.getRozmer().getPozicia().vyvtorPoint());
        }

        ArrayList<Strela> tentoTik = new ArrayList<Strela>(this.strely);
        for (Strela strela : tentoTik) {
            strela.tik(aktMiest);
        }
    }
    /**
     * Nastavi odposluch na klavesove vstupy k oknu
     * @param okno hlavne zobrazenie Hry
     */
    public void nastavVstup(JFrame okno) {
        KeyAdapter ka = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                Hrac.this.vstup(e.getKeyCode());
            }
            public void keyReleased(KeyEvent e) {
                Hrac.this.koniecVstupu(e.getKeyCode());
            }
        };
        okno.addKeyListener(ka);
    }

    private void vstup(int vstup) {
        this.keySetSmer(vstup, true);

    }
    private void koniecVstupu(int vstup) {
        this.keySetSmer(vstup, false);
    }
    /**
     * Mapuje dolezite klavesy na klavesnici ako vstupy pre ovladanie 
     * @param klaves Index klavesu
     * @param pridaj PRAVDA ak bol staceny, NEPRAVDA ak pusteny
     */
    private void keySetSmer(int klaves, boolean pridaj) {   
        int[] indexiKlaves = new int[] {-1, -1};

        switch (klaves) {
            case 87: // W
                indexiKlaves[0] = Smer.HORE.ordinal();
                break;
            case 65: // A
                indexiKlaves[0] = Smer.LAVO.ordinal();
                break;
            case 83: // S
                indexiKlaves[0] = Smer.DOLE.ordinal();
                break;
            case 68: // D
                indexiKlaves[0] = Smer.PRAVO.ordinal();
                break;

            case 38: // sipka Hore
                indexiKlaves[1] = Smer.HORE.ordinal();
                break;
            case 37: // sipka Lavo
                indexiKlaves[1] = Smer.LAVO.ordinal();
                break;
            case 40: // sipka Dole
                indexiKlaves[1] = Smer.DOLE.ordinal();
                break;
            case 39: // sipka Pravo
                indexiKlaves[1] = Smer.PRAVO.ordinal();
                break;

            default:
                break;
        }

        if (indexiKlaves[0] != -1) {
            this.pohybVSmere[indexiKlaves[0]] = pridaj;
        }
        if (indexiKlaves[1] != -1) {
            this.strelbaVSmere[indexiKlaves[1]] = pridaj;
        }
    }
    private Vektor2D ziskajSmerovyVektor2D(boolean[] smery) {   
        Vektor2D v = Vektor2D.ZERO;

        for (int i = 0; i < smery.length; i++) {
            if (smery[i]) {
                v = v.sucet(Smer.values()[i].getVektor2D());
            }
        }
        
        return v;
    }
    private void vystrel(Vektor2D smer) {
        if (!smer.equals(Vektor2D.ZERO)) {
            Vektor2D pozicia = this.telo.getRozmer().ziskajStred();
            Strela strela = new Strela(pozicia, smer, this);
            this.strely.add(strela);
        }
    }
}
