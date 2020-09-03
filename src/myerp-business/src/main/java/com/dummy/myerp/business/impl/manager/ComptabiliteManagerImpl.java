package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;

import java.util.Calendar;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionStatus;
import com.dummy.myerp.business.contrat.manager.ComptabiliteManager;
import com.dummy.myerp.business.impl.AbstractBusinessManager;
import com.dummy.myerp.consumer.dao.impl.cache.JournalComptableDaoCache;
import com.dummy.myerp.consumer.dao.impl.db.dao.ComptabiliteDaoImpl;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import com.dummy.myerp.technical.exception.TechnicalException;

/**
 * Comptabilite manager implementation.
 */
public class ComptabiliteManagerImpl extends AbstractBusinessManager implements ComptabiliteManager {
	
	private static Logger logger = LoggerFactory.getLogger(ComptabiliteManagerImpl.class);

	// ==================== Attributs ====================

	// ==================== Constructeurs ====================
	/**
	 * Instantiates a new Comptabilite manager.
	 */
	public ComptabiliteManagerImpl() {
	}

	// ==================== Methode utilitaire =====================

	private int getYear(EcritureComptable pEcritureComptable) {

		Calendar cal = Calendar.getInstance();

		cal.setTime(pEcritureComptable.getDate());

		int annee = cal.get(Calendar.YEAR);

		return annee;

	}

	// ==================== Getters/Setters ====================
	@Override
	public List<CompteComptable> getListCompteComptable() {
		return getDaoProxy().getComptabiliteDao().getListCompteComptable();
	}

