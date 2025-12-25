import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Timer;

/**
 * Obnovuje Hru
 * 
 * @author y0hn
 * @version v0.2
 */
public class Casovac implements ActionListener {
    private static final int ONESKORENIE_MS = 25;
    private static final long DLZKA_TIKU_MS = 0;  

    private final Timer casovac;
    private final Hra hra;    
    private long dalsiTik;
    private long poslednyTik;

    /**
     * Vytvori casovac pre Hru
     * @param hra
     */
    public Casovac(Hra hra) {
        this.hra = hra;
        
        this.casovac = new javax.swing.Timer(ONESKORENIE_MS, null);        
        this.casovac.addActionListener(this);      
        
        this.dalsiTik = 0;
        this.poslednyTik = 0;
        this.casovac.start();
    }

    /**
     * Volane ked sa aktualizuje cas v casovaci
     * @param event
     */
    public void actionPerformed(ActionEvent event) {
        long sucastnyCas = System.currentTimeMillis();

        if (this.dalsiTik <= sucastnyCas) {
            double deltaCasu = sucastnyCas - this.poslednyTik;
            deltaCasu /= ONESKORENIE_MS;
            this.hra.tik(deltaCasu); 

            this.poslednyTik = System.currentTimeMillis();
            this.dalsiTik = this.poslednyTik + DLZKA_TIKU_MS;
        }
    }  
}