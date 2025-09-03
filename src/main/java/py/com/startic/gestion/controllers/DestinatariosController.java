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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.model.file.UploadedFile;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.models.Archivos;
import py.com.startic.gestion.models.Departamentos;
import py.com.startic.gestion.models.Destinatarios;
import py.com.startic.gestion.models.DetallesDestinatarios;
import py.com.startic.gestion.models.DetallesPlanEstrategicas;
import py.com.startic.gestion.models.DocumentosJudiciales;
import py.com.startic.gestion.models.EstadosDocumento;
import py.com.startic.gestion.models.FlujosDocumento;
import py.com.startic.gestion.models.ParametrosSistema;
import py.com.startic.gestion.models.TiposDocumentosJudiciales;
import py.com.startic.gestion.models.TiposEnvio;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "destinatariosController")
@ViewScoped
public class DestinatariosController extends AbstractController<Destinatarios> {

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
    private DetallesDestinatariosController detallesDestinatariosController;
    @RequestScoped

    private ParametrosSistema par;
    private Archivos docImprimir;
    private String content;
    private HttpSession session;
    private String nombre;
    private String url;
    private Usuarios usuario;
    private Departamentos departamento;
    private Collection<Usuarios> listaUsuariosTransf;
    private Usuarios responsableDestino;
    private FlujosDocumento flujoDoc;
    private String endpoint;
    private Collection<Usuarios> listaUsuarios;
    private List<DetallesDestinatarios> listaDetallesDestinatarios;

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

    public List<DetallesDestinatarios> getListaDetallesDestinatarios() {
        return listaDetallesDestinatarios;
    }

    public void setListaDetallesDestinatarios(List<DetallesDestinatarios> listaDetallesDestinatarios) {
        this.listaDetallesDestinatarios = listaDetallesDestinatarios;
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
        setItems(ejbFacade.getEntityManager().createNamedQuery("Destinatarios.findOrdered", Destinatarios.class).setParameter("responsable", usuario).getResultList());
        if (!getItems2().isEmpty()) {
            Destinatarios dest = getItems2().iterator().next();
            setSelected(dest);

        } else {
            setSelected(null);
        }
    }

