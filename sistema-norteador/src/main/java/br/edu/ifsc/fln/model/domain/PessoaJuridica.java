package br.edu.ifsc.fln.model.domain;

public class PessoaJuridica extends Cliente {
    private String cnpj;
    private String inscricaoEstadual;

    public PessoaJuridica() {}

    public PessoaJuridica(int id, String nome, String celular, String emial, String dataCadastro) {
        super(id, nome, celular, emial, dataCadastro);
    }

    public PessoaJuridica(String cnpj, String inscricaoEstadual, int id, String nome, String celular, String emial, String dataCadastro) {
        super(id, nome, celular, emial, dataCadastro);
        this.cnpj = cnpj;
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    @Override
    public String getDados() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getDados()).append("\n");
        sb.append("CNPJ................: ").append(cnpj).append("\n");
        sb.append("Inscrição Estadual..: ").append(cnpj).append("\n");
        return sb.toString();
    }
}
