package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

import com.dummy.myerp.business.contrat.manager.ComptabiliteManager;
import com.dummy.myerp.business.impl.AbstractBusinessManager;
import com.dummy.myerp.business.impl.BusinessProxyImpl;
import com.dummy.myerp.business.impl.TransactionManager;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.consumer.dao.impl.DaoProxyImpl;
import com.dummy.myerp.consumer.dao.impl.cache.JournalComptableDaoCache;
import com.dummy.myerp.consumer.dao.impl.db.dao.ComptabiliteDaoImpl;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;

@ExtendWith(MockitoExtension.class)
public class ComptabiliteManagerImplTest {

    @Mock (answer = Answers.RETURNS_DEEP_STUBS)
    static DaoProxyImpl daoProxy;
    
    @Mock (answer = Answers.RETURNS_DEEP_STUBS)
    ComptabiliteDaoImpl comptabiliteDao;

    @Mock (answer = Answers.RETURNS_DEEP_STUBS)
    SequenceEcritureComptable sequenceEcritureComptable;
    
    @Mock (answer = Answers.RETURNS_DEEP_STUBS)
    JournalComptable journalComptable;

	@Mock
	static TransactionManager transactionManager;
	
	@Mock
	JournalComptableDaoCache journalComptableDaoCache;

	static ComptabiliteManager manager;
	
	EcritureComptable vEcritureComptable;

	@BeforeAll
	public static void setUp() {


		BusinessProxyImpl businessProxy = BusinessProxyImpl.getInstance(daoProxy, transactionManager);

		manager = businessProxy.getComptabiliteManager();
	

	}

	@BeforeEach
	public void initEcritureComptable() {
		
		EcritureComptable vEcritureComptable = new EcritureComptable();
		vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
		vEcritureComptable.setDate(new Date());
		vEcritureComptable.setLibelle("Libelle");
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(2), null, null, new BigDecimal(123)));

	}

	@Test
	public void addReference_shouldAddReference_ofEcritureComptable() throws Exception {

		String referenceExpected = "AC-2020/00002";
		try {
		SequenceEcritureComptable sequence = new SequenceEcritureComptable(2020, 1,
				new JournalComptable("AC", "Achat"));
	
        
		when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
		
        
				
		when(comptabiliteDao.getSequenceEcritureComptable(2020, any(JournalComptable.class)))
				.thenReturn(sequence);

		manager.addReference(vEcritureComptable);
		
}catch(NullPointerException nep) {
        	
        	System.out.println("probleme");
        }

		assertThat(vEcritureComptable.getReference()).isEqualTo(referenceExpected);

	}

	@Test
	public void addReference_shouldAddReferenceWhenNotFoundExceptionIsThrown_ofEcritureComptable() throws Exception {

		String referenceExpected = "AC-2020/00001";
		
		JournalComptable journalComptable = vEcritureComptable.getJournal();

		when(daoProxy.getComptabiliteDao().getSequenceEcritureComptable(2020, journalComptable))
				.thenThrow(NotFoundException.class);

		manager.addReference(vEcritureComptable);

		assertThat(vEcritureComptable.getReference()).isEqualTo(referenceExpected);

	}

	@Test
	public void checkEcritureComptableUnit() throws Exception {

		manager.checkEcritureComptableUnit(vEcritureComptable);
	}

	@Test
	public void checkEcritureComptableUnitViolation() throws Exception {

		EcritureComptable ecritureComptableError = new EcritureComptable();

		FunctionalException fe = assertThrows(FunctionalException.class,
				() -> manager.checkEcritureComptableUnit(ecritureComptableError));

		assertEquals("L'écriture comptable ne respecte pas les règles de gestion.", fe.getMessage());
	}

	public void checkEcritureComptableUnitRG2() throws Exception {

		vEcritureComptable.getListLigneEcriture().clear();
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
		vEcritureComptable.getListLigneEcriture()
				.add(new LigneEcritureComptable(new CompteComptable(2), null, null, new BigDecimal(1234)));
		FunctionalException fe = assertThrows(FunctionalException.class,
				() -> manager.checkEcritureComptableUnit(vEcritureComptable));

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
				() -> manager.checkEcritureComptableUnit(vEcritureComptable));

		assertEquals("L'écriture comptable doit avoir au moins deux lignes : une ligne au débit et une ligne au crédit.", fe.getMessage());
	}
	
	@Test
	public void checkEcritureComptableUnitRG5_shouldthrowFunctionalException_whenAnneeReferenceIsDifferentFromAnneeEcritureComptable() throws Exception {
		
		vEcritureComptable.setReference("AC-2019/00001");
		FunctionalException fe = assertThrows(FunctionalException.class,
				() -> manager.checkEcritureComptableUnit(vEcritureComptable));

		assertEquals("Format et contenu de la référence : l'année dans la reference ne correspond pas à la date d'ecriture", fe.getMessage());
	}
	
	@Test
	public void checkEcritureComptableUnitRG5_shouldthrowFunctionalException_whenJournalCodeReferenceIsDifferentFromJournalCodeEcritureComptable() throws Exception {
		
		vEcritureComptable.setReference("BC-2020/00001");
		FunctionalException fe = assertThrows(FunctionalException.class,
				() -> manager.checkEcritureComptableUnit(vEcritureComptable));

		assertEquals("Format et contenu de la référence : le code journal dans la reference ne correspond pas au journal de l'écriture.", fe.getMessage());
	}
	
	@Test
	public void checkEcritureComptableUnitRG5_shouldthrowFunctionalException_whenJournalComptableIsNotFoundInDb() throws Exception {
		
		when(journalComptableDaoCache.getByCode(vEcritureComptable.getJournal().getCode()).getCode())
		.thenThrow(NotFoundException.class);
		
		FunctionalException fe = assertThrows(FunctionalException.class,
				() -> manager.checkEcritureComptableUnit(vEcritureComptable));

		assertEquals("Format et contenu de la référence : le journal comptable ayant pour code=AC n'existe pas en BDD", fe.getMessage());
	}
	
	@Test
	public void checkEcritureComptableUnitRG5_shouldthrowFunctionalException_whenReferenceIsNull() throws Exception {
		
		vEcritureComptable.setReference(null);
		
		FunctionalException fe = assertThrows(FunctionalException.class,
				() -> manager.checkEcritureComptableUnit(vEcritureComptable));

		assertEquals("La référence ne peut pas être null.", fe.getMessage());
	}
	
	@Test
	public void checkEcritureComptableUnitRG6_shouldthrowFunctionalException_whenReferenceConstraintNotUnique() throws Exception {
		
		vEcritureComptable.setReference("AC-2020/00001");
		
		when(daoProxy.getComptabiliteDao().getEcritureComptableByRef(vEcritureComptable.getReference())).thenReturn(vEcritureComptable);
		
		FunctionalException fe = assertThrows(FunctionalException.class,
				() -> manager.checkEcritureComptableContext(vEcritureComptable));

		assertEquals("Une autre écriture comptable existe déjà avec la même référence.", fe.getMessage());
	}

}
