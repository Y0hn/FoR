import java.awt.Color;

/**
 * Zoskupuje stavy v ktorych sa Hra moze nachadzat pocas svojho trvania
 * 
 * @author y0hn
 * @version 0.1
 */
public enum StavHry {
    VYHRA(Color.GREEN, Color.BLUE, "Výhra"),
    PREHRA(Color.BLACK, Color.RED, "Koniec Hry"),
    PAUZA(Color.BLACK, Color.WHITE, "Pauza"),
    HRA(Color.BLACK, Color.RED, "");
    
    private Color farbaPozadia;
    private Color farbaTextu;
    private String text;

    StavHry(Color farbaPozadia, Color farbaTextu, String text) {
        this.farbaPozadia = farbaPozadia;
        this.farbaTextu = farbaTextu;
        this.text = text;
    }

    /**
     * Vrati farbu pozadia
     * @return Color pozadia
     */
    public Color getFarbaPozadia() {
        return this.farbaPozadia;
    }
    /**
     * Vrati farbu textu na grafike
     * @return Color textu
     */
    public Color getFarbaTextu() {
        return this.farbaTextu;
    }
    /**
     * Vrati text pre grafiku
     * @return textovy retazec
     */
    public String getText() {
        return this.text;
    }
}
