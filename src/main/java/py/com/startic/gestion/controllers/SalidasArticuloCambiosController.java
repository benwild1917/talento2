package py.com.startic.gestion.controllers;

import py.com.startic.gestion.models.SalidasArticuloCambios;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "salidasArticuloCambiosController")
@ViewScoped
public class SalidasArticuloCambiosController extends AbstractController<SalidasArticuloCambios> {


    public SalidasArticuloCambiosController() {
        // Inform the Abstract parent controller of the concrete DocumentosJudiciales Entity
        super(SalidasArticuloCambios.class);
    }

}
