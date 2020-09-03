package com.dummy.myerp.consumer.dao.impl.db.dao;

import java.sql.Types;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.impl.db.rowmapper.comptabilite.CompteComptableRM;
import com.dummy.myerp.consumer.dao.impl.db.rowmapper.comptabilite.EcritureComptableRM;
import com.dummy.myerp.consumer.dao.impl.db.rowmapper.comptabilite.JournalComptableRM;
import com.dummy.myerp.consumer.dao.impl.db.rowmapper.comptabilite.LigneEcritureComptableRM;
import com.dummy.myerp.consumer.dao.impl.db.rowmapper.comptabilite.SequenceEcritureComptableRm;
import com.dummy.myerp.consumer.db.AbstractDbConsumer;
import com.dummy.myerp.consumer.db.DataSourcesEnum;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import com.dummy.myerp.technical.exception.NotFoundException;
import com.dummy.myerp.technical.exception.TechnicalException;

/**
 * Implémentation de l'interface {@link ComptabiliteDao}
 */
public class ComptabiliteDaoImpl extends AbstractDbConsumer implements ComptabiliteDao {

	private static Logger logger = LoggerFactory.getLogger(ComptabiliteDaoImpl.class);

	// ==================== Constructeurs ====================
	/** Instance unique de la classe (design pattern Singleton) */
	private static final ComptabiliteDaoImpl INSTANCE = new ComptabiliteDaoImpl();

	/**
	 * Renvoie l'instance unique de la classe (design pattern Singleton).
	 *
	 * @return {@link ComptabiliteDaoImpl}
	 */
	public static ComptabiliteDaoImpl getInstance() {
		return ComptabiliteDaoImpl.INSTANCE;
	}

	/**
	 * Constructeur.
	 */
	protected ComptabiliteDaoImpl() {
		super();
	}

	// ==================== Méthodes ====================
	/** SQLgetListCompteComptable */
	private static String SQLgetListCompteComptable;

	public void setSQLgetListCompteComptable(String pSQLgetListCompteComptable) {
		SQLgetListCompteComptable = pSQLgetListCompteComptable;
	}

	@Override
	public List<CompteComptable> getListCompteComptable() {
		JdbcTemplate vJdbcTemplate = new JdbcTemplate(this.getDataSource(DataSourcesEnum.MYERP));
		CompteComptableRM vRM = new CompteComptableRM();
		List<CompteComptable> vList = vJdbcTemplate.query(SQLgetListCompteComptable, vRM);
		logger.info("SQL request: {}", SQLgetListCompteComptable);
		return vList;
	}

	/** SQLgetListJournalComptable */
	private static String SQLgetListJournalComptable;

	public void setSQLgetListJournalComptable(String pSQLgetListJournalComptable) {
		SQLgetListJournalComptable = pSQLgetListJournalComptable;
	}

	public String getString() {

		return SQLgetListJournalComptable;
	}

	@Override
	public List<JournalComptable> getListJournalComptable() {
		JdbcTemplate vJdbcTemplate = new JdbcTemplate(this.getDataSource(DataSourcesEnum.MYERP));
		JournalComptableRM vRM = new JournalComptableRM();
		List<JournalComptable> vList = vJdbcTemplate.query(SQLgetListJournalComptable, vRM);
		logger.info("SQL request: {}", SQLgetListJournalComptable);
		return vList;
	}

	// ==================== EcritureComptable - GET ====================

	/** SQLgetListEcritureComptable */
	private static String SQLgetListEcritureComptable;

	public void setSQLgetListEcritureComptable(String pSQLgetListEcritureComptable) {
		SQLgetListEcritureComptable = pSQLgetListEcritureComptable;
	}

	@Override
	public List<EcritureComptable> getListEcritureComptable() {
		JdbcTemplate vJdbcTemplate = new JdbcTemplate(this.getDataSource(DataSourcesEnum.MYERP));
		EcritureComptableRM vRM = new EcritureComptableRM();
		List<EcritureComptable> vList = null;

		vList = vJdbcTemplate.query(SQLgetListEcritureComptable, vRM);
		logger.info("SQL request: {}", SQLgetListEcritureComptable);

		return vList;
	}

	/** SQLgetEcritureComptable */
	private static String SQLgetEcritureComptable;

