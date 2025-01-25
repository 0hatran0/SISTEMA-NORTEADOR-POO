/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsc.fln.model.domain;

/**
 *
 * @author PC
 */
public class Veiculo {
    private int id;
    private String placa;
    private String observacoes;
    private Modelo modelo;
    private Cor cor;
//    private Cliente cliente;
    
    public Veiculo(){
    }
    
    public Veiculo(String placa){
        this.placa = placa;
    }  
    
    public Veiculo(String placa, Modelo modelo){
        this.placa = placa;
        this.modelo = modelo;
    }

    public Veiculo(int id, String placa, String observacoes, Modelo modelo, Cor cor) {
        this.id = id;
        this.placa = placa;
        this.observacoes = observacoes;
        this.modelo = modelo;
        this.cor = cor;
    }
    
//    public Cliente getCliente() {
//        return cliente;
//    }
    
    public int getId() {
        return id;
    }
    
    public String getPlaca() {
        return placa;
    }
    
    public String getObservacoes() {
        return observacoes;
    }
    
    public Modelo getModelo() {
        return modelo;
    }
    
    public Cor getCor() {
        return cor;
    }
    
//    public void setCliente(Cliente cliente) {
//        this.cliente = cliente;
//    } 

    public void setId(int id) {
        this.id = id;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public void setCor(Cor cor) {
        this.cor = cor;
    }

//    @Override
//    public String toString() {
//        return "Veiculo{" + "id=" + id + ", placa=" 
//                + placa + ", observacoes=" + observacoes
//                + ", cliente=" + cliente.getNome()
//                + ", modelo=" + modelo.getDescricao() 
//                + ", marca=" + modelo.getMarca().getNome()
//                + ", marca=" + modelo.getECategoria()
//                + ", motor=" + modelo.getMotor().getPotencia() + "CV "
//                + ", TipoCombustivel=" + modelo.getMotor().getTipoCobustivel()
//                + ", cor=" + cor.getCor() + '}'+"\n\t";
//    }
    
    
}