	@Override
	public List<JournalComptable> getListJournalComptable() {
		return getDaoProxy().getComptabiliteDao().getListJournalComptable();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<EcritureComptable> getListEcritureComptable() {
		return getDaoProxy().getComptabiliteDao().getListEcritureComptable();
	}

	/**
	 * {@inheritDoc}
	 */
	// TODO à tester
	@Override
	public synchronized void addReference(EcritureComptable pEcritureComptable) {
		// TODO à implémenter
		// Bien se réferer à la JavaDoc de cette méthode !
		/*
		 * Le principe : 1. Remonter depuis la persitance la dernière valeur de la
		 * séquence du journal pour l'année de l'écriture (table
		 * sequence_ecriture_comptable) 2. * S'il n'y a aucun enregistrement pour le
		 * journal pour l'année concernée : 1. Utiliser le numéro 1. Sinon : 1. Utiliser
		 * la dernière valeur + 1 3. Mettre à jour la référence de l'écriture avec la
		 * référence calculée (RG_Compta_5) 4. Enregistrer (insert/update) la valeur de
		 * la séquence en persitance (table sequence_ecriture_comptable)
		 */
		int annee = getYear(pEcritureComptable);

		String reference = pEcritureComptable.getJournal().getCode() + "-" + annee + "/";

		JournalComptable journalComptable = pEcritureComptable.getJournal();

		try {
			SequenceEcritureComptable sequenceEcritureComptable = getDaoProxy().getComptabiliteDao()
					.getSequenceEcritureComptable(annee, journalComptable);
			
			System.out.println(sequenceEcritureComptable.getAnnee());

			reference += (StringUtils.leftPad(String.valueOf(sequenceEcritureComptable.getDerniereValeur() + 1), 5,
					"0"));
			
			System.out.println(reference);

			getDaoProxy().getComptabiliteDao().updateSequenceEcritureComptable(sequenceEcritureComptable);

		} catch (NullPointerException nfe) {

			nfe.getStackTrace();

		} catch (NotFoundException nfe) {
			reference += "00001";

			SequenceEcritureComptable sequenceEcritureComptable = new SequenceEcritureComptable();
			sequenceEcritureComptable.setAnnee(annee);
			sequenceEcritureComptable.setJournalComptable(pEcritureComptable.getJournal());

			getDaoProxy().getComptabiliteDao().insertSequenceEcritureComptable(sequenceEcritureComptable);
		}

		pEcritureComptable.setReference(reference);
	}

	/**
	 * {@inheritDoc}
	 */
	// TODO à tester
	@Override
	public void checkEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {

	    this.checkEcritureComptableUnitViolation(pEcritureComptable);
		this.checkEcritureComptableUnitRG2(pEcritureComptable);
		this.checkEcritureComptableUnitRG3(pEcritureComptable);
		this.checkEcritureComptableUnitRG5(pEcritureComptable);
		this.checkEcritureComptableUnitRG5_isJournalComptableExistingInDb(pEcritureComptable);
		this.checkEcritureComptableContext(pEcritureComptable);

	}

	/**
	 * Vérifie que l'Ecriture comptable respecte les règles de gestion unitaires,
	 * c'est à dire indépendemment du contexte (unicité de la référence, exercie
	 * comptable non cloturé...)
	 *
	 * @param pEcritureComptable -
	 * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les
	 *                             règles de gestion
	 */
	// TODO tests à compléter
	@Override
	public void checkEcritureComptableUnitViolation(EcritureComptable pEcritureComptable) throws FunctionalException {
		// ===== Vérification des contraintes unitaires sur les attributs de l'écriture
		Set<ConstraintViolation<EcritureComptable>> vViolations = getConstraintValidator().validate(pEcritureComptable);
		if (!vViolations.isEmpty()) {
			for (ConstraintViolation<EcritureComptable> vViolation : vViolations) {
				logger.info("message de la violation de contrainte: {}", vViolation.getMessage());
			}
			for (ConstraintViolation<EcritureComptable> vViolation : vViolations) {
				throw new FunctionalException(
						"L'écriture comptable ne respecte pas les règles de gestion: " + vViolation.getMessage());
			}
		}
	}

	@Override
	public void checkEcritureComptableUnitRG2(EcritureComptable pEcritureComptable) throws FunctionalException {

		// ===== RG_Compta_2 : Pour qu'une écriture comptable soit valide, elle doit
		// être équilibrée
		if (!pEcritureComptable.isEquilibree()) {
			throw new FunctionalException("L'écriture comptable n'est pas équilibrée.");
		}
	}

	@Override
	public void checkEcritureComptableUnitRG3(EcritureComptable pEcritureComptable) throws FunctionalException {
		// ===== RG_Compta_3 : une écriture comptable doit avoir au moins 2 lignes
		// d'écriture (1 au débit, 1 au crédit)
		int vNbrCredit = 0;
		int vNbrDebit = 0;
		for (LigneEcritureComptable vLigneEcritureComptable : pEcritureComptable.getListLigneEcriture()) {
			if (BigDecimal.ZERO
					.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getCredit(), BigDecimal.ZERO)) != 0) {
				vNbrCredit++;
			}
			if (BigDecimal.ZERO
					.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getDebit(), BigDecimal.ZERO)) != 0) {
				vNbrDebit++;
			}
		}
		// On test le nombre de lignes car si l'écriture à une seule ligne
		// avec un montant au débit et un montant au crédit ce n'est pas valable
		if (pEcritureComptable.getListLigneEcriture().size() < 2 || vNbrCredit < 1 || vNbrDebit < 1) {
			throw new FunctionalException(
					"L'écriture comptable doit avoir au moins deux lignes : une ligne au débit et une ligne au crédit.");
		}
	}

	@Override
	public void checkEcritureComptableUnitRG5(EcritureComptable pEcritureComptable) throws FunctionalException {

		// TODO ===== RG_Compta_5 : Format et contenu de la référence
		// vérifier que l'année dans la référence correspond bien à la date de
		// l'écriture, idem pour le code journal...

		if (pEcritureComptable.getReference() != null) {

			String annee = pEcritureComptable.getReference().substring(3, 7);
			if (!annee.equals(Integer.toString(getYear(pEcritureComptable)))) {
				throw new FunctionalException(
						"Format et contenu de la référence : l'année dans la reference ne correspond pas à la date d'ecriture");
			}

			String journalCodeReference = pEcritureComptable.getReference().substring(0, 2);
			if (!journalCodeReference.equals(pEcritureComptable.getJournal().getCode())) {
				throw new FunctionalException(
						"Format et contenu de la référence : le code journal dans la reference ne correspond pas au journal de l'écriture.");
			}

		} else {
			throw new FunctionalException("La référence ne peut pas être null.");
		}
	}

	@Override
	public void checkEcritureComptableUnitRG5_isJournalComptableExistingInDb(EcritureComptable pEcritureComptable)
			throws FunctionalException {
	
		try {

			String code = pEcritureComptable.getJournal().getCode();

			List<JournalComptable> jcl = getDaoProxy().getComptabiliteDao().getListJournalComptable();
			JournalComptable.getByCode(jcl, code);

		} catch (NotFoundException nfe) {
			throw new FunctionalException("Format et contenu de la référence : " + nfe.getMessage());
		}
	}

	/**
	 * Vérifie que l'Ecriture comptable respecte les règles de gestion liées au
	 * contexte (unicité de la référence, année comptable non cloturé...)
	 *
	 * @param pEcritureComptable -
	 * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les
	 *                             règles de gestion
	 */
	@Override
	public void checkEcritureComptableContext(EcritureComptable pEcritureComptable) throws FunctionalException {
		// ===== RG_Compta_6 : La référence d'une écriture comptable doit être unique
		if (StringUtils.isNoneEmpty(pEcritureComptable.getReference())) {

			String reference = pEcritureComptable.getReference();

			System.out.println(reference);

			try {
				// Recherche d'une écriture ayant la même référence
				EcritureComptable vECRef = getDaoProxy().getComptabiliteDao().getEcritureComptableByRef(reference);

				// Si l'écriture à vérifier est une nouvelle écriture (id == null),
				// ou si elle ne correspond pas à l'écriture trouvée (id != idECRef),
				// c'est qu'il y a déjà une autre écriture avec la même référence
				// if (pEcritureComptable.getId() == null ||
				// !pEcritureComptable.getId().equals(vECRef.getId())) {

				if (pEcritureComptable.getReference().equals(vECRef.getReference())) {
					throw new FunctionalException("Une autre écriture comptable existe déjà avec la même référence.");

				}

			} catch (NotFoundException e) {

			}

		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws Exception
	 */
	@Override
	public void insertEcritureComptable(EcritureComptable pEcritureComptable)
			throws FunctionalException, TechnicalException {
		this.checkEcritureComptable(pEcritureComptable);
		TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
		try {
			getDaoProxy().getComptabiliteDao().insertEcritureComptable(pEcritureComptable);
			getTransactionManager().commitMyERP(vTS);
			vTS = null;
		} finally {
			getTransactionManager().rollbackMyERP(vTS);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateEcritureComptable(EcritureComptable pEcritureComptable)
			throws FunctionalException, TechnicalException {
		this.checkEcritureComptable(pEcritureComptable);
		TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
		try {
			getDaoProxy().getComptabiliteDao().updateEcritureComptable(pEcritureComptable);
			getTransactionManager().commitMyERP(vTS);
			vTS = null;
		} finally {
			getTransactionManager().rollbackMyERP(vTS);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteEcritureComptable(Integer pId) throws TechnicalException {
		TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
		try {
			getDaoProxy().getComptabiliteDao().deleteEcritureComptable(pId);
			getTransactionManager().commitMyERP(vTS);
			vTS = null;
		} finally {
			getTransactionManager().rollbackMyERP(vTS);
		}
	}

}
