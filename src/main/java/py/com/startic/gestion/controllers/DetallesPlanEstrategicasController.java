package py.com.startic.gestion.controllers;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import py.com.startic.gestion.models.Actividades;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.poi.util.IOUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.models.Acciones;
import py.com.startic.gestion.models.Archivos;
import py.com.startic.gestion.models.CambiosValor;
import py.com.startic.gestion.models.DetallesPlanEstrategicas;
import py.com.startic.gestion.models.EstadosActividad;
import py.com.startic.gestion.models.EstadosInforme;
import py.com.startic.gestion.models.FlujosDocumento;
import py.com.startic.gestion.models.ObservacionesDocumentosJudiciales;
import py.com.startic.gestion.models.ParametrosSistema;
import py.com.startic.gestion.models.PlanEstrategicas;
import py.com.startic.gestion.models.Programacion;
import py.com.startic.gestion.models.TiposDocumentosJudiciales;
import py.com.startic.gestion.models.TiposObjetivos;
import py.com.startic.gestion.models.UsuarioTiposDocumentos;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "detallesPlanEstrategicasController")
@ViewScoped
public class DetallesPlanEstrategicasController extends AbstractController<DetallesPlanEstrategicas> {

    @Inject
    private PlanEstrategicasController planEstrategicasController;
    @Inject
    private CambiosValorController cambiosValorController;
    @Inject
    private ArchivosController archivosController;
    @Inject
    private ObservacionesDocumentosJudicialesController obsController;
    @Inject
    private ProgramacionController programacionController;
    @Inject
    private DetallesPlanEstrategicasController detallesPlanEstrategicasController;
    @Inject
    private TiposDocumentosJudicialesController tiposDocumentosJudicialesController;
    private final FiltroURL filtroURL = new FiltroURL();
    private List<Acciones> listaAcciones;
    private TiposObjetivos tiposObjetivos;
    private Acciones accion;
    private UploadedFile file;
    private String descripcionArchivo;
    private Usuarios usuario;
    List<Archivos> listaArchivos;
    private List<TiposDocumentosJudiciales> tiposDoc;
    List<DetallesPlanEstrategicas> listaDetallesPlanEstrategica;
    private UsuarioTiposDocumentos usuarioTiposDocumentos;
    private Integer activeTab;
    private HttpSession session;
    private DetallesPlanEstrategicas detallesPlanEstrategica;
    private PlanEstrategicas detallesSelected;
    private Programacion programacionSelected;
    private Programacion programacion;
    private FlujosDocumento flujoDoc;
    private TiposDocumentosJudiciales tipoDoc;
    private EstadosInforme estInforme;
    private Collection<Usuarios> listaUsuarios;
    private Collection<Archivos> detalles;
    private String ultimaObservacion;
    private Archivos docImprimir;
    private String nombre;
    private String content;
    private String url;
    private ParametrosSistema par;
    private double resultado;
    private double variable2;
    private double variable;
    private double valor;
    private double valorVariable;
    private double programaPresupuestario;
    private double presupuesto;
    private double resultadoPresupuesto;
    private double nuevaVariable;
    private EstadosActividad estadoActividad;
    private List<CambiosValor> detallesCambios;
    private TiposDocumentosJudiciales tipoDocumentoJudicial;
    private Date fechaDesde;
    private Date fechaHasta;
    private String menu;
    private String endpoint;
    private Actividades actividad;
    private Collection<DetallesPlanEstrategicas> filtrado;

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();
        detallesPlanEstrategica = prepareCreate(null);
       // tipoDoc = tiposDocumentosJudicialesController.prepareCreate(null);
        TiposDocumentosJudiciales tipoDoc = new TiposDocumentosJudiciales();
        tipoDoc.setCodigo(Constantes.TIPO_DOCUMENTO_MISIONAL_MI);
        TiposDocumentosJudiciales tipoDoc2 = new TiposDocumentosJudiciales();
        tipoDoc2.setCodigo(Constantes.TIPO_DOCUMENTO_JUDICIAL_JU);

        tiposDoc = new ArrayList<>();
        tiposDoc.add(tipoDoc);
        tiposDoc.add(tipoDoc2);
        detallesSelected = null;
        listaDetallesPlanEstrategica = null;
        usuario = null;

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        int pos = url.lastIndexOf(uri);
        url = url.substring(0, pos);

