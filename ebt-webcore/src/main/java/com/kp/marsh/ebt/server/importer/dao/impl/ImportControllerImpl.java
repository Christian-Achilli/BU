package com.kp.marsh.ebt.server.importer.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.google.inject.Inject;
import com.kp.marsh.ebt.server.importer.csvimport.exception.ImportException;
import com.kp.marsh.ebt.server.importer.dao.ImportController;
import com.kp.marsh.ebt.server.importer.dto.MarshCE;
import com.kp.marsh.ebt.server.importer.dto.MarshGruppoCommerciale;
import com.kp.marsh.ebt.server.importer.dto.MarshOffice;
import com.kp.marsh.ebt.server.importer.dto.MarshProduct;
import com.kp.marsh.ebt.server.importer.model.MAchieved;
import com.kp.marsh.ebt.server.importer.model.MActuals;
import com.kp.marsh.ebt.server.importer.model.MEbtMapping;
import com.kp.marsh.ebt.server.importer.model.MOffCeAccounts;
import com.kp.marsh.ebt.server.webapp.data.domain.BusinessInformation;
import com.kp.marsh.ebt.server.webapp.data.domain.InformationOwners;
import com.kp.marsh.ebt.server.webapp.data.domain.MarshPeopleCredentials;
import com.kp.marsh.ebt.server.webapp.data.domain.Products;
import com.kp.marsh.ebt.server.webapp.data.domain.ReferenceYears;
import com.kp.marsh.ebt.server.webapp.utils.AnnotatedHibernateUtil;
import com.kp.marsh.ebt.shared.BusinessInfoValueType;



public class ImportControllerImpl implements ImportController {


	private AnnotatedHibernateUtil connection;

	private static Logger  log = Logger.getLogger(ImportControllerImpl.class);

	@Inject
	public ImportControllerImpl(AnnotatedHibernateUtil connection) {
		this.connection = connection;
	}
	
	@Override
	public List<MOffCeAccounts> listAnagCE() {
		List<MOffCeAccounts> offCeAccounts = null;
		Session session = (Session) this.connection.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			Query query = session.createQuery("from MOffCeAccounts");
			offCeAccounts = query.list();
			tx.commit();
			log.debug("Trovati "+offCeAccounts.size()+" MOffCeAccounts");
		} catch (HibernateException ex) {
			log.error("Exception in listAnagCE", ex);
			ex.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
		return offCeAccounts;
	}
	
