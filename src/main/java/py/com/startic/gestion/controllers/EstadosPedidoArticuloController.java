package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.EstadosPedidoArticulo;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "estadosPedidoArticuloController")
@ViewScoped
public class EstadosPedidoArticuloController extends AbstractController<EstadosPedidoArticulo> {

    @Inject
    private EmpresasController empresaController;

    public EstadosPedidoArticuloController() {
        // Inform the Abstract parent controller of the concrete EstadosPedidoArticulo Entity
        super(EstadosPedidoArticulo.class);
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
     * Sets the "items" attribute with a collection of PedidosArticulo
     * entities that are retrieved from EstadosPedidoArticulo?cap_first and returns
     * the navigation outcome.
     *
     * @return navigation outcome for PedidosArticulo page
     */
    public String navigatePedidosArticuloCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("PedidosArticulo_items", this.getSelected().getPedidosArticuloCollection());
        }
        return "/pages/pedidosArticulo/index";
    }

}
