import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

/**
 * Projektil vytvoreny hracom
 * 
 * @author y0hn
 * @version v0.2
 */
public class Strela {
    public static final Vektor2D VELKOST = new Vektor2D(10, 10);
    private static final double RYCHLOST = 15;

    private Rozmer2D rozmer;
    private Vektor2D posun;
    private JPanel grafika;
    private Hrac hrac;
    private int poskodenie;

    /**
     * Vytvori Strelu iducu jednym smerom od hraca
     * @param pozicia pozicia stredu Strely
     * @param smerovyVektor smer vystrelu
     * @param hrac je referencia na objekt, ktory vytvoril Strelu
     */
    public Strela(Vektor2D pozicia, Vektor2D smerovyVektor, Hrac hrac) {
        pozicia = pozicia.sucet(VELKOST.sucinSoSkalarom(-0.5));
        this.rozmer = new Rozmer2D(pozicia, VELKOST);
        this.posun = smerovyVektor.sucinSoSkalarom(RYCHLOST);
        this.poskodenie = hrac.getTelo().getPoskodenie();
        this.hrac = hrac;

        this.grafika = new JPanel();
        this.grafika.setBounds(this.rozmer.vytvorRectangle());
        this.grafika.setBackground(Color.BLACK);
        
        JLayeredPane vrstvenaPlocha = (JLayeredPane)hrac.getGrafika().getParent();
        vrstvenaPlocha.setLayer(this.grafika, Displej.VRSTVA_STRELA);
        vrstvenaPlocha.add(this.grafika);
    }
    /**
     * Posunie Strelu predom nasavenym smerom. 
     * Ak Strela narazi, znici ju.
     * @param aktivMiestnost
     */
    public void tik(Miestnost aktivMiestnost, double deltaCasu) {
        Vektor2D posunVCase = this.posun.sucinSoSkalarom(deltaCasu);
        this.rozmer.pricitajVektor2DKPozicii(posunVCase);
        this.grafika.setLocation(this.rozmer.vytvorPointPozicie());

        if (this.jeVStene(aktivMiestnost) || this.zasah(aktivMiestnost)) {
            this.znic();
        }
    }

    private boolean jeVStene(Miestnost aM) {
        boolean vStene = !aM.jePlochaRozmeruMimoStien(this.rozmer);
        return vStene;
    }
    private boolean zasah(Miestnost aM) {
        boolean zasah = false;
        ArrayList<Nepriatel> nepriatelia = new ArrayList<>(aM.getNepriatelia());

        for (Nepriatel n : nepriatelia) {
            Telo t = n.getTelo();
            if (t.getRozmer().jeRozmerCiastocneVnutri(this.rozmer)) {
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
        this.hrac.odstranStrelu(this);
    }
}
