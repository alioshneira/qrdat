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

package pe.alinet.qrdat.scan;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 *
 * @author Aliosh Neira Ramirez <aliosh2006 at gmail.com>
 */
public class QRcodes {
    
    String filename;
    
    
    public QRcodes(String filename){
        this.filename = filename;
        
    }
    
    public XSSFRow getRowByCode(String qrcode){
        
        XSSFWorkbook wb = open(filename);        
        XSSFSheet sheet;
        XSSFRow row;
        DataFormatter formatter = new DataFormatter();

        System.out.println(filename);
        sheet = wb.getSheetAt(0); //ir a la primera hoja
        int numRows = sheet.getLastRowNum();
        
        for (int i = 0; i <numRows; i++){
            XSSFCell cell = sheet.getRow(i).getCell(0);
            String text = formatter.formatCellValue(cell);
            
            if (text.equals(qrcode)){
                row = sheet.getRow(i);
                return row;
            }              
        }
       
        return null;
    }
    
    public XSSFWorkbook open(String filename){
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
                        JOptionPane.showMessageDialog(null,ex.getMessage());

                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null,ex.getMessage());
                    } catch (org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException ex){
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
    
    public static void main(String args[]){
        QRcodes qrcodes = new QRcodes("C:\\Users\\Aliosh\\Documents\\QRDAT\\ASISTENCIA\\Users.xlsx");
        XSSFRow row =  qrcodes.getRowByCode("xx");
        
        if (row != null){
            DataFormatter formatter = new DataFormatter();

            for (int j=0; j<row.getLastCellNum();j++){
                XSSFCell cell = row.getCell(j);
                String text = formatter.formatCellValue(cell);
                System.out.println(text);
            }   
        }
        else{
            System.out.println("No se encontró");
        }
        
    }
}
