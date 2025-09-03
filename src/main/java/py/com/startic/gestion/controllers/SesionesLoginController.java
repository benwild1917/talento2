package py.com.startic.gestion.controllers;


import javax.inject.Named;
import javax.faces.view.ViewScoped;
import py.com.startic.gestion.models.SesionesLogin;

@Named(value = "sesionesLoginController")
@ViewScoped
public class SesionesLoginController extends AbstractController<SesionesLogin> {

    public SesionesLoginController() {
        // Inform the Abstract parent controller of the concrete SesionesLogin Entity
        super(SesionesLogin.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

}
