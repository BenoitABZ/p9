package com.dummy.myerp.consumer.dao.impl.db.rowmapper.comptabilite;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.dummy.myerp.consumer.dao.impl.cache.JournalComptableDaoCache;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import com.dummy.myerp.technical.exception.NotFoundException;

public class SequenceEcritureComptableRm implements RowMapper<SequenceEcritureComptable> {
	
    /** JournalComptableDaoCache */
    private final JournalComptableDaoCache journalComptableDaoCache = new JournalComptableDaoCache();

	@Override
	public SequenceEcritureComptable mapRow(ResultSet pRS, int pRowNum) throws SQLException {
		SequenceEcritureComptable vBean = new SequenceEcritureComptable();
		vBean.setAnnee(pRS.getInt("annee"));
		vBean.setDerniereValeur(pRS.getInt("derniere_valeur"));
		try {
			vBean.setJournalComptable(journalComptableDaoCache.getByCode(pRS.getString("journal_code")));
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		return vBean;
	}

}