	public void setSQLgetEcritureComptable(String pSQLgetEcritureComptable) {
		SQLgetEcritureComptable = pSQLgetEcritureComptable;
	}

	@Override
	public EcritureComptable getEcritureComptable(Integer pId) throws NotFoundException {
		NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource(DataSourcesEnum.MYERP));
		MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
		vSqlParams.addValue("id", pId);
		EcritureComptableRM vRM = new EcritureComptableRM();
		EcritureComptable vBean;
		try {
			logger.info("SQL request: {}", SQLgetEcritureComptable);
			vBean = vJdbcTemplate.queryForObject(SQLgetEcritureComptable, vSqlParams, vRM);
		} catch (EmptyResultDataAccessException vEx) {
			throw new NotFoundException("EcritureComptable non trouvée : id=" + pId);
		}
		return vBean;
	}

	/** SQLgetEcritureComptableByRef */
	private static String SQLgetEcritureComptableByRef;

	public void setSQLgetEcritureComptableByRef(String pSQLgetEcritureComptableByRef) {
		SQLgetEcritureComptableByRef = pSQLgetEcritureComptableByRef;
	}

	@Override
	public EcritureComptable getEcritureComptableByRef(String pReference) throws NotFoundException {
		NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource(DataSourcesEnum.MYERP));
		MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
		vSqlParams.addValue("reference", pReference);
		EcritureComptableRM vRM = new EcritureComptableRM();
		EcritureComptable vBean;
		try {
			logger.info("SQL request: {}", SQLgetEcritureComptableByRef);
			vBean = vJdbcTemplate.queryForObject(SQLgetEcritureComptableByRef, vSqlParams, vRM);
		} catch (EmptyResultDataAccessException vEx) {
			throw new NotFoundException("EcritureComptable non trouvée : reference=" + pReference);
		}
		return vBean;
	}

	/** SQLloadListLigneEcriture */
	private static String SQLloadListLigneEcriture;

	public void setSQLloadListLigneEcriture(String pSQLloadListLigneEcriture) {
		SQLloadListLigneEcriture = pSQLloadListLigneEcriture;
	}

	@Override
	public void loadListLigneEcriture(EcritureComptable pEcritureComptable) {
		NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource(DataSourcesEnum.MYERP));
		MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
		vSqlParams.addValue("ecriture_id", pEcritureComptable.getId());
		LigneEcritureComptableRM vRM = new LigneEcritureComptableRM();
		List<LigneEcritureComptable> vList = vJdbcTemplate.query(SQLloadListLigneEcriture, vSqlParams, vRM);
		logger.info("SQL request: {}", SQLloadListLigneEcriture);
		pEcritureComptable.getListLigneEcriture().clear();
		pEcritureComptable.getListLigneEcriture().addAll(vList);
	}

	// ==================== EcritureComptable - INSERT ====================

	/** SQLinsertEcritureComptable */
	private static String SQLinsertEcritureComptable;

	public void setSQLinsertEcritureComptable(String pSQLinsertEcritureComptable) {
		SQLinsertEcritureComptable = pSQLinsertEcritureComptable;
	}

	@Override
	public void insertEcritureComptable(EcritureComptable pEcritureComptable) throws TechnicalException {
		// ===== Ecriture Comptable
		NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource(DataSourcesEnum.MYERP));
		MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
		vSqlParams.addValue("journal_code", pEcritureComptable.getJournal().getCode());
		vSqlParams.addValue("reference", pEcritureComptable.getReference());
		vSqlParams.addValue("date", pEcritureComptable.getDate(), Types.DATE);
		vSqlParams.addValue("libelle", pEcritureComptable.getLibelle());
		try {
			logger.info("SQL request: {}", SQLinsertEcritureComptable);
			vJdbcTemplate.update(SQLinsertEcritureComptable, vSqlParams);

		} catch (DataAccessException vEx) {

			throw new TechnicalException("Erreur technique : impossible d'inserer l'ecriture comptable");
		}

		// ----- Récupération de l'id
		Integer vId = this.queryGetSequenceValuePostgreSQL(DataSourcesEnum.MYERP, "myerp.ecriture_comptable_id_seq",
				Integer.class);
		pEcritureComptable.setId(vId);

		// ===== Liste des lignes d'écriture
		this.insertListLigneEcritureComptable(pEcritureComptable);
	}

	/** SQLinsertListLigneEcritureComptable */
	private static String SQLinsertListLigneEcritureComptable;

	public void setSQLinsertListLigneEcritureComptable(String pSQLinsertListLigneEcritureComptable) {
		SQLinsertListLigneEcritureComptable = pSQLinsertListLigneEcritureComptable;
	}

	/**
	 * Insert les lignes d'écriture de l'écriture comptable
	 * 
	 * @param pEcritureComptable l'écriture comptable
	 */
	protected void insertListLigneEcritureComptable(EcritureComptable pEcritureComptable) {
		NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource(DataSourcesEnum.MYERP));
		MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
		vSqlParams.addValue("ecriture_id", pEcritureComptable.getId());

		int vLigneId = 0;
		for (LigneEcritureComptable vLigne : pEcritureComptable.getListLigneEcriture()) {
			vLigneId++;
			vSqlParams.addValue("ligne_id", vLigneId);
			vSqlParams.addValue("compte_comptable_numero", vLigne.getCompteComptable().getNumero());
			vSqlParams.addValue("libelle", vLigne.getLibelle());
			vSqlParams.addValue("debit", vLigne.getDebit());
			vSqlParams.addValue("credit", vLigne.getCredit());
			vJdbcTemplate.update(SQLinsertListLigneEcritureComptable, vSqlParams);
			logger.info("SQL request: {}", SQLinsertListLigneEcritureComptable);

		}
	}

	// ==================== EcritureComptable - UPDATE ====================

	/** SQLupdateEcritureComptable */
	private static String SQLupdateEcritureComptable;

	public void setSQLupdateEcritureComptable(String pSQLupdateEcritureComptable) {
		SQLupdateEcritureComptable = pSQLupdateEcritureComptable;
	}

	@Override
	public void updateEcritureComptable(EcritureComptable pEcritureComptable) throws TechnicalException {
		// ===== Ecriture Comptable
		NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource(DataSourcesEnum.MYERP));
		MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
		vSqlParams.addValue("id", pEcritureComptable.getId());
		vSqlParams.addValue("journal_code", pEcritureComptable.getJournal().getCode());
		vSqlParams.addValue("reference", pEcritureComptable.getReference());
		vSqlParams.addValue("date", pEcritureComptable.getDate(), Types.DATE);
		vSqlParams.addValue("libelle", pEcritureComptable.getLibelle());
		try {

			logger.info("SQL request: {}", SQLupdateEcritureComptable);
			vJdbcTemplate.update(SQLupdateEcritureComptable, vSqlParams);

		} catch (DataAccessException vEx) {

			throw new TechnicalException("Erreur technique : impossible de mettre à jour l'ecriture comptable");
		}

		// ===== Liste des lignes d'écriture
		this.deleteListLigneEcritureComptable(pEcritureComptable.getId());
		this.insertListLigneEcritureComptable(pEcritureComptable);

	}

	// ==================== EcritureComptable - DELETE ====================

	/** SQLdeleteEcritureComptable */
	private static String SQLdeleteEcritureComptable;

	public void setSQLdeleteEcritureComptable(String pSQLdeleteEcritureComptable) {
		SQLdeleteEcritureComptable = pSQLdeleteEcritureComptable;
	}

	@Override
	public void deleteEcritureComptable(Integer pId) throws TechnicalException {
		// ===== Suppression des lignes d'écriture
		this.deleteListLigneEcritureComptable(pId);

		// ===== Suppression de l'écriture
		NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource(DataSourcesEnum.MYERP));
		MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
		vSqlParams.addValue("id", pId);
		try {
			logger.info("SQL request: {}", SQLdeleteEcritureComptable);
			vJdbcTemplate.update(SQLdeleteEcritureComptable, vSqlParams);

		} catch (DataAccessException vEx) {

			throw new TechnicalException("Erreur technique : impossible de supprimer l'ecriture comptable");
		}
	}

	/** SQLdeleteListLigneEcritureComptable */
	private static String SQLdeleteListLigneEcritureComptable;

	public void setSQLdeleteListLigneEcritureComptable(String pSQLdeleteListLigneEcritureComptable) {
		SQLdeleteListLigneEcritureComptable = pSQLdeleteListLigneEcritureComptable;
	}

	/**
	 * Supprime les lignes d'écriture de l'écriture comptable d'id
	 * {@code pEcritureId}
	 * 
	 * @param pEcritureId id de l'écriture comptable
	 */
	protected void deleteListLigneEcritureComptable(Integer pEcritureId) {
		NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource(DataSourcesEnum.MYERP));
		MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
		vSqlParams.addValue("ecriture_id", pEcritureId);
		vJdbcTemplate.update(SQLdeleteListLigneEcritureComptable, vSqlParams);
		logger.info("SQL request: {}", SQLdeleteListLigneEcritureComptable);
	}

	// ==================== SequenceEcritureComptable - INSERT ====================

	/** SQLinsertSequenceEcritureComptable */
	private static String SQLinsertSequenceEcritureComptable;

	public void setSQLinsertSequenceEcritureComptable(String pSQLinsertSequenceEcritureComptable) {
		SQLinsertSequenceEcritureComptable = pSQLinsertSequenceEcritureComptable;
	}

	@Override
	public void insertSequenceEcritureComptable(SequenceEcritureComptable pSequenceEcritureComptable) {
		int derniereValeur = 1;

		NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource(DataSourcesEnum.MYERP));
		MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
		vSqlParams.addValue("annee", pSequenceEcritureComptable.getAnnee());
		vSqlParams.addValue("derniere_valeur", derniereValeur);
		vSqlParams.addValue("journal_code", pSequenceEcritureComptable.getJournalComptable().getCode());
		vJdbcTemplate.update(SQLinsertSequenceEcritureComptable, vSqlParams);
		logger.info("SQL request: {}", SQLinsertSequenceEcritureComptable);
	}

	// ==================== SequenceEcritureComptable - GET ====================

	/** SQLgetSequenceEcritureComptable */
	private static String SQLgetSequenceEcritureComptable;

	public void setSQLgetSequenceEcritureComptable(String pSQLgetSequenceEcritureComptable) {
		SQLgetSequenceEcritureComptable = pSQLgetSequenceEcritureComptable;
	}

	@Override
	public SequenceEcritureComptable getSequenceEcritureComptable(int annee, JournalComptable pJournalComptable)
			throws NotFoundException {
		NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource(DataSourcesEnum.MYERP));
		MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
		SequenceEcritureComptableRm vRm = new SequenceEcritureComptableRm();
		vSqlParams.addValue("annee", annee);
		vSqlParams.addValue("journal_code", pJournalComptable.getCode());
		SequenceEcritureComptable sequenceEcritureComptable;
		try {
			logger.info("SQL request: {}", SQLgetSequenceEcritureComptable);
			sequenceEcritureComptable = vJdbcTemplate.queryForObject(SQLgetSequenceEcritureComptable, vSqlParams, vRm);
			return sequenceEcritureComptable;
		} catch (EmptyResultDataAccessException vEx) {
			throw new NotFoundException(
					"Sequence comptable non trouvée année=" + annee + "journalCode=" + pJournalComptable.getCode());
		}
	}

	// ==================== SequenceEcritureComptable - UPDATE ====================

	/** SQLupdateEcritureComptable */
	private static String SQLupdateSequenceEcritureComptable;

	public void setSQLupdateSequenceEcritureComptable(String pSQLupdateSequenceEcritureComptable) {
		SQLupdateSequenceEcritureComptable = pSQLupdateSequenceEcritureComptable;
	}

	@Override
	public void updateSequenceEcritureComptable(SequenceEcritureComptable pSequenceEcritureComptable) {
		int derniereValeur = pSequenceEcritureComptable.getDerniereValeur() + 1;

		NamedParameterJdbcTemplate vJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource(DataSourcesEnum.MYERP));
		MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
		vSqlParams.addValue("derniere_valeur", derniereValeur);
		vSqlParams.addValue("annee", pSequenceEcritureComptable.getAnnee());
		vSqlParams.addValue("journal_code", pSequenceEcritureComptable.getJournalComptable().getCode());
		vJdbcTemplate.update(SQLupdateSequenceEcritureComptable, vSqlParams);
		logger.info("SQL request: {}", SQLupdateSequenceEcritureComptable);

	}

}
