import java.awt.Color;
/**
 * Plocha vyhradena v Miestnosti
 *  
 * @author y0hn
 * @version v0.1
 */
public enum VyhradenaPlocha {
    VYHERNA_PLOCHA(0.2, 0.2, Color.YELLOW),
    UZDRAVOVACIA_PLOCHA(0.3, 0.3, Color.PINK);

    private Rozmer2D rozmerVMiestnosti;
    private Color farba;

    VyhradenaPlocha(double pX, double pY, Color farba) {
        Vektor2D pomerVelkosti = new Vektor2D(pX, pY);
        Vektor2D pozicia = Hra.ROZMER_HRY.sucinSoSkalarom(0.5);
        Vektor2D velkost = Hra.ROZMER_HRY.roznasobenie(pomerVelkosti);
        pozicia = pozicia.rozdiel(velkost.sucinSoSkalarom(0.5));

        this.rozmerVMiestnosti = new Rozmer2D(pozicia, velkost);
        this.farba = farba;
    }

    public Color getFarba() {
        return this.farba;
    }
    public Rozmer2D getRozmer() {
        return this.rozmerVMiestnosti;
    }

    public StavHry tik(Hrac hrac) {
        StavHry stav = StavHry.HRA;

        if (this.rozmerVMiestnosti.jeRozmerCiastocneVnutri(hrac.getTelo().getRozmer())) {
            switch (this) {
                case VYHERNA_PLOCHA:
                    stav = StavHry.VYHRA;
                    break;
                case UZDRAVOVACIA_PLOCHA:
                    hrac.getTelo().zmenZdravie(1);
                    break;
            }
        }

        return stav;
    }
}
