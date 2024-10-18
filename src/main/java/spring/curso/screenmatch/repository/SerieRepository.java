package spring.curso.screenmatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.curso.screenmatch.model.Categoria;
import spring.curso.screenmatch.model.Episodio;
import spring.curso.screenmatch.model.Serie;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> { // Primeiro paramentro é a entidade e o segundo é o tipo do id
    Optional<Serie> findByTituloContainingIgnoreCase(String titulo);
    List<Serie> findByAtoresContainingIgnoreCase(String ator);
    List<Serie> findTop5ByOrderByAvaliacaoDesc();
    List<Serie> findByGenero(Categoria categoria);
    List<Serie> findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(Integer temporadas, Double avaliacao); //Trocamos esse método por um método com JPQL
    @Query("SELECT s FROM Serie s " +
            "JOIN s.episodios e " +
            "GROUP BY s " +
            "ORDER BY MAX(e.dataLancamento) DESC LIMIT 5")
    List<Serie> encontrarEpisodiosMaisRecentes();

    //Na query JPQL, a grande vantagem é poder usar o nome da entidade é o nome da classe
    @Query("SELECT s FROM Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.avaliacao >= :avalicao") // JPQL, :nomeParametro
    List<Serie> seriePorTemporadaEAvalicao(int totalTemporadas, double avalicao);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:trecho%")
    List<Episodio> episodiosPorTrecho(String trecho);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episodio> top5EpisodiosPorSerie (Serie serie);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie AND YEAR(e.dataDeLancamento) >= :ano ")
    List<Episodio> episodiosPorAno(Serie serie, int ano);

}
