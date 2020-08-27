package com.dummy.myerp.consumer.dao.impl.db.rowmapper.comptabilite;

import java.sql.ResultSet;

import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import com.dummy.myerp.consumer.ConsumerHelper;
import com.dummy.myerp.consumer.dao.impl.cache.JournalComptableDaoCache;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.technical.exception.NotFoundException;

/**
 * {@link RowMapper} de {@link EcritureComptable}
 */
public class EcritureComptableRM implements RowMapper<EcritureComptable> {

    /** JournalComptableDaoCache */
    private final JournalComptableDaoCache journalComptableDaoCache = new JournalComptableDaoCache();


    @Override
    public EcritureComptable mapRow(ResultSet pRS, int pRowNum) throws SQLException {
        EcritureComptable vBean = new EcritureComptable();
        vBean.setId(pRS.getInt("id"));
        try {
			vBean.setJournal(journalComptableDaoCache.getByCode(pRS.getString("journal_code")));
			
    }catch(NullPointerException npe) {
        	
        	System.out.println("ici");
        	
        
        	
        	
        } catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        vBean.setReference(pRS.getString("reference"));
        vBean.setDate(pRS.getDate("date"));
        vBean.setLibelle(pRS.getString("libelle"));

        // Chargement des lignes d'écriture
        ConsumerHelper.getDaoProxy().getComptabiliteDao().loadListLigneEcriture(vBean);
    
        
        

        return vBean;
    }
}
