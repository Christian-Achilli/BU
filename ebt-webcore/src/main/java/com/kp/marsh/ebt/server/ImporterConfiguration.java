package com.kp.marsh.ebt.server;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.kp.marsh.ebt.server.importer.csvimport.Achieved;
import com.kp.marsh.ebt.server.importer.csvimport.AnagraficheCE;
import com.kp.marsh.ebt.server.importer.csvimport.GruppiCommerciali;
import com.kp.marsh.ebt.server.importer.csvimport.PzMapAchievedConfigProvider;
import com.kp.marsh.ebt.server.importer.csvimport.PzMapAnagraficheConfigProvider;
import com.kp.marsh.ebt.server.importer.csvimport.PzMapGruppiCommercialiConfigProvider;
import com.kp.marsh.ebt.server.importer.csvimport.importers.AchievedImporter;
import com.kp.marsh.ebt.server.importer.csvimport.importers.AnagCeImporter;
import com.kp.marsh.ebt.server.importer.csvimport.importers.CsvImporter;
import com.kp.marsh.ebt.server.importer.csvimport.importers.GruppiCommercialiImporter;
import com.kp.marsh.ebt.server.importer.dao.ImportController;
import com.kp.marsh.ebt.server.importer.dao.impl.ImportControllerImpl;
import com.kp.marsh.ebt.server.importer.util.IReadProperties;
import com.kp.marsh.ebt.server.importer.util.ReadPropertiesAchieved;
import com.kp.marsh.ebt.server.importer.util.ReadPropertiesAnagraficaCE;
import com.kp.marsh.ebt.server.webapp.data.domain.dao.CalculationService;
import com.kp.marsh.ebt.server.webapp.data.domain.dao.DomainDrillerService;
import com.kp.marsh.ebt.server.webapp.data.domain.dao.impl.CalculationServiceImpl;
import com.kp.marsh.ebt.server.webapp.data.domain.dao.impl.DomainDrillerServiceImpl;
import com.kp.marsh.ebt.server.webapp.utils.AnnotatedHibernateUtil;

/**
 * Configuration-class for guice. Here are the bindings defined.
 *
 */
public class ImporterConfiguration extends AbstractModule {

    @Override
    protected void configure() {
        bind(CsvImporter.class).annotatedWith(Achieved.class).to(AchievedImporter.class);
        bind(CsvImporter.class).annotatedWith(AnagraficheCE.class).to(AnagCeImporter.class);
        bind(CsvImporter.class).annotatedWith(GruppiCommerciali.class).to(GruppiCommercialiImporter.class);
        bind(IReadProperties.class).annotatedWith(Achieved.class).to(ReadPropertiesAchieved.class);
        bind(IReadProperties.class).annotatedWith(AnagraficheCE.class).to(ReadPropertiesAnagraficaCE.class);
        bind(String.class).annotatedWith(Achieved.class).toProvider(PzMapAchievedConfigProvider.class);
        bind(String.class).annotatedWith(AnagraficheCE.class).toProvider(PzMapAnagraficheConfigProvider.class);
        bind(String.class).annotatedWith(GruppiCommerciali.class)
                .toProvider(PzMapGruppiCommercialiConfigProvider.class);
    }
}
