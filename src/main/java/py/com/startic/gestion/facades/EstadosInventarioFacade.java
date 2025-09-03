/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.facades;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import py.com.startic.gestion.models.EstadosInventario;
import py.com.startic.gestion.models.EstadosInventario;

/**
 *
 * @author eduardo
 */
@Stateless
public class EstadosInventarioFacade extends AbstractFacade<EstadosInventario> {

    @PersistenceContext(unitName = "gestionstarticPU")
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public EstadosInventarioFacade() {
        super(EstadosInventario.class);
    }
    
}
