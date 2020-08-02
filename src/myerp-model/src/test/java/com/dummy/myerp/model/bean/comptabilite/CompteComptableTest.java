package com.dummy.myerp.model.bean.comptabilite;

import static org.assertj.core.api.Assertions.*;


import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CompteComptableTest {

	CompteComptable compteComptableExpected;

	Integer numero;

	@BeforeEach
	public void init() {

		numero = 512;

		compteComptableExpected = new CompteComptable(numero, "Banque");

	}

	@Test
	@DisplayName("test: getByNumero")
	public void getByNumero_shouldReturnCompteComptable_OfInteger() {

		List<CompteComptable> listCompteComptable = Arrays.asList(compteComptableExpected);

		CompteComptable compteComptable1Actual = CompteComptable.getByNumero(listCompteComptable, 0);

		assertThat(compteComptable1Actual).isEqualTo(compteComptableExpected);

	}

	@Test
	public void toString_shouldReturnSummaryString() {

		assertThat(compteComptableExpected.toString()).isEqualTo("CompteComptable{numero=512, libelle='Banque'}");

	}

}
