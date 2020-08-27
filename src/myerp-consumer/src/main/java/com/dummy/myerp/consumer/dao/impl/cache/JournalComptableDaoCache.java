package com.dummy.myerp.consumer.dao.impl.cache;

import java.util.List;

import com.dummy.myerp.consumer.ConsumerHelper;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.technical.exception.NotFoundException;

/**
 * Cache DAO de {@link JournalComptable}
 */
public class JournalComptableDaoCache {

	// ==================== Attributs ====================
	/** The List compte comptable. */
	private List<JournalComptable> listJournalComptable;

	//private JournalComptable journalComptable;

	// ==================== Constructeurs ====================
	/**
	 * Instantiates a new Compte comptable dao cache.
	 */
//	public JournalComptableDaoCache(JournalComptable journalComptable) {

//		this.journalComptable = journalComptable;
//	}

	public JournalComptableDaoCache() {

	}

	// ==================== Méthodes ====================
	/**
	 * Gets by code.
	 *
	 * @param pCode le code du {@link JournalComptable}
	 * @return {@link JournalComptable} ou {@code null}
	 * @throws NotFoundException
	 */
	public JournalComptable getByCode(String pCode) throws NotFoundException {
		if (listJournalComptable == null) {
			listJournalComptable = ConsumerHelper.getDaoProxy().getComptabiliteDao().getListJournalComptable();
		}
		JournalComptable vRetour;

		vRetour = JournalComptable.getByCode(listJournalComptable, pCode);

		if (vRetour == null) {

			throw new NotFoundException("le journal comptable ayant pour code=" + pCode + "n'existe pas en BDD");
		}

		return vRetour;
	}

}
