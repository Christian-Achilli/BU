package com.kp.marsh.ebt.client.webapp.ui.activityintf;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.IsWidget;
import com.kp.marsh.ebt.shared.dto.SintesiDto;

public interface IGaugesViewDisplay extends IsWidget {

    // Aggiorna l'interfaccia con i dati contenuti nei dto
    void updateData(ArrayList<SintesiDto> dtoList);

    void setStatusBar(String ufficioSelectedDescription, String ceSelectedDescription, String gcSelectedDescription);

    /**
     * Tells the gauges whether or not show the loader 
     * @param show
     */
    void showLoaders(boolean show);

    /**
     * In fase di inizializzazione, nasconde il panel prima che sia pronto con i dati. Serve per evitare il flickering dovuto alla gestione del loader
     */
    void initView();

    void setLobNames(String[] lobNames);
}