package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.TiposDocumentosJudiciales;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "tiposDocumentosJudicialesController")
@ViewScoped
public class TiposDocumentosJudicialesController extends AbstractController<TiposDocumentosJudiciales> {

    @Inject
    private EmpresasController empresaController;

    public TiposDocumentosJudicialesController() {
        // Inform the Abstract parent controller of the concrete EstadosDocumento Entity
        super(TiposDocumentosJudiciales.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the Empresas controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareEmpresa(ActionEvent event) {
        if (this.getSelected() != null && empresaController.getSelected() == null) {
            empresaController.setSelected(this.getSelected().getEmpresa());
        }
    }
}