	public List<MarshGruppoCommerciale> listGruppiComercialiWithAchieved() {
		List<MarshGruppoCommerciale> result = new ArrayList<MarshGruppoCommerciale>();
		Session session = (Session) this.connection.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			Query query = session.createQuery("from MAchieved where netRevenues > 0"); //where id.ebtProdotto <> '' and id.ebtProdotto <> 'Out of scope'
			List<MAchieved> collections = query.list();
			
			Map<String, MarshGruppoCommerciale> grupCommMap = new HashMap<String, MarshGruppoCommerciale>(); // mappa dei Gruppi commerciali recuperati da MAchieved con chiave il proprio codice eys
			for (MAchieved mAchieved : collections) { // Per tutti i record della tabella derivata dal file PRODUZIONE (Estrattre ebt) che Marsh ci invia
				
//				//GESTIONE DEL CLIENT EXECUTIVE
//				// check if the CE already exists
//				MarshCE clientExec = ceMap.get(mAchieved.getIdAeCgruppo());
//				if(null == clientExec) {
//					clientExec = new MarshCE();
//					ceMap.put(mAchieved.getIdAeCgruppo(), clientExec);
//					clientExec.setEysCode(mAchieved.getIdAeCgruppo());
//					clientExec.setNominativo(mAchieved.getAeCgruppo());
//					// clientExec.setUsername(username) qui non ce l'ho e comunque in information owners non serve
//					result.add(clientExec);
//				}
	
				MarshGruppoCommerciale gruppoCommerciale = grupCommMap.get(mAchieved.getCodiceCapogruppo());
				if(null == gruppoCommerciale) {
					gruppoCommerciale = new MarshGruppoCommerciale();
					grupCommMap.put(mAchieved.getCodiceCapogruppo(), gruppoCommerciale);
					gruppoCommerciale.setCodiceCapoGruppo(mAchieved.getCodiceCapogruppo());
					gruppoCommerciale.setDenominazione(mAchieved.getCapogruppo());
					result.add(gruppoCommerciale);
				}
				
				
				// GESTIONE DEL PRODOTTO
				// trovo il codice prodotto usato in ebt relativo al codice eurosys indicato nel record di M_achieved
				Query queryProduct = session.createQuery("from MEbtMapping where eysCode = '"+mAchieved.getCodGaranzia()+"'"); 
				MEbtMapping lobProduct = (MEbtMapping) queryProduct.uniqueResult();
				if(lobProduct==null){
					log.debug("ATTENTION: eysCode "+mAchieved.getCodGaranzia()+" not found in MLobProduct");
					continue;
				}
				String ebtProduct = lobProduct.getProdotto(); // nome del prodotto ebt
				MarshProduct product = gruppoCommerciale.getProductMap().get(ebtProduct);
				if(product == null ) {
					product = new MarshProduct();
					gruppoCommerciale.getProductMap().put(ebtProduct,product);
					product.setName(ebtProduct);
					product.setYear(mAchieved.getAnnoContabile());
				}
				if(mAchieved.getNetRevenues() > 0)
					product.setAchieved(product.getAchieved()+mAchieved.getNetRevenues().longValue());
				
			}
	
			tx.commit();
		} catch (HibernateException ex) {
			log.error("Exception in listClientExecutives", ex);
			ex.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see com.kp.marsh.ebt.server.importer.dao.impl.ImportController#listClientExecutivesWithFinalBalance()
	 */
	@Override
	public List<MarshCE> listClientExecutivesWithFinalBalance() {
		List<MarshCE> result = new ArrayList<MarshCE>();
		Session session = (Session) this.connection.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			Query query = session.createQuery("from MActuals"); //where id.ebtProdotto <> '' and id.ebtProdotto <> 'Out of scope'
			List<MActuals> collections = query.list();
			Map<String, MarshCE> ceMap = new HashMap<String, MarshCE>(); // mappa dei CE con chiave il proprio codice eys
			for (MActuals mActual : collections) { // Per tutti i record della tabella derivata dal file PRODUZIONE che marsh ci invia
				// check if the CE already exists
				MarshCE clientExec = ceMap.get(mActual.getIdAeCgruppo());
				if(null == clientExec) {
					clientExec = new MarshCE();
					ceMap.put(mActual.getIdAeCgruppo(), clientExec);
					clientExec.setEysCode(mActual.getIdAeCgruppo());
					clientExec.setNominativo(mActual.getAeCgruppo());
					// clientExec.setUsername(username) qui non ce l'ho e comunque in information owners non serve

					result.add(clientExec);
				}

				MarshGruppoCommerciale mGC = clientExec.getGruppoCommercialeMap().get(mActual.getCodiceCapogruppo());
				
				if(null == mGC) {
					mGC = new MarshGruppoCommerciale();
					clientExec.getGruppoCommercialeMap().put(mActual.getCodiceCapogruppo(), mGC);
					mGC.setCodiceCapoGruppo(mActual.getCodiceCapogruppo());
					mGC.setDenominazione(mActual.getCapogruppo());
				}
				
				Query queryProduct = session.createQuery("from MEbtMapping where eysCode = '"+mActual.getCodGaranzia()+"'"); 
				MEbtMapping lobProduct = (MEbtMapping) queryProduct.uniqueResult();
				if(lobProduct==null){
					log.debug("ATTENTION: eysCode not found in MLobProduct");
					continue;
				}
				String ebtProduct = lobProduct.getProdotto(); // nome del prodotto ebt
				
				MarshProduct product = mGC.getProductMap().get(ebtProduct);
				if(product == null ) {
					product = new MarshProduct();
					mGC.getProductMap().put(ebtProduct,product);
					product.setName(ebtProduct);
					product.setYear(mActual.getAnnoContabile());
				}
				if(mActual.getNetRevenues() > 0)
					product.setActual(product.getActual()+mActual.getNetRevenues().longValue());

			}

			tx.commit();
		} catch (HibernateException ex) {
			log.error("Exception in listClientExecutives", ex);
			ex.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}

		return result;

	}


