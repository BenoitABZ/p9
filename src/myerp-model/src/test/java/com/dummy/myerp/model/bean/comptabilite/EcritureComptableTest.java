package com.dummy.myerp.model.bean.comptabilite;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class EcritureComptableTest {

	EcritureComptable vEcriture;

	private LigneEcritureComptable createLigne(Integer pCompteComptableNumero, String pDebit, String pCredit) {

		BigDecimal vDebit = pDebit == null ? null : new BigDecimal(pDebit);

		BigDecimal vCredit = pCredit == null ? null : new BigDecimal(pCredit);

		String vLibelle = ObjectUtils.defaultIfNull(vDebit, BigDecimal.ZERO)
				.subtract(ObjectUtils.defaultIfNull(vCredit, BigDecimal.ZERO)).toPlainString();

		LigneEcritureComptable vRetour = new LigneEcritureComptable(new CompteComptable(pCompteComptableNumero),
				vLibelle, vDebit, vCredit);

		return vRetour;
	}

	@BeforeEach
	public void initEcritureComptable() {

		vEcriture = new EcritureComptable();

		Date dateNow = new Date();

		vEcriture.setId(-1);

		vEcriture.setJournal(new JournalComptable("TE", "Journal comptable test"));

		vEcriture.setReference("TE-2020/00001");

		vEcriture.setDate(dateNow);

		vEcriture.setLibelle("Equilibrée");
		vEcriture.getListLigneEcriture().add(this.createLigne(1, "201", null));
		vEcriture.getListLigneEcriture().add(this.createLigne(1, "100", "32.50"));
		vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "301.50"));
		vEcriture.getListLigneEcriture().add(this.createLigne(2, "40", "7"));

	}
	@Test
	public void isEquilibree_shouldBeTrue() {

		assertTrue(vEcriture.isEquilibree());

	}

	@Test
	public void isEquilibree_shouldBeFalse() {

		vEcriture.getListLigneEcriture().clear();
		vEcriture.setLibelle("Non équilibrée");
		vEcriture.getListLigneEcriture().add(this.createLigne(1, "10", null));
		vEcriture.getListLigneEcriture().add(this.createLigne(1, "20", "1"));
		vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "30"));
		vEcriture.getListLigneEcriture().add(this.createLigne(2, "1", "6"));
		
		assertFalse(vEcriture.isEquilibree());
	}

	@Test
	@DisplayName("test: total credit d'une ecriture comptable")
	public void getTotalCredit_shouldReturnTheSumOfCreditLines_OfPositivesBigDecimal() {

		BigDecimal totalCreditExpected = new BigDecimal("341.00");

		BigDecimal totalCreditActual = vEcriture.getTotalCredit();

		assertThat(totalCreditActual).isEqualTo(totalCreditExpected);

	}

	@Test
	@DisplayName("test: total debit d'une ecriture comptable")
	public void getTotalDebit_shouldReturnTheSumOfDebitLines_OfPositivesBigDecimal() {

		BigDecimal totalDebitExpected = new BigDecimal("341.00");

		BigDecimal totalDebitActual = vEcriture.getTotalDebit();

		assertThat(totalDebitActual).isEqualTo(totalDebitExpected);

	}

	@Test
	public void toString_shouldReturnSummaryString() {


		assertThat(vEcriture.toString()).isEqualTo(
				"EcritureComptable{id=-1, journal=JournalComptable{code='TE', libelle='Journal comptable test'}, reference='TE-2020/00001', date=" + vEcriture.getDate().toString() + ", libelle='Equilibrée', totalDebit=341.00, totalCredit=341.00, listLigneEcriture=[\n"
						+ "LigneEcritureComptable{compteComptable=CompteComptable{numero=1, libelle='null'}, libelle='201', debit=201, credit=null}\n"
						+ "LigneEcritureComptable{compteComptable=CompteComptable{numero=1, libelle='null'}, libelle='67.50', debit=100, credit=32.50}\n"
						+ "LigneEcritureComptable{compteComptable=CompteComptable{numero=2, libelle='null'}, libelle='-301.50', debit=null, credit=301.50}\n"
						+ "LigneEcritureComptable{compteComptable=CompteComptable{numero=2, libelle='null'}, libelle='33', debit=40, credit=7}\n"
						+ "]}"
				);

	}

}
