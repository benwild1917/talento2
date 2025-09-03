package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.EstadosDocumento;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "estadosDocumentoController")
@ViewScoped
public class EstadosDocumentoController extends AbstractController<EstadosDocumento> {

    @Inject
    private EmpresasController empresaController;

    public EstadosDocumentoController() {
        // Inform the Abstract parent controller of the concrete EstadosDocumento Entity
        super(EstadosDocumento.class);
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
