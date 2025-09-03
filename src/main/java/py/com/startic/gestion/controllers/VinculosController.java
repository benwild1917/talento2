package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.Vinculos;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "vinculosController")
@ViewScoped
public class VinculosController extends AbstractController<Vinculos> {


    public VinculosController() {
        // Inform the Abstract parent controller of the concrete Vinculos Entity
        super(Vinculos.class);
    }
     
}
