package py.com.startic.gestion.controllers;



import java.util.Collection;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpSession;
import py.com.startic.gestion.models.DetallesPlanEstrategicas;
import py.com.startic.gestion.models.ObservacionesDocumentosJudiciales;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "observacionesDocumentosJudicialesController")
@ViewScoped
public class ObservacionesDocumentosJudicialesController extends AbstractController<ObservacionesDocumentosJudiciales> {
 private String paginaVolver;
  private DetallesPlanEstrategicas docOrigen;

    public String getPaginaVolver() {
        return paginaVolver;
    }

    public void setPaginaVolver(String paginaVolver) {
        this.paginaVolver = paginaVolver;
    }

    public DetallesPlanEstrategicas getDocOrigen() {
        return docOrigen;
    }

    public void setDocOrigen(DetallesPlanEstrategicas docOrigen) {
        this.docOrigen = docOrigen;
    }

   

    public ObservacionesDocumentosJudicialesController() {
        // Inform the Abstract parent controller of the concrete Bonificaciones Entity
        super(ObservacionesDocumentosJudiciales.class);
    }
     @PostConstruct
    @Override
    public void initParams() {
        super.initParams();

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        docOrigen = (DetallesPlanEstrategicas) session.getAttribute("doc_origen");

        session.removeAttribute("doc_origen");

        paginaVolver = (String) session.getAttribute("paginaVolver");

        session.removeAttribute("paginaVolver");

    }

    public String navigateVolver() {
        return paginaVolver;
    }
     @Override
    public Collection<ObservacionesDocumentosJudiciales> getItems() {
        ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
        return ejbFacade.getEntityManager().createNamedQuery("ObservacionesDocumentosJudiciales.findByDocumentoJudicial", ObservacionesDocumentosJudiciales.class).setParameter("detallePlanEstrategica", docOrigen).getResultList();
    }
 @Override
    public void save(ActionEvent event) {

        if (getSelected() != null) {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usu);
        }

        super.save(event);
    }
     @Override
    public void saveNew(ActionEvent event) {
        if (getSelected() != null) {

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usu);
            getSelected().setFechaHoraAlta(fecha);
            getSelected().setUsuarioAlta(usu);
            getSelected().setEmpresa(usu.getEmpresa());

            super.saveNew(event);

        }
    }
     
}
