package spring.curso.screenmatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.curso.screenmatch.model.Serie;

import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> { // Primeiro paramentro é a entidade e o segundo é o tipo do id
    Optional<Serie> findByTituloContainingIgnoreCase(String titulo);
}
