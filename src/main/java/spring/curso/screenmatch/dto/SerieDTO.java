package spring.curso.screenmatch.dto;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import spring.curso.screenmatch.model.Categoria;

public record SerieDTO(
         Long id,
         String titulo,
         Integer totalTemporadas,
         Double avaliacao,
         Categoria genero,
         String sinopse,
         String atores,
         String poster
                        ){
}
