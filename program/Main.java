    /**
 * Hlavna spustacia trieda
 * 
 * @author y0hn
 * @version v0.2
 */
public class Main {
    /**
     * Sluzi na spustenie Hry a Cosovaca
     */
    public static void main(String[] args) {
        Hra hra = Hra.start();
        new Casovac(hra);
    }
}
