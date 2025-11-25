import java.util.ArrayList;
import java.util.Arrays;

/**
 * Enumeration trieda Smer
 * Obsahuje a popisuje smer (pohybu) v rovine
 * 
 * @author y0hn
 * @version v0.3
 */
public enum Smer {
    HORE(0, -1),
    PRAVO(1, 0),
    DOLE(0, 1),
    LAVO(-1, 0),;

    /*
    PRAVO_HORE(1, 1),
    PRAVO_DOLE(1, -1),
    LAVO_DOLE(-1, -1),
    LAVO_HORE(-1, 1);
    */

    private Vektor2D vektor;
    Smer(double x, double y) {
        this.vektor = new Vektor2D(x, y).normalizuj();
    }

    /**
     * Vytvori Smer na zaklade priradenej hodnoty
     * @param smernik
     * @return priradena hodnota Smer
     */
    public static Smer toSmer(int smernik) {
        Smer s = null;
        switch (smernik) {
            case 0:
                s = Smer.HORE;                
                break;
            case 1:
                s = Smer.LAVO;                
                break;
            case 2:
                s = Smer.PRAVO;                
                break;
            case 3:
                s = Smer.DOLE;                
                break;
        
            default:
                break;
        }
        return s;
    }
    /**
     * Kontroluje vztah medzi smermi
     * @param s1 prvy smer
     * @param s2 druhy smer
     * @return PRAVDA ak su navzajom protichodne
     */
    public static boolean opacneSmery(Smer s1, Smer s2) {
        boolean opacne;

        opacne  = (s1 == Smer.LAVO  && Smer.PRAVO == s2);
        opacne |= (s2 == Smer.PRAVO && Smer.LAVO == s1);
        opacne |= (s2 == Smer.HORE  && Smer.DOLE == s1);
        opacne |= (s2 == Smer.DOLE  && Smer.HORE == s1);
        
        /*
        opacne |= (s1 == Smer.PRAVO_HORE && Smer.LAVO_DOLE == s2);
        opacne |= (s2 == Smer.LAVO_DOLE  && Smer.PRAVO_HORE == s1);
        opacne |= (s2 == Smer.PRAVO_DOLE && Smer.LAVO_HORE == s1);
        opacne |= (s2 == Smer.LAVO_HORE  && Smer.PRAVO_DOLE == s1);
        */

        return opacne;
    }

    /**
     * Priradi zo smeru hodnotu Vektor2D 
     * @return hodnota v tvare int
     */
    public Vektor2D getVektor2D() {
        return this.vektor;
    }
    /**
     * Vytvara opacny smer ku smeru 
     * @param smer vlozeny smer
     * @return opacny smer
     */
    public Smer opacny() {
        Smer novySmer = toSmer(Math.abs(this.toInt() - 3));
        return novySmer;
    }
    /**
     * Priradi zo smeru hodnotu int 
     * @return hodnota v tvare int
     */
    public int toInt() {
        ArrayList<Smer> list = new ArrayList<Smer>(Arrays.asList(Smer.values()));
        return list.indexOf(this);
    }
}
