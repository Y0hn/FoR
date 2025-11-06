import java.util.ArrayList;
import java.util.Random;

/**
 * Drzi informacie o celom hernom Svete
 * 
 * @author y0hn 
 * @version 0.1
 */
public class Svet {
    private ArrayList<Miestnost> miestnosti;
    /**
     * Konstruktor Sveta
     * @param velkost nastavuje pocet miestnosti vo svete
     */
    public Svet(int velkost) {
        this.miestnosti = new ArrayList<Miestnost>();
        Random random = new Random();

        ArrayList<Integer> smery = new ArrayList<Integer>();   

        Miestnost miestnost = new Miestnost();        
        int smer; // Cislo od 1 do 3 urcuje smer dalsej miestnosti (L, P, D)

        // Vygeneruje miestnosti
        while (this.miestnosti.size() < velkost) {
            Miestnost sused = new Miestnost();

            // zabezpecenie aby nesiel naspat z L do P a opacne
            int poslednySmer = -1;
            if (0 < smery.size() || this.miestnosti.size() + 1 == velkost) {
                poslednySmer = smery.get(smery.size() - 1);
            }
            do {
                smer = (int)Math.round(random.nextDouble() * 2) + 1;
            } while (opacneSmery(smer, poslednySmer));


            miestnost.nastavSuseda(sused, smer);
            sused.nastavSuseda(miestnost, opacnySmer(smer));
            this.miestnosti.add(miestnost);
            miestnost = sused;
            smery.add(smer);
        }

        // Prida mozne prechody medzi miestnostami
        for (int i = 0; i < smery.size(); i++) {
            if (smery.get(i) == 3) {
                for (int predosly = i - 1, dalsi = i + 2; 0 < predosly && dalsi < smery.size(); predosly--, dalsi++) {
                    if (opacneSmery(smery.get(predosly), smery.get(dalsi)) // kontroluje ci su protichodne miestnosti pod sebou
                            || 
                        smery.get(dalsi) == 3 && opacneSmery(smery.get(predosly), smery.get(dalsi - 1))) {   // alebo spodna pokracuje dole

                        this.miestnosti.get(predosly).nastavSuseda(this.miestnosti.get(dalsi), 3);    // do vyssej miestnosti prida cestu dole
                        this.miestnosti.get(dalsi).nastavSuseda(this.miestnosti.get(predosly), 0);    // do nizsej miestnosti prida cestu hore
                    } else {
                        break;
                    }
                }
            }
        }
    }
    /**
     * Kontroluje vztah medzi smermi (1 = L, 2 = R, 3 = D) 
     * @param s1 prvy smer
     * @param s2 druhy smer
     * @return PRAVDA ak su navzajom protichodne
     */
    private static boolean opacneSmery(int s1, int s2) {
        return (s1 == 1 && 2 == s2) || (s2 == 1 && 2 == s1);
    }

    /**
     * Vytvara opacny smer ku smeru 
     * @param smer vlozeny smer
     * @return opacny smer
     */
    private static int opacnySmer(int smer) {
        int novySmer = Math.abs(smer - 3);
        return novySmer;
    }
}
