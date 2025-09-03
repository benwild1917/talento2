package py.com.startic.gestion.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.poi.util.IOUtils;
import org.primefaces.model.file.UploadedFile;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.models.Archivos;
import py.com.startic.gestion.models.Departamentos;
import py.com.startic.gestion.models.EstadosSolicitudPermiso;
import py.com.startic.gestion.models.FlujosDocumento;
import py.com.startic.gestion.models.FormulariosPermisos;
import py.com.startic.gestion.models.ParametrosSistema;
import py.com.startic.gestion.models.Roles;
import py.com.startic.gestion.models.RolesPorUsuarios;
import py.com.startic.gestion.models.TiposDocumentosJudiciales;
import py.com.startic.gestion.models.TiposFormulario;
import py.com.startic.gestion.models.TiposPermisos;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "formulariosPermisosController")
@ViewScoped
public class FormulariosPermisosController extends AbstractController<FormulariosPermisos> {

    @Inject
    private ArchivosController archivosController;
    private List<TiposPermisos> listaTiposPermisos;
    private TiposPermisos tiposPermisos;
    private TiposFormulario tiposFormulario;
    private TiposDocumentosJudiciales tipoDoc;
    private Collection<Usuarios> listaUsuariosTransf;
    private Departamentos dependencia;
    private Usuarios funcionario;
    private String nroDocumento;
    private String direccionGeneral;
    private UploadedFile file;
    private FlujosDocumento flujoDoc;
    private Usuarios usuario;
    private ParametrosSistema par;
    private Date fechaDesde;
    private Date fechaHasta;
    private HttpSession session;
    private String url;
    private String endpoint;

    public List<TiposPermisos> getListaTiposPermisos() {
        return listaTiposPermisos;
    }

    public void setListaTiposPermisos(List<TiposPermisos> listaTiposPermisos) {
        this.listaTiposPermisos = listaTiposPermisos;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public TiposPermisos getTiposPermisos() {
        return tiposPermisos;
    }

    public void setTiposPermisos(TiposPermisos tiposPermisos) {
        this.tiposPermisos = tiposPermisos;
    }

    public TiposFormulario getTiposFormulario() {
        return tiposFormulario;
    }

    public void setTiposFormulario(TiposFormulario tiposFormulario) {
        this.tiposFormulario = tiposFormulario;
    }

    public Departamentos getDependencia() {
        return dependencia;
    }

    public void setDependencia(Departamentos dependencia) {
        this.dependencia = dependencia;
    }

    public Usuarios getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Usuarios funcionario) {
        this.funcionario = funcionario;
    }

    public String getNroDocumento() {
        return nroDocumento;
    }

    public void setNroDocumento(String nroDocumento) {
        this.nroDocumento = nroDocumento;
    }

    public String getDireccionGeneral() {
        return direccionGeneral;
    }

    public void setDireccionGeneral(String direccionGeneral) {
        this.direccionGeneral = direccionGeneral;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public FlujosDocumento getFlujoDoc() {
        return flujoDoc;
    }

    public void setFlujoDoc(FlujosDocumento flujoDoc) {
        this.flujoDoc = flujoDoc;
    }

    public ParametrosSistema getPar() {
        return par;
    }

    public void setPar(ParametrosSistema par) {
        this.par = par;
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

    @PostConstruct
    @Override
    public void initParams() {
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
        setearItems();
    }

    public FormulariosPermisosController() {
        // Inform the Abstract parent controller of the concrete TiposPersona Entity
        super(FormulariosPermisos.class);
    }

    public void prepareTransferir() {

    }

    public Collection<Usuarios> getListaUsuariosTransf() {
        if (Constantes.ESTADO_PEDIDO_ARTICULO_CA.equals(getSelected().getEstado().getCodigo())) {

            if (getSelected() != null) {

                listaUsuariosTransf = this.ejbFacade.getEntityManager()
                        .createNamedQuery("Usuarios.findTransferirDpto", Usuarios.class)
                        .setParameter("tipoDocumento", Constantes.TIPO_DOCUMENTO_JUDICIAL_SO)
                        .setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo())
                        .setParameter("departamento", usuario.getDepartamento())
                        .getResultList();
            } else {
                listaUsuariosTransf = new ArrayList<>();
            }
        } else {
            if (getSelected() != null) {

                listaUsuariosTransf = this.ejbFacade.getEntityManager()
                        .createNamedQuery("Usuarios.findTransferirPedido", Usuarios.class)
                        .setParameter("tipoDocumento", Constantes.TIPO_DOCUMENTO_JUDICIAL_SO)
                        .setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo())
                        .getResultList();
            } else {
                listaUsuariosTransf = new ArrayList<>();
            }
        }
        return listaUsuariosTransf;
    }

    public void setListaUsuariosTransf(Collection<Usuarios> listaUsuariosTransf) {
        this.listaUsuariosTransf = listaUsuariosTransf;
    }

