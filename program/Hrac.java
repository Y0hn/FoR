
/**
 * Drzi informacie o hracovi vo Svete
 * 
 * @author y0hn 
 * @version 0.1
 */
public class Hrac {
    private int zdravie;
    private Vektor pozicia;
    private Miestnost sucasnaMiestnost;
    /**
     * Vytvori hraca vo svete
     */
    public Hrac(Svet svet) {
        this.sucasnaMiestnost = svet.getZaciatocnaMiestnost();
        this.pozicia = Vektor.zero(); 
        this.zdravie = 10;
    }
}
