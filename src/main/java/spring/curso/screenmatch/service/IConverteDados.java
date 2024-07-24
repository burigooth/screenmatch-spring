package spring.curso.screenmatch.service;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe); // T = Generics, como não sabemos ainda o que ira retornar, podemos declarar como generico.
}
