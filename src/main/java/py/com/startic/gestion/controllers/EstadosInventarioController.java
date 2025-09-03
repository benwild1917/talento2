package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.EstadosInventario;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "estadosInventarioController")
@ViewScoped
public class EstadosInventarioController extends AbstractController<EstadosInventario> {

    @Inject
    private EmpresasController empresaController;

    public EstadosInventarioController() {
        // Inform the Abstract parent controller of the concrete EstadosInventario Entity
        super(EstadosInventario.class);
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

    /**
     * Sets the "items" attribute with a collection of Inventarios entities that
     * are retrieved from EstadosInventario?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for Inventarios page
     */
    public String navigateInventariosCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Inventarios_items", this.getSelected().getInventariosCollection());
        }
        return "/pages5/inventarios/index";
    }

}
