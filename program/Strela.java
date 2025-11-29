import javax.swing.JPanel;

/**
 * Projektil vytvoreny hracom
 * 
 * @author y0hn
 * @version v0.1
 */
public class Strela {
    private static final double RYCHLOST = 0.1;

    private Rozmer2D rozmer;
    private Vektor2D posun;
    private JPanel grafika;

    public Strela(Rozmer2D rozmer, Vektor2D smerovyVektor) {
        this.rozmer = rozmer;
        this.posun = smerovyVektor.skalarnySucin(RYCHLOST);
    }
    public void setGrafika(JPanel grafika) {
        this.grafika = grafika;
    }
    public void tik(Miestnost aktivMiestnost) {
        this.rozmer.pricitajVektor2DKPozicii(posun);
        this.grafika.setLocation(this.rozmer.vytvorPointPozicie());

        if (this.jeKolizia(aktivMiestnost) || this.zasah(aktivMiestnost)) {
            this.znic();
        }
    }

    private boolean jeKolizia(Miestnost aM) {
        boolean kontorla = true;
        
        for (Rozmer2D[] rozmerySteny : aM.getRozmery2D()) {
            for (Rozmer2D rozmer : rozmerySteny) {
                kontorla &= !rozmer.jeRozmerCiastocneVnutri(rozmer);
            }
        }
        return kontorla;
    }
    private boolean zasah(Miestnost aM) {
        boolean zasah = false;

        for (Telo n : aM.getNepriatelia()) {
            if (n.getRozmer().jeRozmerCiastocneVnutri(rozmer)) {
                // tu sposobi poskodenie
                zasah = true;
            }
        }
        return zasah;
    }
    private void znic() {
        this.grafika.getParent().remove(this.grafika);
        Hra.odstranStrelu(this);
    }
}
