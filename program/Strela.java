import java.awt.Color;
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

    /**
     * Vytvori Strelu iducu jednym smerom od hraca
     * @param pozicia pozicia stredu Strely
     * @param smerovyVektor smer vystrelu
     * @param hrac je referencia na objekt, ktory vytvoril Strelu
     */
    public Strela(Vektor2D pozicia, Vektor2D smerovyVektor, Hrac hrac) {
        pozicia = pozicia.sucet(VELKOST.skalarnySucin(-0.5));
        this.rozmer = new Rozmer2D(pozicia, VELKOST);
        this.posun = smerovyVektor.skalarnySucin(RYCHLOST);
        this.hrac = hrac;

        this.grafika = new JPanel();
        this.grafika.setBounds(this.rozmer.vytvorRectangle());
        this.grafika.setBackground(Color.GRAY);
        
        JLayeredPane vrstvenaPlocha = (JLayeredPane)hrac.getGrafika().getParent();
        vrstvenaPlocha.setLayer(this.grafika, Displej.VRSTVA_STRELA);
        vrstvenaPlocha.add(this.grafika);
    }
    /**
     * Posunie Strelu predom nasavenym smerom. 
     * Ak Strela narazi, znici ju.
     * @param aktivMiestnost
     */
    public void tik(Miestnost aktivMiestnost) {
        this.rozmer.pricitajVektor2DKPozicii(this.posun);
        this.grafika.setLocation(this.rozmer.vytvorPointPozicie());

        if (this.jeVStene(aktivMiestnost) || this.zasah(aktivMiestnost)) {
            this.znic();
        }
    }

    private boolean jeVStene(Miestnost aM) {
        boolean kontorla = false;        
        for (Rozmer2D[] rozmerySteny : aM.getRozmery2D()) {
            for (Rozmer2D rozmerMuru : rozmerySteny) {
                kontorla |= rozmerMuru.jeRozmerCiastocneVnutri(this.rozmer);
                if (kontorla) {
                    break;
                }
            }
            if (kontorla) {
                break;
            }
        }
        return kontorla;
    }
    private boolean zasah(Miestnost aM) {
        boolean zasah = false;
        for (Telo n : aM.getNepriatelia()) {
            if (n.getRozmer().jeRozmerCiastocneVnutri(this.rozmer)) {
                // tu sposobi poskodenie
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
