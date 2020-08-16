package com.dummy.myerp.model.bean.comptabilite;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Bean représentant une séquence pour les références d'écriture comptable
 */
public class SequenceEcritureComptable {

	// ==================== Attributs ====================
	/** L'année */
	@NotNull
	private Integer annee;
	/** La dernière valeur utilisée */
	@NotNull
	private Integer derniereValeur;
	
	@Valid
	@NotNull
	private JournalComptable journalComptable;


	// ==================== Constructeurs ====================
	/**
	 * Constructeur
	 */
	public SequenceEcritureComptable() {
	}

	/**
	 * Constructeur -- test
	 *
	 * @param pAnnee          -
	 * @param pDerniereValeur -
	 * @param pJournalCode
	 */
	public SequenceEcritureComptable(Integer pAnnee, Integer pDerniereValeur, JournalComptable pJournalComptable) {
		annee = pAnnee;
		derniereValeur = pDerniereValeur;
		journalComptable = pJournalComptable;
	}
	
	public SequenceEcritureComptable(Integer pAnnee, JournalComptable pJournalComptable) {
		annee = pAnnee;
		journalComptable = pJournalComptable;
	}

	// ==================== Getters/Setters ====================
	public Integer getAnnee() {
		return annee;
	}

	public void setAnnee(Integer pAnnee) {
		annee = pAnnee;
	}

	public Integer getDerniereValeur() {
		return derniereValeur;
	}

	public void setDerniereValeur(Integer pDerniereValeur) {
		derniereValeur = pDerniereValeur;
	}
	

	public JournalComptable getJournalComptable() {
		return journalComptable;
	}

	public void setJournalComptable(JournalComptable journalComptable) {
		this.journalComptable = journalComptable;
	}

	// ==================== Méthodes ====================
	@Override
	public String toString() {
		final StringBuilder vStB = new StringBuilder(this.getClass().getSimpleName());
		final String vSEP = ", ";
		vStB.append("{").append("annee=").append(annee).append(vSEP).append("derniereValeur=").append(derniereValeur).append("journalCode=").append(journalComptable.getCode())
				.append("}");
		return vStB.toString();
	}
}
