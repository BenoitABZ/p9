package com.dummy.myerp.business.contrat;

import com.dummy.myerp.business.contrat.manager.ComptabiliteManager;
import com.dummy.myerp.business.impl.BusinessProxyImpl;


/**
 * <p>Interface du Proxy d'accès à la couche Business</p>
 */
public interface BusinessProxy {

    // ==================== Managers ====================

    /**
     * Renvoie le manager du package Comptabilite.
     *
     * @return ComptabiliteManager
     */
    ComptabiliteManager getComptabiliteManager();
    
}
