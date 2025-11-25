
/**
 * Write a description of class Main here.
 * 
 * @author y0hn
 * @version 0.1
 */
public class Main {
    /**
     * Sluzi na spustenie Hry 
     */
    public static void main(String[] args) {
        Hra hra = Hra.start();
        Casovac casovac = new Casovac(hra);
    }
}
