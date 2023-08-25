package com.carlosribeiro;


import com.carlosribeiro.exception.EntidadeNaoEncontradaException;
import com.carlosribeiro.exception.ErrorHandler;
import com.carlosribeiro.exception.ViolacaoDeConstraintException;
import com.carlosribeiro.modelo.Ficha;
import com.carlosribeiro.modelo.Invocador;
import corejava.Console;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SpringbootRestTemplateApplication {

    // Nesta aplicação cliente estamos utilizando o Log4j2 (VERSÃO 2) para evitar que sejam
    // exibidas informações de DEBUG na console emitidas pelos métodos de RestTemplate do
    // framework Spring. Daí ter sido definido, no arquivo log4j2.xml, um logger Root para
    // o nível info. Os víveis de log são: trace < debug < info < warn < error < fatal

    private static Logger logger = LoggerFactory.getLogger(SpringbootRestTemplateApplication.class);
    // private static Logger rootLogger =  LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

    // Criando o objeto RestTemplate
    private static RestTemplate restTemplate = new RestTemplate();
    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {

        // Designando um ErrorHandler ao objeto restTemplate
        restTemplate.setErrorHandler(new ErrorHandler());

        logger.info("Iniciando a execução da aplicação cliente.");
        // rootLogger.info("Iniciando a execução da aplicação cliente utilizando o root logger.");

        String nome;
        String nome_treino;
        String status;
        String Elo;
        LocalDate dia;
        Invocador invocador = null;
        Ficha ficha = null;

        boolean continua = true;
        while (continua) {
            System.out.println('\n' + "O que você deseja fazer?");
            System.out.println('\n' + "1. Cadastrar um invocador");
            System.out.println("2. Remover um invocador");
            System.out.println("3. Alterar um invocador");
            System.out.println("4. Recuperar um invocador");
            System.out.println("5. Listar todos os invocadores");
            System.out.println("6. Listar todos as fichas por invocador(usando request param)");
            System.out.println("7. Listar todos as fichas por invocador (usando path variable)");
            System.out.println("8. Cadastrar uma ficha");
            System.out.println("9. Alterar uma ficha");
            System.out.println("10. Remover uma ficha");
            System.out.println("11. Recuperar uma ficha");
            System.out.println("12. Sair");

            // Métodos utilizados:
            // exchange
            // getForObject
            // postForEntity
            // put
            // delete

            int opcao = Console.readInt('\n' + "Digite um número entre 1 e 12:");

            switch (opcao) {
                case 1 -> {
                    //Cadastro do invocador
                    nome = Console.readLine('\n' + "Informe o nome do invocador: ");
                    String elo = Console.readLine('\n' + "Informe o elo: ");

                    invocador = new Invocador(nome, elo);


                    // O método exchange abaixo envia uma requisição HTTP para o endpoint fornecido.
                    // No corpo da requisição (objeto requestEntity) é enviado um objeto do tipo produto.
                    // Caso seja necessário enviar algum header, deverá ser enviado como segundo argumento
                    // do construtor de HttpEntity. O método exchange retorna um objeto do tipo ResponseEntity
                    // que, por default, retorna o Status http (HttpStatus) 200 OK.

                    // Ao contrário do Postman, que exige um header do tipo Content-type valendo application/json
                    // para que seja enviado ao servidor conteúdo json incluído no corpo da requisição http,
                    // a API restTemplate já assume, por default, que será enviado json. Daí não ser necessário
                    // o envio de um header do tipo Content-type.

                    try {
                        // HttpHeaders httpHeaders = new HttpHeaders();
                        // httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                        // httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                        // HttpEntity<Produto> requestEntity = new HttpEntity<>(umProduto, httpHeaders);

                        // Enviando uma requisição do tipo POST ao servidor para cadastrar um produto
//                        HttpEntity<Produto> requestEntity = new HttpEntity<>(produto);
//                        ResponseEntity<Produto> res = restTemplate.exchange(
//                                "http://localhost:8080/produtos",
//                                HttpMethod.POST, requestEntity, Produto.class);
//                        produto = res.getBody();

                        ResponseEntity<Invocador> res = restTemplate.postForEntity(
                                "http://localhost:8080/invocadores", invocador, Invocador.class);
                        invocador = res.getBody();

                        System.out.println('\n' + "invocador número " + invocador.getId() + " cadastrado com sucesso!");

                        System.out.println('\n' +
                                "   Id = " + invocador.getId() +
                                "   Nome = " + invocador.getNome() +
                                "   Elo = " + invocador.getElo());


                    } catch (ViolacaoDeConstraintException e) {
                        System.out.println('\n' + e.getMessage());
                    }
                }

                case 2 -> {
                    //Remove um invocador
                    try {
                        // Recuperando o Invocador que se deseja remover
                        invocador = recuperarObjeto(
                                "Informe o número do invocador que você deseja remover: ",
                                "http://localhost:8080/invocadores/{id}", Invocador.class);
                    } catch (EntidadeNaoEncontradaException e) {
                        System.out.println('\n' + e.getMessage());
                        break;
                    }

                    System.out.println('\n' +
                            "   Id = " + invocador.getId() +
                            "   Nome = " + invocador.getNome() +
                            "   elo = " + invocador.getElo());


                    String resp = Console.readLine('\n' +
                            "Confirma a remoção do invocador? [s/n]");

                    if (resp.equals("s")) {
                        try {
                            // Enviando uma requisição do tipo DELETE para remover o invocador
//                            restTemplate.exchange(
//                                    "http://localhost:8080/invocadores/{id}",
//                                    HttpMethod.DELETE, null, Invocador.class, invocador.getId());
                            restTemplate.delete("http://localhost:8080/invocadores/{id}", invocador.getId());

                            System.out.println('\n' + "Invocador número " + invocador.getId() + " removido com sucesso!");
                        } catch (EntidadeNaoEncontradaException e) {
                            System.out.println('\n' + e.getMessage());
                            break;
                        }
                        // Onde aparece null, poderia ter sido  especificado um  objeto do tipo  HttpEntity.
                        // Como nesta requisição http não está sendo necessário enviar nada no corpo da
                        // requisição e nem será preciso enviar nenhum header, o objeto HttpEntity não é
                        // necessário. Já o argumento Produto.class indica o tipo do objeto que será retornado
                        // na resposta http.
                    } else {
                        System.out.println('\n' + "Invocador não removido.");
                    }


                }
                case 3 ->{
                    //Alterar um invocador

                    try {
                        // Recuperando o invocador que se deseja alterar
                        invocador = recuperarObjeto(
                                "Informe o número da invocador que você deseja alterar: ",
                                "http://localhost:8080/invocadores/{id}", Invocador.class);
                    } catch (EntidadeNaoEncontradaException e) {
                        System.out.println('\n' + e.getMessage());
                        break;
                    }

                    System.out.println('\n' +
                            "   Id = " + invocador.getId() +
                            "   Nome = " + invocador.getNome() +
                            "   elo = " + invocador.getElo());

                    System.out.println('\n' + "O que você deseja alterar?");
                    System.out.println('\n' + "1. Nome ");
                    System.out.println('\n' + "2. Elo");


                    int opcaoAlteracao = Console.readInt('\n' + "Digite o número 1 ou 2:");

                    if (opcaoAlteracao == 1) {
                        String novoNome = Console.readLine("Digite o novo nome: ");
                        invocador.setNome(novoNome);
                    } else if (opcaoAlteracao == 2) {
                        String novoElo = Console.readLine("Digite o novo elo do invocador: ");
                        invocador.setElo(novoElo);
                    }

                    else {
                        System.out.println("\n  Opção inválida");
                        break;
                    }

                    try {
                        // Enviando uma requisição do tipo PUT para o servidor para atualizar o produto
//                        HttpEntity<Produto> requestEntity = new HttpEntity<>(produto);
//                        ResponseEntity<Produto> res = restTemplate.exchange(
//                                "http://localhost:8080/produtos",
//                                HttpMethod.PUT, requestEntity, Produto.class);
//                        produto = res.getBody();

                        restTemplate.put("http://localhost:8080/invocadores",invocador);

                        System.out.println('\n' + "Invocador número " + invocador.getId() + " alterado com sucesso!");

                        System.out.println('\n' +
                                "   Id = " + invocador.getId() +
                                "   Nome = " + invocador.getNome() +
                                "   elo = " + invocador.getElo());
                    } catch (ViolacaoDeConstraintException | EntidadeNaoEncontradaException e) {
                        System.out.println('\n' + e.getMessage());
                    }
                }
                case 4 -> {
                    //Recuperar um invocador
                    try {
                        invocador = recuperarObjeto(
                                "Informe o número do invocador que você deseja recuperar: ",
                                "http://localhost:8080/invocadores/{id}", Invocador.class);
                    } catch (EntidadeNaoEncontradaException e) {
                        System.out.println('\n' + e.getMessage());
                        break;
                    }

                    System.out.println('\n' +
                            "   Id = " + invocador.getId() +
                            "   Nome = " + invocador.getNome() +
                            "   elo = " + invocador.getElo());
                }
                case 5 -> {
                    // Enviando uma requisição do tipo GET para recuperar todos os invocadores
                    ResponseEntity<Invocador[]> res = restTemplate.exchange(
                            "http://localhost:8080/invocadores",
                            HttpMethod.GET, null, Invocador[].class);
                    Invocador[] invocadores = res.getBody();

                    for (Invocador umInvocador : invocadores) {
                        System.out.println('\n' +
                                "   Id = " + umInvocador.getId() +
                                "   Nome = " + umInvocador.getNome() +
                                "   Elo = " + umInvocador.getElo());
                    }

                }
                case 6 -> {
                    //Listar todos as fichas por invocador(usando request param)

                    long id = Console.readInt('\n' + "Informe o número do invocador: ");

                    // Enviando uma requisição do tipo GET para recuperar todos os produtos
                    // de uma determinada categoria (utilizando parâmetro de requisição)
                    ResponseEntity<Ficha[]> res = restTemplate.exchange(
                            "http://localhost:8080/fichas/invocador?idInvocador=" + id,
                            HttpMethod.GET, null, Ficha[].class);
                    Ficha[] fichas = res.getBody();

                    if (fichas.length == 0) {
                        System.out.println("\n Nenhuma ficha foi encontrada para este invocador.");
                        break;
                    }

                    for (Ficha umaFicha : fichas) {
                        System.out.println('\n' +
                                "Invocador = " + umaFicha.getInvocador().getNome() +
                                "   Id do invocador = " + umaFicha.getInvocador().getId() +
                                "   \nNome do treino = " + umaFicha.getNome() +
                                "    Id da ficha = " + umaFicha.getId() +
                                "   Status = " + umaFicha.getStatus() +
                                "   data de criação = " + umaFicha.getDia());
                    }

                }
                case 7 -> {
                    //Listar todos as fichas por invocador(usando path variable )

                    // Enviando uma requisição do tipo GET para recuperar todos os produtos
                    // de uma determinada categoria (utilizando path variable) através do
                    // método recuperarObjeto()
                    Ficha[] fichas = recuperarObjeto(
                            "Informe o número da invocador: ",
                            "http://localhost:8080/fichas/invocador/{id}", Ficha[].class);

                    if (fichas.length == 0) {
                        System.out.println("\n  Nenhuma ficha foi encontrado para este invocador.");
                        break;
                    }

                    for (Ficha umaFicha : fichas) {
                        System.out.println('\n' +
                                "Invocador = " + umaFicha.getInvocador().getNome() +
                                "   Id do invocador = " + umaFicha.getInvocador().getId() +
                                "   \nNome do treino = " + umaFicha.getNome() +
                                "    Id da ficha = " + umaFicha.getId() +
                                "   Status = " + umaFicha.getStatus() +
                                "   data de criação = " + umaFicha.getDia());
                    }

                }
                case 8 -> {
                    //Cadastrar uma ficha

                    String dataCadastroString = Console.readLine('\n' + "Informe a data de cadastro da ficha: ");
                    nome_treino =  Console.readLine('\n' + "Informe o nome do treino: ");
                    status = Console.readLine('\n' + "Informe o status do treino [ok/fail]: ");


                    long id = Console.readInt('\n' + "Informe o número do invocador da ficha: ");
                    try {
                        // Recuperando o invocador da ficha
                        ResponseEntity<Invocador> res = restTemplate.exchange(
                                "http://localhost:8080/invocadores/{id}",
                                HttpMethod.GET, null, Invocador.class, id);
                        invocador = res.getBody();
                    } catch (EntidadeNaoEncontradaException e) {
                        System.out.println('\n' + e.getMessage());
                        break;
                    }

                    ficha = new Ficha(LocalDate.parse(dataCadastroString, dtf),
                            status, nome_treino, invocador);

                    // O método exchange abaixo envia uma requisição HTTP para o endpoint fornecido.
                    // No corpo da requisição (objeto requestEntity) é enviado um objeto do tipo produto.
                    // Caso seja necessário enviar algum header, deverá ser enviado como segundo argumento
                    // do construtor de HttpEntity. O método exchange retorna um objeto do tipo ResponseEntity
                    // que, por default, retorna o Status http (HttpStatus) 200 OK.

                    // Ao contrário do Postman, que exige um header do tipo Content-type valendo application/json
                    // para que seja enviado ao servidor conteúdo json incluído no corpo da requisição http,
                    // a API restTemplate já assume, por default, que será enviado json. Daí não ser necessário
                    // o envio de um header do tipo Content-type.

                    try {
                        // HttpHeaders httpHeaders = new HttpHeaders();
                        // httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                        // httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                        // HttpEntity<Produto> requestEntity = new HttpEntity<>(umProduto, httpHeaders);

                        // Enviando uma requisição do tipo POST ao servidor para cadastrar um produto
//                        HttpEntity<Produto> requestEntity = new HttpEntity<>(produto);
//                        ResponseEntity<Produto> res = restTemplate.exchange(
//                                "http://localhost:8080/produtos",
//                                HttpMethod.POST, requestEntity, Produto.class);
//                        produto = res.getBody();

                        ResponseEntity<Ficha> res = restTemplate.postForEntity(
                                "http://localhost:8080/fichas", ficha, Ficha.class);
                        ficha = res.getBody();

                        System.out.println('\n' + "Ficha número " + ficha.getId() + " cadastrado com sucesso!");

                        System.out.println('\n' +
                                "Invocador = " + ficha.getInvocador().getNome() +
                                "   Id do invocador = " + ficha.getInvocador().getId() +
                                "   \nNome do treino = " + ficha.getNome() +
                                "    Id da ficha = " + ficha.getId() +
                                "   Status = " + ficha.getStatus() +
                                "   data de criação = " + ficha.getDia());
                    } catch (ViolacaoDeConstraintException e) {
                        System.out.println('\n' + e.getMessage());
                    }

                }
                case 9 -> {
                    //Alterar uma ficha

                    try {
                        // Recuperando a ficha que se deseja alterar
                        ficha = recuperarObjeto(
                                "Informe o número da ficha que você deseja alterar: ",
                                "http://localhost:8080/fichas/{id}", Ficha.class);
                    } catch (EntidadeNaoEncontradaException e) {
                        System.out.println('\n' + e.getMessage());
                        break;
                    }

                    System.out.println('\n' +
                            "Invocador = " + ficha.getInvocador().getNome() +
                            "   Id do invocador = " + ficha.getInvocador().getId() +
                            "   \nNome do treino = " + ficha.getNome() +
                            "    Id da ficha = " + ficha.getId() +
                            "   Status = " + ficha.getStatus() +
                            "   data de criação = " + ficha.getDia());

                    System.out.println('\n' + "O que você deseja alterar?");
                    System.out.println('\n' + "1. Nome do treino");
                    System.out.println('\n' + "2. Status");
                    System.out.println('\n' + "3. Invocador");

                    int opcaoAlteracao = Console.readInt('\n' + "Digite o número de 1 a 3:");

                    if (opcaoAlteracao == 1) {
                        String novoNome = Console.readLine("Digite o novo nome do treino: ");
                        ficha.setNome(novoNome);
                    } else if (opcaoAlteracao == 2) {
                        String novoStatus = Console.readLine("Digite o novo status do treino: ");
                        ficha.setStatus(novoStatus);
                        }
                        else if (opcaoAlteracao == 3)   {
                        try {
                            invocador = recuperarObjeto(
                                    "Informe o número do novo invocador: ",
                                    "http://localhost:8080/invocadores/{id}", Invocador.class);
                            ficha.setInvocador(invocador);
                        } catch (EntidadeNaoEncontradaException e) {
                            System.out.println('\n' + e.getMessage());
                            break;
                    }
                    } else {
                        System.out.println("\n  Opção inválida");
                        break;
                    }

                    try {
                        // Enviando uma requisição do tipo PUT para o servidor para atualizar o produto
//                        HttpEntity<Produto> requestEntity = new HttpEntity<>(produto);
//                        ResponseEntity<Produto> res = restTemplate.exchange(
//                                "http://localhost:8080/produtos",
//                                HttpMethod.PUT, requestEntity, Produto.class);
//                        produto = res.getBody();

                        restTemplate.put("http://localhost:8080/fichas", ficha);

                        System.out.println('\n' + "Ficha número " + ficha.getId() + " alterado com sucesso!");

                        System.out.println('\n' +
                                "Invocador = " + ficha.getInvocador().getNome() +
                                "   Id do invocador = " + ficha.getInvocador().getId() +
                                "   \nNome do treino = " + ficha.getNome() +
                                "    Id da ficha = " + ficha.getId() +
                                "   Status = " + ficha.getStatus() +
                                "   data de criação = " + ficha.getDia());
                    } catch (ViolacaoDeConstraintException | EntidadeNaoEncontradaException e) {
                        System.out.println('\n' + e.getMessage());
                    }
                }

                case 10 ->{
                    //Remover uma ficha
                    try {
                        // Recuperando a ficha que se deseja remover
                        ficha = recuperarObjeto(
                                "Informe o número da ficha que você deseja remover: ",
                                "http://localhost:8080/fichas/{id}", Ficha.class);
                    }
                    catch (EntidadeNaoEncontradaException e) {
                        System.out.println('\n' + e.getMessage());
                        break;
                    }



                    System.out.println('\n' +
                            "Invocador = " + ficha.getInvocador().getNome() +
                            "   Id do invocador = " + ficha.getInvocador().getId() +
                            "   \nNome do treino = " + ficha.getNome() +
                            "    Id da ficha = " + ficha.getId() +
                            "   Status = " + ficha.getStatus() +
                            "   data de criação = " + ficha.getDia());

                    String resp = Console.readLine('\n' +
                            "Confirma a remoção da ficha? [s/n]: ");

                    if (resp.equals("s")) {
                        try {
                            // Enviando uma requisição do tipo DELETE para remover o produto
//                            restTemplate.exchange(
//                                    "http://localhost:8080/fichas/{id}",
//                                    HttpMethod.DELETE, null, Ficha.class, ficha.getId());
                            restTemplate.delete("http://localhost:8080/fichas/{id}", ficha.getId());

                            System.out.println('\n' + "Ficha número " + ficha.getId() + " removido com sucesso!");
                        } catch (EntidadeNaoEncontradaException e) {
                            System.out.println('\n' + e.getMessage());
                            break;
                        }
                        // Onde aparece null, poderia ter sido  especificado um  objeto do tipo  HttpEntity.
                        // Como nesta requisição http não está sendo necessário enviar nada no corpo da
                        // requisição e nem será preciso enviar nenhum header, o objeto HttpEntity não é
                        // necessário. Já o argumento Produto.class indica o tipo do objeto que será retornado
                        // na resposta http.
                    } else {
                        System.out.println('\n' + "Ficha não removida.");
                    }


                }
                case 11 ->{
                    //Recuperar uma ficha
                    try {
                        ficha = recuperarObjeto(
                                "Informe o número da ficha que você deseja recuperar: ",
                                "http://localhost:8080/fichas/{id}", Ficha.class);
                    }
                    catch (EntidadeNaoEncontradaException e) {
                        System.out.println('\n' + e.getMessage());
                        break;
                    }

                    System.out.println('\n' +
                            "Invocador = " + ficha.getInvocador().getNome() +
                            "   Id do invocador = " + ficha.getInvocador().getId() +
                            "   \nNome do treino = " + ficha.getNome() +
                            "    Id da ficha = " + ficha.getId() +
                            "   Status = " + ficha.getStatus() +
                            "   data de criação = " + ficha.getDia());

                }
                case 12 -> {
                    continua = false;
                }
                default -> System.out.println('\n' + "Opção inválida!");
            }
        }
    }

    private static <T> T recuperarObjeto(String msg, String url, Class<T> classe) {
        int id = Console.readInt('\n' + msg);
        return restTemplate.getForObject(url, classe, id);
    }
}
