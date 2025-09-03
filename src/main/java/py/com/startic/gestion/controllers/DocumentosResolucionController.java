package py.com.startic.gestion.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.poi.util.IOUtils;
import org.primefaces.model.file.UploadedFile;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.models.Archivos;
import py.com.startic.gestion.models.ArchivosResolucion;
import py.com.startic.gestion.models.CanalesEntradaDocumentoResolucion;
import py.com.startic.gestion.models.Departamentos;
import py.com.startic.gestion.models.DocumentosResolucion;
import py.com.startic.gestion.models.EntradasDocumentosResolucion;
import py.com.startic.gestion.models.Estados;
import py.com.startic.gestion.models.EstadosDocumento;
import py.com.startic.gestion.models.ObservacionesDocumentosJudiciales;
import py.com.startic.gestion.models.ParametrosSistema;
import py.com.startic.gestion.models.Personas;
import py.com.startic.gestion.models.TiposDocumentosJudiciales;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "documentosResolucionController")
@ViewScoped
public class DocumentosResolucionController extends AbstractController<DocumentosResolucion> {

    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private DepartamentosController departamentoController;
    @Inject
    private EmpresasController empresaController;
    @Inject
    private EntradasDocumentosResolucionController entradasDocumentosResolucionController;
    @Inject
    private ArchivosResolucionController archivosController;
    @Inject
    private EstadosDocumentoController estadosDocumentoController;
    @Inject
    private EstadosDocumentoController estadoController;
    @Inject
    private CanalesEntradaDocumentoResolucionController canalesEntradaDocumentoResolucionController;
    @Inject
    private TiposDocumentosJudicialesController tiposDocumentosJudicialesController;
    private UploadedFile file;
    private ArchivosResolucion docImprimir;
    private Collection<DocumentosResolucion> detalles;
    private String descripcion;
    private String nroResolucion;
    private EntradasDocumentosResolucion entradaDocumentoResolucion;
    private TiposDocumentosJudiciales tipoDocumentoJudicial;
    private TiposDocumentosJudiciales tipoDoc;
    private CanalesEntradaDocumentoResolucion canal;
    private Departamentos departamento;
    private String nombre;
    private String content;
    private String endpoint;
    private String url;
    private ParametrosSistema par;
    private HttpSession session;
    private Usuarios usuario;
    private Date fechaDesde;
    private Date fechaHasta;
    private Date fechaAltaDesde;
    private Date fechaAltaHasta;
    private String pantalla;
    private SimpleDateFormat formatAno = new SimpleDateFormat("yy");
    private boolean busquedaPorFechaAlta;

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public ArchivosResolucion getDocImprimir() {
        return docImprimir;
    }

