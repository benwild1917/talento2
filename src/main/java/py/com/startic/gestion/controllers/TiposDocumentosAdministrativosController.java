package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.TiposDocumentosAdministrativos;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "tiposDocumentosAdministrativosController")
@ViewScoped
public class TiposDocumentosAdministrativosController extends AbstractController<TiposDocumentosAdministrativos> {


    public TiposDocumentosAdministrativosController() {
        // Inform the Abstract parent controller of the concrete EstadosDocumento Entity
        super(TiposDocumentosAdministrativos.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

}
