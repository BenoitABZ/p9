package com.dummy.myerp.model.bean.comptabilite;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.dummy.myerp.technical.exception.NotFoundException;

class JournalComptableTest {

	JournalComptable journalComptableExpected;

	String code;

	@BeforeEach
	public void init() {

		code = "BQ";

		journalComptableExpected = new JournalComptable(code, "journal de banque");

	}

	@Test
	@DisplayName("test: getByCode")
	public void getByCode_shouldReturnJournalComptable_ofInteger() throws NotFoundException {

		List<JournalComptable> journalComptableList = Arrays.asList(journalComptableExpected);

		JournalComptable journalComptableActual = journalComptableExpected.getByCode(journalComptableList, code);

		assertThat(journalComptableActual).isEqualTo(journalComptableExpected);

	}

	@Test
	public void toString_shouldReturnSummaryString() {

		assertThat(journalComptableExpected.toString())
				.isEqualTo("JournalComptable{code='BQ', libelle='journal de banque'}");

	}

}
