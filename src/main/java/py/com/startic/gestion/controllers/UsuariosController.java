package py.com.startic.gestion.controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;

import py.com.startic.gestion.models.Usuarios;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.PrimeFaces;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.models.Departamentos;
import py.com.startic.gestion.models.Estados;
import py.com.startic.gestion.models.EstadosUsuario;
import py.com.startic.gestion.models.Pantallas;
import py.com.startic.gestion.models.ParametrosSistema;
import py.com.startic.gestion.models.Personas;
import py.com.startic.gestion.models.SesionesLogin;
import py.com.startic.gestion.models.UsuariosAsociados;
import py.com.startic.gestion.models.UsuariosAsociadosPK;
import py.com.startic.gestion.models.VPermisosUsuarios;

@Named(value = "usuariosController")
@ViewScoped
public class UsuariosController extends AbstractController<Usuarios> {

    @Inject
    private DepartamentosController departamentoController;
    @Inject
    private EmpresasController empresaController;
    @Inject
    private EstadosUsuarioController estadoController;
    @Inject
    private PantallasController pantallaPrincipalController;
    @Inject
    private SexosController sexoController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private UsuariosAsociadosController usuariosAsociadosController;
    @Inject
    private SesionesLoginController sesionesLoginController;
    @Inject
    private SedeController sedeController;

    private String usuario;
    private String contrasena;
    private String nombreUsu;
    private String cambioContrasena1;
    private String cambioContrasena2;
    private String home;
    private Collection<Usuarios> usuariosDpto;
    private String endpoint;
    private List<Usuarios> listaUsuariosElegir;
    private Usuarios usuarioElegido;
    private Usuarios usuarioOriginal;
    private Personas personaOriginal;
    private Usuarios usuarioBkp;
    
    private Integer menu;

    private Collection<Usuarios> funcionariosSecretaria;

    private String imagenInicio;
    // private String imagenFondo;
    private String imagenLogo;
    private ParametrosSistema par;
    private Usuarios usuarioAsociarSelected;
    private Usuarios usuarioAsociar;
    private List<Usuarios> listaUsuariosAAsociar;
    private List<Usuarios> listaUsuariosAsociar;
    private final FiltroURL filtroURL = new FiltroURL();
    private Usuarios usuLogin;
    private String pantalla;
    private String hash;
    private String sesionLogin;
    private Integer cantidadArchivosAdmEnProyecto;
    private Integer cantidadArchivosAdmEnRevision;
    private Integer cantidadArchivosAdmFinalizado;
    private Integer cantidadArchivosAdmParaRevision;

    public Integer getCantidadArchivosAdmParaRevision() {
        return cantidadArchivosAdmParaRevision;
    }

    public void setCantidadArchivosAdmParaRevision(Integer cantidadArchivosAdmParaRevision) {
        this.cantidadArchivosAdmParaRevision = cantidadArchivosAdmParaRevision;
    }

    public Integer getCantidadArchivosAdmEnRevision() {
        return cantidadArchivosAdmEnRevision;
    }

    public void setCantidadArchivosAdmEnRevision(Integer cantidadArchivosAdmEnRevision) {
        this.cantidadArchivosAdmEnRevision = cantidadArchivosAdmEnRevision;
    }

    public Integer getCantidadArchivosAdmFinalizado() {
        return cantidadArchivosAdmFinalizado;
    }

    public void setCantidadArchivosAdmFinalizado(Integer cantidadArchivosAdmFinalizado) {
        this.cantidadArchivosAdmFinalizado = cantidadArchivosAdmFinalizado;
    }

    public Integer getCantidadArchivosAdmEnProyecto() {
        return cantidadArchivosAdmEnProyecto;
    }

    public void setCantidadArchivosAdmEnProyecto(Integer cantidadArchivosAdmEnProyecto) {
        this.cantidadArchivosAdmEnProyecto = cantidadArchivosAdmEnProyecto;
    }

    public List<Usuarios> getListaUsuariosAsociar() {
        return listaUsuariosAsociar;
    }

