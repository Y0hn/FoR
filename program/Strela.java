import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JLayeredPane;

/**
 * Projektil vytvoreny hracom
 * 
 * @author y0hn
 * @version v0.3
 */
public class Strela implements Serializable {
    private static final Vektor2D VELKOST = new Vektor2D(20, 20);
    private static final String CESTA_ZVUKU = "shot.wav";
    private static final String CESTA_OBRAZU = "shot.png";
    private static final double RYCHLOST = 25;

    private final Rozmer2D rozmer;
    private final Vektor2D posun;
    private final OtacanaGrafika grafika;
    private final int poskodenie;

    /**
     * Vytvori Strelu iducu jednym smerom od hraca
     * @param pozicia pozicia stredu Strely
     * @param smerovyVektor smer vystrelu
     * @param hrac je referencia na objekt, ktory vytvoril Strelu
     */
    public Strela(Vektor2D pozicia, Vektor2D smerovyVektor, Hrac hrac) {
        pozicia = pozicia.sucet(VELKOST.sucinSoSkalarom(-0.5));
        this.rozmer = new Rozmer2D(pozicia, VELKOST);
        this.posun = smerovyVektor.normalizuj().sucinSoSkalarom(RYCHLOST);
        this.poskodenie = hrac.getTelo().getPoskodenie();

        this.grafika = new OtacanaGrafika(CESTA_OBRAZU);
        this.grafika.setBounds(this.rozmer.vytvorRectangle());
        this.grafika.setUhol(this.posun.getUhol());
        
        JLayeredPane vrstvenaPlocha = (JLayeredPane)hrac.getGrafika().getParent();
        vrstvenaPlocha.setLayer(this.grafika, Displej.VRSTVA_STRELA);
        vrstvenaPlocha.add(this.grafika);

        Hudba vystrel = new Hudba(CESTA_ZVUKU, false);
        vystrel.prehraj();
    }
    /**
     * Ziska grafiku Strely
     * @return (OtacanaGrafika)JLabel
     */
    public OtacanaGrafika getGrafika() {
        return this.grafika;
    }
    /**
     * Posunie Strelu predom nasavenym smerom. 
     * Ak Strela narazi, znici ju.
     * @param aM aktualna Miestnost
     * @param hrac objekt Hraca
     * @param deltaCasu casovy rozdiel od posledneho tiku
     */
    public void tik(Miestnost aM, Hrac hrac, double deltaCasu) {
        Vektor2D posunVCase = this.posun.sucinSoSkalarom(deltaCasu);
        this.rozmer.posun(posunVCase);
        this.grafika.setLocation(this.rozmer.getPozicia().vytvorPoint());

        if (this.jeVStene(aM) || this.zasah(aM)) {
            this.znic();
            hrac.odstranStrelu(this);
        }
    }

    private boolean jeVStene(Miestnost aM) {
        boolean vStene = !aM.jeRozmerMimoStien(this.rozmer);
        return vStene;
    }
    private boolean zasah(Miestnost aM) {
        boolean zasah = false;
        ArrayList<Nepriatel> nepriatelia = new ArrayList<>(aM.getNepriatelia());

        for (Nepriatel n : nepriatelia) {
            Telo t = n.getTelo();
            if (t.getRozmer().jeRozmerPrekryty(this.rozmer)) {
                if (!t.zmenZdravie(-this.poskodenie)) {
                    aM.odstranNepriatela(n);
                }
                zasah = true;
            }
        }
        return zasah;
    }
    private void znic() {
        this.grafika.setBounds(Rozmer2D.ZERO.vytvorRectangle());
        this.grafika.getParent().remove(this.grafika);
    }
}
