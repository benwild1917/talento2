package py.com.startic.gestion.controllers;

import java.util.Collection;
import java.util.Date;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import py.com.startic.gestion.models.ObjetosGasto;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "objetosGastoController") 
@ViewScoped
public class ObjetosGastoController extends AbstractController<ObjetosGasto> {

    @Inject
    private EmpresasController empresaController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;

    public ObjetosGastoController() {
        // Inform the Abstract parent controller of the concrete objetosgasto Entity
        super(ObjetosGasto.class);

    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
    }

    @Override
    public Collection<ObjetosGasto> getItems() {
        return this.ejbFacade.getEntityManager().createNamedQuery("ObjetosGasto.findOrdered", ObjetosGasto.class).getResultList();
    }

    @Override
    public void save(ActionEvent event) {

        if (getSelected() != null) {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

            Date fecha = ejbFacade.getSystemDate();

            //   getSelected().setFechaHoraUltimoEstado(fecha);
            //  getSelected().setUsuarioUltimoEstado(usu);
        }

        super.save(event);
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

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

            Date fecha = ejbFacade.getSystemDate();

            /* getSelected().setFechaHoraUltimoEstado(fecha);
           getSelected().setUsuarioUltimoEstado(usu);
           getSelected().setFechaHoraAlta(fecha);
           getSelected().setUsuarioAlta(usu);
           getSelected().setEmpresa(usu.getEmpresa());
             */
            super.saveNew(event);
        }
        
     
        ///     JasperExportManager.exportReportToPdfFile(jasperPrint, "reporte.pdf");
    }
    
}

   
