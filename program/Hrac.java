import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Drzi informacie o Hracovi v Miestnosti
 * 
 * @author y0hn 
 * @version v0.7
 */
public class Hrac implements Serializable {
    public static final Rozmer2D GRAFIKA_ZIVOTOV_HRACA = new Rozmer2D(40, 10, 300, 40);

    private static final int MAX_ZIVOT = 10;
    private static final Vektor2D VELKOST = new Vektor2D(100, 100);
    private static final double RYCHLOST = 15;
    private static final double RYCHLOST_STRELBY = 2;
    private static final int POSKODENIE_STERLY = 1;

    private final Telo telo;
    private boolean pauza;
    private boolean vstupPauza;
    private boolean vstupRestart;
    private boolean[] pohybVSmere;
    private boolean[] strelbaVSmere;

    private ArrayList<Strela> strely;
    private long buducaStrela;

    private transient JPanel[] ui;

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
     * @return reprazentacia Hraca v Miestnosti
     */
    public Telo getTelo() {
        return this.telo;
    }
    /**
     * Ziska grafiku hraca
     * @return Otacana Grafika Hraca
     */
    public OtacanaGrafika getGrafika() {
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
     * Ziska restart
     * @return PRAVDA ak je stlaceny klaves pre restrat
     */
    public boolean getRestart() {
        return this.vstupRestart;
    }
    /**
     * Ziska vsetky aktualne Strely
     * @return list Striel
     */
    public ArrayList<Strela> getStrely() {
        return this.strely;
    }
    /**
     * Odstani Strelu z obnovovacieho listu
     * @param strela
     */
    public void odstranStrelu(Strela strela) {
        this.strely.remove(strela);
    }
    /**
     * Hracovi nastavi plny pocet zivotov a postavi ho do stredu Miestnosti (okna) 
     */
    public void ozivHraca() {
        this.telo.zmenZdravie(Integer.MAX_VALUE);
        this.telo.setPozicia(Hra.ROZMER_OKNA.ziskajStred().rozdiel(VELKOST.sucinSoSkalarom(0.5)));
    }

    /**
     * Obnovi vlastnosti Hraca a jeho komponenty.
     * Ziska a pracuje informacie zo vstupov.
     * 
     * @param aktMiest sucastna Miestnost
     * @param stav sucasny Stav Hry
     * @param deltaCasu casovy rozdiel od posledneho tiku
     */
    public StavHry tik(Miestnost aktMiest, StavHry stav, double deltaCasu) {
        Vektor2D v = this.ziskajSmerovyVektor2D(this.pohybVSmere);
        this.telo.setPohybVektor(v);

        if (this.telo.tik(aktMiest, deltaCasu)) {
            this.obnovZivoty();
        }

        Vektor2D s = this.ziskajSmerovyVektor2D(this.strelbaVSmere);
        if (!s.equals(Vektor2D.ZERO) && this.buducaStrela <= System.currentTimeMillis()) {
            this.vystrel(s);
            this.buducaStrela = System.currentTimeMillis() + Math.round(1 / RYCHLOST_STRELBY * Math.pow(10, 3));
        }

        ArrayList<Strela> tentoTik = new ArrayList<Strela>(this.strely);
        for (Strela strela : tentoTik) {
            strela.tik(aktMiest, this, deltaCasu);
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
     * Obnovi Graficke pocitadlo zivotov, tak aby ukazovalo aktualne udaje
     */
    public void obnovZivoty() {
        if (this.ui != null) {
            int zivoty = this.telo.getZdravie();
            for (int i = 1; i - 1 < this.ui.length; i++) {
                this.ui[i - 1].setVisible(i <= zivoty);
            }
        }
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
        int[] indexVstupu = new int[] {-1, -1};

        switch (klaves) {
            case 87: // W
                indexVstupu[0] = Smer.HORE.ordinal();
                break;
            case 65: // A
                indexVstupu[0] = Smer.LAVO.ordinal();
                break;
            case 83: // S
                indexVstupu[0] = Smer.DOLE.ordinal();
                break;
            case 68: // D
                indexVstupu[0] = Smer.PRAVO.ordinal();
                break;

            case 38: // sipka Hore
                indexVstupu[1] = Smer.HORE.ordinal();
                break;
            case 37: // sipka Lavo
                indexVstupu[1] = Smer.LAVO.ordinal();
                break;
            case 40: // sipka Dole
                indexVstupu[1] = Smer.DOLE.ordinal();
                break;
            case 39: // sipka Pravo
                indexVstupu[1] = Smer.PRAVO.ordinal();
                break;

            case 27: // ESC
                this.vstupPauza = pridaj;
                break;

            case 82: // R
                this.vstupRestart = pridaj;
                break;

            default:
                break;
        }

        if (indexVstupu[0] != -1) {
            this.pohybVSmere[indexVstupu[0]] = pridaj;
        }
        if (indexVstupu[1] != -1) {
            this.strelbaVSmere[indexVstupu[1]] = pridaj;
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
            this.telo.setSmerovyVektor(smer);
            this.telo.obnovGrafiku();
        }            
    }
}
