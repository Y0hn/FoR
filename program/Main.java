/**
 * Hlavna spustacia trieda
 * 
 * @author y0hn
 * @version v0.2
 */
public abstract class Main {
    /**
     * Sluzi na spustenie Hry s Casovacom
     * @param args parametre pri spusteni (nedefinovane)
     */
    public static void main(String[] args) {
        Hra hra = Hra.start();
        Casovac.spust(hra);
    }
}
