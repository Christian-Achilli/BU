package com.kp.malice.server;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.kp.malice.useCases.ScaricoEstrattiContoRiepilogoChiusura;
import com.kp.malice.useCases.ScaricoIncassiExcelGenerator;
import com.kp.malice.useCases.ScaricoTitoliExcelGenerator;

public class DownloadServlet extends HttpServlet {

    Logger log = Logger.getLogger(DownloadServlet.class);
    private final ScaricoTitoliExcelGenerator scaricoTitoliGenerator;
    private final ScaricoIncassiExcelGenerator scaricoIncassiGenerator;
    private final ScaricoEstrattiContoRiepilogoChiusura scaricoEstrattiConto;

    @Inject
    public DownloadServlet(ScaricoTitoliExcelGenerator creatoreExcel,
            ScaricoIncassiExcelGenerator scaricoIncassiGenerator,
            ScaricoEstrattiContoRiepilogoChiusura scaricoEstrattiConto) {
        this.scaricoTitoliGenerator = creatoreExcel;
        this.scaricoIncassiGenerator = scaricoIncassiGenerator;
        this.scaricoEstrattiConto = scaricoEstrattiConto;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        @SuppressWarnings("unchecked")
        Enumeration<String> parametriDellaRichiesta = req.getParameterNames();
        while (parametriDellaRichiesta.hasMoreElements()) {
            String parameterName = parametriDellaRichiesta.nextElement();
            String parameterValue = req.getParameter(parameterName);
            String[] parameters = parameterValue.split(","); //metto in un array di stringhe tutti i valori separati da virgola
            String[] parametriDiRicerca = new String[parameters.length - 2];
            log.debug("numero parametri passati:" + parameters.length);
            String nomeFile = null;
            int identificatoreTipoDownload = 0;

            for (int i = 0; i < parameters.length; i++) {
                String par = parameters[i];
                log.debug("Parametro:" + par);
                if (i == parameters.length - 1) // vado a leggere l'dentificatore
                    identificatoreTipoDownload = Integer.parseInt(par);
                else if (i == parameters.length - 2) // vado a leggere il nome file
                    nomeFile = par;
                else
                    parametriDiRicerca[i] = par;
            }

            try {
                switch (identificatoreTipoDownload) {
                case 1:
                    log.info("avvio DOWNLOAD " + parametriDiRicerca.length + " TITOLI");
                    doDownloadTitoli(req, resp, Arrays.asList(parametriDiRicerca), nomeFile);
                    break;

                case 2:
                    if (null != parametriDiRicerca) {
                        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        Date date = (Date) formatter.parse(parametriDiRicerca[0]);
                        log.info("avvio  DOWNLOAD INCASSI in data: " + parametriDiRicerca[0]);
                        doDownloadIncassi(req, resp, date, nomeFile);
                    }
                    break;

                case 3:
                    log.info("avvio  DOWNLOAD " + parametriDiRicerca.length + " ESTRATTI CONTO");
                    doDownloadEstrattiConto(req, resp, parametriDiRicerca[0], nomeFile);
                    break;

                default:
                    break;
                }
            } catch (Exception e) {
                log.error("Couldn't do the switch case");
                e.printStackTrace();
            }
        }
    }

    private void doDownloadEstrattiConto(HttpServletRequest reqest, HttpServletResponse response,
            String parametroDiRicerca, String nomeFile) throws Exception {
        setXLS(response, nomeFile);
        scaricoEstrattiConto.generaExcelSuOutputStream(parametroDiRicerca, response.getOutputStream()); //recuperare idList
        writeXLS(response);
    }

    private void doDownloadIncassi(HttpServletRequest request, HttpServletResponse response, Date date, String nomeFile)
            throws Exception {
        setXLS(response, nomeFile);
        scaricoIncassiGenerator.generaExcelSuOutputStream(date, response.getOutputStream()); //recuperare idList
        writeXLS(response);
    }

    private void doDownloadTitoli(HttpServletRequest request, HttpServletResponse response, List<String> idList,
            String nomeFile) throws Exception {
        log.debug("DownloadServlet.doDownloadTitoli");
        setXLS(response, nomeFile);
        scaricoTitoliGenerator.generaExcelSuOutputStream(idList, response.getOutputStream()); //recuperare idList
        writeXLS(response);
    }

    private void writeXLS(HttpServletResponse response) throws IOException {
        ServletOutputStream op = response.getOutputStream();
        op.flush();
        op.close();
    }

    private void setXLS(HttpServletResponse response, String nomeFile) {
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("Cp1252");
        response.addHeader("Content-Disposition", "attachment; filename=\"" + nomeFile + "\"");
    }
}