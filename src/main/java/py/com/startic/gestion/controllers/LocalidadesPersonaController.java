package py.com.startic.gestion.controllers;

import java.util.Collection;
import javax.annotation.PostConstruct;
import py.com.startic.gestion.models.LocalidadesPersona;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "localidadesPersonaController")
@ViewScoped
public class LocalidadesPersonaController extends AbstractController<LocalidadesPersona> {

    @Inject
    private CargosPersonaController cargoPersonaController;
    @Inject
    private EmpresasController empresaController;

    public LocalidadesPersonaController() {
        // Inform the Abstract parent controller of the concrete DespachosPersona Entity
        super(LocalidadesPersona.class);
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();
        setItems(this.ejbFacade.getEntityManager().createNamedQuery("LocalidadesPersona.findAll", LocalidadesPersona.class).getResultList());
    }
    
    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        cargoPersonaController.setSelected(null);
        empresaController.setSelected(null);
    }


    /**
     * Sets the "selected" attribute of the Empresas controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareEmpresa(ActionEvent event) {
        LocalidadesPersona selected = this.getSelected();
        if (selected != null && empresaController.getSelected() == null) {
            empresaController.setSelected(selected.getEmpresa());
        }
    }

    @Override
    public Collection<LocalidadesPersona> getItems() {
        return super.getItems2();
    }

}