	/* (non-Javadoc)
	 * @see com.kp.marsh.ebt.server.importer.dao.impl.ImportController#listOffices()
	 */
	@Override
	public List<MarshOffice> listOffices() {
		List<MarshOffice> result = new ArrayList<MarshOffice>();
		Session session = (Session) this.connection.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			Query query = session.createQuery("from MOffCeAccounts");

			List<MOffCeAccounts> collections = query.list();

			Map<String, MarshOffice> officesMap = new HashMap<String, MarshOffice>();


			for (MOffCeAccounts mOffCeAccounts : collections) {

				// check if the office already exists
				MarshOffice office = officesMap.get(mOffCeAccounts.getCodiceUfficio());
				if(null == office || StringUtils.isEmpty(mOffCeAccounts.getCodiceUfficio())) {
					office = new MarshOffice();
					officesMap.put(mOffCeAccounts.getCodiceUfficio(), office);
					office.setEysCode(mOffCeAccounts.getCodiceUfficio());
					office.setOfficeName(mOffCeAccounts.getNomeUfficio());
					result.add(office);
					if(StringUtils.isEmpty(office.getEysCode()))
						log.error(office.getOfficeName()+" eys code: "+office.getEysCode());
				}

				if(office!=null) {
					MarshCE mCE = office.getMarshCEMap().get(mOffCeAccounts.getCodiceEurosys());
					if(null == mCE && StringUtils.isNotEmpty(mOffCeAccounts.getAdUsername())) {
						mCE = new MarshCE();
						office.getMarshCEMap().put(mOffCeAccounts.getCodiceEurosys(), mCE);
						mCE.setEysCode(mOffCeAccounts.getCodiceEurosys());
						mCE.setNominativo(mOffCeAccounts.getNomeIdentificativo());
						mCE.setUsername(mOffCeAccounts.getAdUsername());
						if(StringUtils.isEmpty(mCE.getEysCode()))
							log.debug("\t"+mCE.getNominativo()+" eys code: "+mCE.getEysCode());
					}
				}
			}

			tx.commit();
		} catch (HibernateException ex) {
			log.error("Exception in listOffices", ex);
			ex.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}

