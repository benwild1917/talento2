package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.SituacionesLaboral;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "situacionesLaboralController")
@ViewScoped
public class SituacionesLaboralController extends AbstractController<SituacionesLaboral> {

    public SituacionesLaboralController() {
        // Inform the Abstract parent controller of the concrete SituacionLaboral Entity
        super(SituacionesLaboral.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }
}
