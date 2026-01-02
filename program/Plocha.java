import javax.swing.ImageIcon;
import javax.swing.JLabel;
/**
 * Plocha vyhradena v Miestnosti
 *  
 * @author y0hn
 * @version v0.3
 */
public enum Plocha {
    VYHERNA_PLOCHA(-200, -200, 200, 200, "assets/win_place.png"),
    UZDRAVOVACIA_PLOCHA(80, 0, 150, 75, "assets/heal_place.png");

    private final Rozmer2D rozmerVMiestnosti;
    private final JLabel grafika;

    Plocha(double x, double y, double vX, double vY, String cesta) {
        x += Stena.SIRKA_STENY;
        y += Stena.SIRKA_STENY;

        if (x < 0) {
            x += Hra.ROZMER_OKNA.getVelkost().getX();
            x -= Stena.SIRKA_STENY * 2;
        }
        if (y < 0) {
            y += Hra.ROZMER_OKNA.getVelkost().getY();
            y -= Stena.SIRKA_STENY * 2;
        }

        this.rozmerVMiestnosti = new Rozmer2D(x, y, vX, vY);
        
        this.grafika = new JLabel();
        this.grafika.setIcon(new ImageIcon(cesta));
        this.grafika.setAlignmentX(0);
        this.grafika.setAlignmentY(0);
        this.grafika.setLayout(null);
        this.grafika.setBounds(this.rozmerVMiestnosti.vytvorRectangle());
    }

    /**
     * Ziska grafiku Plochy
     * @return
     */
    public JLabel getGrafika() {
        return this.grafika;
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
