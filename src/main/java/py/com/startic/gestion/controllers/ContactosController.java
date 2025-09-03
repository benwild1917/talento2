package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.Contactos;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "contactosController")
@ViewScoped
public class ContactosController extends AbstractController<Contactos> {


    public ContactosController() {
        // Inform the Abstract parent controller of the concrete Contactos Entity
        super(Contactos.class);
    }

}
