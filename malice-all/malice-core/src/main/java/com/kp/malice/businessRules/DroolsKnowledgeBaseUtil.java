package com.kp.malice.businessRules;

import org.apache.log4j.Logger;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.RuntimeDroolsException;
import org.drools.builder.DecisionTableConfiguration;
import org.drools.builder.DecisionTableInputType;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.runtime.StatelessKnowledgeSession;

import com.kp.malice.MalicePropertyContainer;

public class DroolsKnowledgeBaseUtil {
    private static Logger log = Logger.getLogger(DroolsKnowledgeBaseUtil.class);
    private static final ThreadLocal<StatelessKnowledgeSession> threadSession = new ThreadLocal<StatelessKnowledgeSession>();
    private static final ThreadLocal<KnowledgeRuntimeLogger> threadLogger = new ThreadLocal<KnowledgeRuntimeLogger>();
    private static KnowledgeBase kbase;

    public static final FunzioniAbilitate getFunzioniDisponibiliPerTitolo(TitoloSnapshot stati) {
        StatelessKnowledgeSession ksession = null;
        try {
            ksession = getStatefulKnowledgeSession();
            FunzioniAbilitate funzioniAbilitate = new FunzioniAbilitate();
            ksession.setGlobal("funAb", funzioniAbilitate);
            ksession.execute(stati);
            return funzioniAbilitate;
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RuntimeDroolsException("ECCEZIONE IN getFunzioniAbilitate", t);
        } finally {
            try {
                closeStatelessKnowledgeSession();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeDroolsException("ECCEZIONE IN closeStatelessKnowledgeSession", e);
            }
        }
    }

    static {
        try {
            kbase = readKnowledgeBase();
        } catch (Exception e) {
            log.error("ERRORE IN CREAZIONE DROOLS FACTORY", e);
            e.printStackTrace();
        }
    }

    private static StatelessKnowledgeSession getStatefulKnowledgeSession() throws RuntimeException {
        StatelessKnowledgeSession s = threadSession.get();

        try {
            if (s == null) {
                s = kbase.newStatelessKnowledgeSession();
                threadSession.set(s);
            }
        } catch (Exception ex) {
            throw new RuntimeException("ECCEZIONE IN APERTURA SESSIONE STATEFULL DI DROOLS", ex);
        }
        return s;
    }

    private static void closeStatelessKnowledgeSession() throws Exception {
        threadSession.set(null);
        threadLogger.set(null);
    }

    private static KnowledgeBase readKnowledgeBase() throws Exception {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        DecisionTableConfiguration config = KnowledgeBuilderFactory.newDecisionTableConfiguration();
        config.setInputType(DecisionTableInputType.XLS);
        kbuilder.add(ResourceFactory.newClassPathResource(MalicePropertyContainer.getDroolsConfigurationFileName()),
                ResourceType.DTABLE, config);
        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error : errors) {
                System.err.println(error);
            }
            throw new IllegalArgumentException("Could not parse knowledge.");
        }
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
        return kbase;
    }
}
