/**
 * Miestnost vo svete, ktora ma susedne miestnosti a obsahuje nieco
 * Po vojdeni hraca do miestnosti sa aktivuje
 * 
 * @author y0hn
 * @version 0.1
 */
public class Miestnost {
    /**
     * Hrona 0
     * Lava  1
     * Prava 2
     * Dolna 3
     */
    private Miestnost[] susedneMiestnosti;
    /**
     * Konstruktor triedy Miestnost
     */
    public Miestnost() {
        this.susedneMiestnosti = new Miestnost[4];
    }
    /**
     * Nastavi suseda v predom urcenom smere
     * 
     * @param sused referencia na suseda
     * @param smer smer v ktorom lezi v zavislosti od stredu sucasnej miestnosti
     */
    public void nastavSuseda(Miestnost sused, Smer smer) {
        int s = smer.toInt();
        if (s < 0 || 3 < s) {
            System.out.println("Zly smer: " + s);
            return;
        }
        this.susedneMiestnosti[s] = sused;
    }
    /**
     * Ziska pocet susedov
     * @return pocet susedov bez hodnot "null"
     */
    public int getPocetSusedov() {
        int pocet = 0;
        for (int i = 0; i < this.susedneMiestnosti.length; i++) {
            if (this.susedneMiestnosti[i] != null) {
                pocet++;
            }
        }
        return pocet;
    }
    /**
     * Zisti ci ma suseda v smere
     * @param smer smer zistovania
     * @return PRAVDA ak ma v tom smere suseda
     */
    public boolean maSusedaVSmere(int smer) {
        return this.susedneMiestnosti[smer] != null;
    }
}
