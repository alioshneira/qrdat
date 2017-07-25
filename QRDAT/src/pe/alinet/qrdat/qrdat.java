package pe.alinet.qrdat;

import javax.swing.UIManager;
import pe.alinet.qrdat.scan.QRScannerFrame;

/**
 *
 * @author Aliosh Neira Ramirez <aliosh2006 at gmail.com>
 */
public class qrdat {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(QRScannerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QRScannerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QRScannerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QRScannerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        QRScannerFrame qrscanner = new QRScannerFrame();
        qrscanner.setVisible(true);
        
    }
    
}
