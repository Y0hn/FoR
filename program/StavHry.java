import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Zoskupuje stavy v ktorych sa Hra moze nachadzat pocas svojho trvania
 * 
 * @author y0hn
 * @version v0.6
 */
public enum StavHry {
    HRA(null, "", ""),
    VYHRA(new Color(33, 125, 174), "assets/victory.png", ""),
    PREHRA(Color.BLACK, "assets/death.png", ""),
    PAUZA(Color.BLACK, "assets/pause.png", "Hlavná Ponuka"),
    MENU(Color.BLACK, "assets/menu.png", "Ukonči Hru,Štart,Pokračuj");
    
    private JButton[] grafika;
    private Hashtable<String, Integer> indexiTlacidiel;
    
    StavHry(Color farbaPozadia, String cestaObrazu, String tlacitka) {
        this.grafika = new JButton[1];
        this.indexiTlacidiel = new Hashtable<>();
        
        // Vytvori tlacitka v grafike
        if (!tlacitka.equals("")) {        
            String[] s = tlacitka.split(",");
            this.grafika = new JButton[s.length + 1];

            final Vektor2D velkostMenuTlacitka = new Vektor2D(0.7, 0.15);
            final Font fonnTlacitka = new Font(Font.MONOSPACED, Font.BOLD, 50);
            final double posunTlacitokDole = 1.15;

            Vektor2D velkost = Hra.ROZMER_OKNA.getVelkost().roznasobenie(velkostMenuTlacitka);
            Vektor2D pozicia = Hra.ROZMER_OKNA.ziskajStred();
            pozicia = pozicia.roznasobenie(new Vektor2D(1, 2));
            pozicia = pozicia.rozdiel(new Vektor2D(velkost.getX() * 0.5, velkost.getY()));
            
            Rozmer2D rozmer = new Rozmer2D(pozicia, velkost);

            for (int i = 0; i < s.length; i++) {
                JButton t = this.vytvorTlacitko(false, "", null);
                t.setForeground(Color.WHITE);
                t.setFont(fonnTlacitka);
                t.setText(s[i]);
                
                Rozmer2D r = rozmer.kopia();
                Vektor2D posun = new Vektor2D(r.getPoziciaX(), r.getPoziciaY() - r.getVelkostY() * i * posunTlacitokDole);
                r.setPozicia(posun);
                t.setBounds(r.vytvorRectangle());
                
                this.grafika[i + 1] = t;
                this.indexiTlacidiel.put(s[i], i + 1);
            }
        }

        this.grafika[0] = this.vytvorTlacitko(true, cestaObrazu, farbaPozadia);
        this.grafika[0].setLayout(null);

        // Vlozi vsetky tlacitka do hlavnej grafiky
        for (int i = 1; i < this.grafika.length; i++) {
            this.grafika[0].add(this.grafika[i]);
        }
    }

    /**
     * Ziska grafiku podla poradoveho cisla
     * @param index - cislo v poradi [0] je rodicovsky
     * @return tlacidlo
     */
    public JButton getGrafika(int index) {
        return this.grafika[index];
    }
    /**
     * Ziska vsetky Grafiky vytvorene pre Stav Hry
     * @return Pole Tlacidiel [0] je rodicovsky
     */
    public JButton[] getVsetkyGrafiky() {
        return this.grafika;
    }
    /**
     * Najde index tlacidla podla textu napisanom na nom
     * @param text v tlacidle
     * @return index tlacidla
     */
    public int getIndexTlacidla(String text) {
        return this.indexiTlacidiel.get(text);
    }

    private JButton vytvorTlacitko(boolean vyplnene, String ikona, Color farba) {
        JButton tlacitko = new JButton();

        tlacitko.setFocusable(false);
        tlacitko.setContentAreaFilled(false);
        tlacitko.setRolloverEnabled(false);
        tlacitko.setBorderPainted(false);
        tlacitko.setFocusPainted(false);
        tlacitko.setOpaque(vyplnene);

        tlacitko.setBounds(Rozmer2D.ZERO.vytvorRectangle());

        if (!ikona.equals("")) {
            tlacitko.setIcon(new ImageIcon(ikona));
        }
        if (farba != null) {
            tlacitko.setBackground(farba);
        } 

        return tlacitko;
    }
}
