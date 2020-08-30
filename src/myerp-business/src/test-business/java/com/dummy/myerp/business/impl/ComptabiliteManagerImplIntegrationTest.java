package com.dummy.myerp.business.impl;

import static java.sql.Date.valueOf;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.TransactionUsageException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.assertj.core.api.Assertions.*;

import com.dummy.myerp.business.contrat.manager.ComptabiliteManager;
import com.dummy.myerp.business.impl.manager.ComptabiliteManagerImpl;
import com.dummy.myerp.consumer.dao.impl.DaoProxyImpl;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import com.dummy.myerp.technical.exception.TechnicalException;
import com.dummy.myerp.technical.exception.NotFoundException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(value = "/com/dummy/myerp/business/bootstrapContext.xml")
//@Transactional
//@Rollback(true)
public class ComptabiliteManagerImplIntegrationTest {

	@Autowired
	BusinessProxyImpl businessProxy;

	@MockBean
	DaoProxy daoProxy;

	@MockBean
	ComptabiliteDao comptabiliteDao;

	@SpyBean
	TransactionManager transactionManager;

	private ComptabiliteManager vComptabiliteManager;

	EcritureComptable vEcritureComptable;

	@BeforeEach
	public void initEcritureComptable() {

		vComptabiliteManager = businessProxy.getComptabiliteManager();

		vEcritureComptable = new EcritureComptable();

		LocalDate localDate = LocalDate.of(2016, 12, 28);

		Date date = valueOf(localDate);

		vEcritureComptable.setJournal(new JournalComptable("VE", "Vente"));

		vEcritureComptable.setReference("VE-2016/00001");

		vEcritureComptable.setDate(date);

		vEcritureComptable.setLibelle("libelle");

		vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(
				new CompteComptable(401, "Fournisseurs"), "Ligne1", new BigDecimal("200"), new BigDecimal("200")));
		vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(512, "Banque"),
				"Ligne2", new BigDecimal("150"), new BigDecimal("150")));

	}

	@Test
	public void insertEcritureComptable_shouldInsertEcritureComptable_OfEcritureComptable() throws Exception {

		List<JournalComptable> listJournalComptable = new ArrayList<JournalComptable>();
		listJournalComptable.add(new JournalComptable("VE", "Vente"));

		final ArgumentCaptor<EcritureComptable> ecritureComptableCaptor = ArgumentCaptor
				.forClass(EcritureComptable.class);

		when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);

		doReturn(listJournalComptable).when(comptabiliteDao).getListJournalComptable();

		when(comptabiliteDao.getEcritureComptableByRef(anyString())).thenThrow(NotFoundException.class);

		doNothing().when(comptabiliteDao).insertEcritureComptable(any(EcritureComptable.class));

		vComptabiliteManager.insertEcritureComptable(vEcritureComptable);

		verify(comptabiliteDao, times(1)).insertEcritureComptable(ecritureComptableCaptor.capture());

		EcritureComptable ecritureComptableActual = ecritureComptableCaptor.getValue();

		assertThat(ecritureComptableActual).extracting(EcritureComptable::getReference, EcritureComptable::getLibelle)
				.containsExactly(vEcritureComptable.getReference(), vEcritureComptable.getLibelle());

	}

	@Test
	public void updateEcritureComptable_shouldUpdateEcritureComptable_OfEcritureComptable() throws Exception {

		List<JournalComptable> listJournalComptable = new ArrayList<JournalComptable>();
		listJournalComptable.add(new JournalComptable("VE", "Vente"));

		final ArgumentCaptor<EcritureComptable> ecritureComptableCaptor = ArgumentCaptor
				.forClass(EcritureComptable.class);

		when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);

		doReturn(listJournalComptable).when(comptabiliteDao).getListJournalComptable();

		when(comptabiliteDao.getEcritureComptableByRef(anyString())).thenThrow(NotFoundException.class);

		doNothing().when(comptabiliteDao).updateEcritureComptable(any(EcritureComptable.class));

		when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);

		vComptabiliteManager.updateEcritureComptable(vEcritureComptable);

		verify(comptabiliteDao, times(1)).updateEcritureComptable(ecritureComptableCaptor.capture());

		EcritureComptable ecritureComptableActual = ecritureComptableCaptor.getValue();

		assertThat(ecritureComptableActual).extracting(EcritureComptable::getReference, EcritureComptable::getLibelle)
				.containsExactly(vEcritureComptable.getReference(), vEcritureComptable.getLibelle());

	}

	@Test
	public void deleteEcritureComptable_shouldDeleteEcritureComptable_OfEcritureComptable() throws Exception {

		int id = -3;

		when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);

		doNothing().when(comptabiliteDao).deleteEcritureComptable(any(Integer.class));

		vComptabiliteManager.deleteEcritureComptable(id);

		verify(comptabiliteDao, times(1)).deleteEcritureComptable(any(Integer.class));

	}

	@Test
	public void deleteEcritureComptable_shouldRollBackWhenRuntimeExceptionIsThrown_ofEcritureComptable()
			throws Exception {
		int id = -3;

		when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);

		doNothing().when(comptabiliteDao).deleteEcritureComptable(any(Integer.class));
		doThrow(new TransactionUsageException("probleme")).when(transactionManager)
				.commitMyERP(any(TransactionStatus.class));

		try {

			vComptabiliteManager.deleteEcritureComptable(id);

		} catch (TransactionUsageException te) {

		}

		verify(transactionManager, times(1)).rollbackMyERP(any(TransactionStatus.class));

	}

	@Test
	public void deleteEcritureComptable_shouldRollBackWhenTechnicalExceptionIsThrown_ofEcritureComptable()
			throws Exception {
		int id = -3;

		when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);

		// doNothing().when(transactionManager).commitMyERP(any(TransactionStatus.class));
		try {
			doThrow(TechnicalException.class).when(comptabiliteDao).deleteEcritureComptable(any(Integer.class));

			vComptabiliteManager.deleteEcritureComptable(id);

		} catch (TechnicalException te) {

		}

		verify(transactionManager, times(1)).rollbackMyERP(any(TransactionStatus.class));

	}

	/*
	 * 
	 * @Test public void
	 * addReference_shouldAddReferenceEcritureComptable_OfEcritureComptable() throws
	 * Exception {
	 * 
	 * 
	 * vEcritureComptable.setReference(null);
	 * 
	 * vComptabiliteManager.addReference(vEcritureComptable);
	 * 
	 * assertThat(vEcritureComptable.getReference()).isEqualTo("VE-2016/00042");
	 * 
	 * }
	 * 
	 * @Test public void
	 * addReference_shouldCreateSequenceEcritureComptable_OfEcritureComptable()
	 * throws Exception {
	 * 
	 * 
	 * 
	 * vEcritureComptable.setReference(null); SequenceEcritureComptable
	 * sequenceExpected = new SequenceEcritureComptable(2016,
	 * vEcritureComptable.getJournal());
	 * 
	 * vComptabiliteManager.addReference(vEcritureComptable);
	 * 
	 * SequenceEcritureComptable sequenceActual =
	 * comptabiliteDao.getSequenceEcritureComptable(2016,
	 * vEcritureComptable.getJournal());
	 * 
	 * assertThat(sequenceActual.getDerniereValeur()).isEqualTo(52);
	 * 
	 * }
	 * 
	 * @Test public void
	 * checkEcritureComptable_shouldThrowNothing_OfEcritureComptable() throws
	 * Exception {
	 * 
	 * 
	 * 
	 * vComptabiliteManager.checkEcritureComptable(vEcritureComptable);
	 * 
	 * }
	 * 
	 * 
	 * 
	 * @Test public void
	 * insertEcritureComptable_shouldInsertEcritureComptable_OfEcritureComptable()
	 * throws Exception {
	 * 
	 * 
	 * 
	 * ComptabiliteManager vComptabiliteManager =
	 * businessProxy.getComptabiliteManager();
	 * 
	 * List<EcritureComptable> listEcritureComptableBeforeInsertion =
	 * vComptabiliteManager.getListEcritureComptable();
	 * 
	 * vComptabiliteManager.insertEcritureComptable(vEcritureComptable);
	 * 
	 * List<EcritureComptable> listEcritureComptableAfterInsertion =
	 * vComptabiliteManager.getListEcritureComptable();
	 * 
	 * assertThat(listEcritureComptableAfterInsertion.size() -
	 * 1).isEqualTo(listEcritureComptableBeforeInsertion.size());
	 * 
	 * }
	 * 
	 * 
	 * 
	 * @Test public void
	 * updateEcritureComptable_shouldUpdateEcritureComptable_OfEcritureComptable()
	 * throws Exception {
	 * 
	 * vEcritureComptable.setId(-3);
	 * 
	 * vComptabiliteManager.updateEcritureComptable(vEcritureComptable);
	 * 
	 * EcritureComptable ecritureComptableUpdated =
	 * comptabiliteDao.getEcritureComptable(-3);
	 * 
	 * assertThat(ecritureComptableUpdated.toString()).isEqualTo(vEcritureComptable.
	 * toString());
	 * 
	 * }
	 * 
	 * @Test public void
	 * deleteEcritureComptable_shouldDeleteEcritureComptable_OfEcritureComptable()
	 * throws Exception {
	 * 
	 * List<EcritureComptable> listEcritureComptableBeforeDelete =
	 * vComptabiliteManager.getListEcritureComptable();
	 * 
	 * EcritureComptable ecritureComptableDeleted =
	 * comptabiliteDao.getEcritureComptable(-3);
	 * 
	 * vComptabiliteManager.deleteEcritureComptable(ecritureComptableDeleted);
	 * 
	 * List<EcritureComptable> listEcritureComptableAfterDelete =
	 * vComptabiliteManager.getListEcritureComptable();
	 * 
	 * assertThat(listEcritureComptableAfterDelete +
	 * 1).isEqualTo(listEcritureComptableBeforeDelete);
	 * 
	 * }
	 * 
	 */

}