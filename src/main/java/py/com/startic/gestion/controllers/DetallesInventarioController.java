package py.com.startic.gestion.controllers;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import py.com.startic.gestion.models.DetallesInventario;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.models.Articulos;
import py.com.startic.gestion.models.DetallesEntradaArticulo;
import py.com.startic.gestion.models.EstadosInventario;
import py.com.startic.gestion.models.Inventarios;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "detallesInventarioController")
@ViewScoped
public class DetallesInventarioController extends AbstractController<DetallesInventario> {

    @Inject
    private InventariosController inventarioController;
    @Inject
    private ArticulosController articuloController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private EmpresasController empresaController;
    @Inject
    private SedeController sedeController;
    @Inject
    private DetallesInventarioController detallesInventarioController;
    private Inventarios inventario;
    private BigDecimal costoUnitario;
    private BigDecimal costoTotal;
    private Object articulo;
    private boolean hayArticulosPorVencer = false;

    public BigDecimal getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(BigDecimal costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    public BigDecimal getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(BigDecimal costoTotal) {
        this.costoTotal = costoTotal;
    }

    public Inventarios getInventario() {
        return inventario;
    }

    public void setInventario(Inventarios inventario) {
        this.inventario = inventario;
    }

    public boolean isHayArticulosPorVencer() {
        return hayArticulosPorVencer;
    }

    public void setHayArticulosPorVencer(boolean hayArticulosPorVencer) {
        this.hayArticulosPorVencer = hayArticulosPorVencer;
    }
    
    

    public DetallesInventarioController() {
        // Inform the Abstract parent controller of the concrete DetallesInventario Entity
        super(DetallesInventario.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        inventarioController.setSelected(null);
        articuloController.setSelected(null);
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
        empresaController.setSelected(null);
    }

    @PostConstruct
    @Override
    public void initParams() {
        Collection<Inventarios> col = this.ejbFacade.getEntityManager().createNamedQuery("Inventarios.findEnProceso", Inventarios.class).getResultList();
        if (col != null) {
            if (col.size() > 0) {
                inventario = col.iterator().next();
                setItems(this.ejbFacade.getEntityManager().createNamedQuery("DetallesInventario.findByInventario", DetallesInventario.class).setParameter("inventario", inventario).getResultList());
            }
        }
    }

    public void nuevoInventario() {

        inventario = inventarioController.prepareCreate(null);

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

        Date fecha = ejbFacade.getSystemDate();

        inventario.setFechaHoraAlta(fecha);
        inventario.setFechaHoraUltimoEstado(fecha);
        inventario.setUsuarioAlta(usu);
        inventario.setUsuarioUltimoEstado(usu);
        inventario.setEmpresa(usu.getEmpresa());

        inventario.setEstado(this.ejbFacade.getEntityManager().createNamedQuery("EstadosInventario.findByCodigo", EstadosInventario.class).setParameter("codigo", "PR").getSingleResult());

        Collection<Articulos> col = this.ejbFacade.getEntityManager().createNamedQuery("Articulos.findAll", Articulos.class).getResultList();
        for (Articulos art : col) {
            DetallesInventario det = detallesInventarioController.prepareCreate(null);

            det.setArticulo(art);
            det.setSede(art.getSede());
            det.setCantidad(0);
            det.setEmpresa(usu.getEmpresa());
            det.setFechaHoraAlta(fecha);
            det.setFechaHoraUltimoEstado(fecha);
            det.setUsuarioAlta(usu);
            det.setUsuarioUltimoEstado(usu);
            det.setInventario(inventario);

            detallesInventarioController.setSelected(det);

            detallesInventarioController.saveNew(null);
        }

    }
    public boolean verifArticulosPorVencer(){
        hayArticulosPorVencer = false;
        Collection<DetallesInventario> art = this.getItems();
        
        if( art != null){
            if( art.size() >  0 ){
                hayArticulosPorVencer = true;
            }
        }
        return hayArticulosPorVencer;
    }
       @Override
    public Collection<DetallesInventario> getItems() {
        Collection<DetallesInventario> art = this.ejbFacade.getEntityManager().createNamedQuery("DetallesInventario.findByArticulosPorVencer", DetallesInventario.class).getResultList();
        
        hayArticulosPorVencer = false;
        
        if( art != null){
            if( art.size() >  0 ){
                hayArticulosPorVencer = true;
            }
        }
        
        return art;
    }

    /**
     * Sets the "selected" attribute of the Inventarios controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareInventario(ActionEvent event) {
        if (this.getSelected() != null && inventarioController.getSelected() == null) {
            inventarioController.setSelected(this.getSelected().getInventario());
        }
    }

    /**
     * Sets the "selected" attribute of the Articulos controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareArticulo(ActionEvent event) {
        if (this.getSelected() != null && articuloController.getSelected() == null) {
            articuloController.setSelected(this.getSelected().getArticulo());
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

}
