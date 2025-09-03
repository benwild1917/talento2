package py.com.startic.gestion.controllers;

import java.util.Collection;
import java.util.Date;

import py.com.startic.gestion.models.Proveedores;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import org.primefaces.PrimeFaces;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.datasource.CantidadItem;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "proveedoresController")
@ViewScoped
public class ProveedoresController extends AbstractController<Proveedores> {

    @Inject
    private EmpresasController empresaController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    private Object proveedores;
    private String nrucEdit;

    public String getNrucEdit() {
        return nrucEdit;
    }

    public void setNrucEdit(String nrucEdit) {
        this.nrucEdit = nrucEdit;
    }

    
    public ProveedoresController() {
        // Inform the Abstract parent controller of the concrete Proveedores Entity
        super(Proveedores.class);
    }

    @Override
    public Collection<Proveedores> getItems() {
        return this.ejbFacade.getEntityManager().createNamedQuery("Proveedores.findOrdered", Proveedores.class).getResultList();
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
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

    
    public void prepareEdit() {
        nrucEdit = getSelected().getRuc();
        
        
        
    }

     
    public boolean verificarRucCreate(){
        return verificarRuc(getSelected().getRuc());
    }

    public boolean verificarRucEdit(){
        
         if(!nrucEdit.equals(getSelected().getRuc())){
            return verificarRuc(getSelected().getRuc());
        }
        return true;

    }
    
    public boolean verificarRuc(String nro){
        CantidadItem cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from proveedores where ruc = ?1", CantidadItem.class).setParameter(1, nro).getSingleResult();
    
        if(cant.getCantidad() > 0){
            JsfUtil.addErrorMessage("Ruc ya existe");
            return false;
        }
    
        return true;
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

    @Override
    public void save(ActionEvent event) {
    if (getSelected() != null) {
             if(!verificarRucEdit()){
                return;
            }
        if (getSelected() != null) {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usu);
            getSelected().setRuc(nrucEdit);
        }

        super.save(event);
    }

    /**
     * Store a new item in the data layer.
     *
     * @param event an event from the widget that wants to save a new Entity to
     * the data layer
     */
    }
    @Override
    public void saveNew(ActionEvent event) {
        
        if (getSelected() != null) {
            
            if(!verificarRucCreate()){
                return;
            }

    }
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

            setSelected(ejbFacade.getEntityManager().createNamedQuery("Proveedores.findById", Proveedores.class).setParameter("id", getSelected().getId()).getSingleResult());

        }

    }
    
    
      
    
    /*public String abrirCreate(String nombre, String updateCerrar, String nombreVariable) {
      pantalla = Constantes.NO;
       this.tipoMovimiento = tipoMovimiento;
       Proveedores proveedor = super.prepareCreate(null);
        
       if(Constantes.ABIERTO_DESDE_PANTALLA.equals(desde)){
            pantalla = Constantes.SI;
           this.updateCerrar = updateCerrar;
            this.nombreVariable = nombreVariable;
            
            TiposMovimiento mov = ejbFacade.getEntityManager().createNamedQuery("TiposMovimiento.findById", TiposMovimiento.class).setParameter("id", tipoMovimiento).getSingleResult();
            tipoGasto.setTipoMovimiento(mov);
            
            if(Constantes.TIPO_MOVIMIENTO_EGRESO.equals(tipoMovimiento)){
                tipoGasto.setAsociarMovil(Constantes.SI);
            }
        }
        PrimeFaces current = PrimeFaces.current();
        current.executeScript("PF('TiposGastoCreateDialog').show();");
        return "";
    }
    public TiposGasto getTipoGasto() {
        if (session.getAttribute("tipoGastoSelected") != null) {
            tipogasto = (TiposGasto) session.getAttribute("tipoGastoSelected");
            session.removeAttribute("tipoGastoSelected");
        }
        return tipogasto;
    }
    public String updateCreate() {
        if (Constantes.SI.equals(pantalla)) {
            return updateCerrar;
        } else {
            return "display,:TiposGastoListForm:datalist,:growl";
        }
    }
    
    public boolean deshabilitarTipoMovimiento(){
        return Constantes.SI.equals(pantalla);
    }
    
    public boolean deshabilitarAsociarMovil(){
        if(tipoMovimiento != null){
            return Constantes.TIPO_MOVIMIENTO_EGRESO.equals(tipoMovimiento);
        }
        
        return false;
    } 
   
  */
}
