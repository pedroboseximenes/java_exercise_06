package com.carlosribeiro.service;

import com.carlosribeiro.exception.EntidadeNaoEncontradaException;
import com.carlosribeiro.model.Ficha;
import com.carlosribeiro.repository.InvocadorRepository;
import com.carlosribeiro.repository.FichaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FichaService {
    @Autowired
    private FichaRepository fichaRepository;

    @Autowired
    private InvocadorRepository invocadorRepository;
    public List<Ficha> recuperarFichas() {
        return fichaRepository.findAll();
    }

    public Ficha cadastrarFicha(Ficha ficha) {
        return fichaRepository.save(ficha);
    }

    public Ficha recuperarFichaPorId(Long id) {
        return fichaRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Ficha número " + id + " não encontrado."));
    }

    // O método atualizarFicha() abaixo, recebe um objeto destacado (ficha), recupera um
    // objeto persistente (umFicha) do banco de dados e efetua o merge(). Note que o save
    // recebe uma entidade do tipo S e retornar um objeto do tipo S. Isto é, recebe o objeto
    // ficha e retorna o objeto umFicha após ocorrer o merge.
    public Ficha atualizarFicha(Ficha ficha) {
        // Somente com essa linha de código: return fichaRepository.save(ficha);
        // Se não mudarmos a invocador serão executados esses 2 comandos:
        //    1. select p1_0.id,c1_0.id,c1_0.nome,c1_0.slug,p1_0.data_cadastro,p1_0.nome,p1_0.preco from ficha p1_0 left join invocador c1_0 on c1_0.id=p1_0.invocador_id where p1_0.id=?
        //    2. update ficha set invocador_id=?, data_cadastro=?, nome=?, preco=? where id=?
        // E se mudarmos a invocador serão executados esses 3 comandos:
        //    1. select p1_0.id,c1_0.id,c1_0.nome,c1_0.slug,p1_0.data_cadastro,p1_0.nome,p1_0.preco from ficha p1_0 left join invocador c1_0 on c1_0.id=p1_0.invocador_id where p1_0.id=?
        //    2. select c1_0.id,c1_0.nome,c1_0.slug from invocador c1_0 where c1_0.id=?   (RECUPERA A NOVA CATEGORIA)
        //    3. update ficha set invocador_id=?, data_cadastro=?, nome=?, preco=? where id=?

        // E com o código que escrevemos abaixo será EXATAMENTE IGUAL.

        // Se não mudarmos a invocador, o código abaixo executará esses 2 comandos:
        //    1. Para recuperarFichaPorId(): select p1_0.id,c1_0.id,c1_0.nome,c1_0.slug,p1_0.data_cadastro,p1_0.nome,p1_0.preco from ficha p1_0 left join invocador c1_0 on c1_0.id=p1_0.invocador_id where p1_0.id=?
        //    2. E para o método save(): update ficha set invocador_id=?, data_cadastro=?, nome=?, preco=? where id=?

        // E se mudarmos a invocador:
        // Sem o findById() abaixo esse método executaria esses 3 comandos:
        //    1. Para recuperarFichaPorId(): select p1_0.id,c1_0.id,c1_0.nome,c1_0.slug,p1_0.data_cadastro,p1_0.nome,p1_0.preco from ficha p1_0 left join invocador c1_0 on c1_0.id=p1_0.invocador_id where p1_0.id=?
        // E para o método save(), os 2 abaixo:
        //    2. Para recuperar a nova invocador: select c1_0.id,c1_0.nome,c1_0.slug from invocador c1_0 where c1_0.id=?
        //    3. Para atualizar o ficha: update ficha set invocador_id=?, data_cadastro=?, nome=?, preco=? where id=?

        // E se acrescentarmos o findById() abaixo, então ficará assim:
        //    1. Para recuperarFichaPorId(): select p1_0.id,c1_0.id,c1_0.nome,c1_0.slug,p1_0.data_cadastro,p1_0.nome,p1_0.preco
        //                                     from ficha p1_0 left join invocador c1_0 on c1_0.id=p1_0.invocador_id where p1_0.id=?
        //    2. Para o findById() que recupera a nova invocador: select c1_0.id,c1_0.nome,c1_0.slug from invocador c1_0 where c1_0.id=?
        //    3. E para o save() que atualiza o ficha: update ficha set invocador_id=?, data_cadastro=?, nome=?, preco=? where id=?

        Ficha umFicha = recuperarFichaPorId(ficha.getId());
        if (!(ficha.getInvocador().getId().equals(umFicha.getInvocador().getId()))) {
            invocadorRepository.findById(ficha.getInvocador().getId())
                    .orElseThrow(()-> new EntidadeNaoEncontradaException(
                            "Invocador número " + ficha.getInvocador().getId() + " não encontrada."));
        }
        return fichaRepository.save(ficha);
    }

// Estilo novo - com lock
//    @Transactional
//    public Ficha atualizarFicha(Ficha ficha) {
//        fichaRepository.recuperarFichaPorIdComLock(ficha.getId())
//                .orElseThrow(() -> new EntidadeNaoEncontradaException(
//                    "Ficha número " + ficha.getId() + " não encontrado."));
//        return fichaRepository.save(ficha);
//    }

// Estilo antigo
//    @Transactional
//    public Ficha atualizarFicha(Ficha ficha) {
//        Ficha umFicha = fichaRepository.recuperarFichaPorIdComLock(ficha.getId());
//        if (umFicha == null)
//            throw new EntidadeNaoEncontradaException(
//                    "Ficha número " + ficha.getId() + " não encontrado.");
//        return fichaRepository.save(ficha);
//    }

//    Ruim no caso da classe ter muitos campos
//    @Transactional
//    public Ficha atualizarFicha(Ficha ficha) {
//        Ficha umFicha = fichaRepository.recuperarFichaPorIdComLock(ficha.getId())
//                .orElseThrow(() -> new EntidadeNaoEncontradaException(
//                    "Ficha número " + ficha.getId() + " não encontrado."));
//        umFicha.setNome(ficha.getNome());
//        umFicha.setPreco(ficha.getPreco());
//        umFicha.setDataCadastro(ficha.getDataCadastro());
//        return umFicha;
//    }

    public void removerFicha(Long id) {
        recuperarFichaPorId(id);
        fichaRepository.deleteById(id);
    }

    public List<Ficha> recuperarFichasPorInvocador(Long id) {
        return fichaRepository.findByInvocadorId(id);
    }
}
