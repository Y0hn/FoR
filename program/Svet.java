import java.util.ArrayList;
import java.util.Random;

/**
 * Drzi informacie o celom hernom Svete
 * 
 * @author y0hn 
 * @version v0.3
 */
public class Svet {
    private int indexPociatocnejMiestnosti;
    private ArrayList<Miestnost> miestnosti;
    /**
     * Konstruktor Sveta
     * Vytvori hreny Svet so stanovenym poctom miestnosti
     * Taktiez vytvori prepoje (navaznosti) medzi miestnostami
     * 
     * @param velkost nastavuje pocet miestnosti vo svete
     */
    public Svet(int velkost) {
        this.miestnosti = new ArrayList<Miestnost>();
        Random random = new Random();

        ArrayList<Smer> smery = new ArrayList<Smer>();   

        Miestnost miestnost = new Miestnost();        
        Smer smer; // Urcuje smer dalsej miestnosti (L, P, D)

        // Vygeneruje miestnosti
        while (this.miestnosti.size() < velkost) {
            Miestnost sused = new Miestnost();

            // zabezpecenie aby nesiel naspat z L do P a opacne
            Smer poslednySmer = null;
            if (0 < smery.size() || this.miestnosti.size() + 1 == velkost) {
                poslednySmer = smery.get(smery.size() - 1);
            }
            do {
                smer = Smer.toSmer((int)Math.round(random.nextDouble() * 2) + 1);
            } while (poslednySmer != null && Smer.opacneSmery(smer, poslednySmer));

            int index = smery.size();
            miestnost.nastavSuseda(index - 1, smer);
            sused.nastavSuseda(index, smer.opacny());
            this.miestnosti.add(miestnost);
            miestnost = sused;
            smery.add(smer);
        }

        // Prida mozne prechody medzi miestnostami
        for (int i = 0; i < smery.size(); i++) {
            if (smery.get(i) == Smer.Dole) {
                for (int predosly = i - 1, dalsi = i + 2; 0 < predosly && dalsi < smery.size(); predosly--, dalsi++) {
                    if (Smer.opacneSmery(smery.get(predosly), smery.get(dalsi)) // kontroluje ci su protichodne miestnosti pod sebou
                            || 
                        smery.get(dalsi) == Smer.Dole && Smer.opacneSmery(smery.get(predosly), smery.get(dalsi - 1))) {   // alebo spodna pokracuje dole

                        


                        this.miestnosti.get(predosly).nastavSuseda(dalsi, Smer.Dole);    // do vyssej miestnosti prida cestu dole
                        this.miestnosti.get(dalsi).nastavSuseda(predosly, Smer.Hore);    // do nizsej miestnosti prida cestu hore
                    } else {
                        break;
                    }
                }
            }
        }

        // nastavi miestnost v krotrej hrac zacne hru
        // miestnost je medzi prvou 1/5 a 4/5 celkoveho poctu
        this.indexPociatocnejMiestnosti = (int)Math.round(this.miestnosti.size() * 0.2 + random.nextDouble() * (this.miestnosti.size() * 0.6));

        for (Miestnost m : miestnosti) {
            m.vytvorSteny();
        }
    }

    /**
     * Vrati intdex Zacinajej Miestnosti 
     * @return index Miesnosti v kotorej zacina hra 
     */
    public Miestnost getZaciatocnaMiestnost() {
        return this.miestnosti.get(this.indexPociatocnejMiestnosti);
    }
    /**
     * Ziska Miestnost z pola Miestnosti, 
     * ak je index nespravny vrati hodnotu NULL
     * @param index poradove cislo Miestnosti vo Svete 
     * @return Miestnost na mieste index v poli
     */
    public Miestnost getMiestnost(int index) {
        if (0 < index && index < this.miestnosti.size()) {
            return this.miestnosti.get(index);
        } else {
            return null;
        }
    }
}
