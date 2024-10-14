package spring.curso.screenmatch.model;

import java.util.OptionalDouble;

public class Serie {
    private String titulo;
    private Integer totalTemporadas;
    private Double avaliacao;
    private Categoria genero;
    private String sinopse;
    private String atores;
    private String poster;

    public Serie(DadosSerie dadosSerie){
        this.titulo = dadosSerie.titulo();
        this.totalTemporadas = dadosSerie.totalTemporadas();
        this.avaliacao = OptionalDouble.of(Double.valueOf(dadosSerie.avaliacao())).orElse(0.0);
        this.genero = Categoria.fromString(dadosSerie.genero().split(",")[0].trim()); // Pega sempre o primeiro gênero
        this.sinopse = dadosSerie.sinopse();
        this.atores = dadosSerie.atores();
        this.poster = dadosSerie.poster();

    }

}
