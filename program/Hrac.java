import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Drzi informacie o hracovi vo Svete
 * 
 * @author y0hn 
 * @version v0.6
 */
public class Hrac {
    public static final Rozmer2D GRAFIKA_ZIVOTOV_HRACA = new Rozmer2D(40, 10, 150, 20);

    private static final int MAX_ZIVOT = 10;
    private static final Vektor2D VELKOST = new Vektor2D(50, 50);
    private static final double RYCHLOST = 10;
    private static final double RYCHLOST_STRELBY = 2;
    private static final int POSKODENIE_STERLY = 1;

    private final Telo telo;
    private boolean pauza;
    private boolean vstupPauza;
    private boolean[] pohybVSmere;
    private boolean[] strelbaVSmere;
    private ArrayList<Strela> strely;
    private long buducaStrela;

    private JPanel[] ui;

    /**
     * Vytvori Hraca
     */
    public Hrac() {
        Rozmer2D rozmer = new Rozmer2D(Hra.ROZMER_OKNA.ziskajStred(), VELKOST);
        rozmer.setPozicia(rozmer.getPozicia().rozdiel(VELKOST.sucinSoSkalarom(0.5)));
        this.telo = new Telo(MAX_ZIVOT, rozmer, RYCHLOST, POSKODENIE_STERLY);

        // Nastavenie vstupov
        this.pohybVSmere = new boolean[Smer.values().length];
        this.strelbaVSmere = new boolean[Smer.values().length];
        for (int i = 0; i < this.pohybVSmere.length; i++) {
            this.pohybVSmere[i] = false;
        }
        this.vstupPauza = false;
        this.pauza = false;
        
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
     * Ziska grafiku hraca
     * @return JPanel hraca
     */
    public JPanel getGrafika() {
        return this.telo.getGrafika();
    }
    /**
     * Nastavi grafiku uzivatelskeho rozhrania
     * @param ui
     */
    public void setUI(JPanel[] ui) {
        this.ui = ui;
    }
    /**
     * Ziska grafiku uzivatelskeho rozhrania
     * @param ui
     */
    public JPanel[] getUI() {
        return this.ui;
    }
    /**
     * Odstani Strelu z obnovovacieho listu
     * @param strela
     */
    public void odstranStrelu(Strela strela) {
        this.strely.remove(strela);
    }
    /**
     * Ozivy Hraca 
     */
    public void ozivHraca() {
        this.telo.zmenZdravie(Integer.MAX_VALUE);
        this.telo.setPozicia(Hra.ROZMER_OKNA.ziskajStred().rozdiel(VELKOST.sucinSoSkalarom(0.5)));
    }

    /**
     * Obnovi vlastnosti Hraca a jeho komponenty.
     * Ziska a pracuje informacie zo vstupov.
     * @param aktMiest sucastna Miestnost
     * @param stav sucasny Stav Hry
     * @param deltaCasu casovy rozdiel od posledneho tiku
     */
    public StavHry tik(Miestnost aktMiest, StavHry stav, double deltaCasu) {
        Vektor2D v = this.ziskajSmerovyVektor2D(this.pohybVSmere);
        this.telo.setPohybVektor(v);

        if (this.telo.tik(aktMiest, deltaCasu)) {
            int zivoty = this.telo.getZdravie();
            for (int i = 1; i - 1 < this.ui.length; i++) {
                this.ui[i - 1].setVisible(i <= zivoty);
            }
        }

        Vektor2D s = this.ziskajSmerovyVektor2D(this.strelbaVSmere);
        if (!s.equals(Vektor2D.ZERO) && this.buducaStrela <= System.currentTimeMillis()) {
            this.vystrel(s);
            this.buducaStrela = System.currentTimeMillis() + Math.round(1 / RYCHLOST_STRELBY * Math.pow(10, 3));
        }

        ArrayList<Strela> tentoTik = new ArrayList<Strela>(this.strely);
        for (Strela strela : tentoTik) {
            strela.tik(aktMiest, deltaCasu);
        }

        
        if (this.pauza != this.vstupPauza) {
            this.pauza = false;
        }
        if (!this.pauza && this.vstupPauza && stav == StavHry.HRA) {
            stav = StavHry.PAUZA;
            this.pauza = true;
        }
        return stav;
    }
    /**
     * Volane v pripade pozastavenia Hry
     * @return TRUE ak ma Hra pokracovat inak FALSE
     */
    public boolean pauzaTik() {
        if (this.pauza != this.vstupPauza) {
            this.pauza = false;
        }

        if (!this.pauza && this.vstupPauza) {
            this.pauza = true;
            return true;
        } 
        return false;
    }
    /**
     * Nastavi odposluch na klavesove vstupy k oknu
     * @param okno hlavne zobrazenie Hry
     */
    public void nastavVstup(JFrame okno) {
        KeyAdapter ka = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                Hrac.this.vstup(e.getKeyCode());
            }
            @Override
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

            case 27: // ESC
                this.vstupPauza = pridaj;
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

            // prida jednu sekundu vydelenu rychlostou strelby
            this.buducaStrela = System.nanoTime() + Math.round(1 / RYCHLOST_STRELBY * Math.pow(10, 9));
        }            
    }
}
