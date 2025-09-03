package py.com.startic.gestion.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.model.file.UploadedFile;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.models.Archivos;
import py.com.startic.gestion.models.Departamentos;
import py.com.startic.gestion.models.Destinatarios;
import py.com.startic.gestion.models.DetallesDestinatarios;
import py.com.startic.gestion.models.EnviarADepartamentos;
import py.com.startic.gestion.models.EstadosDocumento;
import py.com.startic.gestion.models.FlujosDocumento;
import py.com.startic.gestion.models.ParametrosSistema;
import py.com.startic.gestion.models.TiposDocumentosJudiciales;
import py.com.startic.gestion.models.TiposEnvio;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "detallesDestinatariosController")
@ViewScoped
public class DetallesDestinatariosController extends AbstractController<DetallesDestinatarios> {

    @Inject
    private EmpresasController empresaController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private EstadosDocumentoController estadoController;
    @Inject
    private DepartamentosController departamentoController;
    @Inject
    private DestinatariosController envinatariosController;
    @Inject
    private EnviarADepartamentosController enviarADepartamentosController;
    @RequestScoped

    private ParametrosSistema par;
    private Archivos docImprimir;
    private String content;
    private HttpSession session;
    private String nombre;
    private String url;
    private Usuarios usuario;
    private TiposDocumentosJudiciales tipoDocumento;
    private Departamentos departamento;
    private Collection<Usuarios> listaUsuariosTransf;
    private Usuarios responsableDestino;
    private FlujosDocumento flujoDoc;
    private String endpoint;
    private Collection<Usuarios> listaUsuarios;
    private Usuarios usuarioActual;
    private List<EnviarADepartamentos> listaEnviarADepartamento;

    public ParametrosSistema getPar() {
        return par;
    }

    public void setPar(ParametrosSistema par) {
        this.par = par;
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

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public Collection<Usuarios> getListaUsuariosTransf() {
        return listaUsuariosTransf;
    }

    public void setListaUsuariosTransf(Collection<Usuarios> listaUsuariosTransf) {
        this.listaUsuariosTransf = listaUsuariosTransf;
    }

    public Usuarios getResponsableDestino() {
        return responsableDestino;
    }

    public void setResponsableDestino(Usuarios responsableDestino) {
        this.responsableDestino = responsableDestino;
    }

    public FlujosDocumento getFlujoDoc() {
        return flujoDoc;
    }

    public void setFlujoDoc(FlujosDocumento flujoDoc) {
        this.flujoDoc = flujoDoc;
    }

    public Departamentos getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamentos departamento) {
        this.departamento = departamento;
    }

    public Usuarios getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Usuarios usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    public TiposDocumentosJudiciales getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TiposDocumentosJudiciales tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public List<EnviarADepartamentos> getListaEnviarADepartamento() {
        return listaEnviarADepartamento;
    }

    public void setListaEnviarADepartamento(List<EnviarADepartamentos> listaEnviarADepartamento) {
        this.listaEnviarADepartamento = listaEnviarADepartamento;
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();

        par = ejbFacade.getEntityManager().createNamedQuery("ParametrosSistema.findById", ParametrosSistema.class).setParameter("id", Constantes.PARAMETRO_ID).getSingleResult();
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        int pos = url.lastIndexOf(uri);
        url = url.substring(0, pos);

        String[] array = uri.split("/");
        endpoint = array[1];

        usuario = (Usuarios) session.getAttribute("Usuarios");
        setItems(ejbFacade.getEntityManager().createNamedQuery("DetallesDestinatarios.findOrdered", DetallesDestinatarios.class).setParameter("responsable", usuario).getResultList());
        if (!getItems2().isEmpty()) {
            DetallesDestinatarios env = getItems2().iterator().next();
            setSelected(env);

        } else {
            setSelected(null);
        }
    }