    public void setListaUsuariosAsociar(List<Usuarios> listaUsuariosAsociar) {
        this.listaUsuariosAsociar = listaUsuariosAsociar;
    }

    public Usuarios getUsuarioAsociarSelected() {
        return usuarioAsociarSelected;
    }

    public void setUsuarioAsociarSelected(Usuarios usuarioAsociarSelected) {
        this.usuarioAsociarSelected = usuarioAsociarSelected;
    }

    public Usuarios getUsuarioAsociar() {
        return usuarioAsociar;
    }

    public void setUsuarioAsociar(Usuarios usuarioAsociar) {
        this.usuarioAsociar = usuarioAsociar;
    }

    public List<Usuarios> getListaUsuariosAAsociar() {
        return listaUsuariosAAsociar;
    }

    public void setListaUsuariosAAsociar(List<Usuarios> listaUsuariosAAsociar) {
        this.listaUsuariosAAsociar = listaUsuariosAAsociar;
    }

    public Usuarios getUsuarioElegido() {
        return usuarioElegido;
    }

    public void setUsuarioElegido(Usuarios usuarioElegido) {
        this.usuarioElegido = usuarioElegido;
    }

    public List<Usuarios> getListaUsuariosElegir() {
        return listaUsuariosElegir;
    }

    public void setListaUsuariosElegir(List<Usuarios> listaUsuariosElegir) {
        this.listaUsuariosElegir = listaUsuariosElegir;
    }

    public String getImagenInicio() throws IOException {
        return imagenInicio;
    }

    public String getImagenLogo() {

        return imagenLogo;
    }

    @PostConstruct
    public void init() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        pantalla = params.get("pantalla");
        hash = params.get("hasg");

        usuario = null;
        contrasena = null;
       
        menu = 1;

        sesionLogin = params.get("id");

        usuLogin = (Usuarios) session.getAttribute("Usuarios");
        usuarioBkp = (Usuarios) session.getAttribute("Backup");

        try {
            par = ejbFacade.getEntityManager().createNamedQuery("ParametrosSistema.findById", ParametrosSistema.class).setParameter("id", Constantes.PARAMETRO_ID).getSingleResult();

            File file = new File(Constantes.RUTA_ARCHIVOS_TEMP + "/" + par.getRutaRecursos());
            boolean bool = file.mkdir();
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String url = request.getRequestURL().toString();
            String uri = request.getRequestURI();
            int pos = url.lastIndexOf(uri);
            url = url.substring(0, pos);
            String[] array = uri.split("/");
            endpoint = array[1];

            // imagenFondo = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + par.getRutaRecursos() + "/" + "imagen_fondo.jpg";
            // imagenInicio = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + par.getRutaRecursos() + "/" + "imagen_inicio.jpg";
            // imagenLogo = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + par.getRutaRecursos() + "/" + "imagen_logo.jpg";
            imagenInicio = url + "/" + par.getRutaRecursos() + "/" + "imagen_inicio.jpg";
            imagenLogo = url + "/" + par.getRutaRecursos() + "/" + "imagen_logo.jpg";
        } catch (Exception ex) {
            ex.printStackTrace();
            imagenInicio = "";
            // imagenFondo = "";
            imagenLogo = "";
        }

        Usuarios usu = (Usuarios) session.getAttribute("Usuarios");
        Usuarios bkp = (Usuarios) session.getAttribute("Backup");

