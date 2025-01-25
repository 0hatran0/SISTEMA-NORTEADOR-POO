/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsc.fln.model.domain;

/**
 *
 * @author PC
 */
public class Modelo { 
    private int id;
    private String descricao;
    private Marca marca; 
    private Motor motor = new Motor(); 
    private ECategoria categoria = ECategoria.PADRAO; 

    public Modelo() { 
    }

    public Modelo(String descricao, Marca marca){
        this.descricao = descricao;
        this.marca = marca;    
    }

    public Modelo(int id, String descricao, Marca marca, ECategoria categoria) {
        this.id = id;
        this.descricao = descricao;
        this.marca = marca;
        this.categoria = categoria;
    }
    
    public int getId() {
        return id;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public Marca getMarca() {
        return marca;
    }

    public Motor getMotor(){
        return motor;
    }
    
    public ECategoria getECategoria(){
        return categoria;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }
    
    public void setECategoria(ECategoria categoria){
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return descricao;
    }
//    @Override
//    public String toString() {
//        return "Modelo{" + "id=" + id + ", descricao=" 
//                + descricao + ", marca=" + marca.getNome() 
//                + ", categoria=" + categoria 
//                + ", motor=" + motor.getPotencia() + "CV "
//                + ", TipoCombustivel=" + motor.getTipoCobustivel() +'}';
//    }
    
    
}
