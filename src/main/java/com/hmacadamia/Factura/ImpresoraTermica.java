package com.hmacadamia.Factura;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ImpresoraTermica {

    public void imprimir(String texto) {
        try {
            // Asegúrate de que el texto esté formateado adecuadamente
            InputStream is = new ByteArrayInputStream(texto.getBytes("UTF8"));
            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
            PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();

            if (printService.length == 0) {
                if (defaultService == null) {
                    System.err.println("No se encontraron impresoras.");
                    return;
                } else {
                    defaultService.createPrintJob().print(new SimpleDoc(is, flavor, null), null);
                }
            } else {
                PrintService service = ServiceUI.printDialog(null, 200, 200, printService, defaultService, flavor, pras);
                if (service != null) {
                    service.createPrintJob().print(new SimpleDoc(is, flavor, null), null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

