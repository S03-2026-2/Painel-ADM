package br.inatel.painel.entidades;

public class Proposta {

    private final String id;
    private final String trocaId;
    private final String proponenteId;
    private final Carta cartaProposta;
    private final StatusProposta status;
    private final String dataProposta;

    public Proposta(String id, String trocaId, String proponenteId, Carta cartaProposta,
                    StatusProposta status, String dataProposta) {
        this.id = id;
        this.trocaId = trocaId;
        this.proponenteId = proponenteId;
        this.cartaProposta = cartaProposta;
        this.status = status;
        this.dataProposta = dataProposta;
    }

    public String getId() {
        return id;
    }

    public String getTrocaId() {
        return trocaId;
    }

    public String getProponenteId() {
        return proponenteId;
    }

    public Carta getCartaProposta() {
        return cartaProposta;
    }

    public StatusProposta getStatus() {
        return status;
    }

    public String getDataProposta() {
        return dataProposta;
    }
}
