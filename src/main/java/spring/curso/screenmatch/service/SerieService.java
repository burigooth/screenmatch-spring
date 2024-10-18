package spring.curso.screenmatch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.curso.screenmatch.dto.SerieDTO;
import spring.curso.screenmatch.repository.SerieRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SerieService {

    @Autowired // Injeção de dependência
    private SerieRepository repositorio;

    public List<SerieDTO> obterTodasAsSeries(){
        return repositorio.findAll()
                .stream()
                .map(s -> new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(), s.getGenero(), s.getSinopse(), s.getAtores(), s.getPoster()))
                .collect(Collectors.toList());
    }

}
