import java.awt.Color;
/**
 * Plocha vyhradena v Miestnosti
 *  
 * @author y0hn
 * @version v0.2
 */
public enum Plocha {
    VYHERNA_PLOCHA(0.2, 0.2, Color.YELLOW),
    UZDRAVOVACIA_PLOCHA(0.3, 0.3, Color.PINK);

    private final Rozmer2D rozmerVMiestnosti;
    private final Color farba;

    Plocha(double pX, double pY, Color farba) {
        Vektor2D pomerVelkosti = new Vektor2D(pX, pY);
        Vektor2D pozicia = Hra.ROZMER_OKNA.getVelkost().sucinSoSkalarom(0.5);
        Vektor2D velkost = Hra.ROZMER_OKNA.getVelkost().roznasobenie(pomerVelkosti);
        pozicia = pozicia.rozdiel(velkost.sucinSoSkalarom(0.5));

        this.rozmerVMiestnosti = new Rozmer2D(pozicia, velkost);
        this.farba = farba;
    }

    /**
     * Ziska farbu Plochy
     * @return
     */
    public Color getFarba() {
        return this.farba;
    }
    /**
     * Ziska Rozmer plochy
     * @return
     */
    public Rozmer2D getRozmer() {
        return this.rozmerVMiestnosti;
    }

    /**
     * Zistuje ci sa Hrac dotyka plochy
     * @param hrac objekt Hraca
     * @return pripadnna zmena Stavu Hry (vyhra)
     */
    public StavHry tik(Hrac hrac) {
        StavHry stav = StavHry.HRA;

        if (this.rozmerVMiestnosti.jeRozmerPrekryty(hrac.getTelo().getRozmer())) {
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
