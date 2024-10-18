package spring.curso.screenmatch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.curso.screenmatch.dto.SerieDTO;
import spring.curso.screenmatch.service.SerieService;

import java.util.List;

@RestController // Indica que a classe é um controller
public class SerieController {

    @Autowired
    private SerieService service;

    @GetMapping("/series") // Indica que o método responde a requisições do tipo GET na rota /series
    public List<SerieDTO> obterSeries(){
        return service.obterTodasAsSeries();
    }

}
