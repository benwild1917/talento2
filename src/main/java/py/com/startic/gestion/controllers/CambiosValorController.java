package py.com.startic.gestion.controllers;

import java.util.Collection;
import java.util.Date;
import javax.annotation.PostConstruct;


import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import py.com.startic.gestion.models.CambiosValor;
import py.com.startic.gestion.models.Programacion;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "cambiosValorController")
@ViewScoped
public class CambiosValorController extends AbstractController<CambiosValor> {

    @Inject
    private ProgramacionController programacionController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private EmpresasController empresaController;
    private Programacion docOrigen;
    private String paginaVolver;

    public Programacion getDocOrigen() {
        return docOrigen;
    }

    public void setDocOrigen(Programacion docOrigen) {
        this.docOrigen = docOrigen;
    }

    public String getPaginaVolver() {
        return paginaVolver;
    }

    public void setPaginaVolver(String paginaVolver) {
        this.paginaVolver = paginaVolver;
    }
    

    public CambiosValorController() {
        // Inform the Abstract parent controller of the concrete ObservacionesDocumentosJudiciales Entity
        super(CambiosValor.class);
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        docOrigen = (Programacion) session.getAttribute("doc_origen");

        session.removeAttribute("doc_origen");

        paginaVolver = (String) session.getAttribute("paginaVolver");

        session.removeAttribute("paginaVolver");

    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        programacionController.setSelected(null);
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
        empresaController.setSelected(null);
    }
/*
    @Override
    public ObservacionesDocumentosJudiciales prepareCreate(ActionEvent event) {
        ObservacionesDocumentosJudiciales obsDoc = super.prepareCreate(event);

        if (docOrigen != null) {
            obsDoc.setDocumentoJudicial(docOrigen);
        }

        return obsDoc;
    }
    /**
     * Sets the "selected" attribute of the Usuarios controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareUsuarioAlta(ActionEvent event) {
        if (this.getSelected() != null && usuarioAltaController.getSelected() == null) {
            usuarioAltaController.setSelected(this.getSelected().getUsuarioAlta());
        }
    }

    /**
     * Sets the "selected" attribute of the Usuarios controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareUsuarioUltimoEstado(ActionEvent event) {
        if (this.getSelected() != null && usuarioUltimoEstadoController.getSelected() == null) {
            usuarioUltimoEstadoController.setSelected(this.getSelected().getUsuarioUltimoEstado());
        }
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
     @Override
    public Collection<CambiosValor> getItems() {
        ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
        return ejbFacade.getEntityManager().createNamedQuery("CambiosValor.findByProgramacion", CambiosValor.class).setParameter("programacion", docOrigen).getResultList();
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

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usu);
            getSelected().setFechaHoraAlta(fecha);
            getSelected().setUsuarioAlta(usu);
            getSelected().setEmpresa(usu.getEmpresa());

            super.saveNew(event);
/*
            if (docOrigen != null) {

                docOrigen.setObservacionDocumentoJudicial(getSelected());
                docOrigen.setUltimaObservacion(getSelected().getObservacion());

                documentoJudicialController.save(event);
            }
*/
            // setItems(ejbFacade.getEntityManager().createNamedQuery("ObservacionesDocumentosJudiciales.findByDocumentoJudicial", ObservacionesDocumentosJudiciales.class).setParameter("documentoJudicial", docOrigen).getResultList());

            // setSelected(ejbFacade.getEntityManager().createNamedQuery("ObservacionesBienes.findById", ObservacionesBienes.class).setParameter("id", getSelected().getId()).getSingleResult());
        }
    }
}
