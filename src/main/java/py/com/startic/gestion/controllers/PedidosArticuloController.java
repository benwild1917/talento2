package py.com.startic.gestion.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;

import py.com.startic.gestion.models.PedidosArticulo;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.persistence.TemporalType;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.primefaces.event.CellEditEvent;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.models.Articulos;
import py.com.startic.gestion.models.CambiosPedidoArticulo;
import py.com.startic.gestion.models.Departamentos;
import py.com.startic.gestion.models.DetallesEntradaArticulo;
import py.com.startic.gestion.models.DetallesInventario;
import py.com.startic.gestion.models.DetallesPedidoArticulo;
import py.com.startic.gestion.models.DetallesSalidaArticulo;
import py.com.startic.gestion.models.EntradasArticulo;
import py.com.startic.gestion.models.EstadosDocumento;
import py.com.startic.gestion.models.EstadosPedidoArticulo;
import py.com.startic.gestion.models.FlujosDocumento;
import py.com.startic.gestion.models.Inventarios;
import py.com.startic.gestion.models.ReporteInventarioValorizado;
import py.com.startic.gestion.models.Roles;
import py.com.startic.gestion.models.RolesPorUsuarios;
import py.com.startic.gestion.models.Rubros;
import py.com.startic.gestion.models.SalidasArticulo;
import py.com.startic.gestion.models.Sede;
import py.com.startic.gestion.models.TiposDocumentosJudiciales;
import py.com.startic.gestion.models.Usuarios;
import py.com.startic.gestion.models.VPermisosUsuarios;

@Named(value = "pedidosArticuloController")
@ViewScoped
public class PedidosArticuloController extends AbstractController<PedidosArticulo> {

    @Inject
    private UsuariosController personaController;
    @Inject
    private DepartamentosController departamentoController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private EmpresasController empresaController;
    @Inject
    private ArticulosController articuloController;
    @Inject
    private ArticulosStockCriticoController articuloStockCriticoController;
    @Inject
    private DetallesPedidoArticuloController detallesPedidoArticuloController;
    @Inject
    private SalidasArticuloController salidasArticuloController;
    @Inject
    private DetallesSalidaArticuloController detallesSalidaArticuloController;
    @Inject
    private SedeController sedeController;
    @Inject
    private TiposDocumentosJudicialesController tiposDocumentosJudicialesController;
    @Inject
    private CambiosPedidoArticuloController cambiosPedidoArticuloController;
    @Inject
    private ArticulosController articulosController;
    private Collection<DetallesPedidoArticulo> detalles;
    private TiposDocumentosJudiciales tipoDoc;

    private DetallesPedidoArticulo detalleSelected;
    private Date fechaDesde;
    private Date fechaHasta;

    private Articulos articulo;
    private Sede sede;
    private Rubros rubro;
    private Roles rol;
    private byte[] imagen;
    private Articulos articuloFiltro;
    private Departamentos departamentoFiltro;
    private final FiltroURL filtroURL = new FiltroURL();

    private Integer cantidad;

    private PedidosArticulo pedidoArticulo;
    private EntradasArticulo entradaArticulo;
    private DetallesEntradaArticulo detallesEntrada;

    private Usuarios usuario;
    private RolesPorUsuarios rolUsuario;
    private Departamentos departamento;
    private String codigo;
    private Collection<Usuarios> listaUsuariosTransf;
    private Collection<Articulos> listaUsuariosArticulos;
    // private String descripcionCambioEstado;
    private FlujosDocumento flujoDoc;
    private Integer nuevaCantidad;
    private String observacion;
    private FiltroURL filtro = new FiltroURL();
    private List<CambiosPedidoArticulo> detallesCambios;
    private boolean hayPedidosArticulo = false;
    private boolean hayPedidosArticuloPorAutotizar = false;
    private Integer nroFormulario;
    private List<Articulos> listaArticulos;
    private List<Articulos> listaArticulosPorUsuarios;
    private List<Articulos> listaImagen;

    private HttpSession session;
    private String menu;

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();
        tipoDoc = tiposDocumentosJudicialesController.prepareCreate(null);
        tipoDoc.setCodigo("SU");
        pedidoArticulo = prepareCreate(null);
        fechaDesde = ejbFacade.getSystemDateOnly(-30);
        fechaHasta = ejbFacade.getSystemDateOnly();
        //nroFormulario = 0;

        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        usuario = (Usuarios) session.getAttribute("Usuarios");

        // descripcionCambioEstado = Constantes.LABEL_BTN_AUTORIZAR_PEDIDO_ARTICULO;
        buscarPorFechaAlta();

