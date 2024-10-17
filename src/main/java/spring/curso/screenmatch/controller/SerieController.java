package spring.curso.screenmatch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.curso.screenmatch.dto.SerieDTO;
import spring.curso.screenmatch.model.Serie;
import spring.curso.screenmatch.repository.SerieRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController // Indica que a classe é um controller
public class SerieController {

    @Autowired // Injeção de dependência
    private SerieRepository repositorio;

    @GetMapping("/series") // Indica que o método responde a requisições do tipo GET na rota /series
    public List<SerieDTO> obterSeries(){
        return repositorio.findAll()
                .stream()
                .map(s -> new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(), s.getGenero(), s.getSinopse(), s.getAtores(), s.getPoster()))
                .collect(Collectors.toList());
    }
}