    public void setDocImprimir(ArchivosResolucion docImprimir) {
        this.docImprimir = docImprimir;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public EntradasDocumentosResolucion getEntradaDocumentoResolucion() {
        return entradaDocumentoResolucion;
    }

    public void setEntradaDocumentoResolucion(EntradasDocumentosResolucion entradaDocumentoResolucion) {
        this.entradaDocumentoResolucion = entradaDocumentoResolucion;
    }

    public TiposDocumentosJudiciales getTipoDocumentoJudicial() {
        return tipoDocumentoJudicial;
    }

    public void setTipoDocumentoJudicial(TiposDocumentosJudiciales tipoDocumentoJudicial) {
        this.tipoDocumentoJudicial = tipoDocumentoJudicial;
    }

    public Departamentos getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamentos departamento) {
        this.departamento = departamento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNroResolucion() {
        return nroResolucion;
    }

    public void setNroResolucion(String nroResolucion) {
        this.nroResolucion = nroResolucion;
    }

    public Date getFechaAltaDesde() {
        return fechaAltaDesde;
    }

    public void setFechaAltaDesde(Date fechaAltaDesde) {
        this.fechaAltaDesde = fechaAltaDesde;
    }

    public Date getFechaAltaHasta() {
        return fechaAltaHasta;
    }

    public void setFechaAltaHasta(Date fechaAltaHasta) {
        this.fechaAltaHasta = fechaAltaHasta;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public TiposDocumentosJudiciales getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(TiposDocumentosJudiciales tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public Collection<DocumentosResolucion> getDetalles() {
        return detalles;
    }

    public void setDetalles(Collection<DocumentosResolucion> detalles) {
        this.detalles = detalles;
    }
    

    @PostConstruct
    @Override
    public void initParams() {
        departamento = departamentoController.prepareCreate(null);
        departamento.setId(43); // Secretaria General
        canal = canalesEntradaDocumentoResolucionController.prepareCreate(null);
        canal.setCodigo("SE");
        tipoDoc = tiposDocumentosJudicialesController.prepareCreate(null);
        tipoDoc.setCodigo(Constantes.TIPO_DOCUMENTO_JUDICIAL_DR);
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        usuario = (Usuarios) session.getAttribute("Usuarios");
        //fechaAltaDesde = ejbFacade.getSystemDateOnly(-30);
        Calendar myCal = Calendar.getInstance();
        myCal.set(Calendar.YEAR, 2025);
        myCal.set(Calendar.MONTH, 1);
        myCal.set(Calendar.DAY_OF_MONTH, 1);
        fechaAltaDesde = myCal.getTime();
        fechaAltaHasta = ejbFacade.getSystemDateOnly();

        pantalla = Constantes.NO;

        buscarPorFechaAlta();
        super.initParams();

    }

    public DocumentosResolucionController() {
        // Inform the Abstract parent controller of the concrete Archivos Entity
        super(DocumentosResolucion.class);
    }
     @Override
     
    public DocumentosResolucion prepareCreate(ActionEvent event) {
        DocumentosResolucion doc = super.prepareCreate(event);

        return doc;
    }
     @Override
     
    public Collection<DocumentosResolucion> getItems() {
        return super.getItems2();
    }

    public String datePattern() {
        return "dd/MM/yyyy";
    }

    public String customFormatDate(Date date) {
        if (date != null) {
            DateFormat format = new SimpleDateFormat(datePattern());
            return format.format(date);
        }
        return "";
    }

    public boolean deshabilitarVerDoc(DocumentosResolucion doc) {
        if (doc != null) {
            List<ArchivosResolucion> lista = ejbFacade.getEntityManager().createNamedQuery("ArchivosResolucion.findByDocumentoResolucionOrdered", ArchivosResolucion.class).setParameter("documentoResolucion", doc).getResultList();

            return lista.isEmpty();

        }
        return true;
    }

   

    public void prepareVerDoc(DocumentosResolucion doc) {

        List<ArchivosResolucion> lista = ejbFacade.getEntityManager().createNamedQuery("ArchivosResolucion.findByDocumentoJudicialOrdered", ArchivosResolucion.class).setParameter("documentoResolucion", doc).getResultList();

        if (!lista.isEmpty()) {
            docImprimir = lista.get(0);
        }

        //PrimeFaces.current().ajax().update("ExpAcusacionesViewForm");
    }

    public void prepareCerrarDialogoVerDoc() {
        if (docImprimir != null) {
            File f = new File(Constantes.RUTA_ARCHIVOS_TEMP + "/tmp/" + nombre);
            f.delete();

            docImprimir = null;
        }
    }

    public void prepareAlzarArchivo() {
        descripcion = "";
    }

    public void buscarPorFechaPresentacion() {
        if (fechaDesde == null || fechaHasta == null) {
            JsfUtil.addErrorMessage("Debe ingresar Rango de Fechas");
        } else {
            setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosResolucion.findOrderedDptoTipoDoc", DocumentosResolucion.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", fechaHasta).setParameter("departamento", departamento).setParameter("tipoDocumentoJudicial", tipoDocumentoJudicial).getResultList());
        }
    }

   
    public void resetParents() {
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
        empresaController.setSelected(null);
    }

    public void buscarPorFechaAlta() {
        if (fechaAltaDesde == null || fechaAltaHasta == null) {
            JsfUtil.addErrorMessage("Debe ingresar Rango de Fechas");
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaAltaHasta);
            cal.add(Calendar.DATE, 1);
            Date nuevaFechaHasta = cal.getTime();
            busquedaPorFechaAlta = true;
            setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosResolucion.findOrderedFechaAlta", DocumentosResolucion.class).setParameter("fechaDesde", fechaAltaDesde).setParameter("fechaHasta", nuevaFechaHasta).setParameter("canalEntradaDocumentoResolucion", canal).getResultList());

        }
    }