    public DestinatariosController() {
        // Inform the Abstract parent controller of the concrete Empresas Entity
        super(Destinatarios.class);
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

    public String datePattern3() {
        return "dd/MM/yyyy HH:mm:ss";
    }

    public String customFormatDate3(Date date) {
        if (date != null) {
            DateFormat format = new SimpleDateFormat(datePattern3());
            return format.format(date);
        }
        return "";
    }

    public boolean deshabilitarVerDoc(Destinatarios dest) {
        if (dest != null) {
            if (dest.getResponsable().equals(usuario)) {
                List<Archivos> lista = ejbFacade.getEntityManager().createNamedQuery("Archivos.findByDocumentoResolucion", Archivos.class).setParameter("documentosResolucion", dest.getDocumentosResoluciones()).getResultList();

                return lista.isEmpty();
            }
        }
        return true;
    }

    public void prepareTransferir() {
        prepareTransferir(getSelected());
    }

    public String rowClass(Destinatarios item) {
        return (item.getResponsable().equals(usuario)) ? ((item.getTipoEnvio().getCodigo().equals("DE")) ? "white" : "green") : "";
    }

    private void obtenerDetallesDestinatarios() {
        if (getSelected() != null) {
            listaDetallesDestinatarios = ejbFacade.getEntityManager().createNamedQuery("DetallesDestinatarios.findByDocumentoResolucion", DetallesDestinatarios.class).setParameter("destinatario", getSelected()).getResultList();
        } else {
            listaDetallesDestinatarios = new ArrayList<>();
        }
    }

    public void prepareTransferir(Destinatarios dest) {
        if (dest != null) {
            if (dest.getTipoDocumento().getCodigo().equals("DE")) {
                listaUsuariosTransf = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferir", Usuarios.class).setParameter("tipoDocumento", dest.getTipoDocumento().getCodigo()).setParameter("estadoDocumentoActual", dest.getEstado().getCodigo()).setParameter("rolEncargado", Constantes.ROL_REENVIARDEDIRECCIONEJECUTIVA).getResultList();
            }
            if (dest.getTipoDocumento().getCodigo().equals("DA")) {
                listaUsuariosTransf = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferir", Usuarios.class).setParameter("tipoDocumento", dest.getTipoDocumento().getCodigo()).setParameter("estadoDocumentoActual", dest.getEstado().getCodigo()).setParameter("rolEncargado", Constantes.ROL_REENVIARDIRECCIONADMINISTRATIVA).getResultList();
            }
            if (dest.getTipoDocumento().getCodigo().equals("AL")) {
                listaUsuariosTransf = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferir", Usuarios.class).setParameter("tipoDocumento", dest.getTipoDocumento().getCodigo()).setParameter("estadoDocumentoActual", dest.getEstado().getCodigo()).setParameter("rolEncargado", Constantes.ROL_RECIBIRDESDELASDIRECCIONES).getResultList();
            }
             if (dest.getTipoDocumento().getCodigo().equals("SG")) {
                listaUsuariosTransf = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferir", Usuarios.class).setParameter("tipoDocumento", dest.getTipoDocumento().getCodigo()).setParameter("estadoDocumentoActual", dest.getEstado().getCodigo()).setParameter("rolEncargado", Constantes.ROL_RECIBIRDESDELASDG).getResultList();
            }
              if (dest.getTipoDocumento().getCodigo().equals("AF")) {
                listaUsuariosTransf = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferir", Usuarios.class).setParameter("tipoDocumento", dest.getTipoDocumento().getCodigo()).setParameter("estadoDocumentoActual", dest.getEstado().getCodigo()).setParameter("rolEncargado", Constantes.ROL_REENVIARADMINISTRACIONFINANZAS).getResultList();
            }
              if (dest.getTipoDocumento().getCodigo().equals("GG")) {
                listaUsuariosTransf = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferir", Usuarios.class).setParameter("tipoDocumento", dest.getTipoDocumento().getCodigo()).setParameter("estadoDocumentoActual", dest.getEstado().getCodigo()).setParameter("rolEncargado", Constantes.ROL_REENVIARGABINETE).getResultList();
            }
               if (dest.getTipoDocumento().getCodigo().equals("TH")) {
                listaUsuariosTransf = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferir", Usuarios.class).setParameter("tipoDocumento", dest.getTipoDocumento().getCodigo()).setParameter("estadoDocumentoActual", dest.getEstado().getCodigo()).setParameter("rolEncargado", Constantes.ROL_RECIBIRDRDESDETALENTOHUMANO).getResultList();
            }
                 if (dest.getTipoDocumento().getCodigo().equals("GA")) {
                listaUsuariosTransf = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferir", Usuarios.class).setParameter("tipoDocumento", dest.getTipoDocumento().getCodigo()).setParameter("estadoDocumentoActual", dest.getEstado().getCodigo()).setParameter("rolEncargado", Constantes.ROL_RECIBIRDRDESDEDIRAUDITORIA).getResultList();
            }
             
            if (dest.getResponsable() != null) {
                List<Usuarios> respAnt = ejbFacade.getEntityManager().createNamedQuery("Usuarios.findById", Usuarios.class).setParameter("id", dest.getResponsable().getId()).getResultList();
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
                flujos = this.ejbFacade.getEntityManager().createNamedQuery("FlujosDocumento.findByEstadoDocumentoActualFin", FlujosDocumento.class).setParameter("tipoDocumento", dest.getTipoDocumento().getCodigo()).setParameter("estadoDocumentoActual", dest.getEstado().getCodigo()).getResultList();

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
        if (getSelected() != null && getSelected().getEstado() != null && Constantes.DOCUMENTOENVIADO.equals(getSelected().getEstado().getCodigo())) {

            if (getSelected().getTipoDocumento() != null) {

                listaUsuarios = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferirDpto", Usuarios.class).setParameter("tipoDocumento", getSelected().getTipoDocumento().getCodigo()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).getResultList();
            } else {
                listaUsuarios = new ArrayList<>();
            }

        } else {
            if (getSelected().getTipoDocumento() != null && getSelected().getEstado() != null) {
                listaUsuarios = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferirPedido", Usuarios.class).setParameter("tipoDocumento", getSelected().getTipoDocumento().getCodigo()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).getResultList();
            } else {
                listaUsuarios = new ArrayList<>();
            }
        }
        return listaUsuarios;

    }

    public void prepareVerDoc(Destinatarios dest) {

        List<Archivos> lista = ejbFacade.getEntityManager().createNamedQuery("Archivos.findByDocumentoResolucion", Archivos.class).setParameter("documentosResolucion", dest.getDocumentosResoluciones()).getResultList();

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
                DetallesDestinatarios det = detallesDestinatariosController.prepareCreate(null);

                det.setFechaHoraUltimoEstado(fecha);
                det.setUsuarioUltimoEstado(usuario);
                det.setFechaHoraAlta(fecha);
                det.setUsuarioAlta(usuario);
                det.setEmpresa(usuario.getEmpresa());
                det.setDocumentosResoluciones(getSelected().getDocumentosResoluciones());
                det.setDepartamento(responsableDestino.getDepartamento());
                det.setResponsable(responsableDestino);
                det.setResponsableAnterior(getSelected().getResponsable());
                det.setDestinatario(getSelected());
                if (responsableDestino.getDepartamento().getId() == 64) {
                    det.setTipoDocumento(new TiposDocumentosJudiciales("DF"));
                    det.setTipoEnvio(new TiposEnvio("VO"));
                det.setEstado(new EstadosDocumento("-6"));
                }
                if (responsableDestino.getDepartamento().getId() == 56) {
                    det.setTipoDocumento(new TiposDocumentosJudiciales("DA"));
                    det.setTipoEnvio(new TiposEnvio("VO"));
                det.setEstado(new EstadosDocumento("-6"));
                }
                if (responsableDestino.getDepartamento().getId() == 14) {
                    det.setTipoDocumento(new TiposDocumentosJudiciales("DP"));
                    det.setTipoEnvio(new TiposEnvio("VO"));
                det.setEstado(new EstadosDocumento("-6"));
                }
                if (responsableDestino.getDepartamento().getId() == 15) {
                    det.setTipoDocumento(new TiposDocumentosJudiciales("DU"));
                    det.setTipoEnvio(new TiposEnvio("VO"));
                det.setEstado(new EstadosDocumento("-6"));
                }
                if (responsableDestino.getDepartamento().getId() == 4) {
                    det.setTipoDocumento(new TiposDocumentosJudiciales("AF"));
                    det.setTipoEnvio(new TiposEnvio("VO"));
                det.setEstado(new EstadosDocumento("-6"));
                }
                if (responsableDestino.getDepartamento().getId() == 52) {
                    det.setTipoDocumento(new TiposDocumentosJudiciales("FA"));
                    det.setTipoEnvio(new TiposEnvio("VO"));
                det.setEstado(new EstadosDocumento("-6"));
                }
                 if (responsableDestino.getDepartamento().getId() == 40) {
                    det.setTipoDocumento(new TiposDocumentosJudiciales("DT"));
                    det.setTipoEnvio(new TiposEnvio("VO"));
                det.setEstado(new EstadosDocumento("-6"));
                }
                 if (responsableDestino.getDepartamento().getId() == 58) {
                    det.setTipoDocumento(new TiposDocumentosJudiciales("DI"));
                    det.setTipoEnvio(new TiposEnvio("VO"));
                det.setEstado(new EstadosDocumento("-6"));
                }
                 if (responsableDestino.getDepartamento().getId() == 69) {
                    det.setTipoDocumento(new TiposDocumentosJudiciales("SI"));
                    det.setTipoEnvio(new TiposEnvio("VO"));
                det.setEstado(new EstadosDocumento("-6"));
                 }
                 if (responsableDestino.getDepartamento().getId() == 92) {
                    det.setTipoDocumento(new TiposDocumentosJudiciales("DC"));
                    det.setTipoEnvio(new TiposEnvio("VO"));
                det.setEstado(new EstadosDocumento("-6"));
                 }
                 if (responsableDestino.getDepartamento().getId() == 93) {
                    det.setTipoDocumento(new TiposDocumentosJudiciales("RP"));
                    det.setTipoEnvio(new TiposEnvio("VO"));
                det.setEstado(new EstadosDocumento("-6"));
                 }
                 if (responsableDestino.getDepartamento().getId() == 119) {
                    det.setTipoDocumento(new TiposDocumentosJudiciales("RR"));
                    det.setTipoEnvio(new TiposEnvio("VO"));
                det.setEstado(new EstadosDocumento("-6"));
                 }
                  if (responsableDestino.getDepartamento().getId() == 37) {
                    det.setTipoDocumento(new TiposDocumentosJudiciales("PD"));
                    det.setTipoEnvio(new TiposEnvio("VO"));
                det.setEstado(new EstadosDocumento("-6"));
                 }
                  if (responsableDestino.getDepartamento().getId() == 36) {
                    det.setTipoDocumento(new TiposDocumentosJudiciales("JA"));
                    det.setTipoEnvio(new TiposEnvio("VO"));
                det.setEstado(new EstadosDocumento("-6"));
                 }
                  if (responsableDestino.getDepartamento().getId() == 57) {
                    det.setTipoDocumento(new TiposDocumentosJudiciales("SJ"));
                    det.setTipoEnvio(new TiposEnvio("VO"));
                det.setEstado(new EstadosDocumento("-6"));
                 }
                  if (responsableDestino.getDepartamento().getId() == 63) {
                    det.setTipoDocumento(new TiposDocumentosJudiciales("AJ"));
                    det.setTipoEnvio(new TiposEnvio("VO"));
                det.setEstado(new EstadosDocumento("-6"));
                 }
                  if (responsableDestino.getDepartamento().getId() == 9) {
                    det.setTipoDocumento(new TiposDocumentosJudiciales("PC"));
                    det.setTipoEnvio(new TiposEnvio("VO"));
                det.setEstado(new EstadosDocumento("-6"));
                 }
                  if (responsableDestino.getDepartamento().getId() == 66) {
                    det.setTipoDocumento(new TiposDocumentosJudiciales("DD"));
                    det.setTipoEnvio(new TiposEnvio("VO"));
                det.setEstado(new EstadosDocumento("-6"));
                 }
                  if (responsableDestino.getDepartamento().getId() == 67) {
                    det.setTipoDocumento(new TiposDocumentosJudiciales("AP"));
                    det.setTipoEnvio(new TiposEnvio("VO"));
                det.setEstado(new EstadosDocumento("-6"));
                 }
                  if (responsableDestino.getDepartamento().getId() == 70) {
                    det.setTipoDocumento(new TiposDocumentosJudiciales("AI"));
                    det.setTipoEnvio(new TiposEnvio("VO"));
                det.setEstado(new EstadosDocumento("-6"));
                 }
                  if (responsableDestino.getDepartamento().getId() == 71) {
                    det.setTipoDocumento(new TiposDocumentosJudiciales("AG"));
                    det.setTipoEnvio(new TiposEnvio("VO"));
                det.setEstado(new EstadosDocumento("-6"));
                 }
                 
                detallesDestinatariosController.setSelected(det);
                detallesDestinatariosController.saveNew(null);
                getSelected().setFechaHoraUltimoEstado(fecha);
                getSelected().setUsuarioUltimoEstado(usuario);
                EstadosDocumento estado = ejbFacade.getEntityManager().createNamedQuery("EstadosDocumento.findByCodigo", EstadosDocumento.class).setParameter("codigo", Constantes.DOCUMENTORENVIADODESDELASDIRECCIONES).getSingleResult();

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
        obtenerDetallesDestinatarios();

    }

    public boolean verDocumentoResolucionEnDpto() {

        try {
            this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuarioRol", FlujosDocumento.class).setParameter("usuario", usuario.getId()).setParameter("rol", 51).getSingleResult();
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
