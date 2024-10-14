package spring.curso.screenmatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.curso.screenmatch.model.Serie;

public interface SerieRepository extends JpaRepository<Serie, Long> { // Primeiro paramentro é a entidade e o segundo é o tipo do id
}