    private List<TiposPermisos> obtenerListaTiposPermisos(TiposFormulario tiposFormulario) {
        if (tiposFormulario != null) {
            return this.ejbFacade.getEntityManager().createNamedQuery("TiposPermisos.findByListas", TiposPermisos.class
            ).setParameter("tiposFormulario", tiposFormulario).getResultList();
        }
        return null;
    }

    public void actualizarListas(TiposFormulario tiposFormulario) {
        listaTiposPermisos = obtenerListaTiposPermisos(tiposFormulario);
    }

    public void resetearListas(TiposFormulario tiposFormulario) {
        tiposPermisos = null;

        if (getSelected() != null) {
            getSelected().setTiposPermisos(null);

        }
        actualizarListas(tiposFormulario);
    }

    public void cargarDatosAdicionales() {
        if (funcionario != null) {
            dependencia = funcionario.getDepartamento();
            nroDocumento = funcionario.getCi();
            direccionGeneral = funcionario.getDepartamento().getDepartamentoPadre().getNombre();
            // direccionGeneral = "";
            // nroDocumento= "";

        }
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        //empresaController.setSelected(null);
    }

    public boolean desabilitarBotonCambioEstado() {

        if (getSelected() != null) {
            if (getSelected().getFuncionario() != null) {
                //if (getSelected().getFuncionario().equals(usuario)) {
                String tipo = Constantes.TIPO_DOCUMENTO_JUDICIAL_SO;
                String estado = getSelected().getEstado().getCodigo();
                try {
                    flujoDoc = this.ejbFacade.getEntityManager()
                            .createNamedQuery("FlujosDocumento.findByEstadoTipoDocumento", FlujosDocumento.class)
                            .setParameter("tipoDocumento", tipo)
                            .setParameter("estadoDocumentoActual", estado)
                            .getSingleResult();
                    Roles rolFinal = flujoDoc.getRolFinal();

                    Collection<RolesPorUsuarios> rolesPorUsu = usuario.getRolesPorUsuariosCollection2();
                    Collection<Roles> roles = new ArrayList<>();
                    boolean cargaSolicitud = false;
                    boolean esdirector = false;
                    boolean estalento = false;
                    for (RolesPorUsuarios rpu : rolesPorUsu) {
                        roles.add(rpu.getRoles());
                    }

                    for (Roles rr : roles) {
                        if (rr.getId().equals(Constantes.ROL_CARGASOLICITUD)) {
                            cargaSolicitud = true;
                        }
                        if (rr.getId().equals(Constantes.ROL_DIRECTOR)) {
                            esdirector = true;
                        }
                        if (rr.getId().equals(Constantes.ROL_TALENTO)) {
                            estalento = true;
                        }
                    }
                    if (cargaSolicitud && rolFinal.getId().equals(Constantes.ROL_DIRECTOR)) {
                        return false;
                    } else {
                        if (esdirector && rolFinal.getId().equals(Constantes.ROL_TALENTO)) {
                            return false;
                        } else if (estalento) {
                            return false;
                        }
                    }
                    //return false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //}

        }
        return true;
    }

    //  @Override
    public void saveNew() {
        if (getSelected() != null) {

            Date fecha = ejbFacade.getSystemDate();
            getSelected().setEmpresa(usuario.getEmpresa());
            getSelected().setFechaHoraAlta(fecha);
            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioAlta(usuario);
            getSelected().setUsuarioUltimoEstado(usuario);
            getSelected().setDependencia(dependencia);
            getSelected().setFuncionario(funcionario);
            getSelected().setNroDocumento(nroDocumento);
            getSelected().setDireccionGeneral(direccionGeneral);
            getSelected().setTiposFormulario(tiposFormulario);
            getSelected().setEstado(new EstadosSolicitudPermiso("1"));

            super.saveNew(null);

            if (getSelected().getTiposFormulario().getId() == 1 && getSelected().getTiposFormulario().getId() == 2) {
                alzarArchivo(null);
            }

        }

    }

    public void alzarArchivo(Archivos arch) {

        if (getSelected() != null) {

            if (file == null) {
                return;
            } else if (file.getContent().length == 0) {
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
                fos = new FileOutputStream(par.getRutaArchivos() + File.separator + nombreArchivo);
                fos.write(bytes);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo guardar archivo");
                fos = null;
            } catch (IOException ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo guardar archivo");
                fos = null;
            }

            if (arch == null) {
                Archivos archivo = new Archivos();

                archivo.setFormulariosPermisos(getSelected());
                archivo.setRuta(nombreArchivo);
                archivo.setFechaHoraAlta(fecha);
                archivo.setFechaHoraUltimoEstado(fecha);
                archivo.setUsuarioAlta(usuario);
                archivo.setUsuarioUltimoEstado(usuario);

                archivosController.setSelected(archivo);
                archivosController.saveNew(null);
            } else {
                arch.setFormulariosPermisos(getSelected());
                arch.setRuta(nombreArchivo);
                arch.setFechaHoraAlta(fecha);
                arch.setFechaHoraUltimoEstado(fecha);
                arch.setUsuarioAlta(usuario);
                arch.setUsuarioUltimoEstado(usuario);

                archivosController.setSelected(arch);
                archivosController.save(null);
            }
        }

    }

    public void saveDptoRechazar() {
        if (getSelected() != null) {
            Date fecha = ejbFacade.getSystemDate();

            EstadosSolicitudPermiso estado = null;
            String stadoStr = getSelected().getEstado().getCodigo();
            //RECHAZO DE DIRECTOR
            if(getSelected().getEstado().getCodigo().equals(Constantes.ESTADO_SOLICITUD_PERMISO_3)){
                stadoStr = Constantes.ESTADO_SOLICITUD_PERMISO_5;
            }
            //RECHAZO DE TALENTO H.
            if(getSelected().getEstado().getCodigo().equals(Constantes.ESTADO_SOLICITUD_PERMISO_2)){
                stadoStr = Constantes.ESTADO_SOLICITUD_PERMISO_4;
            }

            try {
                // Codigo de enviado a secretaria
                estado = this.ejbFacade.getEntityManager()
                        .createNamedQuery("EstadosSolicitudPermiso.findByCodigo", EstadosSolicitudPermiso.class)
                        .setParameter("codigo", stadoStr)
                        .getSingleResult();

            } catch (Exception ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo determinar sgte estado. Documento no se puede transferir");
                return;
            }

            getSelected().setEstado(estado);
            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);
            // getSelected().setResponsableDestino(responsableDestino);

            super.save(null);

        }
    }

    public void saveDpto() {
        if (getSelected() != null) {
            Date fecha = ejbFacade.getSystemDate();
            FlujosDocumento flujoDoc = null;
            try {
                flujoDoc = this.ejbFacade.getEntityManager()
                        .createNamedQuery("FlujosDocumento.findSgteEstado", FlujosDocumento.class)
                        .setParameter("tipoDocumento", Constantes.TIPO_DOCUMENTO_JUDICIAL_SO)
                        .setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo())
                        .getSingleResult();
            } catch (Exception e) {
                e.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo determinar flujo del documento. Documento no se puede transferir");
                return;
            }

            EstadosSolicitudPermiso estado = null;

            try {
                // Codigo de enviado a secretaria
                estado = this.ejbFacade.getEntityManager()
                        .createNamedQuery("EstadosSolicitudPermiso.findByCodigo", EstadosSolicitudPermiso.class)
                        .setParameter("codigo", flujoDoc.getEstadoDocumentoFinal())
                        .getSingleResult();

            } catch (Exception ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo determinar sgte estado. Documento no se puede transferir");
                return;
            }

            getSelected().setEstado(estado);
            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);
            // getSelected().setResponsableDestino(responsableDestino);

            super.save(null);

        }
    }

    public void aprobar() {
        saveDpto();
    }

    public void setearItems() {
        Collection<RolesPorUsuarios> rolesPorUsu = usuario.getRolesPorUsuariosCollection2();
        Collection<Roles> roles = new ArrayList<>();
        boolean cargaSolicitud = false;
        boolean esdirector = false;
        boolean estalento = false;
        for (RolesPorUsuarios rpu : rolesPorUsu) {
            roles.add(rpu.getRoles());
        }
        for (Roles rr : roles) {
            if (rr.getId().equals(Constantes.ROL_CARGASOLICITUD)) {
                cargaSolicitud = true;
            }
            if (rr.getId().equals(Constantes.ROL_DIRECTOR)) {
                esdirector = true;
            }
            if (rr.getId().equals(Constantes.ROL_TALENTO)) {
                estalento = true;
            }
        }
        Collection<FormulariosPermisos> itn = new ArrayList<>();
        if (cargaSolicitud) {
            itn = ejbFacade.getEntityManager().createNamedQuery("FormulariosPermisos.findByFuncionario", FormulariosPermisos.class)
                    .setParameter("funcionario", usuario)
                    .getResultList();
        } else if (esdirector || estalento) {
            Collection<FlujosDocumento> fdoc = ejbFacade.getEntityManager().createNamedQuery("FlujosDocumento.findByTipoRol", FlujosDocumento.class)
                    .setParameter("tipoDocumento", Constantes.TIPO_DOCUMENTO_JUDICIAL_SO)
                    .setParameter("rolFinal", esdirector ? Constantes.ROL_TALENTO : (estalento ? 0 : -1))
                    .getResultList();
            if (fdoc.iterator().hasNext()) {
                FlujosDocumento row = fdoc.iterator().next();
                String codigoEstado = row.getEstadosDocumento();

                itn = ejbFacade.getEntityManager().createNamedQuery("FormulariosPermisos.findByCodigoEstado", FormulariosPermisos.class)
                        .setParameter("estado", codigoEstado)
                        .getResultList();
            }
        }
        setItems(itn);
    }

    /**
     * Sets the "selected" attribute of the Empresas controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    /* public void prepareEmpresa(ActionEvent event) {
        if (this.getSelected() != null && empresaController.getSelected() == null) {
            empresaController.setSelected(this.getSelected().getEmpresa());
        }
    }*/
}
