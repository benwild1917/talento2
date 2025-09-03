/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package py.com.startic.gestion.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import py.com.startic.gestion.models.Dependencia;
import py.com.startic.gestion.models.Registro;

/**
 *
 * @author DELL
 */
@Named
@ViewScoped
public class RegistroBean implements Serializable {

    private Registro registro = new Registro();
    private Dependencia dependenciaSeleccionada;
    private List<Dependencia> dependenciasDisponibles;
    
    
     public RegistroBean() {
        dependenciasDisponibles = new ArrayList<>();
        dependenciasDisponibles.add(new Dependencia(1, "Dirección de Finanzas"));
        dependenciasDisponibles.add(new Dependencia(2, "Recursos Humanos"));
        dependenciasDisponibles.add(new Dependencia(3, "Tecnología de la Información"));
    }
     
      public void agregarDependencia() {
        if (dependenciaSeleccionada != null && !registro.getDependencias().contains(dependenciaSeleccionada)) {
            registro.getDependencias().add(dependenciaSeleccionada);
        }
    }

    public void guardarRegistro() {
        // Aquí deberías persistir el registro y sus dependencias
        System.out.println("Registro guardado con dependencias: " + registro.getDependencias().size());
    }

    public Registro getRegistro() {
        return registro;
    }

    public void setRegistro(Registro registro) {
        this.registro = registro;
    }

    public Dependencia getDependenciaSeleccionada() {
        return dependenciaSeleccionada;
    }

    public void setDependenciaSeleccionada(Dependencia dependenciaSeleccionada) {
        this.dependenciaSeleccionada = dependenciaSeleccionada;
    }

    public List<Dependencia> getDependenciasDisponibles() {
        return dependenciasDisponibles;
    }

    public void setDependenciasDisponibles(List<Dependencia> dependenciasDisponibles) {
        this.dependenciasDisponibles = dependenciasDisponibles;
    }

}
