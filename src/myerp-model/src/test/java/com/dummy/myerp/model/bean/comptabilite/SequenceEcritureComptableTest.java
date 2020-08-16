package com.dummy.myerp.model.bean.comptabilite;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SequenceEcritureComptableTest {

	@Test
	public void toString_shouldReturnSummaryString() {

		SequenceEcritureComptable sequenceEcritureComptable = new SequenceEcritureComptable();
		sequenceEcritureComptable.setDerniereValeur(90);
		sequenceEcritureComptable.setAnnee(2020);
		sequenceEcritureComptable.setJournalComptable(new JournalComptable("BQ", "Banque"));

		assertThat(sequenceEcritureComptable.toString())
				.isEqualTo("SequenceEcritureComptable{annee=2020, journalCode=BQ, derniereValeur=90}");

	}

}
