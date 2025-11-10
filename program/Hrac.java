
/**
 * Drzi informacie o hracovi vo Svete
 * 
 * @author y0hn 
 * @version v0.1
 */
public class Hrac {
    private Telo telo;
    private Miestnost sucasnaMiestnost;
    /**
     * Vytvori hraca vo svete
     * @param svet Svet v ktorom hrac zacina hru 
     */
    public Hrac(Svet svet) {
        this.sucasnaMiestnost = svet.getZaciatocnaMiestnost();
        this.telo = new Telo(10, Vektor.zero(), Vektor.dole(), 0, 20);
    }
    /**
     * Vrati sucastnu Miestnost
     * @return Miestnost v ktorej je Hrac
     */
    public Miestnost getSucasnaMiestnost() {
        return this.sucasnaMiestnost;
    }
    /**
     * Vrati Telo Hraca
     * @return reprazentacia Hraca vo Svete
     */
    public Telo getTelo() {
        return this.telo;
    }
}
