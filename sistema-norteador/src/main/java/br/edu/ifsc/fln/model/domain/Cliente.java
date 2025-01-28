package br.edu.ifsc.fln.model.domain;

//import lombok.Data;

public abstract class Cliente {
    protected int id;
    protected String nome;
    protected String celular;
    protected String email;
    protected String dataCadastro;

//    protected static String dataCadastro;

    public Cliente() {
    }

    public Cliente(int id, String nome, String celular, String email, String dataCadastro) {
        this();
        this.id = id;
        this.nome = nome;
        this.celular = celular;
        this.email = email;
        this.dataCadastro = dataCadastro;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCelular() {
        return celular;
    }

    public String getEmail() {
        return email;
    }

    public String getDataCadastro() {
        return dataCadastro;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDataCadastro(String dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    @Override
    public String toString() {
        return nome;
    }

    public String getDados() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id...............: ").append(id).append("\n");
        sb.append("Nome.............: ").append(nome).append("\n");
        sb.append("Celular..........: ").append(celular).append("\n");
        sb.append("Email............: ").append(email).append("\n");
        sb.append("Data de Cadastro.: ").append(email).append("\n");
        return sb.toString();
    }

}
