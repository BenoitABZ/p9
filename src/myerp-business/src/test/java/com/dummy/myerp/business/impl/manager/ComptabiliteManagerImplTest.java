package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.*;
import org.springframework.transaction.TransactionStatus;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.dummy.myerp.business.contrat.manager.ComptabiliteManager;
import com.dummy.myerp.business.impl.BusinessProxyImpl;
import com.dummy.myerp.business.impl.TransactionManager;
import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import com.dummy.myerp.technical.exception.TechnicalException;

@ExtendWith(MockitoExtension.class)
public class ComptabiliteManagerImplTest {

	@Mock
	DaoProxy daoProxy;

	@Mock
	ComptabiliteDao comptabiliteDao;
	
	@Mock
	TransactionManager transactionManager;
	
	//@Mock
	//TransactionManager transactionManager;
	
	private EcritureComptable vEcritureComptable;

	private ComptabiliteManager manager;

	//TransactionManager transactionManager = TransactionManager.getInstance();

	/*
	 * @BeforeAll public void setUp() {
	 * 
	 * 
	 * 
	 * // AbstractBusinessManager.configure(null, daoProxy, transactionManager);
	 * 
	 * }
	 */

	@BeforeEach
	public void initEcritureComptable() {

		MockitoAnnotations.initMocks(this);

		BusinessProxyImpl businessProxy = BusinessProxyImpl.getInstance(daoProxy, transactionManager);
		manager = businessProxy.getComptabiliteManager();

		vEcritureComptable = new EcritureComptable();
		vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
		vEcritureComptable.setDate(new Date());
		vEcritureComptable.setLibelle("Libelle");
		vEcritureComptable.getListLigneEcriture().add(
				new LigneEcritureComptable(new CompteComptable(1, "Libelle1"), "Libelle1", new BigDecimal(123), null));
		vEcritureComptable.getListLigneEcriture().add(
				new LigneEcritureComptable(new CompteComptable(2, "Libelle2"), "Libelle2", null, new BigDecimal(123)));

	}

	@Test
	public void addReference_shouldAddReferenceWithoutException_ofEcritureComptable() throws Exception {
		String referenceExpected = "AC-2020/00002";
		SequenceEcritureComptable sequence = new SequenceEcritureComptable(2020, 1,
				new JournalComptable("AC", "Achat"));

		when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
		when(comptabiliteDao.getSequenceEcritureComptable(anyInt(), any(JournalComptable.class))).thenReturn(sequence);
		doNothing().when(comptabiliteDao).updateSequenceEcritureComptable(any(SequenceEcritureComptable.class));

		manager.addReference(vEcritureComptable);

		assertThat(vEcritureComptable.getReference()).isEqualTo(referenceExpected);

	}


	@Test
	public void addReference_shouldAddReferenceWhenNotFoundExceptionIsThrown_ofEcritureComptable() throws Exception {
		String referenceExpected = "AC-2020/00001";

		when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
		when(comptabiliteDao.getSequenceEcritureComptable(anyInt(), any(JournalComptable.class)))
				.thenThrow(NotFoundException.class);
		doNothing().when(comptabiliteDao).insertSequenceEcritureComptable(any(SequenceEcritureComptable.class));

		manager.addReference(vEcritureComptable);

		assertThat(vEcritureComptable.getReference()).isEqualTo(referenceExpected);

	}

	@Test
	public void checkEcritureComptableUnit() throws Exception {

		ZoneId defaultZoneId = ZoneId.systemDefault();
		LocalDate localDate = LocalDate.of(2016, 12, 27);
		Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());

		EcritureComptable ecritureComptable = new EcritureComptable();
		ecritureComptable .setJournal(new JournalComptable("VE", "Vente"));
		ecritureComptable .setDate(date);
		ecritureComptable .setLibelle("Libelle");
		ecritureComptable .setReference("VE-2016/00004");
		ecritureComptable .getListLigneEcriture().add(
				new LigneEcritureComptable(new CompteComptable(1, "Libelle1"), "Libelle1", new BigDecimal(123), null));
		ecritureComptable .getListLigneEcriture().add(
				new LigneEcritureComptable(new CompteComptable(2, "Libelle2"), "Libelle2", null, new BigDecimal(123)));
		
       when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
       
       List<JournalComptable> listJournalComptable = new ArrayList<JournalComptable>();
	   listJournalComptable.add(new JournalComptable("VE", "Vente"));
	
		doReturn(listJournalComptable).when(comptabiliteDao).getListJournalComptable();
		
		when(comptabiliteDao.getEcritureComptableByRef(anyString())).thenThrow(NotFoundException.class);

