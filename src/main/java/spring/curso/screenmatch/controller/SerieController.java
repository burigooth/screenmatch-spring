package spring.curso.screenmatch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.curso.screenmatch.dto.EpisodioDTO;
import spring.curso.screenmatch.dto.SerieDTO;
import spring.curso.screenmatch.model.Episodio;
import spring.curso.screenmatch.service.SerieService;

import java.util.List;

@RestController // Indica que a classe é um controller
@RequestMapping("/series") // Indica que a rota base para os métodos desse controller é /series
public class SerieController {

    @Autowired
    private SerieService service;

    @GetMapping // Indica que o método responde a requisições do tipo GET na rota /series
    public List<SerieDTO> obterSeries(){
        return service.obterTodasAsSeries();
    }

    @GetMapping("/top5")
    public List<SerieDTO> obterTop5(){
        return service.obterTop5Series();
    }

    @GetMapping("/lancamentos")
    public List<SerieDTO> obterLancamentos(){
        return service.obterLancamentos();
    }

    @GetMapping("/{id}")
    public SerieDTO obterSeriePorId(@PathVariable Long id){ // @PathVariable indica que o valor do id vem da URL
        return service.obterSeriePorId(id);
    }

    @GetMapping("{id}/temporadas/todas")
    public List<EpisodioDTO> obterTodasAsTemporadas(@PathVariable Long id){
        return service.obterSeriesPorTemporadas(id);
    }

    @GetMapping("{id}/temporadas/{numeroTemporada}")
    public List<EpisodioDTO> obterEpisodiosPorTemporada(@PathVariable Long id, @PathVariable Long numeroTemporada){
        return service.obterEpisodiosPorTemporada(id, numeroTemporada);
    }
}
