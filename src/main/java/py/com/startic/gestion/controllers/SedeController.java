package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.Empresas;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import py.com.startic.gestion.models.Sede;

@Named(value = "sedeController")
@ViewScoped
public class SedeController extends AbstractController<Sede> {


    public SedeController() {
        // Inform the Abstract parent controller of the concrete Empresas Entity
        super(Sede.class);
    }

    /**
     * Sets the "items" attribute with a collection of Departamentos entities
     * that are retrieved from Empresas?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for Departamentos page
     */
    public String navigateArticulosCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Articulos_items", this.getSelected().getArticulosCollection());
        }
        return "/pages/articulos/index";
    }
    
     /**
     * Sets the "items" attribute with a collection of Departamentos entities
     * that are retrieved from Empresas?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for Departamentos page
     */
    public String navigateEntradasArticuloCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("EntradasArticulo_items", this.getSelected().getEntradasArticulosCollection());
        }
        return "/pages/entradasArticulo/index";
    }

   
}
