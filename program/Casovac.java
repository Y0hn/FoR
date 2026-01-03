import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Timer;

/**
 * Obnovuje Hru
 * 
 * @author y0hn
 * @version v0.3
 */
public class Casovac implements ActionListener {
    private static final int DLZKA_TIKU_MS = 25;
    private static Casovac instancia;

    private final Timer casovac;
    private final Hra hra;    
    private long dalsiTik;
    private long poslednyTik;

    /**
     * Vytvori casovac pre Hru
     * @param hra
     */
    public static Casovac spust(Hra hra) {
        if (Casovac.instancia == null) {
            Casovac.instancia = new Casovac(hra);
        }
        return Casovac.instancia;
    }
    
    private Casovac(Hra hra) {
        this.hra = hra;
        
        this.casovac = new javax.swing.Timer(DLZKA_TIKU_MS, null);        
        this.casovac.addActionListener(this);      
        
        this.dalsiTik = 0;
        this.poslednyTik = 0;
        this.casovac.start();
    }

    /**
     * Volane ked sa aktualizuje cas v casovaci
     * @param ae udalost
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        long sucastnyCas = System.currentTimeMillis();

        if (this.dalsiTik <= sucastnyCas) {
            double deltaCasu = sucastnyCas - this.poslednyTik;
            deltaCasu /= DLZKA_TIKU_MS;
            this.hra.tik(deltaCasu); 

            this.poslednyTik = System.currentTimeMillis();
            this.dalsiTik = this.poslednyTik;
        }
    }  
}