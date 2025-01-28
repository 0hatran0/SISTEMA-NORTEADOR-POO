package br.edu.ifsc.fln.model.domain;

public class ItemOS {
    private double valorServico;
    private String observacoes;

    private Servico servico;
    private OrdemServico ordemServico;

    public ItemOS(String observacoes, Servico servico) {
        this.observacoes = observacoes;
        this.servico = servico;
    }

    public double getValorServico() {
        return valorServico;
    }

    public void setValorServico(double valorServico) {
        this.valorServico = servico.getValor();
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public OrdemServico getOrdemServico() {
        return ordemServico;
    }

    public void setOrdemServico(OrdemServico ordemServico) {
        this.ordemServico = ordemServico;
    }

}
