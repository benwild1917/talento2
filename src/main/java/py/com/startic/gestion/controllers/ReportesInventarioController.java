package py.com.startic.gestion.controllers;


import py.com.startic.gestion.datasource.ReportesInventario;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

@Named(value = "reportesInventarioController")
@ViewScoped
public class ReportesInventarioController extends AbstractController<ReportesInventario> {


    public ReportesInventarioController() {
        // Inform the Abstract parent controller of the concrete ReportesInventario Entity
        super(ReportesInventario.class);
    }

}
