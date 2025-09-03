package py.com.startic.gestion.controllers;


import javax.inject.Named;
import javax.faces.view.ViewScoped;
import py.com.startic.gestion.models.FirmasArticulosSalida;

@Named(value = "firmasArticulosSalidaController")
@ViewScoped
public class FirmasArticulosSalidaController extends AbstractController<FirmasArticulosSalida> {

    public FirmasArticulosSalidaController() {
        // Inform the Abstract parent controller of the concrete Roles Entity
        super(FirmasArticulosSalida.class);
    }
}
