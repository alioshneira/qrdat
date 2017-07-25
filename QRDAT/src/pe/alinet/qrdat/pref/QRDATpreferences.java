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

package pe.alinet.qrdat.pref;

import java.io.File;
import java.util.prefs.Preferences;
/**
 *
 * @author Aliosh Neira Ramirez <aliosh2006 at gmail.com>
 */
public class QRDATpreferences {
    
    private static final String QRDAT_DIR="qrdat_dir";
    private static final String SHOW_LOG="show_log";
    
    Preferences prefs;
    
    public QRDATpreferences(){
        prefs = Preferences.userNodeForPackage(this.getClass());  
    }

    public String getQRDATdir() {
        return prefs.get(QRDAT_DIR, System.getProperty("user.home")+File.separator+"QRDAT");
    }

    public void setQRDATdir(String images_directory) {
        prefs.put(QRDAT_DIR, images_directory);
    }

    public Boolean getShowLog() {
        return prefs.getBoolean(SHOW_LOG, true);
    }

    public void setShowLog(Boolean show_log) {
        prefs.putBoolean(SHOW_LOG, show_log);
    }
    
}
