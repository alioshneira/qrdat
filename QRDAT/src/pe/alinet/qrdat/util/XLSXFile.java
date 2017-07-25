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

package pe.alinet.qrdat.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Aliosh Neira Ramirez <aliosh2006 at gmail.com>
 */
public class XLSXFile {

    XSSFWorkbook wb;
    int row = 0;
    int column = 0;
    XSSFSheet sheet;
    String filename;
    
    public XLSXFile(String filename){
        
        this.filename = filename;
        wb = open(filename);

        if (wb == null){
            wb = create(filename);
            if (wb == null){
                JOptionPane.showMessageDialog(null,"No se puede crear el archivo:"+filename);
            }       
        }
        else{
            sheet = wb.getSheetAt(0);
            row = sheet.getLastRowNum();
        }
            
    }
    
    
    public static XSSFWorkbook open(String filename){
        File f = new File(filename);
        XSSFWorkbook w = null;
        if (f.exists()){
            
            if (f.isFile()){
                if (f.canRead()){
                    try {
                        FileInputStream fis = new FileInputStream(filename);
                        w = new XSSFWorkbook(fis);
                        fis.close();

                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(XLSXFile.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(null,ex.getMessage());

                    } catch (IOException ex) {
                        Logger.getLogger(XLSXFile.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(null,ex.getMessage());
                    } catch (org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException ex){
                        Logger.getLogger(XLSXFile.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(null,ex.getMessage());
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null,"No se puede leer elarchivo"+f.getPath());
                }
            }
            else{
                JOptionPane.showMessageDialog(null,"No es un archivo"+f.getPath());
            }            
        }
        else {
            //JOptionPane.showMessageDialog(null,"No existe el archivo:"+f.getPath());
        }
        return w;
    }
    
    public XSSFWorkbook create(String filename){
        XSSFWorkbook wb = null;
        File f = new File(filename);
        
        if (!f.exists()){
            wb = new XSSFWorkbook();
            this.sheet = wb.createSheet();
            this.sheet.createRow(this.row);
            this.addHeaders();    
        }
        return wb;
    }
    
           
       
    public void append(String texto){
        XSSFRow r ;
        XSSFCell cell;
        
        r = this.sheet.getRow(this.row);
        cell = r.createCell(this.column);
        cell.setCellValue(texto);
   
        this.column++;
    }
    public void append(int numero){
        XSSFRow r ;
        XSSFCell cell;
        
        r = this.sheet.getRow(this.row);
        cell = r.createCell(this.column);
        cell.setCellValue(numero);
        
        this.column++;
    }
    
    public void setFormat(XSSFCell cell, String format){
        CreationHelper createHelper = this.wb.getCreationHelper();
        CellStyle style = this.wb.createCellStyle();
        style.setDataFormat(
            createHelper.createDataFormat().getFormat(format));
        
        cell.setCellStyle(style);
    }
    public void appendDate(Date date){
        XSSFRow r ;
        XSSFCell cell;
        
        r = this.sheet.getRow(this.row);
        cell = r.createCell(this.column);
        cell.setCellValue(date);
        
        CreationHelper createHelper = this.wb.getCreationHelper();
        CellStyle style = this.wb.createCellStyle();
        style.setDataFormat(
            createHelper.createDataFormat().getFormat("dd/mm/yyyy"));
        
        cell.setCellStyle(style);
        
        this.column++;
    }

    public void appendTime(Date date){
        XSSFRow r ;
        XSSFCell cell;
        
        r = this.sheet.getRow(this.row);
        cell = r.createCell(this.column);
        cell.setCellValue(date);
        
        CreationHelper createHelper = this.wb.getCreationHelper();
        CellStyle style = this.wb.createCellStyle();
        style.setDataFormat(
            createHelper.createDataFormat().getFormat("hh:mm:ss"));
        
        cell.setCellStyle(style);
        
        this.column++;
    }
    
    public final void nextRow(){
        this.row++;
        this.column=0;
        this.sheet.createRow(this.row);
        
    }
    
    private void addHeaders(){
        this.append("Fecha");
        this.append("Hora");
        this.append("Día");
        this.append("Mes");
        this.append("Año");
        this.append("Hora");
        this.append("Minuto");
        this.append("Segundo");
        this.append("ID");
    }
    
    public void save(){
       
        try {
            FileOutputStream fs = new FileOutputStream(this.filename);
            this.wb.write(fs);
            fs.close();
        }
        catch (java.io.FileNotFoundException ex){
            JOptionPane.showMessageDialog(null,ex.getMessage());
        }
        catch (IOException ex) {
            Logger.getLogger(XLSXFile.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }
    
    public static boolean saveFile(XSSFWorkbook wb,String filename){
        
        try {
            FileOutputStream fs = new FileOutputStream(filename);
            wb.write(fs);
            fs.close();
            
            return true;
        }
        catch (java.io.FileNotFoundException ex){
            JOptionPane.showMessageDialog(null,ex.getMessage());
            return false;
        }
        catch (IOException ex) {
            Logger.getLogger(XLSXFile.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } 
    }

    
}
