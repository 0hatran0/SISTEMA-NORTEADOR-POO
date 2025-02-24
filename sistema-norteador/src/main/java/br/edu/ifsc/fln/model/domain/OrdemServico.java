package br.edu.ifsc.fln.model.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrdemServico {
    // Atributos - variaveis de instacia
    private long numero;
    private double total;
    private LocalDate agenda;
    private double desconto;

    private EStatus status = EStatus.ABERTA;
    private Veiculo veiculo;
    private List<ItemOS> itemOS = new ArrayList<>();

    // Construtores
    public OrdemServico() {
    }

    public OrdemServico(long numero, LocalDate agenda, double desconto) {
        this.numero = numero;
        this.agenda = agenda;
        this.desconto = desconto;
    }

    public long getNumero() {
        return numero;
    }

    public void setNumero(long numero) {
        this.numero = numero;
    }

    public double getTotal() {
        calcularServico();
        return total; }

    public LocalDate getAgenda() {
        return agenda;
    }

    public void setAgenda(LocalDate agenda) {
        this.agenda = agenda;
    }

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }

    public EStatus getStatus() {
        return status;
    }

    public void setStatus(EStatus status) {
        this.status = status;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public List<ItemOS> getItemOS() {
        return itemOS;
    }

    public void setItemOS(List<ItemOS> itemOS) {
        this.itemOS = itemOS;
    }

    // Metodos
    public double calcularServico(){
        for(int i=0;i<(this.itemOS.size());i++ ){
            this.total+= this.itemOS.get(i).getValorServico();
        }
        this.total-= (this.total*(this.desconto/100));
        return total;

    }

    public void add(ItemOS itemOS){
        this.itemOS.add(itemOS);
        itemOS.setOrdemServico(this);
    }

    public void remove(ItemOS itemOS){
        this.itemOS.remove(itemOS);
        itemOS.setOrdemServico(null);
    }

}
