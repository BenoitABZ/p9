package com.dummy.myerp.model.bean.comptabilite;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class LigneEcritureComptableTest {

	@Test
	public void toString_shouldReturnSummaryString() {

		CompteComptable compteComptable = new CompteComptable();
		
		compteComptable.setLibelle("Banque");
		compteComptable.setNumero(512);

		LigneEcritureComptable ligneEcritureComptable = new LigneEcritureComptable();
		
		ligneEcritureComptable.setCredit(new BigDecimal("200.10"));
		ligneEcritureComptable.setCompteComptable(compteComptable);
		ligneEcritureComptable.setLibelle("Test");

		assertThat(ligneEcritureComptable.toString()).isEqualTo(
				"LigneEcritureComptable{compteComptable=CompteComptable{numero=512, libelle='Banque'}, libelle='Test', debit=null, credit=200.10}"
				);
	}

}
