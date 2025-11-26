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
    private Timer casovac;
    private Hra hra;
    
    private long poslednyTik;    
    private static final long DLZKA_TIKU = 1000;   

    /**
     * Vytvori casovac
     *  
     * @param hra   
     */
    public Casovac(Hra hra) {
        this.hra = hra;
        
        this.casovac = new javax.swing.Timer(25, null);        
        this.casovac.addActionListener(this);      
        
        this.poslednyTik = 0;
        this.casovac.start();
    }
    /**
     * Volane ked sa aktualizuje cas v casovaci
     */
    public void actionPerformed(ActionEvent event) {
        long newTick = System.nanoTime();
        if (Casovac.DLZKA_TIKU <= newTick - this.poslednyTik  || newTick < Casovac.DLZKA_TIKU) {
            this.poslednyTik = (newTick / Casovac.DLZKA_TIKU) * Casovac.DLZKA_TIKU;
            this.hra.tik(); 
        }
    }  
}