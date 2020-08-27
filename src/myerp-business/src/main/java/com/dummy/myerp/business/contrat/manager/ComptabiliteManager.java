package com.dummy.myerp.business.contrat.manager;

import java.util.List;

import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import com.dummy.myerp.technical.exception.TechnicalException;

/**
 * Interface du manager du package comptabilite.
 */
public interface ComptabiliteManager {

	/**
	 * Renvoie la liste des comptes comptables.
	 *
	 * @return {@link List}
	 */
	List<CompteComptable> getListCompteComptable();

	/**
	 * Renvoie la liste des journaux comptables.
	 *
	 * @return {@link List}
	 */
	List<JournalComptable> getListJournalComptable();

	/**
	 * Renvoie la liste des écritures comptables.
	 *
	 * @return {@link List}
	 */
	List<EcritureComptable> getListEcritureComptable();

	/**
	 * Ajoute une référence à l'écriture comptable.
	 *
	 * <strong>RG_Compta_5 : </strong> La référence d'une écriture comptable est
	 * composée du code du journal dans lequel figure l'écriture suivi de l'année et
	 * d'un numéro de séquence (propre à chaque journal) sur 5 chiffres incrémenté
	 * automatiquement à chaque écriture. Le formatage de la référence est :
	 * XX-AAAA/#####. <br>
	 * Ex : Journal de banque (BQ), écriture au 31/12/2016
	 * 
	 * <pre>
	 * BQ - 2016 / 00001
	 * </pre>
	 *
	 * <p>
	 * <strong>Attention :</strong> l'écriture n'est pas enregistrée en persistance
	 * </p>
	 * 
	 * @param pEcritureComptable L'écriture comptable concernée
	 */
	void addReference(EcritureComptable pEcritureComptable);

	void checkEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException;

	/**
	 * Vérifie que l'Ecriture comptable respecte les règles de gestion.
	 *
	 * @param pEcritureComptable -
	 * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les
	 *                             règles de gestion
	 */
	void checkEcritureComptableUnitViolation(EcritureComptable pEcritureComptable) throws FunctionalException;

	void checkEcritureComptableUnitRG2(EcritureComptable pEcritureComptable) throws FunctionalException;

	void checkEcritureComptableUnitRG3(EcritureComptable pEcritureComptable) throws FunctionalException;

	void checkEcritureComptableUnitRG5(EcritureComptable pEcritureComptable) throws FunctionalException;

	void checkEcritureComptableUnitRG5_isJournalComptableExistingInDb(EcritureComptable pEcritureComptable)
			throws FunctionalException;

	/**
	 * Vérifie que l'Ecriture comptable respecte les règles de gestion.
	 *
	 * @param pEcritureComptable -
	 * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les
	 *                             règles de gestion
	 * @throws NotFoundException
	 */
	void checkEcritureComptableContext(EcritureComptable pEcritureComptable) throws FunctionalException;

	/**
	 * Insert une nouvelle écriture comptable.
	 *
	 * @param pEcritureComptable -
	 * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les
	 *                             règles de gestion
	 */
	void insertEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException, TechnicalException;

	/**
	 * Met à jour l'écriture comptable.
	 *
	 * @param pEcritureComptable -
	 * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les
	 *                             règles de gestion
	 * @throws TechnicalException 
	 */
	void updateEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException, TechnicalException ;

	/**
	 * Supprime l'écriture comptable d'id {@code pId}.
	 *
	 * @param pId l'id de l'écriture
	 */
	void deleteEcritureComptable(Integer pId) throws TechnicalException;

}
