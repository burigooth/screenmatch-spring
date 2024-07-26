package spring.curso.screenmatch.principal;

import spring.curso.screenmatch.model.DadosEpisodios;
import spring.curso.screenmatch.model.DadosSerie;
import spring.curso.screenmatch.model.DadosTemporada;
import spring.curso.screenmatch.model.Episodio;
import spring.curso.screenmatch.service.ConsumoApi;
import spring.curso.screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private Scanner leitor = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String URL = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=f23c5cfe&";

    public void exibeMenu(){
        System.out.println("Digite o nome da série para busca: ");
        var nomeSerie = leitor.nextLine();
        var json = consumoApi.obterDados(URL + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();
		for (int i = 1; i < dados.totalTemporadas(); i++) {
			json = consumoApi.obterDados(URL + nomeSerie.replace(" ", "+") + "&season="+ i + API_KEY);
			DadosTemporada dadosTemporadas = conversor.obterDados(json, DadosTemporada.class);
			temporadas.add(dadosTemporadas);
		}
		temporadas.forEach(System.out::println);

        temporadas.forEach(t ->  t.episodios().forEach(e -> System.out.println(e.titulo())));

        List<DadosEpisodios> todosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        System.out.println("\nTop 5 episódios mais avaliados");
        todosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodios::avaliacao).reversed()) //Colocando os eps por ordem decrescente pela avaliação
                .limit(5)
                .forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numeroTemporada(), d))
                ).collect(Collectors.toList());
        episodios.forEach(System.out::println);

        System.out.println("A partir de que ano você deseja ver os episódios? ");
        var ano = leitor.nextInt();
        leitor.nextLine();

        LocalDate dataBusca = LocalDate.of(ano, 1, 1);

        DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        episodios.stream()
                .filter(e -> e .getDataDeLancamento() != null && e.getDataDeLancamento().isAfter(dataBusca))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporada() +
                                " Episódio: " + e.getTitulo() +
                                " Data de lançamento: " + e.getDataDeLancamento().format(formatadorData)
                ));
    }
}
