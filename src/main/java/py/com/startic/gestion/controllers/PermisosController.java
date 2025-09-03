package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.Permisos;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "permisosController")
@ViewScoped
public class PermisosController extends AbstractController<Permisos> {

    @Inject
    private EmpresasController empresaController;

    public PermisosController() {
        // Inform the Abstract parent controller of the concrete Permisos Entity
        super(Permisos.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }

    /**
     * Sets the "items" attribute with a collection of PermisosPorRoles entities
     * that are retrieved from Permisos?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for PermisosPorRoles page
     */
    public String navigatePermisosPorRolesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("PermisosPorRoles_items", this.getSelected().getPermisosPorRolesCollection());
        }
        return "/pages/permisosPorRoles/index";
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
