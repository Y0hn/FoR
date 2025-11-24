import java.util.ArrayList;
import java.util.Arrays;

/**
 * Enumeration trieda Smer
 * Obsahuje a popisuje smer (pohybu) v rovine
 * 
 * @author y0hn
 * @version v0.2
 */
public enum Smer {
    Hore(0, 1),
    PravoHore(1, 1),
    Pravo(1, 0),
    PravoDole(1, -1),
    Dole(0, -1),
    LavoDole(-1, -1),
    Lavo(-1, 0),
    LavoHore(-1, 1);

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
                s = Smer.Hore;                
                break;
            case 1:
                s = Smer.Lavo;                
                break;
            case 2:
                s = Smer.Pravo;                
                break;
            case 3:
                s = Smer.Dole;                
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

        opacne  = (s1 == Lavo && Pravo == s2);
        opacne |= (s2 == Pravo && Lavo == s1);
        opacne |= (s2 == Hore && Dole == s1);
        opacne |= (s2 == Dole && Hore == s1);

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
