package com.kp.malice.boundaries.locators;

import javax.inject.Inject;

import com.google.web.bindery.requestfactory.shared.Locator;
import com.kp.malice.entities.business.TitoloLloyds;
import com.kp.malice.repositories.TitoliRepository;

public class TitoloLocator<T> extends Locator<T, Long> {

    private final TitoliRepository repositoryTitoli;

    @Inject
    public TitoloLocator(TitoliRepository repositoryTitoli) {
        this.repositoryTitoli = repositoryTitoli;
    }

    @Override
    public T create(Class<? extends T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public T find(Class<? extends T> clazz, Long id) {
        try {
            return (T) repositoryTitoli.findTitolo(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Class<T> getDomainType() {
        return (Class<T>) TitoloLloyds.class;
    }

    @Override
    public Long getId(T domainObject) {
        TitoloLloyds c = (TitoloLloyds) domainObject;
        return c.getId();
    }

    @Override
    public Class<Long> getIdType() {
        return Long.class;
    }

    @Override
    public Object getVersion(T domainObject) {
        TitoloLloyds titolo = (TitoloLloyds) domainObject;
        return titolo.getVersion();
    }
}