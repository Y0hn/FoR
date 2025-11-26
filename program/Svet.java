import java.util.ArrayList;
import java.util.Random;

/**
 * Drzi informacie o celom hernom Svete
 * 
 * @author y0hn 
 * @version v0.4
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

        Miestnost miestnost = new Miestnost(0);
        Smer smer; // Urcuje smer k dalsej miestnosti (L, P, D)

        // Vygeneruje Suseda Miestnosti
        while (this.miestnosti.size() + 1 < velkost) {
            Miestnost sused = new Miestnost(this.miestnosti.size() + 1);

            // zabezpecenie aby nesiel naspat z L do P a opacne
            Smer poslednySmer = null;
            if (0 < smery.size()) {
                poslednySmer = smery.get(smery.size() - 1);
            }
            // Vygeneruje iny smer aby nesiel naspat
            do {
                smer = this.vygenerujNahodnySmer(random);
            } while (smer == Smer.HORE || poslednySmer != null && smer.jeOpacny(poslednySmer));

            miestnost.setSused(this.miestnosti.size() + 1, smer);
            sused.setSused(this.miestnosti.size(), smer.opacny());

            this.miestnosti.add(miestnost);
            miestnost = sused;
            smery.add(smer);
        }
        // prida poslednu miestnost
        this.miestnosti.add(miestnost);

        // Prida mozne prechody medzi miestnostami
        for (int i = 0; i < smery.size(); i++) {
            if (smery.get(i) == Smer.DOLE) {
                for (int predosly = i - 1, dalsi = i + 2; 0 < predosly && dalsi < smery.size(); predosly--, dalsi++) {
                    if (smery.get(predosly).jeOpacny(smery.get(dalsi)) // kontroluje ci su protichodne miestnosti pod sebou
                            || 
                        smery.get(dalsi) == Smer.DOLE && smery.get(predosly).jeOpacny(smery.get(dalsi - 1))) {   // alebo spodna pokracuje dole

                        this.miestnosti.get(predosly).setSused(dalsi, Smer.DOLE);    // do vyssej miestnosti prida cestu dole
                        this.miestnosti.get(dalsi).setSused(predosly, Smer.HORE);    // do nizsej miestnosti prida cestu hore
                    } else {
                        break;
                    }
                }
            }
        }

        // nastavi miestnost v krotrej hrac zacne hru
        // miestnost je medzi prvou 1/5 a 4/5 celkoveho poctu
        this.indexPociatocnejMiestnosti = (int)Math.round(this.miestnosti.size() * 0.2 + random.nextDouble() * (this.miestnosti.size() * 0.6));

        for (Miestnost m : this.miestnosti) {
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
        if (0 <= index && index < this.miestnosti.size()) {
            return this.miestnosti.get(index);
        } else {
            return null;
        }
    }

    private Smer vygenerujNahodnySmer(Random random) {
        int smernik = random.nextInt(Smer.values().length);
        return Smer.values()[smernik];
    }
}
