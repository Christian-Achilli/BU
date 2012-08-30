package database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.google.inject.Inject;
import com.kp.malice.entities.business.AgenziaRMA;
import com.kp.malice.entities.business.PuntoVenditaRMAPerLloyds;

@SuppressWarnings("serial")
public class AuthForTest implements Authentication {

    private List<PuntoVenditaRMAPerLloyds> puntiVendita = new ArrayList<PuntoVenditaRMAPerLloyds>(2);

    @Inject
    public AuthForTest() {
        PuntoVenditaRMAPerLloyds pv1 = new PuntoVenditaRMAPerLloyds();
        PuntoVenditaRMAPerLloyds pv2 = new PuntoVenditaRMAPerLloyds();
        AgenziaRMA age = new AgenziaRMA();
        age.setOmcCode("176164");
        age.setDescription("FERRO E GAMBARDELLA S.R.L.");
        age.setId(25L);
        age.setEmail("christianachilli@kubepartners.com");
        pv1.setId(39L);
        pv1.setAgenzia(age);
        pv2.setId(40L);
        pv2.setAgenzia(age);
        getPuntiVendita().add(pv1);
        getPuntiVendita().add(pv2);
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isAuthenticated() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Object getPrincipal() {
        return "palermo.880@agenzie.realemutua.it";
    }

    @Override
    public Object getDetails() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getCredentials() {
        return "sole457";
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        return null;
    }

    public List<PuntoVenditaRMAPerLloyds> getPuntiVendita() {
        return puntiVendita;
    }

    public void setPuntiVendita(List<PuntoVenditaRMAPerLloyds> puntiVendita) {
        this.puntiVendita = puntiVendita;
    }
}