		return result;
	}



	/* (non-Javadoc)
	 * @see com.kp.marsh.ebt.server.importer.dao.impl.ImportController#findInformationOwnerByEysCode(java.lang.String)
	 */
	@Override
	public InformationOwners findInformationOwnerByEysCode(String codEys) throws Exception {

		InformationOwners result = null;
		Session session = (Session) this.connection.getSessionFactory().openSession();

		Transaction tx = session.beginTransaction();
		try {

			Query q = session.createQuery("from InformationOwners where cod_Eys =:codEys");
			q.setParameter("codEys",codEys);
			List<InformationOwners> queryResList = q.list();
			tx.commit();
			if(queryResList.size() == 1) {
				result = queryResList.get(0); 
			} else if(queryResList.size() > 1) {
				throw new Exception("There should be only one InformationOwner with codEys =" +codEys+"- "+queryResList.get(0).getDescription());
			}

		} catch(HibernateException ex) {
			log.error("Exception in findInformationOwnerById()");
			ex.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}


		return result;

	}



	/* (non-Javadoc)
	 * @see com.kp.marsh.ebt.server.importer.dao.impl.ImportController#saveUpdate(java.lang.Object)
	 */
	@Override
	public void saveUpdate(Object object) {
		Session session = (Session) this.connection.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			session.beginTransaction();
			session.saveOrUpdate(object);
			tx.commit();
		} catch (Exception e) {
			log.error("Exception saving or updating", e);
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
	}
	
	@Override
	public void saveUpdateThrowable(Object object) throws ImportException {
		Session session = (Session) this.connection.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			session.beginTransaction();
			session.saveOrUpdate(object);
			tx.commit();
		} catch (Exception e) {
			log.error("Exception saving or updating", e);
			e.printStackTrace();
			tx.rollback();
			throw(new ImportException("Exception saving or updating"));
		} finally {
			session.close();
		}
	}



	/* (non-Javadoc)
	 * @see com.kp.marsh.ebt.server.importer.dao.impl.ImportController#findMarshCredentialByADUsername(java.lang.String)
	 */
	@Override
	public MarshPeopleCredentials findMarshCredentialByADUsername (
			String username) throws Exception  {
		MarshPeopleCredentials result = null;
		Session session = (Session) this.connection.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			Query q = session.createQuery("from MarshPeopleCredentials where username =:username");
			q.setParameter("username",username);
			List<MarshPeopleCredentials> queryResList = q.list();
			tx.commit();
			if(queryResList.size() == 1) {
				result = queryResList.get(0); 
			} else if(queryResList.size() > 1) {
				throw new Exception("There should be only one MarshPeopleCredentials with username =" +username+"- "+queryResList.get(0).getUsername());
			}
		} catch(HibernateException ex) {
			log.error("Exception in findMarshCredentialByADUsername()");
			ex.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
		return result;
	}



	/* (non-Javadoc)
	 * @see com.kp.marsh.ebt.server.importer.dao.impl.ImportController#findFinalBalanceByProdYearGruppoCommerciale(com.kp.marsh.backend.data.domain.Products, com.kp.marsh.backend.data.domain.ReferenceYears, com.kp.marsh.backend.data.domain.InformationOwners)
	 */
	@Override
	public BusinessInformation findBusInfoByProdYearGruppoCommerciale(
			Products prd, ReferenceYears year, InformationOwners io, BusinessInfoValueType valtype) throws Exception {
		BusinessInformation result = null;
		Session session = (Session) this.connection.getSessionFactory().openSession();

		Transaction tx = session.beginTransaction();
		try {

			Query q = session.createQuery("from BusinessInformation " +
					"where products =:prd " +
					"and referenceYears =:year " +
					"and informationOwners =:io " +
			"and valueType = :valType");
			q.setParameter("prd",prd);
			q.setParameter("io",io);
			q.setParameter("year",year);
			q.setParameter("valType",valtype.name());
			List<BusinessInformation> queryResList = q.list();

			tx.commit();
			if(queryResList.size() == 1) {
				result = queryResList.get(0); 
			} else if(queryResList.size() > 1) {
				throw new Exception("There should be only one "+valtype.name()+" BusinessInformation for product =" +prd.getItemName()+" - year: "+year.getReferenceYear()+" -gruppo commerciale: "+io.getDescription());
			}

		} catch(HibernateException ex) {
			log.error("Exception in findFinalBalanceByProdYearGruppoCommerciale()");
			ex.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}


		return result;
	}



	/* (non-Javadoc)
	 * @see com.kp.marsh.ebt.server.importer.dao.impl.ImportController#findEbtProductByEysId(java.lang.String)
	 */
	@Override
	public Products findEbtProductByName(String name) {
		Products result = null;
		Session session = (Session) this.connection.getSessionFactory().openSession();

		Transaction tx = session.beginTransaction();
		try {

			Query q = session.createQuery("from Products where eys_id =:name and itemType = 'PRODUCT'");
			q.setParameter("name", name);

			List<Products> lProd = q.list();
			if(!lProd.isEmpty()) {
				result = lProd.get(0);
			}

			tx.commit();

		} catch (Exception e) {
			log.error("Exception occurred in findEbtProductByName: "+name);
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
		return result;
	}



	/* (non-Javadoc)
	 * @see com.kp.marsh.ebt.server.importer.dao.impl.ImportController#findReferenceYearByName(java.lang.String)
	 */
	@Override
	public ReferenceYears findReferenceYearByName(String year) {
		ReferenceYears dbYear = null;
		Session session = (Session) this.connection.getSessionFactory().openSession();

		Transaction tx = session.beginTransaction();
		try {
			Query q = session.createQuery("from ReferenceYears where referenceYear =:year");
			q.setParameter("year", Integer.valueOf(year));
			List<ReferenceYears> list = q.list();
			if(!list.isEmpty())
				dbYear = list.get(0);
			tx.commit();

		} catch (Exception ex) {
			log.error("Exception in findReferenceYearByName()");
			ex.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}

		return dbYear;
	}

	@Override
	public void pulisciAchieved() {
		cleanTable(MAchieved.class.getName());
	}

	@Override
	public void pulisciAnagCe() {
		cleanTable(MOffCeAccounts.class.getName());
	}
	
	private void cleanTable(String tableName){
		Session session = (Session) this.connection.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			String hql = "delete from " + tableName;
	        Query q = session.createQuery( hql );
	         q.executeUpdate();
		} catch (RuntimeException re) {
			log.error("Errore durante eliminazione dei dati dalla table "+tableName, re);
			tx.rollback();
			throw re;
		} finally {
			session.close();
		}
	}

	@Override
	public List<MAchieved> listGruppiCommerciali() {
		List<MAchieved> result = null;
		Session session = (Session) this.connection.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			Query q = session.createQuery("from MAchieved");
			result = q.list();
		} catch (RuntimeException re) {
			tx.rollback();
			log.error(re.getLocalizedMessage());
			re.printStackTrace();
			throw re;
		} finally {
			session.close();
		}
		return result;
	}
}