        String[] array = uri.split("/");
        endpoint = array[1];
        par = ejbFacade.getEntityManager().createNamedQuery("ParametrosSistema.findById", ParametrosSistema.class).setParameter("id", Constantes.PARAMETRO_ID).getSingleResult();
        try {
            tipoDocumentoJudicial = this.ejbFacade.getEntityManager().createNamedQuery("TiposDocumentosJudiciales.findByCodigo", TiposDocumentosJudiciales.class).setParameter("codigo", Constantes.TIPO_DOCUMENTO_MISIONAL_MI).getSingleResult();
        } catch (Exception ex) {
            ex.printStackTrace();
            JsfUtil.addErrorMessage("Error de configuracion. No se puede iniciar pantalla");
            return;
        }

        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        usuario = (Usuarios) session.getAttribute("Usuarios");
        activeTab = 0;
        menu = Constantes.NO;

        setItems(ejbFacade.getEntityManager().createNamedQuery("DetallesPlanEstrategicas.findOrdered", DetallesPlanEstrategicas.class).getResultList());
        if (!getItems2().isEmpty()) {
            DetallesPlanEstrategicas art = getItems2().iterator().next();
            setSelected(art);

        } else {
            setSelected(null);
        }
    }

    @Override
    public DetallesPlanEstrategicas prepareCreate(ActionEvent event) {
        programacionSelected = null;
        valorVariable = 0;
        //activeTab = 0;
        return super.prepareCreate(event);

    }

    public void prepareEdit() {
        ultimaObservacion = null;
        file = null;
    }

    public List<Acciones> getListaAcciones() {
        return listaAcciones;
    }

    public void setListaAcciones(List<Acciones> listaAcciones) {
        this.listaAcciones = listaAcciones;
    }

    public Acciones getAccion() {
        return accion;
    }

    public void setAccion(Acciones accion) {
        this.accion = accion;
    }

    public TiposObjetivos getTiposObjetivos() {
        if (session.getAttribute("tipoSelected") != null) {
            tiposObjetivos = (TiposObjetivos) session.getAttribute("tipoSelected");
            session.removeAttribute("tipoSelected");
        }
        return tiposObjetivos;
    }

    public void setTiposObjetivos(TiposObjetivos tiposObjetivos) {
        this.tiposObjetivos = tiposObjetivos;
    }

    public DetallesPlanEstrategicasController() {
        // Inform the Abstract parent controller of the concrete Actividades Entity
        super(DetallesPlanEstrategicas.class);
        seleccionarCambiosValor();
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getDescripcionArchivo() {
        return descripcionArchivo;
    }

    public void prepareTransferir() {
        // listaUsuarios = prepareTransferir(getSelected());
    }

    public boolean deshabilitarVerObs() {
        return deshabilitarCamposAdm();
    }

    private boolean deshabilitarCamposAdm() {
        return filtroURL.verifPermiso("verTodosDocsAdm");
    }

    public void setDescripcionArchivo(String descripcionArchivo) {
        this.descripcionArchivo = descripcionArchivo;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public List<Archivos> getListaArchivos() {
        return listaArchivos;
    }

    public void setListaArchivos(List<Archivos> listaArchivos) {
        this.listaArchivos = listaArchivos;
    }

    public Integer getActiveTab() {
        return activeTab;
    }

    public void setActiveTab(Integer activeTab) {
        this.activeTab = activeTab;
    }

    public String datePattern3() {
        return "dd/MM/yyyy HH:mm:ss";
    }

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public DetallesPlanEstrategicas getDetallesPlanEstrategica() {
        return detallesPlanEstrategica;
    }

    public void setDetallesPlanEstrategica(DetallesPlanEstrategicas detallesPlanEstrategica) {
        this.detallesPlanEstrategica = detallesPlanEstrategica;
    }

    public UsuarioTiposDocumentos getUsuarioTiposDocumentos() {
        return usuarioTiposDocumentos;
    }

    public void setUsuarioTiposDocumentos(UsuarioTiposDocumentos usuarioTiposDocumentos) {
        this.usuarioTiposDocumentos = usuarioTiposDocumentos;
    }

    public TiposDocumentosJudiciales getTipoDocumentoJudicial() {
        return tipoDocumentoJudicial;
    }

    public void setTipoDocumentoJudicial(TiposDocumentosJudiciales tipoDocumentoJudicial) {
        this.tipoDocumentoJudicial = tipoDocumentoJudicial;
    }

    public List<TiposDocumentosJudiciales> getTiposDoc() {
        return tiposDoc;
    }

    public void setTiposDoc(List<TiposDocumentosJudiciales> tiposDoc) {
        this.tiposDoc = tiposDoc;
    }

    public List<CambiosValor> getDetallesCambios() {
        return detallesCambios;
    }

    public void setDetallesCambios(List<CambiosValor> detallesCambios) {
        this.detallesCambios = detallesCambios;
    }

    public FlujosDocumento getFlujoDoc() {
        return flujoDoc;
    }

    public void setFlujoDoc(FlujosDocumento flujoDoc) {
        this.flujoDoc = flujoDoc;
    }

    public TiposDocumentosJudiciales getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(TiposDocumentosJudiciales tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public EstadosInforme getEstInforme() {
        return estInforme;
    }

    public void setEstInforme(EstadosInforme estInforme) {
        this.estInforme = estInforme;
    }

    public Collection<Archivos> getDetalles() {
        return detalles;
    }

    public void setDetalles(Collection<Archivos> detalles) {
        this.detalles = detalles;
    }

    public String getUltimaObservacion() {
        return ultimaObservacion;
    }

    public void setUltimaObservacion(String ultimaObservacion) {
        this.ultimaObservacion = ultimaObservacion;
    }

    public Archivos getDocImprimir() {
        return docImprimir;
    }

    public void setDocImprimir(Archivos docImprimir) {
        this.docImprimir = docImprimir;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ParametrosSistema getPar() {
        return par;
    }

    public void setPar(ParametrosSistema par) {
        this.par = par;
    }

    public double getResultado() {
        return resultado;
    }

    public void setResultado(double resultado) {
        this.resultado = resultado;
    }

    public double getVariable2() {
        return variable2;
    }

    public void setVariable2(double variable2) {
        this.variable2 = variable2;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getValorVariable() {
        return valorVariable;
    }

    public void setValorVariable(double valorVariable) {
        this.valorVariable = valorVariable;
    }

    public double getProgramaPresupuestario() {
        return programaPresupuestario;
    }

    public void setProgramaPresupuestario(double programaPresupuestario) {
        this.programaPresupuestario = programaPresupuestario;
    }

    public double getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(double presupuesto) {
        this.presupuesto = presupuesto;
    }

    public double getVariable() {
        return variable;
    }

    public void setVariable(double variable) {
        this.variable = variable;
    }

    public double getResultadoPresupuesto() {
        return resultadoPresupuesto;
    }

    public void setResultadoPresupuesto(double resultadoPresupuesto) {
        this.resultadoPresupuesto = resultadoPresupuesto;
    }

    public void prepareVerDoc(Archivos doc) {
        docImprimir = doc;

        //PrimeFaces.current().ajax().update("ExpAcusacionesViewForm");
    }

    public EstadosActividad getEstadoActividad() {
        return estadoActividad;
    }

    public void setEstadoActividad(EstadosActividad estadoActividad) {
        this.estadoActividad = estadoActividad;
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

    public double getNuevaVariable() {
        return nuevaVariable;
    }

    public void setNuevaVariable(double nuevaVariable) {
        this.nuevaVariable = nuevaVariable;
    }

    public Programacion getProgramacion() {
        return programacion;
    }

    public void setProgramacion(Programacion programacion) {
        this.programacion = programacion;
    }

    public Actividades getActividad() {
        return actividad;
    }

    public void setActividad(Actividades actividad) {
        this.actividad = actividad;
    }

    public PlanEstrategicas getDetallesSelected() {
        return detallesSelected;
    }

    public void setDetallesSelected(PlanEstrategicas detallesSelected) {
        this.detallesSelected = detallesSelected;
    }

    public List<DetallesPlanEstrategicas> getListaDetallesPlanEstrategica() {
        return listaDetallesPlanEstrategica;
    }

    public void setListaDetallesPlanEstrategica(List<DetallesPlanEstrategicas> listaDetallesPlanEstrategica) {
        this.listaDetallesPlanEstrategica = listaDetallesPlanEstrategica;
    }

    public Programacion getProgramacionSelected() {
        return programacionSelected;
    }

    public void setProgramacionSelected(Programacion programacionSelected) {
        this.programacionSelected = programacionSelected;
    }

    public boolean deshabilitarAgregarObs() {
        return deshabilitarCamposAdm();
    }

    public boolean renderedBorrarArchivo() {
        return filtroURL.verifPermiso("borrarArchivo");
    }

    public boolean renderedCargarDocumentosyEvidencia() {
        return filtroURL.verifPermiso("verTodoLosDocumentos");
    }

    public Collection<DetallesPlanEstrategicas> getFiltrado() {
        return filtrado;
    }

    public void setFiltrado(Collection<DetallesPlanEstrategicas> filtrado) {
        this.filtrado = filtrado;
    }

    @Override
    public Collection<DetallesPlanEstrategicas> getItems() {
        return super.getItems2();
    }

    public Collection<Usuarios> getListaUsuarios() {
        if (getSelected() != null && getSelected().getEstado() != null && Constantes.ASIGNADO.equals(getSelected().getEstado().getCodigo())) {

            if (getSelected().getTipoDocumentos() != null) {

                listaUsuarios = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferirDpto", Usuarios.class).setParameter("tipoDocumento", getSelected().getTipoDocumentos().getCodigo()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).getResultList();
            } else {
                listaUsuarios = new ArrayList<>();
            }

        } else {
            if (getSelected().getTipoDocumentos() != null && getSelected().getEstado() != null) {
                listaUsuarios = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferirPedido", Usuarios.class).setParameter("tipoDocumento", getSelected().getTipoDocumentos().getCodigo()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).getResultList();
            } else {
                listaUsuarios = new ArrayList<>();
            }
        }
        return listaUsuarios;

    }

    public boolean desabilitarBotonAdjuntarArchivos() {
        if (getSelected() != null) {
            if (!getSelected().getEstado().getCodigo().equals("7")) {
                return true;
            }

            if (!getSelected().getResponsable().equals(usuario)) {
                return true;
            }

            try {
                this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuarioRol", FlujosDocumento.class).setParameter("usuario", usuario.getId()).setParameter("rol", -18).getSingleResult();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return true;
    }

    public String rowClass(DetallesPlanEstrategicas item) {
        return (item.getResponsable().equals(usuario)) ? ((item.getTipoDocumentos().getCodigo().equals("MI")) ? "white" : "green") : "";
    }

    public boolean deshabilitarBorrarArchivo(Archivos item) {
        if (filtroURL.verifPermiso("borrarArchivo")) {
            if (item != null) {
                if (getSelected() != null) {
                    if (getSelected().getResponsable().equals(usuario)) {
                        if (item.getFechaHoraAlta() != null) {
                            if (item.getUsuarioAlta().equals(usuario)) {
                                return false;
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    public void borrarArchivo(Archivos item) {
        if (filtroURL.verifPermiso("borrarArchivo")) {
            if (item != null) {
                if (getSelected() != null) {
                    if (getSelected().getResponsable().equals(usuario)) {
                        if (item.getUsuarioAlta() != null) {
                            if (item.getUsuarioAlta().equals(usuario)) {

                                item.setUsuarioBorrado(usuario);
                                item.setFechaHoraBorrado(ejbFacade.getSystemDate());

                                archivosController.setSelected(item);
                                archivosController.save(null);

                                archivosController.delete(null);
                                obtenerArchivos();
                            } else {
                                JsfUtil.addErrorMessage("No se puede borrar el archivo");
                            }
                        } else {
                            JsfUtil.addErrorMessage("No se puede borrar el archivo");
                        }
                    } else {
                        JsfUtil.addErrorMessage("No se puede borrar el archivo");
                    }
                } else {
                    JsfUtil.addErrorMessage("No se puede borrar el archivo");
                }
            } else {
                JsfUtil.addErrorMessage("No se puede borrar el archivo");
            }
        } else {
            JsfUtil.addErrorMessage("No se puede borrar el archivo");
        }
    }

    public boolean desabilitarBotonCalcular() {
        if (getSelected() != null) {
            if (!getSelected().getEstado().getCodigo().equals("5")) {
                return true;
            }

            if (!getSelected().getResponsable().equals(usuario)) {
                return true;
            }

            try {
                this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuarioRol", FlujosDocumento.class).setParameter("usuario", usuario.getId()).setParameter("rol", 32).getSingleResult();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return true;
    }

    public void setListaUsuarios(Collection<Usuarios> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public void cambiarEstado() {

        if (getSelected().getEstado() != null) {

            Date fecha = ejbFacade.getSystemDate();

            EstadosInforme estado = null;

            try {
                // Codigo de enviado a secretaria
                estado = this.ejbFacade.getEntityManager().createNamedQuery("EstadosInforme.findByCodigo", EstadosInforme.class
                ).setParameter("codigo", getSelected().getEstado().getCodigo()).getSingleResult();

            } catch (Exception ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo determinar sgte estado");
                return;
            }
            if (estado.getCodigo().equals("1")) {
                getSelected().setEstado(estado);
                getSelected().setFechaHoraUltimoEstado(fecha);
                getSelected().setResponsable(usuario);
                getSelected().setUsuarioUltimoEstado(usuario);

            }

            getSelected().setEstado(estado);
            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);

            super.save(null);

        }
    }

    public String customFormatDate3(Date date) {
        if (date != null) {
            DateFormat format = new SimpleDateFormat(datePattern3());
            return format.format(date);
        }
        return "";
    }

    public void prepareObs() {
        ultimaObservacion = null;
        if (getSelected() != null) {
            getSelected().setUltimaObservacionAux(null);
            getSelected().setUltimaObservacion(null);
        }
    }

    public String navigateObservacionesDocumentosJudicialesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("doc_origen", getSelected());
            //FacesContext
            //        .getCurrentInstance().getExternalContext().getRequestMap().put("ObservacionesDocumentosJudiciales_items", ejbFacade.getEntityManager().createNamedQuery("ObservacionesDocumentosJudiciales.findByDocumentoJudicial", ObservacionesDocumentosJudiciales.class
            //        ).setParameter("documentoJudicial", getSelected()).getResultList());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paginaVolver", "/pages/accionOperativa/index");
        }
        return "/pages/observacionesDocumentosJudiciales/index";
    }

    public void agregarObs() {

        if (getSelected() != null) {
            if (getSelected().verifObs()) {
                Date fecha = ejbFacade.getSystemDate();

                getSelected().setFechaUltimaObservacion(fecha);

                getSelected().transferirObs();

                getSelected().setUsuarioUltimaObservacion(usuario);

                ObservacionesDocumentosJudiciales obs = obsController.prepareCreate(null);

                obs.setUsuarioAlta(usuario);
                obs.setUsuarioUltimoEstado(usuario);
                obs.setFechaHoraAlta(fecha);
                obs.setFechaHoraUltimoEstado(fecha);
                obs.setEmpresa(usuario.getEmpresa());
                obs.setObservacion(getSelected().getUltimaObservacionAux());

                obsController.setSelected(obs);
                obsController.saveNew(null);

                getSelected().setObservacionDocumentoJudicial(obs);

                super.save(null);
                obs.setDetallePlanEstrategica(getSelected());

                obsController.save(null);
            }

        }
    }

    public void guardarCambio() {

        if (getSelected() != null) {
            //if (getSelected().getVariable2() != 0) {
            valorVariable = getSelected().getPlanEstrategica().getProgramacion().getValorVariable();
            variable2 = getSelected().getPlanEstrategica().getVariable2();
            variable = (valor + variable2);
            // resultado = (variable / valorVariable) * 100;
            resultado = ((valor + variable2) / valorVariable) * 100;

            getSelected().setValor(valor);
            // getSelected().setValorVariable(valorVariable);
            getSelected().setVariable2(variable);
            getSelected().setValorVariable(valorVariable);
            getSelected().setResultado(Math.round(resultado));
            getSelected().setEstado(new EstadosInforme("3"));
            getSelected().setResponsable(usuario);

            detallesPlanEstrategicasController.setSelected(getSelected());
            detallesPlanEstrategicasController.save(null);
            PlanEstrategicas plan = ejbFacade.getEntityManager().createNamedQuery("PlanEstrategicas.findById", PlanEstrategicas.class
            ).setParameter("id", getSelected().getPlanEstrategica().getId()).getSingleResult();
            if (getSelected().getResultado() < 30) {
                plan.setEstado(new EstadosActividad("RO"));

            }
            if (getSelected().getResultado() > 30) {
                plan.setEstado(new EstadosActividad("NA"));
            }
            if (getSelected().getResultado() == 100) {
                plan.setEstado(new EstadosActividad("VE"));

            }

            plan.setValor(valor);
            // plan.setValorVariable(Long.MAX_VALUE);
            plan.setVariable2(variable);
            plan.setResultado(Math.round(resultado));
            planEstrategicasController.setSelected(plan);
            planEstrategicasController.save(null);

            // } else {
            //    JsfUtil.addErrorMessage("No hay ningun cambio que registrar");
            // }
        } else {
            JsfUtil.addErrorMessage("No hay ningun cambio que registrar");
        }
    }

    public void guardarPrepuestoActual() {

        if (getSelected() != null) {
            // if (getSelected().getVariable2() != valor) {
            programaPresupuestario = getSelected().getPlanEstrategica().getProgramacion().getProgramaPresupuestario();
            variable2 = getSelected().getPlanEstrategica().getVariable2();
            variable = (valor + variable2);
            resultadoPresupuesto = (variable / programaPresupuestario) * 100;
            getSelected().setValor(valor);
            getSelected().setVariable2(variable);
            getSelected().setResultado(Math.round(resultadoPresupuesto));
            getSelected().setEstado(new EstadosInforme("3"));
            getSelected().setResponsable(usuario);

            detallesPlanEstrategicasController.setSelected(getSelected());
            detallesPlanEstrategicasController.save(null);
            PlanEstrategicas plan = ejbFacade.getEntityManager().createNamedQuery("PlanEstrategicas.findById", PlanEstrategicas.class
            ).setParameter("id", getSelected().getPlanEstrategica().getId()).getSingleResult();
            if (getSelected().getResultado() < 30) {
                plan.setEstado(new EstadosActividad("RO"));

            }
            if (getSelected().getResultado() > 30) {
                plan.setEstado(new EstadosActividad("NA"));
            }
            if (getSelected().getResultado() == 100) {
                plan.setEstado(new EstadosActividad("VE"));

            }
            plan.setValor(valor);
            plan.setVariable2(variable);
            plan.setResultado(Math.round(resultadoPresupuesto));
            planEstrategicasController.setSelected(plan);
            planEstrategicasController.save(null);

        } else {
            JsfUtil.addErrorMessage("No hay ningun cambio que registrar");
        }

        // }
    }

    public void guardarSumatoria() {

        if (getSelected() != null) {
            // if (getSelected().getVariable2() != 0) {
            valorVariable = getSelected().getPlanEstrategica().getProgramacion().getValorVariable();
            resultado = valor + getSelected().getPlanEstrategica().getVariable2();
            getSelected().setValor(valor);
            getSelected().setVariable2(resultado);
            getSelected().setResultado(Math.round(resultado));
            getSelected().setEstado(new EstadosInforme("3"));
            getSelected().setResponsable(usuario);
            if (getSelected().getTipoDocumentos().getCodigo().equals("CO")) {
                resultado = valor + valorVariable;
                getSelected().setValor(valor);
                getSelected().setVariable2(resultado);
                getSelected().setResultado(Math.round(resultado));
                getSelected().setEstado(new EstadosInforme("3"));
                getSelected().setResponsable(usuario);
            }

            detallesPlanEstrategicasController.setSelected(getSelected());
            detallesPlanEstrategicasController.save(null);
            PlanEstrategicas plan = ejbFacade.getEntityManager().createNamedQuery("PlanEstrategicas.findById", PlanEstrategicas.class
            ).setParameter("id", getSelected().getPlanEstrategica().getId()).getSingleResult();
            if (getSelected().getResultado() < 30) {
                plan.setEstado(new EstadosActividad("RO"));

            }
            if (getSelected().getResultado() > 30) {
                plan.setEstado(new EstadosActividad("NA"));
            }
            if (getSelected().getResultado() == 100) {
                plan.setEstado(new EstadosActividad("VE"));

            }

            plan.setValor(valor);
            plan.setVariable2(resultado);
            plan.setResultado(Math.round(resultado));
            planEstrategicasController.setSelected(plan);
            planEstrategicasController.save(null);

        } else {
            JsfUtil.addErrorMessage("No hay ningun cambio que registrar");
        }

        // }
    }

    private void seleccionarCambiosValor() {
        if (getSelected() != null) {
            detallesCambios = this.ejbFacade.getEntityManager().createNamedQuery("CambiosValor.findByProgramacion", CambiosValor.class).setParameter("programacion", getSelected()).getResultList();
        } else {
            detallesCambios = null;
        }

    }

    public void prepararCambio(Programacion item) {
        programacionSelected = item;
        nuevaVariable = getSelected().getProgramacion().getValorVariable();

    }

    public void guardarEdit() {

        if (getSelected().getProgramacion() != null) {

            if (getSelected().getProgramacion().getValorVariable() != nuevaVariable) {
                CambiosValor cambio = cambiosValorController.prepareCreate(null);

                cambio.setCantidadOriginal(getSelected().getProgramacion().getValorVariable());
                cambio.setCantidadFinal(nuevaVariable);
                cambio.setDetallesPlanEstrategica(getSelected());
                cambio.setProgramacion(getSelected().getProgramacion());

                getSelected().getProgramacion().setValorVariable(nuevaVariable);
                programacionController.setSelected(getSelected().getProgramacion());
                programacionController.save(null);

                cambiosValorController.setSelected(cambio);
                cambiosValorController.saveNew(null);
                // resetParents();
            } else {
                JsfUtil.addErrorMessage("No hay ningun cambio que registrar");
            }
        }
    }

    public void prepareCerrarDialogoVerDoc() {
        if (docImprimir != null) {
            File f = new File(Constantes.RUTA_ARCHIVOS_TEMP + "/tmp/" + nombre);

            f.delete();

            docImprimir = null;
        }
    }

    public String getContent() {

        nombre = "";
        try {
            if (docImprimir != null) {

                byte[] fileByte = null;

                if (docImprimir.getRuta() != null) {
                    try {
                        fileByte = Files.readAllBytes(new File(par.getRutaArchivos() + "/" + docImprimir.getRuta()).toPath());

                    } catch (IOException ex) {
                        JsfUtil.addErrorMessage("No tiene documento adjunto");
                        content = "";
                    }
                }

                if (fileByte != null) {
                    Date fecha = ejbFacade.getSystemDate();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

                    String partes[] = docImprimir.getRuta().split("[.]");
                    String ext = "pdf";

                    if (partes.length > 1) {
                        ext = partes[partes.length - 1];
                    }

                    nombre = session.getId() + "_" + simpleDateFormat.format(fecha) + "." + ext;
                    FileOutputStream outputStream = new FileOutputStream(Constantes.RUTA_ARCHIVOS_TEMP + "/tmp/" + nombre);

                    outputStream.write(fileByte);

                    outputStream.close();

                    // content = new DefaultStreamedContent(new ByteArrayInputStream(fileByte), "application/pdf");
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
            content = null;
        }
        // return par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/tmp/" + nombre;
        return url + "/tmp/" + nombre;

    }

    public void resetParents() {
        planEstrategicasController.setSelected(null);
        if (this.getSelected() == null && this.getItems() != null) {
            if (!this.getItems().isEmpty()) {
                this.setSelected(getItems().iterator().next());
            }
        }
        obtenerArchivos();
        seleccionar();
        // obtenerListasAccionesOperativas();
    }

    public void setListaUsuariosTransf(Collection<Usuarios> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
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
            getSelected().setEstado(new EstadosInforme(Constantes.ASIGNADO));

            super.saveNew(event);

        }

    }

    public boolean desabilitarBotonCambioEstado() {

        if (getSelected() != null) {
            // if (getSelected().getUsuarioAlta() != null) {
            if (getSelected().getResponsable() != null) {

                if (getSelected().getResponsable().equals(usuario)) {

                    try {
                        flujoDoc = this.ejbFacade.getEntityManager().createNamedQuery("FlujosDocumento.findByEstadoDocumentoActual", FlujosDocumento.class).setParameter("tipoDocumento", getSelected().getTipoDocumentos().getCodigo()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).getSingleResult();
                        return false;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
            //}

        }

        return true;
    }

    public boolean desabilitarBotonObservar() {

        if (getSelected() != null) {
            if (getSelected().getResponsable() != null && getSelected().getEstado() != null) {
                return !(getSelected().getResponsable().equals("444") && getSelected().getEstado().getCodigo().equals("3"));
            }

        }

        return true;
    }

    private void obtenerArchivos() {
        if (getSelected() != null) {
            listaArchivos = ejbFacade.getEntityManager().createNamedQuery("Archivos.findByDocumentoPlanificacion", Archivos.class).setParameter("accionOperativa", getSelected()).getResultList();
        }
    }

    private void obtenerListasAccionesOperativas() {
        if (getSelected() != null) {
            listaDetallesPlanEstrategica = ejbFacade.getEntityManager().createNamedQuery("DetallesPlanEstrategicas.findByDocumentoPlanificacion", DetallesPlanEstrategicas.class).setParameter("tiposDocumento", actividad.getTiposDocumento()).setParameter("responsable", getSelected().getResponsable()).getResultList();
        }
    }

    private void seleccionar() {
        if (getSelected() != null) {
            detalles = this.ejbFacade.getEntityManager().createNamedQuery("Archivos.findByDetallesPlanEstrategica", Archivos.class).setParameter("accionOperativa", getSelected()).getResultList();
        } else {
            detalles = null;
        }

    }

    public void obtenerDatos() {
        if (null != activeTab) {

            setItems(null);
            listaDetallesPlanEstrategica = null;

            int tabListas = filtroURL.verifPermiso("verTodosDocsTablero") ? 3 : 1;

            if (activeTab == tabListas) {
                obtenerListasAccionesOperativas();
            }
        }

    }

    public void alzarArchivo() {

        if (getSelected() != null) {

            if (file == null) {
                JsfUtil.addErrorMessage("Debe adjuntar un escrito");
                return;
            } else if (file.getContent().length == 0) {
                JsfUtil.addErrorMessage("El documento esta vacio");
                return;
            }

            Date fecha = ejbFacade.getSystemDate();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String nombreArchivo = simpleDateFormat.format(fecha);
            nombreArchivo += "_" + getSelected().getId() + ".pdf";

            byte[] bytes = null;
            try {
                bytes = IOUtils.toByteArray(file.getInputStream());
            } catch (IOException ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("Error al leer archivo");
                return;
            }

            FileOutputStream fos = null;
            try {
                // fos = new FileOutputStream("C:/Users/DELL/Documents/sistema/archivos" + nombreArchivo);
                fos = new FileOutputStream(par.getRutaArchivos() + File.separator + nombreArchivo);
                fos.write(bytes);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException ex) {
                JsfUtil.addErrorMessage("No se pudo guardar archivo");
                fos = null;
            } catch (IOException ex) {
                JsfUtil.addErrorMessage("No se pudo guardar archivo");
                fos = null;
            }

            Archivos archivo = new Archivos();

            archivo.setAccionOperativa(getSelected());
            archivo.setDescripcion(descripcionArchivo);
            archivo.setRuta(nombreArchivo);
            archivo.setFechaHoraAlta(fecha);
            archivo.setFechaHoraUltimoEstado(fecha);
            archivo.setUsuarioAlta(usuario);
            archivo.setUsuarioUltimoEstado(usuario);
            archivo.setDepartamento(getSelected().getDepartamento());

            getSelected().setResponsable(usuario);
            if (getSelected().getEstado().getCodigo().equals("7")) {
                getSelected().setEstado(new EstadosInforme("3"));
            } else {
                getSelected().setEstado(new EstadosInforme("2"));
            }
            detallesPlanEstrategicasController.setSelected(getSelected());
            detallesPlanEstrategicasController.save(null);

            archivosController.setSelected(archivo);
            archivosController.saveNew(null);
            obtenerArchivos();

        }

    }

    public void saveDpto() {

        if (getSelected() != null && getSelected().getEstado() != null) {
            // if (getSelected().getResponsable().equals(usuario)) {

            Date fecha = ejbFacade.getSystemDate();

            // Usuarios resp = getSelected().getResponsable();
            FlujosDocumento flujoDoc = null;

            try {
                //RolesPorUsuarios rol = this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuario", RolesPorUsuarios.class
                //).setParameter("usuario", getSelected().getResponsable().getId()).getSingleResult();
                //RolesPorUsuarios rol = this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findRolFlujo", RolesPorUsuarios.class).setParameter("usuario", getSelected().getResponsable().getId()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).setParameter("tipoDocumento", tipoDoc.getCodigo()).getSingleResult();
                flujoDoc
                        = this.ejbFacade.getEntityManager().createNamedQuery("FlujosDocumento.findSgteEstado", FlujosDocumento.class
                        ).setParameter("tipoDocumento", getSelected().getTipoDocumentos().getCodigo()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).getSingleResult();
            } catch (Exception e) {
                e.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo determinar flujo del documento. Documento no se puede transferir");
                return;
            }

            EstadosInforme estado = null;

            try {
                // Codigo de enviado a secretaria
                estado = this.ejbFacade.getEntityManager().createNamedQuery("EstadosInforme.findByCodigo", EstadosInforme.class
                ).setParameter("codigo", flujoDoc.getEstadoDocumentoFinal()).getSingleResult();
                /* if (estado.getCodigo().equals("6")) {

                    getSelected().setResponsableAutoriza(usuario);

                    super.save(null);
                }*/

            } catch (Exception ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo determinar sgte estado. Documento no se puede transferir");
                return;
            }

            getSelected().setEstado(estado);
            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);

            super.save(null);

           
        }
    }

}
