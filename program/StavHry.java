import java.awt.Color;

/**
 * Zoskupuje stavy v ktorych sa Hra moze nachadzat pocas svojho trvania
 * 
 * @author y0hn
 * @version v0.4
 */
public enum StavHry {
    VYHRA(new Color(33, 125, 174), "assets/victory.png"),
    PREHRA(Color.BLACK, "assets/death.png"),
    PAUZA(Color.BLACK, "assets/pause.png"),
    HRA(new Color(0, 0, 0, 0), ""),
    MENU(Color.BLACK, "assets/menu.png");
    
    private Color farba;
    private String cesta;
    
    /**
     * Vytvori plne priehladny StavHry
     */
    StavHry(Color farbaPozadia, String cestaObrazu) {
        this.farba = farbaPozadia;
        this.cesta = cestaObrazu;
    }

    /**
     * Vrati cestu ku grafike
     * @return text cesty
     */
    public String getCesta() {
        return this.cesta;
    }

    /**
     * Vrati farbu pozadia grafiky
     * @return farba za obrazkom grafiky
     */
    public Color getFarbaPozadia() {
        return this.farba;
    }
}
