/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.facades;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import py.com.startic.gestion.models.Formulario;
import py.com.startic.gestion.models.SalidasArticulo;

/**
 *
 * @author grecia
 */
@Stateless
public class FormularioFacade extends AbstractFacade<Formulario> {

    @PersistenceContext(unitName = "gestionstarticPU")
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public FormularioFacade() {
        super(Formulario.class);
    }
    
}
