import java.awt.Color;
import java.awt.Font;

/**
 * Zoskupuje stavy v ktorych sa Hra moze nachadzat pocas svojho trvania
 * 
 * @author y0hn
 * @version v0.2
 */
public enum StavHry {
    VYHRA(Color.GREEN, Color.BLUE, "Výhra", 100, 0, 0),
    PREHRA(Color.BLACK, Color.RED, "Koniec Hry", 100, 0, 0),
    PAUZA(Color.BLACK, Color.WHITE, "Pauza", 50, 0, 1),
    HRA;
    
    private Color farbaPozadia;
    private Color farbaTextu;
    private String text;
    private Font font;
    /**
     * SwingConstants.
     *  CENTER = 0
     *  LEFT = 2
     *  RIGHT = 4
     */
    private int osX;
    /**
     * SwingConstants.
     *  CENTER = 0
     *  TOP = 1
     *  BOTTOM = 3
     */
    private int osY;
    
    /**
     * Vytvori plne priehladny StavHry
     */
    StavHry() {
        
    }
    StavHry(Color farbaPozadia, Color farbaTextu, String text, int velkost, int osX, int osY) {
        this.farbaPozadia = farbaPozadia;
        this.farbaTextu = farbaTextu;
        this.text = text;
        this.font = new Font("Arial", 0, velkost);

        this.osX = osX;
        this.osY = osY;
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
    /**
     * Ziska horizontalne zarovnanie
     * @return zarovnanie textu v osi X
     */
    public int getZarovananieX() {
        return this.osX;
    }
    /**
     * Ziska vertikalne zarovnanie
     * @return zarovnanie textu v osi Y
     */
    public int getZarovananieY() {
        return this.osY;
    }
    /**
     * Ziska Font textu nad grafikou
     * @return
     */
    public Font getFont() {
        return this.font;
    }
}
