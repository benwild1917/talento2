package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.UsuariosAsociados;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import py.com.startic.gestion.models.TiposObjetivos;

@Named(value = "tiposObjetivosController")
@ViewScoped
public class TiposObjetivosController extends AbstractController<TiposObjetivos> {

    public TiposObjetivosController() {
        // Inform the Abstract parent controller of the concrete FormPermisosPorRoles Entity
        super(TiposObjetivos.class);
    }

    
    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }
    

    /**
     * Store a new item in the data layer.
     *
     * @param event an event from the widget that wants to save a new Entity to
     * the data layer
     */
    @Override
    public void saveNew(ActionEvent event) {
        if (getSelected() != null) {
            super.saveNew(event);
        }

    }
}
