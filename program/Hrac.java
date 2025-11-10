
/**
 * Drzi informacie o hracovi vo Svete
 * 
 * @author y0hn 
 * @version v0.2
 */
public class Hrac {
    private Telo telo;
    /**
     * Vytvori hraca vo svete
     * @param svet Svet v ktorom hrac zacina hru 
     */
    public Hrac() {
        this.telo = new Telo(10, Vektor.zero(), Vektor.dole(), 0, 20);
    }
    /**
     * Vrati Telo Hraca
     * @return reprazentacia Hraca vo Svete
     */
    public Telo getTelo() {
        return this.telo;
    }
}
