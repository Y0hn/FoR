import javax.swing.ImageIcon;
import javax.swing.JLabel;
/**
 * Plocha vyhradena v Miestnosti
 *  
 * @author y0hn
 * @version v0.3
 */
public enum Plocha {
    VYHERNA_PLOCHA(-200, -200, 200, 200, "win_place.png"),
    UZDRAVOVACIA_PLOCHA(80, 0, 150, 75, "heal_place.png");

    private final Rozmer2D rozmerVMiestnosti;
    private final String grafika;

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
        
        this.grafika = Hra.CESTA_K_SUBOROM + cesta;
    }

    /**
     * Ziska grafiku Plochy
     * @return
     */
    public JLabel getGrafika() {
        JLabel vytovenaGrafika = new JLabel();
        vytovenaGrafika.setIcon(new ImageIcon(Plocha.class.getResource(this.grafika)));
        vytovenaGrafika.setAlignmentX(0);
        vytovenaGrafika.setAlignmentY(0);
        vytovenaGrafika.setLayout(null);
        vytovenaGrafika.setBounds(this.rozmerVMiestnosti.vytvorRectangle());
        return vytovenaGrafika;
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
