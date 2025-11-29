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
    }

    private boolean kontrolaKolizii(Miestnost aM) {
        boolean kontorla = true;
        
        for (Rozmer2D[] rozmerySteny : aM.getRozmery2D()) {
            for (Rozmer2D rozmer : rozmerySteny) {
                kontorla &= !rozmer.jeRozmerVnutri(rozmer);
            }
        }

        return kontorla;
    }
    private boolean zasah(Telo teloNeriatela) {
        boolean zasah;

        this.grafika.getParent().remove(grafika);
        return true;
    }
}