    public void saveNew() {
        if (getSelected() != null) {

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);
            getSelected().setFechaHoraAlta(fecha);
            getSelected().setUsuarioAlta(usuario);
            getSelected().setCanalEntradaDocumentoResolucion(canal);
            getSelected().setTipoDocumentoJudicial(tipoDoc);
            getSelected().setResponsable(usuario);
            getSelected().setDepartamento(usuario.getDepartamento());
            //  getSelected().setMostrarWeb("SI");

            EstadosDocumento estado = estadoController.prepareCreate(null);
            estado.setCodigo("0");

            getSelected().setEstado(estado);

            if (file != null) {
                if (file.getContent().length > 0) {

                    byte[] bytes = null;
                    try {
                        bytes = IOUtils.toByteArray(file.getInputStream());
                    } catch (IOException ex) {
                    }

                }

            }

            EntradasDocumentosResolucion doc = entradasDocumentosResolucionController.prepareCreate(null);

            doc.setFechaHoraUltimoEstado(fecha);
            doc.setUsuarioUltimoEstado(usuario);
            doc.setFechaHoraAlta(fecha);
            doc.setUsuarioAlta(usuario);
            javax.persistence.Query query = ejbFacade.getEntityManager().createNativeQuery(
                    "select ifnull(max(CONVERT(substring(nro_identificacion,6),UNSIGNED INTEGER)),0) as VALOR \n"
                    + "from entradas_documentos_resolucion WHERE substring(nro_identificacion,1,4) = 'AUTO';", NroResolucion.class);

            NroResolucion cod = (NroResolucion) query.getSingleResult();

            doc.setNroIdentificacion("AUTO-" + String.valueOf(cod.getCodigo() + 1));

            //entradasDocumentosJudicialesController.setSelected(doc);
            //entradasDocumentosJudicialesController.saveNew(null);
            getSelected().setEntradaDocumentoResolucion(doc);

            super.saveNew(null);        

            // super.saveNew2(null);
            if (busquedaPorFechaAlta) {
                /*
                if (fechaAltaDesde == null) {
                    fechaAltaDesde = ejbFacade.getSystemDateOnly(-30);
                }
                if (fechaAltaHasta == null) {
                    fechaAltaHasta = ejbFacade.getSystemDateOnly();
                }
                 */
                buscarPorFechaAlta();
            } else {

                if (fechaDesde == null) {
                    fechaDesde = ejbFacade.getSystemDateOnly(-30);
                }
                if (fechaHasta == null) {
                    fechaHasta = ejbFacade.getSystemDateOnly();
                }

                buscarPorFechaPresentacion();

            }
           
        }
    }
    public void save() {

        if (getSelected() != null) {
            boolean guardar = true;

            Date fecha = ejbFacade.getSystemDate();
           
             
                getSelected().setFechaHoraUltimoEstado(fecha);
                getSelected().setUsuarioUltimoEstado(usuario);
                

                if (file != null && guardar) {
                    if (file.getContent().length > 0) {

                        byte[] bytes = null;
                        try {
                            bytes = IOUtils.toByteArray(file.getInputStream());
                        } catch (IOException ex) {
                        }

                       

                    }
                }



            if (guardar) {
                super.save(null);
                //actualizarPersonas(fecha);
            } else {

                if (busquedaPorFechaAlta) {
                    /*
                    if (fechaAltaDesde == null) {
                        fechaAltaDesde = ejbFacade.getSystemDateOnly(-30);
                    }
                    if (fechaAltaHasta == null) {
                        fechaAltaHasta = ejbFacade.getSystemDateOnly();
                    }
                     */
                    buscarPorFechaAlta();
                } else {

                    if (fechaDesde == null) {
                        fechaDesde = ejbFacade.getSystemDateOnly(-30);
                    }
                    if (fechaHasta == null) {
                        fechaHasta = ejbFacade.getSystemDateOnly();
                    }

                    buscarPorFechaPresentacion();
                }
            }
        }
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

}
