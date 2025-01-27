package br.edu.ifsc.fln.model.domain;

public class PessoaFisica extends Cliente{
    private String cpf;
    private String dataNascimento;

    public PessoaFisica() {
    }

    public PessoaFisica(String cpf, String dataNascimento) {
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
    }

    public PessoaFisica(String cpf, String dataNascimento, int id, String nome, String celular, String emial, String dataCadastro) {
        super(id, nome, celular, emial, dataCadastro);
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    @Override
    public String getDados() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getDados()).append("\n");
        sb.append("CPF.................: ").append(cpf).append("\n");
        sb.append("Data de Nascimento..: ").append(dataNascimento).append("\n");
        return sb.toString();
    }

}
