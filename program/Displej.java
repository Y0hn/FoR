import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

/**
 * Zobrazovac okna Hry
 * 
 * @author y0hn
 * @version v0.10
 */
public class Displej {
    private static final int POSUN_ROZMERU_X = 10;
    private static final int POSUN_ROZMERU_Y = 36;

    private static final int VRSTVA_PODLAHA = 0;
    private static final int VRSTVA_STENA = 1;
    public static final int VRSTVA_STRELA = 2;
    private static final int VRSTVA_HRAC = 3;
    private static final int VRSTVA_NEPRIATEL = 3;
    private static final int VRSTVA_UI = 5;


    private static Rozmer2D rozmer;
    /**
     * Vrati Rozmer2D Displeja
     * @return [0,0]x[vX,vY]
     */
    public static Rozmer2D getRozmer() {
        return Displej.rozmer;
    }
    /**
     * Vrati Vektor2D stred Displeja
     * @return [vX/2, vY/2]
     */
    public static Vektor2D getStred() {
        return Displej.rozmer.ziskajStred();
    }

    private JFrame okno;
    private JLayeredPane aktivnaMiestnost;

    /**
     * Vytvori okno Displeja 
     * @param nazovOkna zobrazovany v hlavicke okna
     * @param rozmer Vektor2D velkosti okna
     */
    public Displej(String ikonaOkna, String nazovOkna, Vektor2D rozmerOkna) {
        Displej.rozmer = new Rozmer2D(Vektor2D.ZERO, rozmerOkna);
        Vektor2D velkost = new Vektor2D(POSUN_ROZMERU_X, POSUN_ROZMERU_Y);
        velkost = velkost.sucet(Displej.rozmer.getVelkost());
        Rozmer2D skutocnyRozmer2D = new Rozmer2D(Displej.rozmer.getPozicia(), velkost);

        this.okno = new JFrame();

        // hlavicka okna
        if (!ikonaOkna.equals("")) {
            this.okno.setIconImage(new ImageIcon(ikonaOkna).getImage());
        }
        if (!nazovOkna.equals("")) {
            this.okno.setTitle(nazovOkna);
        }

        // nastavenie suradnic a velkost okna
        this.okno.setBounds(skutocnyRozmer2D.vytvorRectangle());
        this.okno.setResizable(false);
        
        // ukoncenie Hry pri zatvoreni okna
        this.okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.okno.setVisible(true);
    }
    /**
     * Vrati Hlavne Zobrazovacie Okno
     * @return hlave okno
     */
    public JFrame getOkno() {
        return this.okno;
    }
    /**
     * Vytvori Hracovi objekt na Displeji 
     * @param h objekt Hraca
     */
    public void nastavHraca(Hrac objektHraca) {
        JPanel grafika = new JPanel();
        this.aktivnaMiestnost.setLayer(grafika, VRSTVA_HRAC);
        this.aktivnaMiestnost.add(grafika);
        objektHraca.getTelo().setGrafika(grafika, Color.GREEN);
        
        JPanel[] zivotyUI = new JPanel[objektHraca.getTelo().getMaxZdravie()];
        Rozmer2D rozmerZivota = Hrac.GRAFIKA_ZIVOTOV_HRACA;
        for (int i = 0; i < zivotyUI.length; i++) {
            JPanel zivot = new JPanel();
            zivot.setBackground(Color.PINK);
            zivot.setBounds(rozmerZivota.vytvorRectangle());
            this.aktivnaMiestnost.setLayer(zivot, VRSTVA_UI);
            this.aktivnaMiestnost.add(zivot);
            zivotyUI[i] = zivot;

            Vektor2D novaPozica = new Vektor2D(rozmerZivota.getPoziciaX() + rozmerZivota.getIntVeX(), rozmerZivota.getPoziciaY());
            rozmerZivota.setPozicia(novaPozica);
        }
        objektHraca.setUI(zivotyUI);
    }
    /**
     * Zmeni Miestnost vykreslovanu na Displej
     * @param m nova aktivna Mistnost
     */
    public void zmenAktivnuMiestnost(Miestnost m, Hrac h) {
        if (this.aktivnaMiestnost != null) {
            this.okno.remove(this.aktivnaMiestnost);
        }
        this.aktivnaMiestnost = this.vytvorGrafikuMiestnosti(m);
        this.aktivnaMiestnost.setLayout(null);
        this.okno.setContentPane(this.aktivnaMiestnost);
        
        JPanel grafika = h.getTelo().getGrafika();
        if (null != grafika) {
            this.aktivnaMiestnost.setLayer(grafika, VRSTVA_HRAC);
            this.aktivnaMiestnost.add(grafika);

            JPanel[] ui = h.getUI();
            if (ui != null) {
                for (int i = 0; i < ui.length; i++) {
                    this.aktivnaMiestnost.setLayer(ui[i], VRSTVA_UI);
                    this.aktivnaMiestnost.add(ui[i]);
                }
            }
        }
    }
        
    private JLayeredPane vytvorGrafikuMiestnosti(Miestnost m) {
        JLayeredPane miestnost = new JLayeredPane();
        miestnost.setBounds(Displej.rozmer.vytvorRectangle());

        for (Rozmer2D[] rozmery : m.getRozmery2D()) {
            for (Rozmer2D r : rozmery) {
                JPanel stena = this.vytvorGrafikuMuru(r);
                miestnost.setLayer(stena, VRSTVA_STENA);
                miestnost.add(stena);
            }
        }

        for (Nepriatel nepriatel : m.getNepriatelia()) {
            JPanel grafika = new JPanel();
            miestnost.setLayer(grafika, VRSTVA_NEPRIATEL);
            miestnost.add(grafika);
            nepriatel.setGrafika(grafika);
        }

        JPanel podlaha = new JPanel();
        podlaha.setBackground(Color.GRAY); // docasne
        podlaha.setBounds(Displej.getRozmer().vytvorRectangle());
        podlaha.setLayout(new BorderLayout());
        miestnost.setLayer(podlaha, VRSTVA_PODLAHA);
        miestnost.add(podlaha);

        JLabel label = new JLabel();
        label.setText(m.getIndex() + "");
        label.setForeground(Color.DARK_GRAY);
        label.setLayout(new BorderLayout());
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);    
        label.setFont(new Font("Arial", 1, 100));    
        podlaha.add(label, BorderLayout.CENTER);


        return miestnost;
    }
    private JPanel vytvorGrafikuMuru(Rozmer2D rozmer) {
        JPanel stena = new JPanel();
        stena.setBounds(rozmer.vytvorRectangle());
        stena.setBackground(Color.BLACK);
        return stena;
    }
}
