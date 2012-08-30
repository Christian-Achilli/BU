package com.kp.malice.factories;

import com.google.inject.Inject;
import com.kp.malice.entities.business.AgenziaRMA;
import com.kp.malice.entities.business.ChiusuraMensileLio;
import com.kp.malice.entities.business.EstrattoContoLio;
import com.kp.malice.entities.business.LioReferenceCode;
import com.kp.malice.entities.persisted.ChrMslLio;
import com.kp.malice.entities.persisted.EstrContLio;
import com.kp.malice.entities.persisted.HibernateSessionFactoryUtil;
import com.kp.malice.entities.persisted.PunVnd;
import com.kp.malice.entities.persisted.RecordIdentifier;
import com.kp.malice.entities.persisted.UntaOperAun;

public class ChiusuraMensileLioFactory {

    private final FilieraLloydsFactory filieraFactory;

    @Inject
    public ChiusuraMensileLioFactory(FilieraLloydsFactory filieraFactory) {
        this.filieraFactory = filieraFactory;
    }

    public ChiusuraMensileLio create(AgenziaRMA age, int year, int month) {
        ChiusuraMensileLio newChiusura = new ChiusuraMensileLio();
        newChiusura.setReferenceCalendarMonth(month);
        newChiusura.setReferenceYear(year);
        newChiusura.setAgenzia(age);
        return newChiusura;
    }

    public ChiusuraMensileLio hidrateChiusuraMensile(ChrMslLio chr) throws Exception {
        ChiusuraMensileLio chrMensileLio = new ChiusuraMensileLio();
        chrMensileLio.setId(chr.getRecordId());
        chrMensileLio.setStatoChiusura(chr.getStatoChiusura());
        chrMensileLio.setDtInvio(chr.getDtInvio());
        chrMensileLio.setReferenceCalendarMonth(chr.getMeseDiRiferimento());
        chrMensileLio.setReferenceYear(chr.getAnnoDiRiferimento());
        chrMensileLio.setTotEstrattiConto(chr.getEstrattiConto().size());
        chrMensileLio.setTotCommissioniEuroCent(chr.getTotCommissioni());
        chrMensileLio.setTotPremiEuroCent(chr.getTotPremi());
        chrMensileLio.setTotTitoli(chr.getNumTitoli());
        return chrMensileLio;
    }

    public ChrMslLio getNotPersistedObject(ChiusuraMensileLio meseX) throws Exception {
        ChrMslLio chr = new ChrMslLio(RecordIdentifier.getInitRecord());
        chr.setAnnoDiRiferimento(meseX.getReferenceYear());
        chr.setMeseDiRiferimento(meseX.getReferenceCalendarMonth());
        chr.setStatoChiusura(meseX.getStatoChiusura());
        UntaOperAun uoa = (UntaOperAun) HibernateSessionFactoryUtil.getSession().load(UntaOperAun.class,
                meseX.getAgenzia().getId());
        chr.setUntaOperAun(uoa);
        chr.setNumEstrattiConto(meseX.getTotEstrattiConto());
        chr.setTotCommissioni(meseX.getTotCommissioniEuroCent());
        chr.setTotPremi(meseX.getTotPremiEuroCent());
        chr.setDtInvio(meseX.getDtInvio());
        chr.setNumTitoli(meseX.getTotTitoli());
        return chr;
    }

    public EstrContLio getNotPersistedObject(PunVnd pv, ChrMslLio chr, LioReferenceCode lrc) throws Exception {
        EstrContLio ec = new EstrContLio(RecordIdentifier.getInitRecord());
        ec.setChiusura(chr);
        ec.setLioReferenceCodeString(lrc.getCodice());
        ec.setPunVndDelegatoIncasso(pv);
        return ec;
    }

    public EstrattoContoLio hidrateEstrattoContoLio(EstrContLio ecl) {
        EstrattoContoLio ec = new EstrattoContoLio();
        ec.setId(ecl.getRecordId());
        ec.setLabel(filieraFactory.getLioReferenceCode(ecl.getLioReferenceCodeString()).getReferente());
        ec.setTotCommissioniEuroCent(ecl.getTotCommissioni());
        ec.setTotPremiEuroCent(ecl.getTotPremi());
        ec.setTotRimessaEuroCent(ecl.getTotRimesse());
        ec.setTotTitoli(ecl.getTitoliEstratto().size());
        return ec;
    }
}