        verifPedidosArticulo();
        verifPedidosArticuloParaAutorizar();
    }

    public boolean isHayPedidosArticulo() {
        return hayPedidosArticulo;
    }

    public void setHayPedidosArticulo(boolean hayPedidosArticulo) {
        this.hayPedidosArticulo = hayPedidosArticulo;
    }

    public boolean isHayPedidosArticuloPorAutotizar() {
        return hayPedidosArticuloPorAutotizar;
    }

    public void setHayPedidosArticuloPorAutotizar(boolean hayPedidosArticuloPorAutotizar) {
        this.hayPedidosArticuloPorAutotizar = hayPedidosArticuloPorAutotizar;
    }

    public String navigatePedidosArticulo() {
        return "/pages/pedidosArticulo/index";
    }

    public Integer getNroFormulario() {
        return nroFormulario;
    }

    public void setNroFormulario(Integer nroFormulario) {
        this.nroFormulario = nroFormulario;
    }

    public List<Articulos> getListaArticulos() {
        return listaArticulos;
    }

    public void setListaArticulos(List<Articulos> listaArticulos) {
        this.listaArticulos = listaArticulos;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public List<Articulos> getListaImagen() {
        return listaImagen;
    }

    public void setListaImagen(List<Articulos> listaImagen) {
        this.listaImagen = listaImagen;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Roles getRol() {
        return rol;
    }

    public void setRol(Roles rol) {
        this.rol = rol;
    }

    public EntradasArticulo getEntradaArticulo() {
        return entradaArticulo;
    }

    public void setEntradaArticulo(EntradasArticulo entradaArticulo) {
        this.entradaArticulo = entradaArticulo;
    }

    public DetallesEntradaArticulo getDetallesEntrada() {
        return detallesEntrada;
    }

    public void setDetallesEntrada(DetallesEntradaArticulo detallesEntrada) {
        this.detallesEntrada = detallesEntrada;
    }

    public List<Articulos> getListaArticulosPorUsuarios() {
        return listaArticulosPorUsuarios;
    }

    public void setListaArticulosPorUsuarios(List<Articulos> listaArticulosPorUsuarios) {
        this.listaArticulosPorUsuarios = listaArticulosPorUsuarios;
    }

    public Departamentos getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamentos departamento) {
        this.departamento = departamento;
    }

    public RolesPorUsuarios getRolUsuario() {
        return rolUsuario;
    }

    public void setRolUsuario(RolesPorUsuarios rolUsuario) {
        this.rolUsuario = rolUsuario;
    }
    
    

    public void prepareEdit() {
        if (getSelected() != null) {
            // actualizarListas(getSelected().getSede());

        }

    }

    public boolean verifPedidosArticulo() {
        hayPedidosArticulo = false;
        try {
            Collection<PedidosArticulo> art = this.ejbFacade.getEntityManager().createNamedQuery("PedidosArticulo.findPedidosPendientes", PedidosArticulo.class).setParameter("departamento", usuario.getDepartamento()).getResultList();

            hayPedidosArticulo = false;

            if (art != null) {
                if (art.size() > 0) {
                    hayPedidosArticulo = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            hayPedidosArticulo = false;
        }

        return hayPedidosArticulo;
    }

    public boolean verifPedidosArticuloParaAutorizar() {
        hayPedidosArticuloPorAutotizar = false;
        try {
            Collection<PedidosArticulo> art = this.ejbFacade.getEntityManager().createNamedQuery("PedidosArticulo.findPedidosPorAutorizar", PedidosArticulo.class).setParameter("departamento", usuario.getDepartamento()).getResultList();

            hayPedidosArticuloPorAutotizar = false;

            if (art != null) {
                if (art.size() > 0) {
                    hayPedidosArticuloPorAutotizar = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            hayPedidosArticuloPorAutotizar = false;
        }

        return hayPedidosArticuloPorAutotizar;
    }

    @Override
    public PedidosArticulo prepareCreate(ActionEvent event) {
        PedidosArticulo nroFor = super.prepareCreate(null);
        nroFor.setNroFormulario(obtenerProximoNroFormulario());
        detalles = null;
        detallesEntrada = null;
        detalleSelected = null;
        articulo = null;
        cantidad = 0;
        Inventarios inv = null;
        try {
            inv = ejbFacade.getEntityManager().createNamedQuery("Inventarios.findVigente", Inventarios.class).getSingleResult();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (inv == null) {
            JsfUtil.addErrorMessage("Para ingresar un movimiento debe haber al menos un inventario vigente");
            return null;
        }

        if (pedidoArticulo != null) {
            //pedidoArticulo.setNroFormulario(0);
            pedidoArticulo.setPersona(null);
            pedidoArticulo.setDepartamento(null);
            pedidoArticulo.setSede(null);
        }

        return super.prepareCreate(event);
    }

    public PedidosArticuloController() {
        // Inform the Abstract parent controller of the concrete PedidosArticulo Entity
        super(PedidosArticulo.class);
    }

    private Integer obtenerProximoNroFormulario() {

        Object obj = this.ejbFacade.getEntityManager().createQuery("select COALESCE(max(s.nroFormulario),0) from SalidasArticulo s").getSingleResult();

        if (obj instanceof Long) {
            return ((Long) obj).intValue() + 1;
        } else {
            return ((Integer) obj) + 1;
        }
    }

    private List<Articulos> obtenerListaImagen(Articulos codigo) {
        if (codigo != null) {
            try {
                Articulos art = ejbFacade.getEntityManager().createNamedQuery("Articulos.findByCodigo", Articulos.class
                ).setParameter("codigo", codigo).getSingleResult();
                byte[] arreglo = art.getImagen();
                HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
                response.getOutputStream().write(arreglo);
                response.getOutputStream().close();
                FacesContext.getCurrentInstance().responseComplete();

            } catch (Exception e) {
                FacesMessage message = new FacesMessage("Error");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }

        }
        return null;
    }

    public void actualizarListas(Rubros rubro) {
        listaArticulos = obtenerListaArticulos(rubro);

    }

    public void actualizarListasImagen(Articulos codigo) {
        listaImagen = obtenerListaImagen(codigo);

    }

    public void resetearListas(Rubros rubro) {
        articulo = null;

        if (getSelected() != null) {
            getSelected().setArticulo(null);

        }
        actualizarListas(rubro);
        // actualizarListasImagen(imagen);
    }

    public void resetearListasImagen(Articulos codigo) {
        imagen = null;

        if (getSelected() != null) {
            getSelected().getArticulo().setImagen(null);

        }

        actualizarListasImagen(codigo);
    }

    private List<Articulos> obtenerListaArticulos(Rubros rubro) {
        if (rubro != null) {
            return this.ejbFacade.getEntityManager().createNamedQuery("Articulos.findByPorUsuario", Articulos.class).setParameter("rubro", rubro).getResultList();
        }
        return null;
    }

    private List<Articulos> obtenerListaArticulosPorUsuarios(Usuarios usu) {
        if (usu != null) {
            return this.ejbFacade.getEntityManager().createNamedQuery("Articulos.findByListaArticulos", Articulos.class).setParameter("usuario", usuario).setParameter("rubro", rubro).setParameter("rol", usuario).getResultList();
        }
        return null;
    }

    public void actualizarListasArticulosPorUsuario(Usuarios usu) {
        listaArticulosPorUsuarios = obtenerListaArticulosPorUsuarios(usu);

    }

    public Collection<Usuarios> getListaUsuariosTransf() {
        if (Constantes.ESTADO_PEDIDO_ARTICULO_CA.equals(getSelected().getEstado().getCodigo())) {

            if (getSelected() != null) {

                listaUsuariosTransf = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferirDpto", Usuarios.class).setParameter("tipoDocumento", Constantes.TIPO_DOCUMENTO_JUDICIAL_SU).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).setParameter("departamento", usuario.getDepartamento()).getResultList();
            } else {
                listaUsuariosTransf = new ArrayList<>();
            }
        } else {
            if (getSelected() != null) {

                listaUsuariosTransf = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferirPedido", Usuarios.class).setParameter("tipoDocumento", Constantes.TIPO_DOCUMENTO_JUDICIAL_SU).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).getResultList();
            } else {
                listaUsuariosTransf = new ArrayList<>();
            }
        }
        return listaUsuariosTransf;

    }

    public List<CambiosPedidoArticulo> getDetallesCambios() {
        return detallesCambios;
    }

    public void setDetallesCambios(List<CambiosPedidoArticulo> detallesCambios) {
        this.detallesCambios = detallesCambios;
    }

    public Integer getNuevaCantidad() {
        return nuevaCantidad;
    }

    public void setNuevaCantidad(Integer nuevaCantidad) {
        this.nuevaCantidad = nuevaCantidad;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public void setListaUsuariosTransf(Collection<Usuarios> listaUsuariosTransf) {
        this.listaUsuariosTransf = listaUsuariosTransf;
    }

    public void setListaUsuariosArticulos(Collection<Articulos> listaUsuariosArticulos) {
        this.listaUsuariosArticulos = listaUsuariosArticulos;
    }

    public Departamentos getDepartamentoFiltro() {
        return departamentoFiltro;
    }

    public void setDepartamentoFiltro(Departamentos departamentoFiltro) {
        this.departamentoFiltro = departamentoFiltro;
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

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public Articulos getArticuloFiltro() {
        return articuloFiltro;
    }

    public void setArticuloFiltro(Articulos articuloFiltro) {
        this.articuloFiltro = articuloFiltro;
    }

    public Collection<DetallesPedidoArticulo> getDetalles() {
        return detalles;
    }

    public void setDetalles(Collection<DetallesPedidoArticulo> detalles) {
        this.detalles = detalles;
    }

    public Articulos getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulos articulo) {
        this.articulo = articulo;
    }

    public Sede getSede() {

        if (session.getAttribute("sedeSelected") != null) {
            sede = (Sede) session.getAttribute("sedeSelected");
            session.removeAttribute("sedeSelected");
            //actualizarListas(sede);
        }

        return sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }

    public Rubros getRubro() {
        return rubro;
    }

    public void setRubro(Rubros rubro) {
        this.rubro = rubro;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public PedidosArticulo getPedidoArticulo() {
        return pedidoArticulo;
    }

    public void setPedidoArticulo(PedidosArticulo pedidoArticulo) {
        this.pedidoArticulo = pedidoArticulo;
    }

    public DetallesPedidoArticulo getDetalleSelected() {
        return detalleSelected;
    }

    public void setDetalleSelected(DetallesPedidoArticulo detalleSelected) {
        this.detalleSelected = detalleSelected;
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        personaController.setSelected(null);
        departamentoController.setSelected(null);
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);

        seleccionar();
        seleccionarCambios();
        /*
        if (getSelected() != null) {
            if (Constantes.ESTADO_PEDIDO_ARTICULO_6.equals(getSelected().getEstado().getCodigo())) {
                descripcionCambioEstado = Constantes.LABEL_BTN_ENTREGAR_PEDIDO_ARTICULO;
            } else {
                descripcionCambioEstado = Constantes.LABEL_BTN_AUTORIZAR_PEDIDO_ARTICULO;
            }
        } else {
            descripcionCambioEstado = Constantes.LABEL_BTN_AUTORIZAR_PEDIDO_ARTICULO;
        }
         */
    }

    public Collection<Articulos> getListaUsuariosArticulos() {
        if (getSelected() == null) {
            listaUsuariosArticulos = this.ejbFacade.getEntityManager().createNamedQuery("Articulos.findDptoArticulos", Articulos.class).setParameter("sede", rubro.getSede()).setParameter("usuario", usuario).getResultList();
        } else {
            listaUsuariosArticulos = new ArrayList<>();
        }
        return listaUsuariosArticulos;
    }

    @Override
    public Collection<PedidosArticulo> getItems() {
        return super.getItems2();
    }

    public String datePattern() {
        return "dd/MM/yyyy HH:mm:ss";
    }

    public String customFormatDate(Date date) {
        if (date != null) {
            DateFormat format = new SimpleDateFormat(datePattern());
            return format.format(date);
        }
        return "";
    }

    public void buscarPorFechaAlta() {
        if ((fechaDesde == null || fechaHasta == null) && (nroFormulario == null || "".equals(nroFormulario))) {
            JsfUtil.addErrorMessage("Debe ingresar Rango de Fechas o Nro de Formulario");
        } else {
            if (nroFormulario == null || "".equals(nroFormulario)) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(fechaHasta);
                cal.add(Calendar.DATE, 1);
                Date nuevaFechaHasta = cal.getTime();
                if (verPedidosArticuloEnDpto()) {
                    setItems(this.ejbFacade.getEntityManager().createNamedQuery("PedidosArticulo.findOrderedFechaAltaAsignado", PedidosArticulo.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", nuevaFechaHasta).setParameter("departamento", usuario.getDepartamento()).getResultList());
                } else if (verTodosPedidosArticulo()) {
                    setItems(this.ejbFacade.getEntityManager().createNamedQuery("PedidosArticulo.findOrdered", PedidosArticulo.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", nuevaFechaHasta).getResultList());
                } else {
                    setItems(this.ejbFacade.getEntityManager().createNamedQuery("PedidosArticulo.findOrderedFechaAltaAsignadoPersona", PedidosArticulo.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", nuevaFechaHasta).setParameter("usuario", usuario).getResultList());
                }
            } else {
                if (verPedidosArticuloEnDpto()) {
                    setItems(this.ejbFacade.getEntityManager().createNamedQuery("PedidosArticulo.findOrderedNroFormularioAsignado", PedidosArticulo.class).setParameter("nroFormulario", nroFormulario).setParameter("departamento", usuario.getDepartamento()).getResultList());
                } else if (verTodosPedidosArticulo()) {
                    setItems(this.ejbFacade.getEntityManager().createNamedQuery("PedidosArticulo.findOrderedNroFormulario", PedidosArticulo.class).setParameter("nroFormulario", nroFormulario).getResultList());
                } else {
                    setItems(this.ejbFacade.getEntityManager().createNamedQuery("PedidosArticulo.findOrderedNroFormularioAsignadoPersona", PedidosArticulo.class).setParameter("nroFormulario", nroFormulario).setParameter("usuario", usuario).getResultList());
                }
            }

            if (getItems2().size() > 0) {
                PedidosArticulo art = getItems2().iterator().next();
                setSelected(art);
                //detalles = art.getDetallesPedidoArticuloCollection();
                resetParents();

                detalleSelected = null;
            } else {
                setSelected(null);
            }

        }
    }

    private void seleccionar() {
        if (getSelected() != null) {
            detalles = this.ejbFacade.getEntityManager().createNamedQuery("DetallesPedidoArticulo.findByPedidoArticulo", DetallesPedidoArticulo.class).setParameter("pedidoArticulo", getSelected()).getResultList();
        } else {
            detalles = null;
        }

    }

    private void seleccionarCambios() {
        if (getSelected() != null) {
            detallesCambios = this.ejbFacade.getEntityManager().createNamedQuery("CambiosPedidoArticulo.findByPedidoArticulo", CambiosPedidoArticulo.class).setParameter("pedidoArticulo", getSelected()).getResultList();
        } else {
            detallesCambios = null;
        }

    }

    public void prepararCambio(DetallesPedidoArticulo item) {
        detalleSelected = item;
        nuevaCantidad = detalleSelected.getCantidad();
        observacion = "";
    }

    public void guardarCambio() {

        if (detalleSelected != null) {

            if (detalleSelected.getCantidad() != nuevaCantidad) {
                CambiosPedidoArticulo cambio = cambiosPedidoArticuloController.prepareCreate(null);

                cambio.setCantidadOriginal(detalleSelected.getCantidad());
                cambio.setCantidadFinal(nuevaCantidad);
                cambio.setPedidoArticulo(detalleSelected.getPedidoArticulo());
                cambio.setDetallePedidoArticulo(detalleSelected);
                cambio.setObservacion(observacion);
                cambio.setArticulo(detalleSelected.getArticulo());

                //detallesSelected = ejbFacade.getEntityManager().createNamedQuery("DetallesPedidoArticulo.findById", DetallesPedidoArticulo.class).setParameter("id", Integer.valueOf(key)).getSingleResult();
                detalleSelected.setCantidad(nuevaCantidad);
                detallesPedidoArticuloController.setSelected(detalleSelected);
                detallesPedidoArticuloController.save(null);

                cambiosPedidoArticuloController.setSelected(cambio);
                cambiosPedidoArticuloController.saveNew(null);

                // resetParents();
            } else {
                JsfUtil.addErrorMessage("No hay ningun cambio que registrar");
            }
        }
    }

    public boolean verPedidosArticuloEnDpto() {

        try {
            this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuarioRol", FlujosDocumento.class).setParameter("usuario", usuario.getId()).setParameter("rol", -16).getSingleResult();
            return true;
        } catch (Exception e) {
            // e.printStackTrace();
        }

        return false;
    }

    public boolean verTodosPedidosArticulo() {

        try {
            this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuarioRol", FlujosDocumento.class).setParameter("usuario", usuario.getId()).setParameter("rol", -18).getSingleResult();
            return true;
        } catch (Exception e) {
            // e.printStackTrace();
        }

        /* try {
            this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuarioRol", FlujosDocumento.class).setParameter("usuario", usuario.getId()).setParameter("rol", -18).getSingleResult();
            return true;
        } catch (Exception e) {
            // e.printStackTrace();
        }*/
        return false;
    }

    public boolean desabilitarBotonCambioCantidad(DetallesPedidoArticulo item) {

        if (item != null) {

            /* if (item.getPedidoArticulo().getEstado().getCodigo().equals("1")) {
                return false;
            }*/
            if (!item.getPedidoArticulo().getEstado().getCodigo().equals("2") && !item.getPedidoArticulo().getEstado().getCodigo().equals("4")) {
                return true;
            }

            if (!item.getPedidoArticulo().getResponsable().equals(usuario)) {
                return true;
            }

            try {
                this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuarioRol", FlujosDocumento.class).setParameter("usuario", usuario.getId()).setParameter("rol", -16).getSingleResult();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuarioRol", FlujosDocumento.class).setParameter("usuario", usuario.getId()).setParameter("rol", -18).getSingleResult();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
            }
           /* try {
                this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuarioRol", FlujosDocumento.class).setParameter("usuario", usuario.getId()).setParameter("rol", -14).getSingleResult();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }

        return true;
    }

    public boolean desabilitarBotonAutorizar() {
        if (getSelected() != null) {
            if (!getSelected().getEstado().getCodigo().equals("2")) {
                return true;
            }

            if (!getSelected().getResponsable().equals(usuario)) {
                return true;
            }

            /*try {
                this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuarioRol", FlujosDocumento.class).setParameter("usuario", usuario.getId()).setParameter("rol", -17).getSingleResult();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
            }*/

            try {
                this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuarioRol", FlujosDocumento.class).setParameter("usuario", usuario.getId()).setParameter("rol", -16).getSingleResult();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    public boolean desabilitarBotonRechazar() {
        if (getSelected() != null) {
            if (!getSelected().getEstado().getCodigo().equals("2")) {
                return true;
            }

            if (!getSelected().getResponsable().equals(usuario)) {
                return true;
            }

            /*try {
                this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuarioRol", FlujosDocumento.class).setParameter("usuario", usuario.getId()).setParameter("rol", -17).getSingleResult();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
            }*/

            try {
                this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuarioRol", FlujosDocumento.class).setParameter("usuario", usuario.getId()).setParameter("rol", -16).getSingleResult();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    public boolean desabilitarBotonAnular() {
        if (getSelected() != null) {
            if (!getSelected().getEstado().getCodigo().equals("5")) {
                return true;
            }

            // if (!getSelected().getResponsable().equals(usuario)) {
            //    return true;
            // }
            try {
                this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuarioRol", FlujosDocumento.class).setParameter("usuario", usuario.getId()).setParameter("rol", -18).getSingleResult();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return true;
    }

    public void verificarPedidos() {
        if (getSelected() != null) {
            if (getSelected().getEstado().getCodigo().equals("3")) {
                EstadosPedidoArticulo estado = this.ejbFacade.getEntityManager().createNamedQuery("EstadosPedidoArticulo.findByCodigo", EstadosPedidoArticulo.class).setParameter("codigo", "4").getSingleResult();
                Date fecha = ejbFacade.getSystemDate();
                getSelected().setEstado(estado);
                getSelected().setFechaHoraUltimoEstado(fecha);
                getSelected().setUsuarioUltimoEstado(usuario);
                super.save(null);
                buscarPorFechaAlta();
            } else {
                JsfUtil.addErrorMessage("Error al verificar");
            }
        }

    }

    public boolean verStock() {
        try {
            this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuarioRol", Articulos.class).setParameter("usuario", usuario.getId()).setParameter("rol", -16).getSingleResult();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

         try {
            this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuarioRol", FlujosDocumento.class).setParameter("usuario", usuario.getId()).setParameter("rol", -17).getSingleResult();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean desabilitarBotonEntregar() {
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

    public boolean desabilitarBotonVerificar() {
        if (getSelected() != null) {
            if (!getSelected().getEstado().getCodigo().equals("4")) {
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

    public boolean desabilitarBotonBorrar() {

        if (getSelected() != null) {
            if (getSelected().getResponsable() != null) {
                return !(getSelected().getResponsable().equals(usuario) && getSelected().getEstado().getCodigo().equals("1"));
            }

        }

        return true;
    }

    public boolean desabilitarBotonCambioEstado() {

        if (getSelected() != null) {
            if (getSelected().getResponsable() != null) {
                if (getSelected().getResponsable().equals(usuario)) {

                    try {
                        flujoDoc = this.ejbFacade.getEntityManager().createNamedQuery("FlujosDocumento.findByEstadoDocumentoActual", FlujosDocumento.class).setParameter("tipoDocumento", tipoDoc.getCodigo()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).getSingleResult();
                        return false;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

        }

        return true;
    }

    public boolean desabilitarBotonConfirmacionPedidos() {

        if (getSelected() != null) {
            if (getSelected().getResponsable() != null) {
                // if (getSelected().getResponsable().equals(usuario)) {

                try {
                    flujoDoc = this.ejbFacade.getEntityManager().createNamedQuery("FlujosDocumento.findByEstadoDocumentoActual", FlujosDocumento.class).setParameter("tipoDocumento", tipoDoc.getCodigo()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).getSingleResult();
                    return false;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //}
            }

        }

        return true;
    }

    public void rechazar() {
        if (getSelected() != null) {
            if (getSelected().getEstado().getCodigo().equals("2")) {
                EstadosPedidoArticulo estado = this.ejbFacade.getEntityManager().createNamedQuery("EstadosPedidoArticulo.findByCodigo", EstadosPedidoArticulo.class).setParameter("codigo", "9").getSingleResult();
                Date fecha = ejbFacade.getSystemDate();
                getSelected().setEstado(estado);
                getSelected().setFechaHoraUltimoEstado(fecha);
                getSelected().setUsuarioUltimoEstado(usuario);
                super.save(null);
                buscarPorFechaAlta();
            } else if (getSelected().getEstado().getCodigo().equals("4")) {
                EstadosPedidoArticulo estado = this.ejbFacade.getEntityManager().createNamedQuery("EstadosPedidoArticulo.findByCodigo", EstadosPedidoArticulo.class).setParameter("codigo", "9").getSingleResult();
                Date fecha = ejbFacade.getSystemDate();
                getSelected().setEstado(estado);
                getSelected().setFechaHoraUltimoEstado(fecha);
                getSelected().setUsuarioUltimoEstado(usuario);
                super.save(null);
                buscarPorFechaAlta();
            } else {
                JsfUtil.addErrorMessage("Error al rechazar");
            }
        }

    }

    public void anularPedidos() {
        if (getSelected() != null) {
            if (getSelected().getEstado().getCodigo().equals("5")) {
                EstadosPedidoArticulo estado = this.ejbFacade.getEntityManager().createNamedQuery("EstadosPedidoArticulo.findByCodigo", EstadosPedidoArticulo.class).setParameter("codigo", "10").getSingleResult();
                Date fecha = ejbFacade.getSystemDate();
                getSelected().setEstado(estado);
                getSelected().setFechaHoraUltimoEstado(fecha);
                getSelected().setUsuarioUltimoEstado(usuario);
                super.save(null);
                buscarPorFechaAlta();
            } else {
                JsfUtil.addErrorMessage("Error al anular");
            }
        }

    }

    public void cambiarEstado() {
        if (getSelected() != null) {
            try {
                flujoDoc = this.ejbFacade.getEntityManager().createNamedQuery("FlujosDocumento.findByEstadoDocumentoActual", FlujosDocumento.class).setParameter("tipoDocumento", tipoDoc.getCodigo()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).getSingleResult();
            } catch (Exception e) {
                e.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo determinar flujo del documento. Documento no se puede cambiar estado");
                return;
            }

            Date fecha = ejbFacade.getSystemDate();

            if (Constantes.ESTADO_PEDIDO_ARTICULO_8.equals(flujoDoc.getEstadoDocumentoFinal())) {

                Inventarios inv = null;
                try {
                    inv = ejbFacade.getEntityManager().createNamedQuery("Inventarios.findVigente", Inventarios.class).getSingleResult();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (inv == null) {
                    JsfUtil.addErrorMessage("Para ingresar un movimiento debe haber al menos un inventario vigente");
                    return;
                }

                List<DetallesPedidoArticulo> detPedido = ejbFacade.getEntityManager().createNamedQuery("DetallesPedidoArticulo.findByPedidoArticulo", DetallesPedidoArticulo.class).setParameter("pedidoArticulo", getSelected()).getResultList();

                // Verificando si los articulos todavia estan en existencia
                for (DetallesPedidoArticulo ped : detPedido) {
                    Articulos art = ejbFacade.getEntityManager().createNamedQuery("Articulos.findByCodigo", Articulos.class).setParameter("codigo", ped.getArticulo().getCodigo()).getSingleResult();

                    if (art.getStock() - ped.getCantidad() < 0) {
                        JsfUtil.addErrorMessage("No se puede entragar " + ped.getCantidad() + " " + ped.getArticulo().getUnidad().getSimbolo() + " del articulo " + ped.getArticulo().getDescripcion() + " pues su stock solo es de " + ped.getArticulo().getStock() + " " + ped.getArticulo().getUnidad().getSimbolo());
                        return;
                    }
                }

                SalidasArticulo salidaArticulo = salidasArticuloController.prepareCreate(null);
                DetallesSalidaArticulo detallesSalidaArticulo = null;
                salidaArticulo.setPersona(getSelected().getUsuarioAlta());
                salidaArticulo.setDepartamento(getSelected().getResponsable().getDepartamento());

                salidaArticulo.setNroFormulario(obtenerProximoNroFormulario());
                salidaArticulo.setFechaHoraAlta(fecha);
                salidaArticulo.setFechaHoraUltimoEstado(fecha);
                salidaArticulo.setUsuarioAlta(usuario);
                salidaArticulo.setUsuarioUltimoEstado(usuario);
                salidaArticulo.setEmpresa(getSelected().getEmpresa());
                salidaArticulo.setPersonas(getSelected().getPersonas());
                salidaArticulo.setInventario(inv);
                salidaArticulo.setFechaSalida(fecha);
                salidaArticulo.setResponsableRetiro(getSelected().getResponsableRetiro());
                salidaArticulo.setResponsableAutoriza(getSelected().getResponsableAutoriza());
                salidasArticuloController.setSelected(salidaArticulo);

                salidasArticuloController.saveNewOnly(null);

                getSelected().setSalidaArticulo(salidaArticulo);

                List<DetallesPedidoArticulo> detPed = ejbFacade.getEntityManager().createNamedQuery("DetallesPedidoArticulo.findByPedidoArticulo", DetallesPedidoArticulo.class).setParameter("pedidoArticulo", getSelected()).getResultList();

                for (DetallesPedidoArticulo ped : detPed) {

                    //List<ReporteInventarioValorizado> lista = salidasArticuloController.obtenerEntradas(inv, ped.getArticulo(), ped.getCantidad(), salidasArticuloController.getDetalles());
                    List<ReporteInventarioValorizado> lista = ejbFacade
                            .getEntityManager()
                            .createNativeQuery("select codigo, inventario, detalle_entrada_articulo, detalle_inventario, articulo, sede, cant_entrada, SUM(cant_salida) cant_salida, tipo, fecha_vencimiento, fecha_entrada, fecha_hora_alta, costo_unitario, costo_total, (cant_entrada - SUM(cant_salida)) * costo_unitario as valorizado from reporte_inventario_valorizado AS e WHERE e.inventario = ? AND e.articulo = ? group by codigo, inventario, detalle_entrada_articulo, detalle_inventario, articulo, sede, cant_entrada, tipo, fecha_vencimiento, fecha_entrada, fecha_hora_alta, costo_unitario, costo_total having cant_entrada - SUM(cant_salida) > 0 ORDER BY fecha_vencimiento, e.fecha_entrada, e.costo_unitario", ReporteInventarioValorizado.class)
                            .setParameter(1, inv.getId())
                            .setParameter(2, ped.getArticulo().getCodigo())
                            .getResultList();
                    if (lista == null) {
                        JsfUtil.addErrorMessage("La cantidad del artículo seleccionado no esta disponible en stock");
                        return;
                    }

                    if (lista.isEmpty()) {
                        JsfUtil.addErrorMessage("La cantidad del artículo seleccionado no esta disponible en stock");
                        return;
                    }
                    Integer cantidadNeto;//cuanto se tiene disponible
                    Integer cantidadSobrante = ped.getCantidad();//cantidad pedida

                    for (ReporteInventarioValorizado item : lista) {
                        cantidadNeto = item.getCantEntrada() - item.getCantSalida();//diferencia entre entrada y salida de lotes
                        Integer cantidadSalida = 0;
                        if (cantidadNeto > 0 && cantidadSobrante > 0) {//si en el lote hay suficiente y si la cantidad pedida aun no se satisface
                            if (cantidadNeto > cantidadSobrante) {//si la cantidad del detalle del pedido es mayor a la cantidad disponible en el lote. se setea la cantidad pedida
                                cantidadSalida = cantidadSobrante;
                            } else {//si la cantidad disponible es mayor o igual al del pedido. se setea la cantidad disponible, y se pasa el RESTO al siguiente ciclo 
                                cantidadSalida = cantidadNeto;
                            }

                            detallesSalidaArticulo = detallesSalidaArticuloController.prepareCreate(null);

                            detallesSalidaArticulo.setSalidaArticulo(salidaArticulo);
                            detallesSalidaArticulo.setArticulo(ped.getArticulo());
                            detallesSalidaArticulo.setInventario(inv);
                            detallesSalidaArticulo.setCantidad(cantidadSalida);
                            detallesSalidaArticulo.setCostoUnitario(item.getCostoUnitario());
                            detallesSalidaArticulo.setCostoTotal(item.getCostoUnitario() == null ? BigDecimal.ZERO : item.getCostoUnitario().multiply(new BigDecimal(item.getCantEntrada())));
                            detallesSalidaArticulo.setModelo(ped.getArticulo().getModelo());
                            detallesSalidaArticulo.setObjetoGasto(ped.getArticulo().getObjetoGasto());
                            detallesSalidaArticulo.setCodigoSuministro(ped.getArticulo().getCodigoSuministro());

                            detallesSalidaArticulo.setFechaHoraUltimoEstado(fecha);
                            detallesSalidaArticulo.setUsuarioUltimoEstado(usuario);
                            detallesSalidaArticulo.setFechaHoraAlta(fecha);
                            detallesSalidaArticulo.setUsuarioAlta(usuario);
                            detallesSalidaArticulo.setEmpresa(usuario.getEmpresa());
                            detallesSalidaArticulo.setNroFormulario(salidaArticulo.getNroFormulario());
                            detallesSalidaArticulo.setPersona(ped.getPersona());
                            detallesSalidaArticulo.setDepartamento(ped.getDepartamento());
                            detallesSalidaArticulo.setItem(ped.getItem());
                            detallesSalidaArticulo.setSede(ped.getSede());

                            detallesSalidaArticulo.setEntradaArticulo(item.getDetalleEntradaArticulo() == null ? null : item.getDetalleEntradaArticulo().getEntradaArticulo());
                            detallesSalidaArticulo.setDetalleEntradaArticulo(item.getDetalleEntradaArticulo());
                            detallesSalidaArticulo.setDetalleInventario(item.getDetalleInventario());
                            detallesSalidaArticulo.setFechaVencimiento(item.getDetalleEntradaArticulo() == null ? item.getDetalleInventario().getFechaVencimiento() : item.getDetalleEntradaArticulo().getFechaVencimiento());

                            detallesSalidaArticulo.setId(null);

                            Articulos art = ejbFacade.getEntityManager().createNamedQuery("Articulos.findByCodigo", Articulos.class).setParameter("codigo", ped.getArticulo().getCodigo()).getSingleResult();

                            art.setStock(art.getStock() - cantidadSalida);

                            articuloController.setSelected(art);

                            articuloController.save(null);

                            detallesSalidaArticuloController.setSelected(detallesSalidaArticulo);

                            detallesSalidaArticuloController.saveNew(null);

                            cantidadSobrante = cantidadSobrante - cantidadSalida;//RESTO
                        }
                    }
                }
            }

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);
            EstadosPedidoArticulo estadoDocumentoFinal = null;
            try {
                estadoDocumentoFinal = this.ejbFacade.getEntityManager().createNamedQuery("EstadosPedidoArticulo.findByCodigo", EstadosPedidoArticulo.class).setParameter("codigo", flujoDoc.getEstadoDocumentoFinal()).getSingleResult();
            } catch (Exception e) {
                e.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo determinar estado final del pedido. El pedido no pudo cambiar de estado");
                return;
            }
            getSelected().setEstado(estadoDocumentoFinal);

            super.save(null);

            buscarPorFechaAlta();

            /* Ver si vamos a guardar los estados
            EstadosProcesalesDocumentosJudiciales estadoProc = estadosProcesalesDocumentosJudicialesController.prepareCreate(null);

            estadoProc.setUsuarioAlta(usuario);
            estadoProc.setUsuarioUltimoEstado(usuario);
            estadoProc.setFechaHoraAlta(fecha);
            estadoProc.setFechaHoraUltimoEstado(fecha);
            estadoProc.setEmpresa(usuario.getEmpresa());
            estadoProc.setEstadoProcesal(estadoDocumentoFinal.getDescripcion());
            estadoProc.setDocumentoJudicial(getSelected());

            // estadosProcesalesDocumentosJudicialesController.setSelected(estadoProc);
            // estadosProcesalesDocumentosJudicialesController.saveNew2(null);
            getSelected().setEstadoProcesalDocumentoJudicial(estadoProc);
            getSelected().setFechaHoraEstadoProcesal(fecha);
            getSelected().setUsuarioEstadoProcesal(usuario);

            super.save(null);

            verifDocumentosAtencion();

             */
        }
    }

    /**
     * Sets the "items" attribute with a collection of DetallesPedidoArticulo
     * entities that are retrieved from PedidosArticulo?cap_first and returns
     * the navigation outcome.
     *
     * @return navigation outcome for DetallesPedidoArticulo page
     */
    public String navigateDetallesPedidoArticuloCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("DetallesPedidoArticulo_items", this.getSelected().getDetallesPedidoArticuloCollection());
        }
        return "/pages/detallesPedidoArticulo/index";
    }

    /**
     * Sets the "selected" attribute of the Usuarios controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void preparePersona(ActionEvent event) {
        if (this.getSelected() != null && personaController.getSelected() == null) {
            personaController.setSelected(this.getSelected().getPersona());
        }
    }

    public void prepareEmpresas(ActionEvent event) {
        if (this.getSelected() != null && empresaController.getSelected() == null) {
            empresaController.setSelected(this.getSelected().getEmpresa());
        }
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
     * Sets the "selected" attribute of the Usuarios controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareArticulos(ActionEvent event) {
        if (this.getSelected() != null && articulosController.getSelected() == null) {
            articulosController.setSelected(this.getSelected().getArticulo());
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

    /**
     * Sets the "selected" attribute of the Departamentos controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareSede(ActionEvent event) {
        if (this.getSelected() != null && sedeController.getSelected() == null) {
            sedeController.setSelected(this.getSelected().getSede());
        }
    }

    public void prepareTransferir() {

    }

    public void prepareVerificarPedido() {

    }

    public void saveDpto() {

        if (getSelected() != null) {
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
                        ).setParameter("tipoDocumento", tipoDoc.getCodigo()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).getSingleResult();
            } catch (Exception e) {
                e.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo determinar flujo del documento. Documento no se puede transferir");
                return;
            }

            EstadosPedidoArticulo estado = null;

            try {
                // Codigo de enviado a secretaria
                estado = this.ejbFacade.getEntityManager().createNamedQuery("EstadosPedidoArticulo.findByCodigo", EstadosPedidoArticulo.class
                ).setParameter("codigo", flujoDoc.getEstadoDocumentoFinal()).getSingleResult();
                if (estado.getCodigo().equals("6")) {

                    getSelected().setResponsableAutoriza(usuario);

                    super.save(null);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo determinar sgte estado. Documento no se puede transferir");
                return;
            }

            getSelected().setEstado(estado);
            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);
            getSelected().setResponsableRetiro(usuario);

            super.save(null);

            buscarPorFechaAlta();
            /*
                if (busquedaPorFechaAlta) {
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
             */
        }
    }

    public void borrarDetalle() {
        if (detalles != null && detalleSelected != null) {
            for (DetallesPedidoArticulo det : detalles) {
                if (det.getArticulo().getCodigo().equals(detalleSelected.getArticulo().getCodigo())) {
                    detalles.remove(det);
                    break;
                }
            }
        }
    }

    @Override
    public void delete(ActionEvent event) {

        Articulos art = null;

        Collection<DetallesPedidoArticulo> col = ejbFacade.getEntityManager().createNamedQuery("DetallesPedidoArticulo.findByPedidoArticulo", DetallesPedidoArticulo.class).setParameter("pedidoArticulo", getSelected()).getResultList();

        for (DetallesPedidoArticulo det : col) {
            //art = det.getArticulo();

            //art.setStock(art.getStock() + det.getCantidad());
            //articuloController.setSelected(art);
            //articuloController.save(event);
            detallesPedidoArticuloController.setSelected(det);

            detallesPedidoArticuloController.delete(event);
        }

        Collection<CambiosPedidoArticulo> cambios = ejbFacade.getEntityManager().createNamedQuery("CambiosPedidoArticulo.findByPedidoArticulo", CambiosPedidoArticulo.class).setParameter("pedidoArticulo", getSelected()).getResultList();

        for (CambiosPedidoArticulo det : cambios) {

            cambiosPedidoArticuloController.setSelected(det);

            cambiosPedidoArticuloController.delete(event);
        }

        super.delete(event);
        //Calendar cal = Calendar.getInstance();
        //cal.setTime(fechaHasta);
        //cal.add(Calendar.DATE, 1);
        //Date nuevaFechaHasta = cal.getTime();

        buscarPorFechaAlta();

        //setItems(this.ejbFacade.getEntityManager().createNamedQuery("PedidosArticulo.findOrdered", PedidosArticulo.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", nuevaFechaHasta).getResultList());
        //setSelected(null);
        //detalleSelected = null;
        //detalles = null;
    }

    public void agregar() {

        if (articulo == null) {
            JsfUtil.addErrorMessage("Debe seleccionar el articulo");
            return;
        }

        if (cantidad == null) {
            JsfUtil.addErrorMessage("Debe especificar la cantidad");
            return;
        } else if (cantidad <= 0) {
            JsfUtil.addErrorMessage("La cantidad debe ser mayor a cero");
            return;
        }

        if (detalles == null) {
            detalles = new ArrayList<>();
        }

        boolean encontro = false;

        int contador = 0;

        for (DetallesPedidoArticulo det : detalles) {
            contador++;
            if (det.getArticulo().getCodigo().equals(articulo.getCodigo())) {

                if (articulo.getStock() < det.getCantidad() + cantidad) {
                    JsfUtil.addErrorMessage("La cantidad del articulo no debe supera el stock actual");
                    return;
                }

                detalles.remove(det);

                DetallesPedidoArticulo dsa = detallesPedidoArticuloController.prepareCreate(null);

                SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");

                Date fecha = ejbFacade.getSystemDate();

                dsa.setCantidad(det.getCantidad() + cantidad);
                dsa.setArticulo(articulo);
                dsa.setSede(articulo.getRubro().getSede());
                dsa.setId(Integer.valueOf(format.format(fecha)));
                detalles.add(dsa);
                encontro = true;
                break;
                //}
            }
        }

        if (!encontro) {
            DetallesPedidoArticulo dsa = detallesPedidoArticuloController.prepareCreate(null);

            SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");

            Date fecha = ejbFacade.getSystemDate();

            if (articulo.getStock() < cantidad) {
                JsfUtil.addErrorMessage("La cantidad del articulo no debe supera el stock actual");
                return;
            }

            dsa.setItem(contador + 1);
            dsa.setCantidad(cantidad);
            dsa.setArticulo(articulo);
            dsa.setSede(articulo.getRubro().getSede());
            dsa.setId(Integer.valueOf(format.format(fecha)));

            detalles.add(dsa);
        }

    }

    @Override
    public void save(ActionEvent event) {

        if (getSelected() != null) {

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);
        }

        super.save(event);

        this.verPedidosArticuloEnDpto();
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

            Date fecha = ejbFacade.getSystemDate();
            // pedidoArticulo.setNroFormulario(obtenerProximoNroFormulario());
            pedidoArticulo.setFechaHoraUltimoEstado(fecha);
            pedidoArticulo.setUsuarioUltimoEstado(usuario);
            pedidoArticulo.setFechaHoraAlta(fecha);
            pedidoArticulo.setUsuarioAlta(usuario);
            pedidoArticulo.setEmpresa(usuario.getEmpresa());
            pedidoArticulo.setPersona(usuario);
            pedidoArticulo.setResponsable(usuario);
            pedidoArticulo.setDepartamento(usuario.getDepartamento());
            pedidoArticulo.setPersonas(usuario.getNombresApellidos());
            //pedidoArticulo.setSede(sede);
            pedidoArticulo.setArticulo(articulo);

            EstadosPedidoArticulo estado = null;
            try {
                estado = this.ejbFacade.getEntityManager().createNamedQuery("EstadosPedidoArticulo.findByCodigo", EstadosPedidoArticulo.class).setParameter("codigo", Constantes.ESTADO_PEDIDO_ARTICULO_CA).getSingleResult();
            } catch (Exception e) {
                e.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo determinar estado inicial del pedido. El pedido no se pudo guardar");
                return;
            }
            pedidoArticulo.setEstado(estado);
            //pedidoArticulo.setDepartamento(pedidoArticulo.getPersona().getDepartamento());

            setSelected(pedidoArticulo);

            super.saveNew(event);

            for (DetallesPedidoArticulo det : detalles) {
                det.setPedidoArticulo(getSelected());
                det.setFechaHoraUltimoEstado(fecha);
                det.setUsuarioUltimoEstado(usuario);
                det.setFechaHoraAlta(fecha);
                det.setUsuarioAlta(usuario);
                det.setEmpresa(usuario.getEmpresa());
                // det.setNroFormulario(getSelected().getNroFormulario());
                det.setPersona(getSelected().getPersona());
                det.setDepartamento(getSelected().getDepartamento());
                //det.setSede(sede);
                det.setId(null);

                detallesPedidoArticuloController.setSelected(det);

                detallesPedidoArticuloController.saveNew(event);
            }
            /*
            setItems(ejbFacade.getEntityManager().createNamedQuery("PedidosArticulo.findAll", PedidosArticulo.class).getResultList());

            if (getItems2().size() > 0) {
                PedidosArticulo art = getItems2().iterator().next();
                setSelected(art);
                detalles = ejbFacade.getEntityManager().createNamedQuery("DetallesPedidoArticulo.findByPedidoArticulo", DetallesPedidoArticulo.class).setParameter("pedidoArticulo", art).getResultList();

                detalleSelected = null;
            } else {
                setSelected(null);
            }
             */

            if (fechaDesde == null) {
                fechaDesde = ejbFacade.getSystemDateOnly(-30);
            }
            if (fechaHasta == null) {
                fechaHasta = ejbFacade.getSystemDateOnly();
            }

            buscarPorFechaAlta();

            // articuloStockCriticoController.verifArticulosStockCritico();
        }

    }

    public boolean mostrar() {
        return false;
    }

    public void pdf() {

        if (articuloFiltro != null) {

            HttpServletResponse httpServletResponse = null;
            try {
                JRBeanCollectionDataSource beanCollectionDataSource = null;

                Calendar cal = Calendar.getInstance();
                cal.setTime(fechaHasta);
                cal.add(Calendar.DATE, 1);
                Date nuevaFechaHasta = cal.getTime();

                ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
                beanCollectionDataSource = new JRBeanCollectionDataSource(ejbFacade.getEntityManager().createNamedQuery("DetallesPedidoArticulo.findByArticuloFechaAlta", DetallesPedidoArticulo.class).setParameter("fechaAltaDesde", fechaDesde).setParameter("fechaAltaHasta", nuevaFechaHasta).setParameter("articulo", articuloFiltro).getResultList());

                HashMap map = new HashMap();
                // SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");

                // Date fecha = ejbFacade.getSystemDate();
                map.put("fechaDesde", format2.format(fechaDesde));
                map.put("fechaHasta", format2.format(fechaHasta));
                map.put("descArticulo", articuloFiltro.getDescripcion());

                String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reportePedidoArticulo.jasper");
                JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                httpServletResponse.addHeader("Content-disposition", "filename=reporte.pdf");

                ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

                FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
                JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
                FacesContext.getCurrentInstance().responseComplete();

            } catch (Exception e) {
                FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
                e.printStackTrace();

                if (httpServletResponse != null) {
                    if (httpServletResponse.getHeader("Content-disposition") == null) {
                        httpServletResponse.addHeader("Content-disposition", "inline");
                    } else {
                        httpServletResponse.setHeader("Content-disposition", "inline");
                    }

                }
                JsfUtil.addErrorMessage("No se pudo generar el reporte.");

            }
        } else {
            JsfUtil.addErrorMessage("Debe escojer un articulo.");
        }

        ///     JasperExportManager.exportReportToPdfFile(jasperPrint, "reporte.pdf");
    }

    public void pdf2() {

        if (departamentoFiltro != null) {

            HttpServletResponse httpServletResponse = null;
            try {
                JRBeanCollectionDataSource beanCollectionDataSource = null;

                Calendar cal = Calendar.getInstance();
                cal.setTime(fechaHasta);
                cal.add(Calendar.DATE, 1);
                Date nuevaFechaHasta = cal.getTime();

                ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

                beanCollectionDataSource = new JRBeanCollectionDataSource(ejbFacade.getEntityManager().createNamedQuery("DetallesPedidoArticulo.findByDptoFechaAlta", DetallesPedidoArticulo.class).setParameter("fechaAltaDesde", fechaDesde).setParameter("fechaAltaHasta", nuevaFechaHasta).setParameter("departamento", departamentoFiltro).getResultList());

                HashMap map = new HashMap();
                // SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");

                // Date fecha = ejbFacade.getSystemDate();
                map.put("fechaDesde", format2.format(fechaDesde));
                map.put("fechaHasta", format2.format(fechaHasta));
                map.put("descDpto", departamentoFiltro.getNombre());

                String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reportePedidoArticuloDpto.jasper");
                JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                httpServletResponse.addHeader("Content-disposition", "filename=reporte.pdf");

                ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

                FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
                JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
                FacesContext.getCurrentInstance().responseComplete();

            } catch (Exception e) {
                FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
                e.printStackTrace();

                if (httpServletResponse != null) {
                    if (httpServletResponse.getHeader("Content-disposition") == null) {
                        httpServletResponse.addHeader("Content-disposition", "inline");
                    } else {
                        httpServletResponse.setHeader("Content-disposition", "inline");
                    }

                }
                JsfUtil.addErrorMessage("No se pudo generar el reporte.");

            }
        } else {
            JsfUtil.addErrorMessage("Debe escojer un departamento.");
        }

        ///     JasperExportManager.exportReportToPdfFile(jasperPrint, "reporte.pdf");
    }

    public void view() {
        Articulos art = null;

        try {
            //Articulos art = ejbFacade.find(selected.getArticulo().getCodigo());
            art = ejbFacade.getEntityManager().createNamedQuery("Articulos.findByCodigo", Articulos.class
            ).setParameter("codigo", articulo.getCodigo()).getSingleResult();
            byte[] arreglo = art.getImagen();
            if (art != null) {
                HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
                response.getOutputStream().write(arreglo);
                response.getOutputStream().close();
                FacesContext.getCurrentInstance().responseComplete();
            } else {
                //aca tiene que mostrar un mensaje que hubo un error
            }
        } catch (Exception e) {
            FacesMessage message = new FacesMessage("Error");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }
    //}
}
