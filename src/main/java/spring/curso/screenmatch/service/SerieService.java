package spring.curso.screenmatch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.curso.screenmatch.dto.EpisodioDTO;
import spring.curso.screenmatch.dto.SerieDTO;
import spring.curso.screenmatch.model.Serie;
import spring.curso.screenmatch.repository.SerieRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {

    @Autowired // Injeção de dependência
    private SerieRepository repositorio;

    private List<SerieDTO> converteDados(List<Serie> series){
        return series.stream()
                .map(s -> new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(), s.getGenero(), s.getSinopse(), s.getAtores(), s.getPoster()))
                .collect(Collectors.toList());
    }

    public List<SerieDTO> obterTodasAsSeries(){
        return converteDados(repositorio.findAll());
    }

    public List<SerieDTO> obterTop5Series() {
       return converteDados(repositorio.findTop5ByOrderByAvaliacaoDesc());
    }

    public List<SerieDTO> obterLancamentos() {
        return converteDados(repositorio.encontrarEpisodiosMaisRecentes());
    }

    public SerieDTO obterSeriePorId(Long id) {
        Optional<Serie> serie = repositorio.findById(id);

        if(serie.isPresent()){
            Serie s = serie.get();
            return new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(), s.getGenero(), s.getSinopse(), s.getAtores(), s.getPoster());
        }
        return null;
    }

    public List<EpisodioDTO> obterSeriesPorTemporadas(Long id) {
        Optional<Serie> serie = repositorio.findById(id);

        if(serie.isPresent()){
            Serie s = serie.get();
            return s.getEpisodios().stream()
                    .map(e -> new EpisodioDTO(e.getNumeroEpisodio(), e.getTemporada(), e.getTitulo()))
                    .collect(Collectors.toList());
        }
        return null;
    }

    public List<EpisodioDTO> obterEpisodiosPorTemporada(Long id, Long numeroTemporada) {
     return repositorio.episodiosPorTemporada(id, numeroTemporada)
             .stream()
             .map(e -> new EpisodioDTO(e.getNumeroEpisodio(), e.getTemporada(), e.getTitulo()))
             .collect(Collectors.toList());
    }
}
