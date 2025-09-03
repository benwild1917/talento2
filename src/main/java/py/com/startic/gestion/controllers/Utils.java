/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.controllers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author eduardo
 */
public class Utils {
    public static final String politicasContrasena = "La contraseña debe tener un mínimo de 7 caracteres, contener al menos una letra, al menos un número o caracteres especial y no debe tener más de 2 caracteres iguales simultáneos. Además, se recomienda no utilizar palabras de uso común o nombre propios. ";
    public static String anotarCambio(Object ori, Object actual, String nombre){
        String cambio = "";
        Object obj = new Object();
        if(ori == null){
            ori = obj;
        }

        if(actual == null){
            actual = obj;
        }

        if(!ori.equals(actual)){
            cambio += nombre + " Antes: " + ori + "<br />";
            cambio += nombre + " Despues: " + actual + "<br/><br/>";
        }
        
        return cambio;
    }
    
    public static String anotarCambio(Date fechaOri, Date fechaActual, String nombre){
        
        String cambio = "";
        Date fecha = new Date();
        if(fechaOri == null){
            fechaOri = fecha;
        }

        if(fechaActual == null){
            fechaActual = fecha;
        }

        if(!fechaOri.equals(fechaActual)){
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            cambio += nombre + " Antes: " + format.format(fechaOri) + "<br/>";
            cambio += nombre + " Despues: " + format.format(fechaActual) + "<br/><br/>";
        }
        
        return cambio;
    }

    public static String anotarCambio(String ori, String actual, String nombre){
        String cambio = "";
        if(ori == null){
            ori = "";
        }

        if(actual == null){
            actual = "";
        }

        if(!ori.equals(actual)){
            cambio += nombre + " Antes: " + ori + "<br />";
            cambio += nombre + " Despues: " + actual + "<br/><br/>";
        }
        
        return cambio;
    }
    
    public static String padRight(String s, String p, int n) {
        return String.format("%1$-" + n + "s", s).replace(" ", p);
    }

    public static String padLeft(String s, String p, int n) {
        return String.format("%1$" + n + "s", s).replace(" ", p);
    }
    public static String passwordToHash(String password) {

        String myHash = "";
        if (true) {
            MessageDigest md;
            try {
                md = MessageDigest.getInstance("MD5");
                md.update(password.getBytes());
                byte[] digest = md.digest();
                myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            }
        } else {
            myHash = password;
        }

        return myHash;
    }
    
    public static String politicasContrasena(String contrasena) {

        if (contrasena == null) {
            return "La contraseña debe tener un mínimo de 7 caracteres";
        }

        if ("".equals(contrasena)) {
            return "La contraseña debe tener un mínimo de 7 caracteres";
        }

        if (contrasena.length() < 7) {
            return "La contraseña debe tener un mínimo de 7 caracteres";
        }
        
        Pattern letter = Pattern.compile("[a-zA-z]");
        Pattern digit = Pattern.compile("[0-9]");
        Pattern special = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
        //Pattern eight = Pattern.compile (".{8}");

        Matcher hasLetter = letter.matcher(contrasena);
        Matcher hasDigit = digit.matcher(contrasena);
        Matcher hasSpecial = special.matcher(contrasena);

        if(!hasLetter.find()){
            return "La contraseña debe contener al menos una letra";
        }
        
        if(!(hasDigit.find() || hasSpecial.find())){
            return "Contener al menos un número o un caracter especial";
        }
        
        int cantRep = 0;
        char actual;
        char anterior = 0;
        for (int i = 0; i < contrasena.length(); i++) {
            actual = contrasena.charAt(i);
            if(anterior == actual){
                cantRep++;
            }else{
                cantRep = 0;
            }
            
            if(cantRep > 2){
                break;
            }
            
            anterior = actual;
        }

        if (cantRep > 2) {
            return "No debe tener más de 2 caracteres iguales simultáneos";
        }
        
        return "";
    }
}
