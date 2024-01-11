package moviebookingapp.report;

import java.io.InputStream;
import java.sql.Connection;
import java.util.Map;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class Report {

    private static JasperReport jreport;
    private static JasperViewer jviewer;
    private static JasperPrint jprint;

    public static void createReport(Connection connect, Map<String, Object> map, InputStream by) {
        try {
            jreport = (JasperReport)JRLoader.loadObjectFromFile("/moviebookingapp/asset/Invoice_3.jasper");
            jprint = JasperFillManager.fillReport(jreport, map, connect);

        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void showReport() {
        jviewer = new JasperViewer(jprint, false); // false to avoid closing the main application and will only close the print preview
        jviewer.setVisible(true);
    }


}
