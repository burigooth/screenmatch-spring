package spring.curso.screenmatch.principal;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import spring.curso.screenmatch.model.*;
import spring.curso.screenmatch.repository.SerieRepository;
import spring.curso.screenmatch.service.ConsumoApi;
import spring.curso.screenmatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner leitor = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    Dotenv dotenv = Dotenv.load();
    private final String URL = "https://www.omdbapi.com/?t=";
    String apiKey = dotenv.get("API_KEY");
    private final String API_KEY_URL = "&apikey="+apiKey;
    private List<DadosSerie> dadosSeries = new ArrayList<>();
    private SerieRepository repositorio;
    private List<Serie> series = new ArrayList<>();

    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                    4 - Buscar série por nome
                    5 - Buscar série por ator
                    6 - Top 5 séries
                    7 - Buscar séries por categoria
            
                    0 - Sair
                    """;
            System.out.println(menu);
            opcao = leitor.nextInt();
            leitor.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriePorAtor();
                    break;
                case 6:
                    buscarTop5Series();
                    break;
                case 7:
                    buscarSeriesPorCategoria();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        //dadosSeries.add(dados);
        repositorio.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie(){
        System.out.println("Digite o nome da série a ser buscada: ");
        var nomeSerie = leitor.nextLine();
        var json = consumoApi.obterDados(URL + nomeSerie.replace(" ", "+") + API_KEY_URL);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie(){
        listarSeriesBuscadas();
        System.out.println("Escolha uma série pelo nome: ");
        var nomeSerie = leitor.nextLine();

        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if(serie.isPresent()){
            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();
            for (int i = 1; i < serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumoApi.obterDados(URL + serieEncontrada.getTitulo().replace(" ", "+") + "&Season="+ i + API_KEY_URL);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);

                List<Episodio> episodios = temporadas.stream()
                        .flatMap(d -> d.episodios().stream()
                                .map(e -> new Episodio(d.numeroTemporada(), e)))
                                .collect(Collectors.toList());
                serieEncontrada.setEpisodios(episodios);
                repositorio.save(serieEncontrada);
            }
            temporadas.forEach(System.out::println);
        }else{
            System.out.println("Série não encontrada");
        }
    }

    private void listarSeriesBuscadas() {
        series = repositorio.findAll();

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }


    private void buscarSeriePorTitulo(){
        System.out.println("Escolha uma série pelo nome: ");
        var nomeSerie = leitor.nextLine();
        Optional<Serie> serieBuscada = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if(serieBuscada.isPresent()){
            System.out.println(serieBuscada.get());
        }else{
            System.out.println("Série não encontrada");
        }
    }

    private void  buscarSeriePorAtor(){
        System.out.println("Digite o nome de um ator para buscar a série: ");
        var ator = leitor.nextLine();
        List <Serie> serieBuscada = repositorio.findByAtoresContainingIgnoreCase(ator);
        System.out.println("Séries em que o ator " + ator + " participou: ");
        serieBuscada.forEach(s -> System.out.println(s.getTitulo() + " - " + s.getAtores()));
    }

    private void buscarTop5Series(){
        List <Serie> top5 = repositorio.findTop5ByOrderByAvaliacaoDesc();
        top5.forEach(s -> System.out.println(s.getTitulo() + " - " + s.getAvaliacao()));
    }

    private void buscarSeriesPorCategoria(){
        System.out.println("Digite o nome de uma categoria para buscar a série: ");
        var nomeGenero = leitor.nextLine();
        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        List <Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
        System.out.println("Séries da categoria " + nomeGenero + ": ");
        seriesPorCategoria.forEach(s -> System.out.println(s.getTitulo() + " - " + s.getGenero()));
    }
}
