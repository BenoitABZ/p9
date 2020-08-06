package com.dummy.myerp.consumer.dao.impl.db.dao;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import com.dummy.myerp.technical.exception.NotFoundException;

import org.springframework.test.context.junit.jupiter.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import static java.sql.Date.valueOf;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(value = "/bootstrapContext.xml")
@Transactional
@Rollback(true)

public class ComptabiliteDaoImplIT {

	@Autowired
	private ComptabiliteDaoImpl comptabiliteDao;

	private EcritureComptable ecritureComptable;

	@BeforeEach
	public void initEcritureComptable() {

		ecritureComptable = new EcritureComptable();
				
	    LocalDate localDate = LocalDate.of(2020, 7, 2);
	        	       
	    Date date = valueOf(localDate);
		
		ecritureComptable.setId(-1000);

		ecritureComptable.setJournal(new JournalComptable("BQ", "Banque"));

		ecritureComptable.setReference("BQ-2020/00001");

		ecritureComptable.setDate(date);

		ecritureComptable.setLibelle("Ecriture comptable");

		ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401, "Fournisseurs"), "Ligne1",
				new BigDecimal("200.01"), new BigDecimal("100.00")));
		ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(512, "Banque"), "Ligne2",
				new BigDecimal("150.01"), new BigDecimal("150.00")));

	}

	@Test
	@Tag("getList")
	@DisplayName("test: récupération de la liste des comptes comptable")
	public void getListCompteComptable_shouldReturnAnonEmptyList() {

		List<CompteComptable> compteComptables = comptabiliteDao.getListCompteComptable();

		assertThat(compteComptables).isNotNull();

	}

	@Test
	@Tag("getList")
	@DisplayName("test: récupération de la liste des journaux comptable")
	public void getListJournalComptable_shouldReturnAnonEmptyList() {

		List<JournalComptable> journalComptables = comptabiliteDao.getListJournalComptable();

		assertThat(journalComptables).isNotNull();

	}

	@Test
	@Tag("getList")
	@DisplayName("test: récupération de la liste des ecritures comptable")
	public void getListEcritureComptable_shouldReturnAnonEmptyList() {

		List<EcritureComptable> ecritureComptables = comptabiliteDao.getListEcritureComptable();

		assertThat(ecritureComptables).isNotNull();

	}

	@Test
	@Tag("getUneEcritureComptable")
	@DisplayName("test: récupération d'une ecriture comptable par son id")
	public void getEcritureComptable_shouldReturnUneEcritureComptable_OfInteger() throws NotFoundException {

		String NombreLigneEcritureComptable = "3";

		StringBuffer ecExpected = new StringBuffer();

		ecExpected.append("-4")
		          .append("VE")
		          .append("VE-2016/00004")
		          .append("2016-12-28")
				  .append("TMA Appli Yyy")
				  .append(NombreLigneEcritureComptable)
		          .toString();
				
		StringBuffer ecActual = new StringBuffer();
		
		EcritureComptable ec = comptabiliteDao.getEcritureComptable(-4);
		
		ecActual.append(ec.getId())
                .append(ec.getJournal().getCode())
                .append(ec.getReference())
                .append(ec.getDate())
		        .append(ec.getLibelle())
		        .append(ec.getListLigneEcriture().size())
		        .toString();

		boolean isEcEquals = StringUtils.equals(ecExpected, ecActual);

		assertThat(isEcEquals).isTrue();

	}
	
	@Test
	@Tag("getUneEcritureComptable")
	@DisplayName("test: récupération d'une ecriture comptable par sa reference")
	public void getEcritureComptableByRef_shouldReturnUneEcritureComptable_OfString() throws NotFoundException {

		String NombreLigneEcritureComptable = "3";

		StringBuffer ecExpected = new StringBuffer();

		ecExpected.append("-4")
                  .append("VE")
                  .append("VE-2016/00004")
                  .append("2016-12-28")
		          .append("TMA Appli Yyy")
		          .append(NombreLigneEcritureComptable).toString();
		
		StringBuffer ecActual = new StringBuffer();
		
		EcritureComptable ec = comptabiliteDao.getEcritureComptableByRef("VE-2016/00004");
		
		ecActual.append(ec.getId())
                .append(ec.getJournal().getCode())
                .append(ec.getReference())
                .append(ec.getDate())
		        .append(ec.getLibelle())
		        .append(ec.getListLigneEcriture().size()).toString();
		
		boolean isEcEquals = StringUtils.equals(ecExpected, ecActual);

		assertThat(isEcEquals).isTrue();

	}
	
	@Test
	@DisplayName("test: récupération de la liste des lignes par ecriture comptable ")
	public void loadListLigneEcriture_shouldReturnListLigneEcritureInDb_OfEcritureComptable() {

		
		ecritureComptable.getListLigneEcriture().clear();;
		
		ecritureComptable.setId(-4);

		comptabiliteDao.loadListLigneEcriture(ecritureComptable);
		
		List<LigneEcritureComptable> linesAfterLoading = ecritureComptable.getListLigneEcriture();
		
		assertThat(linesAfterLoading).isNotEmpty();

	}
	
	@Test
	@Tag("insertion")
	@DisplayName("test: insertion d'une ecriture comptable ")
	public void insertEcritureComptable_shouldInsertEcritureComptableInDb_OfEcritureComptable() throws NotFoundException {
		
		comptabiliteDao.insertEcritureComptable(ecritureComptable);
		
		EcritureComptable ecritureComptableAferInsertion = comptabiliteDao.getEcritureComptableByRef("BQ-2020/00001");
				
		assertThat(ecritureComptableAferInsertion.toString()).isEqualTo(ecritureComptable.toString());

	}
	
	@Test
	@Tag("insertion")
	@DisplayName("test: insertion d'une sequence d'ecriture comptable ")
	public void insertSequenceEcritureComptable_shouldInsertSequenceEcritureComptableInDb_OfSequenceEcritureComptable() throws NotFoundException {
		
		int annee = 2016;
		
		int derniereValeur = 51;
		
		SequenceEcritureComptable seq = new SequenceEcritureComptable(annee, derniereValeur);
		
		comptabiliteDao.insertSequenceEcritureComptable(seq);
		
		EcritureComptable ecritureComptableAferInsertion = comptabiliteDao.getEcritureComptableByRef("BQ-2020/00001");
				
		assertThat(ecritureComptableAferInsertion.toString()).isEqualTo(ecritureComptable.toString());

	}
	
	
	
	
	@Test
	@DisplayName("test: maj d'une ecriture comptable ")
	public void updateEcritureComptable_shouldUpdateEcritureComptableInDb_OfEcritureComptable() throws NotFoundException {
		
		ecritureComptable.setId(-4);
		
		comptabiliteDao.updateEcritureComptable(ecritureComptable);
		
		EcritureComptable ecritureComptableAfterUpdate = comptabiliteDao.getEcritureComptable(-4);
						
		assertThat(ecritureComptableAfterUpdate.toString()).isEqualTo(ecritureComptable.toString());

	}
	
	@Test
	@Tag("delete")
	@DisplayName("test: suppression d'une ecriture comptable ")
	public void deleteEcritureComptable_shouldDeleteEcritureComptableInDb_OfInteger() throws NotFoundException {
		
		List<EcritureComptable> ListEcritureComptableBeforeDelete = comptabiliteDao.getListEcritureComptable();		
		
		comptabiliteDao.deleteEcritureComptable(-4);
		
		List<EcritureComptable> ListEcritureComptableAfterDelete = comptabiliteDao.getListEcritureComptable();
		
		assertThat(ListEcritureComptableAfterDelete.size()).isEqualTo(ListEcritureComptableBeforeDelete.size() - 1);

	}
	
	@Test
	@Tag("delete")
	@DisplayName("test: suppression d'une liste de lignes par ecriture comptable ")
	public void deleteListLigneEcritureComptable_shouldDeleteListLigneEcritureComptableInDb_OfEcritureComptable() throws NotFoundException {
			
		comptabiliteDao.deleteListLigneEcritureComptable(-4);
		
		List<LigneEcritureComptable> ListLigneEcritureComptableAfterDelete = comptabiliteDao.getEcritureComptable(-4).getListLigneEcriture();		
						
		assertThat(ListLigneEcritureComptableAfterDelete).isEmpty();

	}
	
	@Test
	@Tag("error")
	@DisplayName("test: exception notFoundException pour mauvais id ")
	public void getEcritureComptable_shouldReturnNotFoundException__OfInteger() {
		
		int id = -1000;
			
		NotFoundException nfe = assertThrows(NotFoundException.class, () -> comptabiliteDao.getEcritureComptable(id), "id non existant");
	    
		assertEquals("EcritureComptable non trouvée : id=" + id, nfe.getMessage());
	
	}
	
	@Test
	@Tag("error")
	@DisplayName("test: exception notFoundException pour mauvaise référence ")
	public void getEcritureComptable_shouldReturnNotFoundException__OfString() {
		
		String reference = "TT-2028/00001";
			
		NotFoundException nfe = assertThrows(NotFoundException.class, () -> comptabiliteDao.getEcritureComptableByRef(reference), "id non existant");
	    
		assertEquals("EcritureComptable non trouvée : reference=" + reference, nfe.getMessage());
	
	}
	


}