		manager.checkEcritureComptable(ecritureComptable);

	}

	

	@Test
	public void checkEcritureComptableUnitViolation_shouldThrowFunctionalException_whenReferenceFormatIsNotCorrect()
			throws Exception {
		vEcritureComptable.setReference("xxxxxxx");
		FunctionalException fe = assertThrows(FunctionalException.class,
				() -> manager.checkEcritureComptableUnitViolation(vEcritureComptable));

		assertEquals(
				"L'écriture comptable ne respecte pas les règles de gestion: format de la reference incorect: LL-DDDD/NNNNN",
				fe.getMessage());
	}

	@Test
	public void checkEcritureComptableUnitViolation_shouldThrowFunctionalException_whenMontantComptableFormatIsNotCorrect()
			throws Exception {

		vEcritureComptable.getListLigneEcriture().clear();
		vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(3, "Libelle3"),
				"Libelle3", new BigDecimal("122.222"), null));
		vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(4, "Libelle4"),
				"Libelle4", null, new BigDecimal("122.222")));

		vEcritureComptable.setReference("AC-2020/00001");

		FunctionalException fe = assertThrows(FunctionalException.class,
				() -> manager.checkEcritureComptableUnitViolation(vEcritureComptable));

		assertEquals(
				"L'écriture comptable ne respecte pas les règles de gestion: un montant comptable doit etre composé de 13 chiffres + 2 chiffres max apres la virgule",
				fe.getMessage());
	}

	public void checkEcritureComptableUnitRG2() throws Exception {
		vEcritureComptable.getListLigneEcriture().clear();
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(2), null, null, new BigDecimal(1234)));
		FunctionalException fe = assertThrows(FunctionalException.class,
				() -> manager.checkEcritureComptableUnitRG2(vEcritureComptable));

		assertEquals("L'écriture comptable n'est pas équilibrée.", fe.getMessage());
	}

	@Test
	public void checkEcritureComptableUnitRG3() throws Exception {
		vEcritureComptable.getListLigneEcriture().clear();
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
		FunctionalException fe = assertThrows(FunctionalException.class,
				() -> manager.checkEcritureComptableUnitRG3(vEcritureComptable));

		assertEquals(
				"L'écriture comptable doit avoir au moins deux lignes : une ligne au débit et une ligne au crédit.",
				fe.getMessage());
	}

	@Test
	public void checkEcritureComptableUnitRG5_shouldthrowFunctionalException_whenAnneeReferenceIsDifferentFromAnneeEcritureComptable()
			throws Exception {
		vEcritureComptable.setReference("AC-2019/00001");
		FunctionalException fe = assertThrows(FunctionalException.class,
				() -> manager.checkEcritureComptableUnitRG5(vEcritureComptable));

		assertEquals(
				"Format et contenu de la référence : l'année dans la reference ne correspond pas à la date d'ecriture",
				fe.getMessage());
	}

	@Test
	public void checkEcritureComptableUnitRG5_shouldthrowFunctionalException_whenJournalCodeReferenceIsDifferentFromJournalCodeEcritureComptable()
			throws Exception {
		vEcritureComptable.setReference("BC-2020/00001");
		FunctionalException fe = assertThrows(FunctionalException.class,
				() -> manager.checkEcritureComptableUnitRG5(vEcritureComptable));

		assertEquals(
				"Format et contenu de la référence : le code journal dans la reference ne correspond pas au journal de l'écriture.",
				fe.getMessage());
	}

	@Test
	public void checkEcritureComptableUnitRG5_isJournalComptableExistingInDb_shouldthrowFunctionalException_whenJournalComptableIsNotFoundInDb()
			throws Exception {
		List<JournalComptable> listJournalComptable = new ArrayList<JournalComptable>();
		listJournalComptable.add(new JournalComptable("BC", "Banque"));

		when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
		doReturn(listJournalComptable).when(comptabiliteDao).getListJournalComptable();

		FunctionalException fe = assertThrows(FunctionalException.class,
				() -> manager.checkEcritureComptableUnitRG5_isJournalComptableExistingInDb(vEcritureComptable));

		assertEquals("Format et contenu de la référence : le journal comptable ayant pour code=AC n'existe pas en BDD",
				fe.getMessage());
	}

	@Test
	public void checkEcritureComptableUnitRG5_shouldthrowFunctionalException_whenReferenceIsNull() throws Exception {
		vEcritureComptable.setReference(null);

		FunctionalException fe = assertThrows(FunctionalException.class,
				() -> manager.checkEcritureComptableUnitRG5(vEcritureComptable));

		assertEquals("La référence ne peut pas être null.", fe.getMessage());
	}

	@Test
	public void checkEcritureComptableUnitRG6_shouldthrowFunctionalException_whenReferenceConstraintNotUnique()
			throws Exception {
		vEcritureComptable.setReference("AC-2020/00001");

		when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
		when(comptabiliteDao.getEcritureComptableByRef(anyString())).thenReturn(vEcritureComptable);

		FunctionalException fe = assertThrows(FunctionalException.class,
				() -> manager.checkEcritureComptableContext(vEcritureComptable));

		assertEquals("Une autre écriture comptable existe déjà avec la même référence.", fe.getMessage());
	}

	
}
