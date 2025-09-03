package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.Profesiones;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "profesionesController")
@ViewScoped
public class ProfesionesController extends AbstractController<Profesiones> {

    @Inject
    private EmpresasController empresaController;

    public ProfesionesController() {
        // Inform the Abstract parent controller of the concrete TiposPersona Entity
        super(Profesiones.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }


}