    public DetallesDestinatariosController() {
        // Inform the Abstract parent controller of the concrete Empresas Entity
        super(DetallesDestinatarios.class);
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

    public boolean isDepartamentoAutorizado(Departamentos deptoPermitido) {
        return usuarioActual.getDepartamento().equals(deptoPermitido);
    }

    public boolean deshabilitarVerDoc(DetallesDestinatarios det) {
        if (det != null) {
            if (det.getResponsable().equals(usuario)) {
                List<Archivos> lista = ejbFacade.getEntityManager().createNamedQuery("Archivos.findByDocumentoResolucion", Archivos.class).setParameter("documentosResolucion", det.getDocumentosResoluciones()).getResultList();

                return lista.isEmpty();
            }
        }
        return true;
    }

    public void prepareTransferir() {
        prepareTransferir(getSelected());
    }

    public String rowClass(DetallesDestinatarios item) {
        return (item.getResponsable().equals(usuario)) ? ((item.getTipoEnvio().getCodigo().equals("DE")) ? "white" : "green") : "";
    }

    public void prepareTransferir(DetallesDestinatarios det) {
        if (det != null) {

            if (det.getTipoDocumento().getCodigo().equals("DP")) {
                listaUsuariosTransf = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferir", Usuarios.class).setParameter("tipoDocumento", det.getTipoDocumento().getCodigo()).setParameter("estadoDocumentoActual", det.getEstado().getCodigo()).setParameter("rolEncargado", Constantes.ROL_RECIBIRDESDEPRESUPUESTO).getResultList();
            }
            if (det.getTipoDocumento().getCodigo().equals("DT")) {
                listaUsuariosTransf = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferir", Usuarios.class).setParameter("tipoDocumento", det.getTipoDocumento().getCodigo()).setParameter("estadoDocumentoActual", det.getEstado().getCodigo()).setParameter("rolEncargado", Constantes.ROL_RECIBIRDESDETIC).getResultList();
            }
            if (det.getTipoDocumento().getCodigo().equals("PD")) {
                listaUsuariosTransf = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferir", Usuarios.class).setParameter("tipoDocumento", det.getTipoDocumento().getCodigo()).setParameter("estadoDocumentoActual", det.getEstado().getCodigo()).setParameter("rolEncargado", Constantes.ROL_RECIBIRDESDEPLANIFICACIONDESARROLLO).getResultList();
            }
            if (det.getTipoDocumento().getCodigo().equals("DF")) {
                listaUsuariosTransf = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferir", Usuarios.class).setParameter("tipoDocumento", det.getTipoDocumento().getCodigo()).setParameter("estadoDocumentoActual", det.getEstado().getCodigo()).setParameter("rolEncargado", Constantes.ROL_RECIBIRRESOLUCIONESDESDEADFINANCIERA).getResultList();
            }
            if (det.getTipoDocumento().getCodigo().equals("DA")) {
                listaUsuariosTransf = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferir", Usuarios.class).setParameter("tipoDocumento", det.getTipoDocumento().getCodigo()).setParameter("estadoDocumentoActual", det.getEstado().getCodigo()).setParameter("rolEncargado", Constantes.ROL_RECIBIRDRDESDEDIRADMINISTRATIVA).getResultList();
            }
            if (det.getTipoDocumento().getCodigo().equals("DU")) {
                listaUsuariosTransf = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferir", Usuarios.class).setParameter("tipoDocumento", det.getTipoDocumento().getCodigo()).setParameter("estadoDocumentoActual", det.getEstado().getCodigo()).setParameter("rolEncargado", Constantes.ROL_RECIBIRDRDESDEDUOC).getResultList();
            }
            if (det.getTipoDocumento().getCodigo().equals("JA")) {
                listaUsuariosTransf = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferir", Usuarios.class).setParameter("tipoDocumento", det.getTipoDocumento().getCodigo()).setParameter("estadoDocumentoActual", det.getEstado().getCodigo()).setParameter("rolEncargado", Constantes.ROL_RECIBIRDRDESDEJADMINISTRATIVO).getResultList();
            }
            if (det.getTipoDocumento().getCodigo().equals("SJ")) {
                listaUsuariosTransf = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferir", Usuarios.class).setParameter("tipoDocumento", det.getTipoDocumento().getCodigo()).setParameter("estadoDocumentoActual", det.getEstado().getCodigo()).setParameter("rolEncargado", Constantes.ROL_RECIBIRDRDESDESECRETARIAJURIDICA).getResultList();
            }
            if (det.getTipoDocumento().getCodigo().equals("AJ")) {
                listaUsuariosTransf = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferir", Usuarios.class).setParameter("tipoDocumento", det.getTipoDocumento().getCodigo()).setParameter("estadoDocumentoActual", det.getEstado().getCodigo()).setParameter("rolEncargado", Constantes.ROL_RECIBIRDRDESDEASESORIAJURIDICA).getResultList();
            }
            if (det.getTipoDocumento().getCodigo().equals("DD")) {
                listaUsuariosTransf = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferir", Usuarios.class).setParameter("tipoDocumento", det.getTipoDocumento().getCodigo()).setParameter("estadoDocumentoActual", det.getEstado().getCodigo()).setParameter("rolEncargado", Constantes.ROL_RECIBIRDRDESDEDIRDESARROLLO).getResultList();
            }
            if (det.getTipoDocumento().getCodigo().equals("AP")) {
                listaUsuariosTransf = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferir", Usuarios.class).setParameter("tipoDocumento", det.getTipoDocumento().getCodigo()).setParameter("estadoDocumentoActual", det.getEstado().getCodigo()).setParameter("rolEncargado", Constantes.ROL_RECIBIRDRDESDEADMPERSONAL).getResultList();
            }
            if (det.getTipoDocumento().getCodigo().equals("DI")) {
                listaUsuariosTransf = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferir", Usuarios.class).setParameter("tipoDocumento", det.getTipoDocumento().getCodigo()).setParameter("estadoDocumentoActual", det.getEstado().getCodigo()).setParameter("rolEncargado", Constantes.ROL_RECIBIRDRDESDEGESTIONINTERNA).getResultList();
            }

            if (det.getResponsable() != null) {
                List<Usuarios> respAnt = ejbFacade.getEntityManager().createNamedQuery("Usuarios.findById", Usuarios.class).setParameter("id", det.getResponsable().getId()).getResultList();
                if (!respAnt.isEmpty()) {
                    boolean encontro = false;
                    for (Usuarios trans : listaUsuariosTransf) {
                        if (trans.equals(respAnt.get(0))) {
                            encontro = true;
                        }
                    }

                    if (!encontro) {
                        listaUsuariosTransf.add(respAnt.get(0));
                    }
                }
            }

            List<FlujosDocumento> flujos = null;
            try {
                flujos = this.ejbFacade.getEntityManager().createNamedQuery("FlujosDocumento.findByEstadoDocumentoActualFin", FlujosDocumento.class).setParameter("tipoDocumento", det.getTipoDocumento().getCodigo()).setParameter("estadoDocumentoActual", det.getEstado().getCodigo()).getResultList();

                int contador = -1;
                for (FlujosDocumento flujo : flujos) {

                    EstadosDocumento est = this.ejbFacade.getEntityManager().createNamedQuery("EstadosDocumento.findByCodigo", EstadosDocumento.class).setParameter("codigo", flujo.getEstadoDocumentoFinal()).getSingleResult();

                    Usuarios usu = new Usuarios(contador, est.getDescripcion(), est);
                    listaUsuariosTransf.add(usu);
                    contador--;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            listaUsuariosTransf = new ArrayList<>();
        }

        responsableDestino = null;
    }

    public Collection<Usuarios> getListaUsuarios() {
        if (getSelected() != null && getSelected().getEstado() != null && Constantes.DOCUMENTOREENVIAR.equals(getSelected().getEstado().getCodigo())) {

            if (getSelected().getTipoDocumento() != null) {

                listaUsuarios = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferir", Usuarios.class).setParameter("tipoDocumento", getSelected().getTipoDocumento().getCodigo()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).setParameter("departamento", usuario.getDepartamento()).getResultList();
            } else {
                listaUsuarios = new ArrayList<>();
            }

        } else {
            if (getSelected().getTipoDocumento() != null && getSelected().getEstado() != null) {

                listaUsuarios = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferirDptoP", Usuarios.class).setParameter("tipoDocumento", getSelected().getTipoDocumento().getCodigo()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).setParameter("departamentoPadre", usuario.getDepartamento().getDepartamentoPadre()).getResultList();

            } else {
                listaUsuarios = new ArrayList<>();
            }
        }
        return listaUsuarios;

    }

    public void prepareVerDoc(DetallesDestinatarios det) {

        List<Archivos> lista = ejbFacade.getEntityManager().createNamedQuery("Archivos.findByDocumentoResolucion", Archivos.class).setParameter("documentosResolucion", det.getDocumentosResoluciones()).getResultList();

        if (!lista.isEmpty()) {
            docImprimir = lista.get(0);
        }

        //PrimeFaces.current().ajax().update("ExpAcusacionesViewForm");
    }

    /* public void prepareVerDoc(Archivos doc) {
        docImprimir = doc;

        //PrimeFaces.current().ajax().update("ExpAcusacionesViewForm");
    }*/
    public void prepareCerrarDialogoVerDoc() {
        if (docImprimir != null) {
            File f = new File(Constantes.RUTA_ARCHIVOS_TEMP + "/tmp/" + nombre);
            f.delete();

            docImprimir = null;
        }
    }

    private void obtenerListaDpto() {
        if (getSelected() != null) {
            listaEnviarADepartamento = ejbFacade.getEntityManager().createNamedQuery("EnviarADepartamentos.findByDocumentoResolucion", EnviarADepartamentos.class).setParameter("detallesDestinatario", getSelected()).getResultList();
        } else {
            listaEnviarADepartamento = new ArrayList<>();
        }
    }

    public boolean desabilitarBotonCambioEstado() {

        if (getSelected() != null) {
            // if (getSelected().getUsuarioAlta() != null) {
            if (getSelected().getResponsable() != null) {

                if (getSelected().getResponsable().equals(usuario)) {

                    try {
                        flujoDoc = this.ejbFacade.getEntityManager().createNamedQuery("FlujosDocumento.findByEstadoDocumentoActual", FlujosDocumento.class).setParameter("tipoDocumento", getSelected().getTipoDocumento().getCodigo()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).getSingleResult();
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

    public void guardarTranferirDocumento() {
        if (getSelected() != null) {
            for (Usuarios usu : listaUsuariosTransf) {
                // if (usu.getId().equals(responsableDestinoId)) {
                responsableDestino = usu;
                // }
                //if (usu.isSeleccionado()) {
                Date fecha = ejbFacade.getSystemDate();
                EnviarADepartamentos env = enviarADepartamentosController.prepareCreate(null);

                env.setFechaHoraUltimoEstado(fecha);
                env.setUsuarioUltimoEstado(usuario);
                env.setFechaHoraAlta(fecha);
                env.setUsuarioAlta(usuario);
                env.setEmpresa(usuario.getEmpresa());
                env.setDetallesDestinatario(getSelected());
                env.setDocumentosResoluciones(getSelected().getDocumentosResoluciones());
                env.setDepartamento(responsableDestino.getDepartamento());
                env.setResponsable(responsableDestino);
                env.setResponsableAnterior(getSelected().getResponsable());
                env.setTipoEnvio(new TiposEnvio("ED"));
                env.setEstado(new EstadosDocumento("-7"));

                enviarADepartamentosController.setSelected(env);
                enviarADepartamentosController.saveNew(null);
                getSelected().setFechaHoraUltimoEstado(fecha);
                getSelected().setUsuarioUltimoEstado(usuario);
                EstadosDocumento estado = ejbFacade.getEntityManager().createNamedQuery("EstadosDocumento.findByCodigo", EstadosDocumento.class).setParameter("codigo", Constantes.DOCUMENTORESOLUCIONENVIDOADPTO).getSingleResult();

                getSelected().setEstado(estado);
                setSelected(getSelected());
                super.save(null);

                //}
                // }
            }
        }
    }

    public void resetParents() {
        empresaController.setSelected(null);
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
        estadoController.setSelected(null);
        departamentoController.setSelected(null);
        if (this.getSelected() == null && this.getItems() != null) {
            if (!this.getItems().isEmpty()) {
                this.setSelected(getItems().iterator().next());
            }
        }
        obtenerListaDpto();

    }

    public boolean verDocumentoResolucionEnDpto() {

        try {
            this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuarioRol", FlujosDocumento.class).setParameter("usuario", usuario.getId()).setParameter("rol", 53).getSingleResult();
            return true;
        } catch (Exception e) {
            // e.printStackTrace();
        }

        return false;
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

    @Override
    public void save(ActionEvent event) {

        if (getSelected() != null) {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            Usuarios usuario = (Usuarios) session.getAttribute("Usuarios");

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);
        }

        super.save(event);
        this.verDocumentoResolucionEnDpto();
    }

}
