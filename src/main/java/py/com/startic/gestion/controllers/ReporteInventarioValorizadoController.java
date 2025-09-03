package py.com.startic.gestion.controllers;



import java.util.Collection;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

import py.com.startic.gestion.models.ReporteInventarioValorizado;

@Named(value = "reporteInventarioValorizadoController")
@ViewScoped
public class ReporteInventarioValorizadoController extends AbstractController<ReporteInventarioValorizado> {
 private boolean hayArticulosPorVencer = false;

    public boolean isHayArticulosPorVencer() {
        return hayArticulosPorVencer;
    }

    public void setHayArticulosPorVencer(boolean hayArticulosPorVencer) {
        this.hayArticulosPorVencer = hayArticulosPorVencer;
    }


    public ReporteInventarioValorizadoController() {
        // Inform the Abstract parent controller of the concrete ReportesInventario Entity
        super(ReporteInventarioValorizado.class);
    }
     public boolean verifArticulosPorVencer(){
        hayArticulosPorVencer = false;
        Collection<ReporteInventarioValorizado> art = this.getItems();
        
        if( art != null){
            if( art.size() >  0 ){
                hayArticulosPorVencer = true;
            }
        }
        return hayArticulosPorVencer;
    }
    
    @Override
    public Collection<ReporteInventarioValorizado> getItems() {
        Collection<ReporteInventarioValorizado> art = this.ejbFacade.getEntityManager().createNamedQuery("ReporteInventarioValorizado.findByArticulosPorVencer", ReporteInventarioValorizado.class).getResultList();
        
        hayArticulosPorVencer = false;
        
        if( art != null){
            if( art.size() >  0 ){
                hayArticulosPorVencer = true;
            }
        }
        
        return art;
    }

}
