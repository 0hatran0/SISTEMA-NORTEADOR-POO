/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsc.fln.model.domain;

/**
 *
 * @author PC
 */

public class Motor { 
    private int potencia;
    
    private ETipoCombustivel tipoCobustivel = ETipoCombustivel.GASOLINA; 
    
    private Modelo modelo;
    
    public int getPotencia() {
        return potencia;
    }

    public ETipoCombustivel getTipoCombustivel() {
        return tipoCobustivel;
    }
    
    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }
    
    public void setPotencia(int potencia) {
        this.potencia = potencia;
    }

    public void setTipoCombustivel(ETipoCombustivel tipoCobustivel) {
        this.tipoCobustivel = tipoCobustivel;
    }

//    @Override
//    public String toString() {
//        return "Motor{" + "potencia=" + potencia + "CV " + ", TipoCombustivel=" + tipoCobustivel + '}';
//    }
       
}
