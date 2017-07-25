package pe.alinet.qrdat.scan;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import pe.alinet.qrdat.pref.QRDATpreferences;
import static java.nio.file.StandardCopyOption.*;
import pe.alinet.qrdat.util.XLSXFile;

/*
 * Copyright (C) 2017 Aliosh Neira Ramirez <aliosh2006 at gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 *
 * @author Aliosh Neira Ramirez <aliosh2006 at gmail.com>
 */
public class QRScanner {
    
    QRDATpreferences prefs = new QRDATpreferences();
    String qrdatdir = prefs.getQRDATdir();
    
    String qrcodesFile = qrdatdir + File.separator + "Users.xlsx";
    String recordFile = qrdatdir + File.separator + "Records.xlsx";
    
    
    //UserList users = new UserList(usersFile);
    
    DefaultListModel model = new DefaultListModel();
    Calendar calendar = Calendar.getInstance();
    Date datetime;
    SimpleDateFormat sdfTime;
    SimpleDateFormat sdfDate;
    
    String scanText = null;
    QRcodes qrcodes; 
    XSSFRow row;
    
    public QRScanner(){
        
        //create qrdat directory if not exists
        
        File d = new File(qrdatdir);
        if (!d.exists()){
            d.mkdir();
        }
        
        qrcodes = new QRcodes(qrcodesFile);
        //users.readFile();
    }
    
    public boolean readID(String id){
        
        String desc = null;
        String time = getTime("HH:mm:ss");
        Boolean r = false;
        //desc = xls.getDescByID(id);
        XSSFRow row = qrcodes.getRowByCode(id);
        
        if (row != null){
            DataFormatter formatter = new DataFormatter();

            // establecer el maximo la tercera columna
            int maxCells = row.getLastCellNum();
            
            if (maxCells > 6){
                maxCells = 6;
            }
            
            for (int j=0; j< maxCells; j++){ 
                XSSFCell cell = row.getCell(j);
                
                if (j == 0 ) {
                    desc = formatter.formatCellValue(cell);;
                }
                else if (j == 1 ) {
                    desc = desc + " - " + formatter.formatCellValue(cell);;
                }
                else 
                {
                    desc = desc + " " + formatter.formatCellValue(cell);
                }
            }
            scanText = time + " - " + desc;
            appendText(scanText);
            r = appendFile(row);
        }
        else{
            scanText = time + " - " + id +  " - ¡Código desconocido "
                    + "fue registrado en la base de datos!";
            appendText(scanText);
            appendFile(id);
        }
      
        return r;
    }
    public String getText(){
        return scanText;
    }
    public void appendText(String text){
        model.add(0, text);
    }
    
    public String getTime(String format){
        sdfTime = new SimpleDateFormat(format);
        return sdfTime.format(datetime);
    }
    
    public void setTime(Date dt){
        datetime = dt;
        calendar.setTime(dt);
    }
    
    public boolean appendFile(XSSFRow row){
        
        boolean r = false;
        
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH)+1;
        int año = calendar.get(Calendar.YEAR);

        int hora = calendar.get(Calendar.HOUR_OF_DAY);
        int minuto = calendar.get(Calendar.MINUTE);
        int segundo = calendar.get(Calendar.SECOND);

        XLSXFile xlsx = new XLSXFile(recordFile);

        xlsx.nextRow();
        xlsx.appendDate(datetime);
        xlsx.appendTime(datetime);
        xlsx.append(dia);
        xlsx.append(mes);
        xlsx.append(año);
        xlsx.append(hora);
        xlsx.append(minuto);
        xlsx.append(segundo);
        
        DataFormatter formatter = new DataFormatter();
        String text; 

        for (int j=0; j< row.getLastCellNum();j++){ 
            XSSFCell cell = row.getCell(j);
            text = formatter.formatCellValue(cell);
            xlsx.append(text);
        }
  
        xlsx.save();
        r = true;        
        return r;
    }
    public boolean appendFile(String id){
        
        boolean r = false;
        
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH)+1;
        int año = calendar.get(Calendar.YEAR);

        int hora = calendar.get(Calendar.HOUR_OF_DAY);
        int minuto = calendar.get(Calendar.MINUTE);
        int segundo = calendar.get(Calendar.SECOND);

        XLSXFile xlsx = new XLSXFile(recordFile);

        xlsx.nextRow();
        xlsx.appendDate(datetime);
        xlsx.appendTime(datetime);
        xlsx.append(dia);
        xlsx.append(mes);
        xlsx.append(año);
        xlsx.append(hora);
        xlsx.append(minuto);
        xlsx.append(segundo);
        xlsx.append(id);
        
  
        xlsx.save();
        r = true;        
        return r;
    }
    
    
    
    public void saveImage(BufferedImage image, String text){
        
        String filename;
        
        if (qrdatdir.equals("")){
            filename = getTime("yyyy-MM-dd-HH-mm-ss")+"_"+text+".png";
        } else{
            File d = new File(qrdatdir +
                    File.separator + getTime("yyyyMMdd"));
            
            if (!d.exists()){
                d.mkdir();
            }
            
            filename = qrdatdir + File.separator + getTime("yyyyMMdd")+
                        File.separator + getTime("yyyyMMdd-HHmmss")+
                        "_"+text+".png";
        }
        
        try {
            ImageIO.write(image, "PNG", new File(filename));
        } catch (IOException ex) {
            Logger.getLogger(QRScanner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String scanImage(BufferedImage image){
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result = null;
        String text = null;
        try {
            result = new MultiFormatReader().decode(bitmap);
            text = result.getText();
            
        } catch (NotFoundException e) {
                // fall thru, it means there is no QR code in image
            //Logger.getLogger(QRScannerFrame.class.getName()).log(Level.FINEST, null, e);
        }
        return text;
    }
    
    public String processImage(BufferedImage image){

        String text = null;
        text = scanImage(image);
        
        if (text != null && !text.contains("/") && !text.contains(":")){
            
            if (readID(text)){
                beep();
                saveImage(image,text);
            }
        }
        
        return text;
    }
    
    public void beep(){
        Toolkit.getDefaultToolkit().beep();
    }

    public void importDialog(JFrame parent) {
        
        ImportQRcodesFrame importer = new ImportQRcodesFrame(parent,true);
        String filename = importer.showDialog();
        
        if(filename != null) {
            try {
                Files.copy(Paths.get(filename),Paths.get(qrcodesFile),REPLACE_EXISTING);
            } catch (IOException ex) {
                Logger.getLogger(QRScanner.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    } 

}
