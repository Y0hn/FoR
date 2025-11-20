import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Timer;

/**
 * Obnovuje Hru
 * 
 * @author y0hn (not totaly)
 * @version v0.1
 */
public class Casovac implements ActionListener {
    private Timer timer;
    private Hra hra;
    
    private long oldTick;    
    private static final long TICK_LENGTH = 100;   

    /**
     * Vytvori casovac
     *  
     * @param hra   
     */
    public Casovac(Hra hra) {
        this.hra = hra;
        
        this.timer = new javax.swing.Timer(25, null);        
        this.timer.addActionListener(this);      
        
        this.oldTick = 0;
        this.timer.start();
    }
    
    public void actionPerformed(ActionEvent event) {
        long newTick = System.nanoTime();
        if (Casovac.TICK_LENGTH <= newTick - this.oldTick  || newTick < Casovac.TICK_LENGTH) {
            this.oldTick = (newTick / Casovac.TICK_LENGTH) * Casovac.TICK_LENGTH;
            this.hra.tik(); 
        }
    }  
}