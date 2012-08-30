package com.kp.marsh.ebt.client.webapp.gin;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Singleton;
import com.kp.marsh.ebt.client.webapp.mvp.AuthenticationActivityMapper;
import com.kp.marsh.ebt.client.webapp.mvp.GaugesActivityMapper;
import com.kp.marsh.ebt.client.webapp.mvp.HeaderActivityMapper;
import com.kp.marsh.ebt.client.webapp.mvp.HistogramActivityMapper;
import com.kp.marsh.ebt.client.webapp.mvp.PillActivityMapper;
import com.kp.marsh.ebt.client.webapp.ui.activityintf.IGaugesViewDisplay;
import com.kp.marsh.ebt.client.webapp.ui.activityintf.IHeaderBarDisplay;
import com.kp.marsh.ebt.client.webapp.ui.activityintf.IHistogramsViewDisplay;
import com.kp.marsh.ebt.client.webapp.ui.activityintf.IInsertPotentialViewDisplay;
import com.kp.marsh.ebt.client.webapp.ui.activityintf.ILoginViewDisplay;
import com.kp.marsh.ebt.client.webapp.ui.activityintf.ILogoutViewDisplay;
import com.kp.marsh.ebt.client.webapp.ui.views.InsertPotentialViewImpl;
import com.kp.marsh.ebt.client.webapp.ui.views.LoginViewImpl;
import com.kp.marsh.ebt.client.webapp.ui.views.LogoutViewImpl;
import com.kp.marsh.ebt.client.webapp.ui.views.manager.GaugesViewImpl;
import com.kp.marsh.ebt.client.webapp.ui.views.manager.HeaderBarViewImpl;
import com.kp.marsh.ebt.client.webapp.ui.views.manager.HistogramsViewImpl;

public class ManagerWidgetsClientModule extends AbstractGinModule {

    @Override
    protected void configure() {

        bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);

        bind(PlaceController.class).to(InjectablePlaceController.class).in(Singleton.class);

        bind(HeaderActivityMapper.class).in(Singleton.class);

        bind(GaugesActivityMapper.class).in(Singleton.class);

        bind(HistogramActivityMapper.class).in(Singleton.class);

        bind(AuthenticationActivityMapper.class).in(Singleton.class);

        bind(PillActivityMapper.class).in(Singleton.class);

        // GESTIONE DELLE VIEW COME SINGLETON, aggiornate coerentemente dalle activity che le prendono in carico

        bind(ILoginViewDisplay.class).to(LoginViewImpl.class).in(Singleton.class);

        bind(IHeaderBarDisplay.class).to(HeaderBarViewImpl.class).in(Singleton.class);

        bind(IGaugesViewDisplay.class).to(GaugesViewImpl.class).in(Singleton.class);

        bind(IHistogramsViewDisplay.class).to(HistogramsViewImpl.class).in(Singleton.class);

        bind(ILogoutViewDisplay.class).to(LogoutViewImpl.class).in(Singleton.class);

        bind(IInsertPotentialViewDisplay.class).to(InsertPotentialViewImpl.class);

    }

}
