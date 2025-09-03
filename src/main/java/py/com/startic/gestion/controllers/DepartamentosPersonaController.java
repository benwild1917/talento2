package py.com.startic.gestion.controllers;

import java.util.Collection;
import javax.annotation.PostConstruct;
import py.com.startic.gestion.models.DepartamentosPersona;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "departamentosPersonaController")
@ViewScoped
public class DepartamentosPersonaController extends AbstractController<DepartamentosPersona> {

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();
        setItems(this.ejbFacade.getEntityManager().createNamedQuery("DepartamentosPersona.findAll", DepartamentosPersona.class).getResultList());
    }

    public DepartamentosPersonaController() {
        // Inform the Abstract parent controller of the concrete DespachosPersona Entity
        super(DepartamentosPersona.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    @Override
    public Collection<DepartamentosPersona> getItems() {
        return super.getItems2();
    }

}