        if (usu != null) {

            if ("FE".equals(usu.getSexo().getCodigo())) {
                nombreUsu = "Bienvenida ";
            } else {
                nombreUsu = "Bienvenido ";
            }
            nombreUsu += usu.getNombresApellidos() + " - (" + usu.getUsuario() + ")";

            if (bkp != null) {
                nombreUsu += " como backup de " + bkp.getNombresApellidos();
            }
/*
            List<EstadosTransferenciaDocumentoAdministrativo> estados = ejbFacade.getEntityManager().createNamedQuery("EstadosTransferenciaDocumentoAdministrativo.findByCodigo", EstadosTransferenciaDocumentoAdministrativo.class).setParameter("codigo", Constantes.ESTADO_ARCHIVO_ADM_PROYECTO).getResultList();
            if (!estados.isEmpty()) {
                cantidadArchivosAdmEnProyecto = documentosAdministrativosController.buscarPorFechaAltaBorradores(estados.get(0), usuLogin, null).size();
                if (cantidadArchivosAdmEnProyecto == 0) {
                    cantidadArchivosAdmEnProyecto = null;
                }
            }

            estados = ejbFacade.getEntityManager().createNamedQuery("EstadosTransferenciaDocumentoAdministrativo.findByCodigo", EstadosTransferenciaDocumentoAdministrativo.class).setParameter("codigo", Constantes.ESTADO_ARCHIVO_ADM_REVISION).getResultList();
            if (!estados.isEmpty()) {
                cantidadArchivosAdmEnRevision = documentosAdministrativosController.buscarPorFechaAltaBorradores(estados.get(0), usuLogin, null).size();
                if (cantidadArchivosAdmEnRevision == 0) {
                    cantidadArchivosAdmEnRevision = null;
                }
            }

            estados = ejbFacade.getEntityManager().createNamedQuery("EstadosTransferenciaDocumentoAdministrativo.findByCodigo", EstadosTransferenciaDocumentoAdministrativo.class).setParameter("codigo", Constantes.ESTADO_ARCHIVO_ADM_REVISION).getResultList();
            if (!estados.isEmpty()) {
                cantidadArchivosAdmParaRevision = documentosAdministrativosController.buscarPorFechaAltaBorradores(estados.get(0), null, usuLogin).size();
                if (cantidadArchivosAdmParaRevision == 0) {
                    cantidadArchivosAdmParaRevision = null;
                }
            }
           
*/
        }
    }
        
        

    public UsuariosController() {
        // Inform the Abstract parent controller of the concrete Usuarios Entity
        super(Usuarios.class);
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Collection<Usuarios> getUsuariosDpto() {

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

        return ejbFacade.getEntityManager().createNamedQuery("Usuarios.findByDepartamento", Usuarios.class).setParameter("departamento", usu.getDepartamento()).getResultList();

        /*
        
        List<Usuarios> lista = ejbFacade.getEntityManager().createNamedQuery("Usuarios.findByDepartamento", Usuarios.class).setParameter("departamento", usu.getDepartamento()).getResultList();
        
        List<Usuarios> lista2 = new ArrayList<>();
        for(int i = 0; i < lista.size(); i++){
            Usuarios uno = lista.get(i);
            uno.setContrasena("");
            
            lista2.add(uno);
        }

        return lista2;
         */
    }

    public Collection<Usuarios> getFuncionariosSecretaria() {
        Departamentos dpto = departamentoController.prepareCreate(null);
        dpto.setId(5); // Secretaria
        return ejbFacade.getEntityManager().createNamedQuery("Usuarios.findByDepartamento", Usuarios.class).setParameter("departamento", dpto).getResultList();
    }

    public void setFuncionariosSecretaria(Collection<Usuarios> funcionariosSecretaria) {
        this.funcionariosSecretaria = funcionariosSecretaria;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getNombreUsu() {
        return nombreUsu;
    }

    public void setNombreUsu(String nombreUsu) {
        this.nombreUsu = nombreUsu;
    }

    public String getCambioContrasena1() {
        return cambioContrasena1;
    }

    public void setCambioContrasena1(String cambioContrasena1) {
        this.cambioContrasena1 = cambioContrasena1;
    }

    public String getCambioContrasena2() {
        return cambioContrasena2;
    }

    public void setCambioContrasena2(String cambioContrasena2) {
        this.cambioContrasena2 = cambioContrasena2;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String obtenerHome() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        return (String) session.getAttribute("Home");
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        departamentoController.setSelected(null);
        empresaController.setSelected(null);
        estadoController.setSelected(null);
        pantallaPrincipalController.setSelected(null);
        sexoController.setSelected(null);
    }

    /**
     * Sets the "items" attribute with a collection of Departamentos entities
     * that are retrieved from Usuarios?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for Departamentos page
     */
    public String navigateDepartamentosCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Departamentos_items", this.getSelected().getDepartamentosCollection());
        }
        return "/pages/departamentos/index";
    }

    /**
     * Sets the "items" attribute with a collection of Departamentos entities
     * that are retrieved from Usuarios?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for Departamentos page
     */
    public String navigateDepartamentosCollection1() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Departamentos_items", this.getSelected().getDepartamentosCollection1());
        }
        return "/pages/departamentos/index";
    }

    /**
     * Sets the "items" attribute with a collection of Roles entities that are
     * retrieved from Usuarios?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Roles page
     */
    public String navigateRolesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Roles_items", this.getSelected().getRolesCollection());
        }
        return "/pages/roles/index";
    }

    /**
     * Sets the "items" attribute with a collection of Roles entities that are
     * retrieved from Usuarios?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Roles page
     */
    public String navigateRolesCollection1() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Roles_items", this.getSelected().getRolesCollection1());
        }
        return "/pages/roles/index";
    }

    /**
     * Sets the "selected" attribute of the Departamentos controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareDepartamento(ActionEvent event) {
        if (this.getSelected() != null && departamentoController.getSelected() == null) {
            departamentoController.setSelected(this.getSelected().getDepartamento());
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

    /**
     * Sets the "selected" attribute of the EstadosUsuario controller in order
     * to display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareEstado(ActionEvent event) {
        if (this.getSelected() != null && estadoController.getSelected() == null) {
            estadoController.setSelected(this.getSelected().getEstado());
        }
    }

    /**
     * Sets the "selected" attribute of the Pantallas controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void preparePantallaPrincipal(ActionEvent event) {
        if (this.getSelected() != null && pantallaPrincipalController.getSelected() == null) {
            pantallaPrincipalController.setSelected(this.getSelected().getPantallaPrincipal());
        }
    }

    /**
     * Sets the "selected" attribute of the Sexos controller in order to display
     * its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareSexo(ActionEvent event) {
        if (this.getSelected() != null && sexoController.getSelected() == null) {
            sexoController.setSelected(this.getSelected().getSexo());
        }
    }

    /**
     * Sets the "items" attribute with a collection of Usuarios entities that
     * are retrieved from Usuarios?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Usuarios page
     */
    public String navigateUsuariosCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Usuarios_items", this.getSelected().getUsuariosCollection());
        }
        return "/pages/usuarios/index";
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
     * Sets the "items" attribute with a collection of Usuarios entities that
     * are retrieved from Usuarios?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Usuarios page
     */
    public String navigateUsuariosCollection1() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Usuarios_items", this.getSelected().getUsuariosCollection1());
        }
        return "/pages/usuarios/index";
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
     * Sets the "items" attribute with a collection of RolesPorUsuarios entities
     * that are retrieved from Usuarios?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for RolesPorUsuarios page
     */
    public String navigateRolesPorUsuariosCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("RolesPorUsuarios_items", this.getSelected().getRolesPorUsuariosCollection());
        }
        return "/pages/rolesPorUsuarios/index";
    }

    /**
     * Sets the "items" attribute with a collection of RolesPorUsuarios entities
     * that are retrieved from Usuarios?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for RolesPorUsuarios page
     */
    public String navigateRolesPorUsuariosCollection1() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("RolesPorUsuarios_items", this.getSelected().getRolesPorUsuariosCollection1());
        }
        return "/pages/rolesPorUsuarios/index";
    }

    /**
     * Sets the "items" attribute with a collection of RolesPorUsuarios entities
     * that are retrieved from Usuarios?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for RolesPorUsuarios page
     */
    public String navigateRolesPorUsuariosCollection2() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("RolesPorUsuarios_items", this.getSelected().getRolesPorUsuariosCollection2());
        }
        return "/pages/rolesPorUsuarios/index";
    }

    /**
     * Sets the "items" attribute with a collection of Proveedores entities that
     * are retrieved from Usuarios?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Proveedores page
     */
    public String navigateProveedoresCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Proveedores_items", this.getSelected().getProveedoresCollection());
        }
        return "/pages/proveedores/index";
    }
    

    /**
     * Sets the "items" attribute with a collection of Proveedores entities that
     * are retrieved from Usuarios?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Proveedores page
     */
    public String navigateProveedoresCollection1() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Proveedores_items", this.getSelected().getProveedoresCollection1());
        }
        return "/pages/proveedores/index";
    }

    public void onload() {

        if (sesionLogin != null) {
            Date fecha = ejbFacade.getSystemDate();
            List<SesionesLogin> ses = ejbFacade.getEntityManager().createNamedQuery("SesionesLogin.findByHashFechaHoraCaducidadEstado", SesionesLogin.class).setParameter("hash", sesionLogin).setParameter("fechaHoraCaducidad", fecha).setParameter("estado", "AC").getResultList();
            sesionLogin = null;
            if (!ses.isEmpty()) {

                ses.get(0).setEstado(new Estados("IN"));

                sesionesLoginController.setSelected(ses.get(0));
                sesionesLoginController.save(null);
                if (ses.get(0).getUsuario() != null) {
                    String resp = loginControl(ses.get(0).getUsuario().getUsuario(), ses.get(0).getUsuario().getContrasena(), true);

                    if (resp != null) {
                        if (!"".equals(resp)) {
                            try {
                                FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/" + resp);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }

                if (usuarioOriginal == null) {
                    if (ses.get(0).getPersona() != null) {
                        loginControl(ses.get(0).getPersona().getUsuario(), ses.get(0).getPersona().getContrasena(), false);
                    } else {
                        JsfUtil.addErrorMessage("Sesion no tiene ni usuario ni persona");
                    }
                }

            } else {
                JsfUtil.addErrorMessage("No se encontro sesión o sesión caducada");
            }
        }
    }

    public boolean renderedUsuarioAsociar() {
        boolean resp = filtroURL.verifPermiso(Constantes.PERMISO_USUARIOS_ASOCIAR) && usuarioBkp == null;
        return resp;
    }

    public void prepareUsuarioAsociar() {
        if (usuLogin != null) {

            listaUsuariosAAsociar = ejbFacade.getEntityManager().createNamedQuery("Usuarios.findByEstado", Usuarios.class).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();

            List<UsuariosAsociados> lista = ejbFacade.getEntityManager().createNamedQuery("UsuariosAsociados.findByUsuario", UsuariosAsociados.class).setParameter("usuario", usuLogin.getId()).getResultList();
            listaUsuariosAsociar = new ArrayList<>();
            for (UsuariosAsociados per : lista) {
                listaUsuariosAsociar.add(per.getUsuarioAsociado());
                listaUsuariosAAsociar.remove(per.getUsuarioAsociado());
            }
        }
    }

    public void borrarUsuarioAsociar(Usuarios personaActual) {

        if (listaUsuariosAsociar != null) {

            if (personaActual != null) {
                listaUsuariosAsociar.remove(personaActual);
            }

        }
    }

    public void agregarUsuarioAsociar() {

        if (usuarioAsociar != null) {

            if (listaUsuariosAsociar == null) {
                listaUsuariosAsociar = new ArrayList<>();
            }

            boolean encontro = false;
            Usuarios usuActual = null;
            for (Usuarios usu : listaUsuariosAsociar) {
                if (usu.equals(usuarioAsociar)) {
                    usuActual = usu;
                    encontro = true;
                }
            }

            if (!encontro) {
                listaUsuariosAsociar.add(usuarioAsociar);
            }
        }
    }

    public void saveUsuariosAsociar() {
        if (usuLogin != null) {
            List<UsuariosAsociados> listaAsociarActual = ejbFacade.getEntityManager().createNamedQuery("UsuariosAsociados.findByUsuario", UsuariosAsociados.class).setParameter("usuario", usuLogin.getId()).getResultList();
            saveUsuariosAsociar(usuLogin, listaUsuariosAsociar, listaAsociarActual);
        }
    }

    public void saveUsuariosAsociar(Usuarios persona, List<Usuarios> listaUsuariosAsociar, List<UsuariosAsociados> listaAsociarActual) {

        Date fecha = ejbFacade.getSystemDate();
        Usuarios per2 = null;
        UsuariosAsociados perDocActual = null;
        boolean encontro = false;

        for (Usuarios per : listaUsuariosAsociar) {
            encontro = false;
            perDocActual = null;
            for (int i = 0; i < listaAsociarActual.size(); i++) {
                per2 = listaAsociarActual.get(i).getUsuarioAsociado();
                if (per2.equals(per)) {
                    encontro = true;
                    perDocActual = listaAsociarActual.get(i);
                    break;
                }
            }
            if (!encontro) {
                UsuariosAsociadosPK pk = new UsuariosAsociadosPK(persona.getId(), per.getId());
                UsuariosAsociados perDoc = new UsuariosAsociados(pk);
                perDoc.setUsuario(persona);
                perDoc.setUsuarioAsociado(per);

                usuariosAsociadosController.setSelected(perDoc);
                usuariosAsociadosController.saveNew(null);
            }
        }

        for (int i = 0; i < listaAsociarActual.size(); i++) {
            per2 = listaAsociarActual.get(i).getUsuarioAsociado();
            encontro = false;
            for (Usuarios per : listaUsuariosAsociar) {
                if (per2.equals(per)) {
                    encontro = true;
                    break;
                }
            }
            if (!encontro) {
                usuariosAsociadosController.setSelected(listaAsociarActual.get(i));
                usuariosAsociadosController.delete(null);
            }
        }
    }

    public String redirigir() {

        List<VPermisosUsuarios> p = ejbFacade.getEntityManager().createNamedQuery("VPermisosUsuarios.findByUsuaId", VPermisosUsuarios.class).setParameter("usuaId", usuarioElegido.getId()).getResultList();

        if (!usuarioElegido.equals(usuarioOriginal)) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Backup", usuarioElegido);
        }

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("VPermisosUsuarios", p);

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Usuarios", usuarioOriginal);

        if (usuarioElegido.getPantallaPrincipal() != null) {
            home = usuarioElegido.getPantallaPrincipal().getUrl();
        } else {
            home = "index";
        }

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Home", home);

        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/index.xhtml");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return home + ".xhtml?faces-redirect=true";
    }

    public String loginControl() {
        return loginControl(usuario, contrasena);
    }

    public String loginControl(String usuario, String contrasena) {
        return loginControl(usuario, contrasena, true);
    }

    public String loginControl(String usuario, String contrasena, boolean procesarError) {

        Usuarios usu = verifUsuario(usuario, contrasena, "AC");
        if (usu == null) {
            if (procesarError) {
                PrimeFaces.current().ajax().update("growl");
                FacesContext context = FacesContext.getCurrentInstance();

                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error de acceso"));
                return "";
            }

        } else {
            List<UsuariosAsociados> usuAs = ejbFacade.getEntityManager().createNamedQuery("UsuariosAsociados.findByUsuarioAsociado", UsuariosAsociados.class).setParameter("usuarioAsociado", usu.getId()).getResultList();
            /* if (usuAs.size() > 1) {
                return null;
            }
             */

            List<VPermisosUsuarios> p = null;
            if (usuAs.isEmpty()) {
                p = ejbFacade.getEntityManager().createNamedQuery("VPermisosUsuarios.findByUsuaId", VPermisosUsuarios.class).setParameter("usuaId", usu.getId()).getResultList();
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("VPermisosUsuarios", p);

                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Usuarios", usu);

                if (usu.getPantallaPrincipal() != null) {
                    if (pantalla == null) {
                        home = usu.getPantallaPrincipal().getUrl();
                    } else {
                        List<Pantallas> lista = ejbFacade.getEntityManager().createNamedQuery("Pantallas.findByTag", Pantallas.class).setParameter("tag", pantalla).getResultList();
                        if (lista.isEmpty()) {
                            home = usuarioOriginal.getPantallaPrincipal().getUrl();
                        } else {
                            home = lista.get(0).getUrl();
                        }
                    }
                } else {
                    home = "index";
                }

                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Home", home);

                return home + ".xhtml?faces-redirect=true";
            } else {
                listaUsuariosElegir = new ArrayList<>();
                listaUsuariosElegir.add(usuarioOriginal);
                for (UsuariosAsociados us : usuAs) {
                    listaUsuariosElegir.add(us.getUsuario());
                }

                PrimeFaces.current().ajax().update("DocumentosListForm");
                PrimeFaces current = PrimeFaces.current();
                current.executeScript("PF('DocumentosCreateDialog').show();");
                return "";
            }
        }
        return "";
    }

    /*
    public Usuarios verifHash() {
        
    }
     */
    public Usuarios verifUsuario(String username, String pass, String estado) {
        try {

            this.ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

            String password = Utils.passwordToHash(pass);

            // List<Usuarios> usu = null;
            EstadosUsuario est = new EstadosUsuario();

            est.setCodigo(estado);

            try {
                usuarioOriginal = ejbFacade.getEntityManager().createNamedQuery("Usuarios.control", Usuarios.class).setParameter("usuario", username).setParameter("contrasena", password).setParameter("estado", est).getSingleResult();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (usuarioOriginal == null) {
                personaOriginal = this.ejbFacade.getEntityManager().createNamedQuery("Personas.control", Personas.class).setParameter("usuario", username).setParameter("contrasena", password).getSingleResult();
                usuarioOriginal = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findByPersona", Usuarios.class).setParameter("id", personaOriginal.getId()).getSingleResult();

                return null;
            }

            return usuarioOriginal;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String cerrarSession() {
        this.usuario = null;
        this.contrasena = null;
        HttpSession httpSession;
        httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        httpSession.invalidate();//para borrar la session
        return "/login?faces-redirect=true";

    }

    public void setSelected(Usuarios selected) {
        super.setSelected(selected);
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

    public void saveContrasena(ActionEvent event) {

        if (getSelected() != null) {

            if (cambioContrasena1.equals(cambioContrasena2)) {

                ((Usuarios) getSelected()).setContrasena(cambioContrasena1);
                cambioContrasena1 = "";
                cambioContrasena2 = "";
                this.save(event);

            } else {
                JsfUtil.addErrorMessage("Contrasenas no coinciden");
            }
        }
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

            if (cambioContrasena1 != null && cambioContrasena2 != null) {
                if ("".equals(cambioContrasena1) || "".equals(cambioContrasena2)) {
                    JsfUtil.addErrorMessage("Debe ingresar contrasena y confirmar contrasena.");

                } else if (cambioContrasena1.equals(cambioContrasena2)) {

                    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

                    Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

                    Date fecha = ejbFacade.getSystemDate();

                    getSelected().setFechaHoraUltimoEstado(fecha);
                    getSelected().setUsuarioUltimoEstado(usu);
                    getSelected().setFechaHoraAlta(fecha);
                    getSelected().setUsuarioAlta(usu);
                    getSelected().setEmpresa(usu.getEmpresa());

                    getSelected().setContrasena(cambioContrasena1);
                    cambioContrasena1 = "";
                    cambioContrasena2 = "";
                    super.saveNew(event);

                } else {
                    JsfUtil.addErrorMessage("Contrasenas no coinciden");
                }
            } else {
                JsfUtil.addErrorMessage("Debe ingresar contrasena y confirmar contrasena");
            }
        }

    }
    
    public boolean deshabilitarMenues(Integer menu) {

        return !(this.menu.equals(menu));
    }
    
    public boolean deshabilitarMenues(String url, String permiso, Integer menu) {
        return !(filtroURL.verifPermiso(url, permiso) && this.menu.equals(menu));
    }

    public boolean deshabilitarMenues(String url, Integer menu) {
        return !(filtroURL.verifPermiso(url) && this.menu.equals(menu));
    }
}
