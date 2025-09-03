package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.TiposEnvio;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

@Named(value = "tiposEnvioController")
@ViewScoped
public class TiposEnvioController extends AbstractController<TiposEnvio> {

    @Inject
    private EmpresasController empresaController;

    public TiposEnvioController() {
        // Inform the Abstract parent controller of the concrete TiposEnvio Entity
        super(TiposEnvio.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }
}
