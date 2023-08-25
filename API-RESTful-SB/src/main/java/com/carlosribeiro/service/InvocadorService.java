package com.carlosribeiro.service;

import com.carlosribeiro.exception.EntidadeNaoEncontradaException;
import com.carlosribeiro.model.Invocador;
import com.carlosribeiro.repository.InvocadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvocadorService {
    @Autowired
    private InvocadorRepository invocadorRepository;

    public List<Invocador> recuperarInvocadores() {
        return invocadorRepository.findAll(Sort.by("id"));
    }

    public Invocador recuperarInvocadorPorId(Long id) {
        return invocadorRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Invocador número " + id + " não encontrada."));
    }

    public Invocador cadastrarInvocador(Invocador invocador) {
        return invocadorRepository.save(invocador);
    }
    public void removerInvocador(Long id) {
        recuperarInvocadorPorId(id);
       invocadorRepository.deleteById(id);
    }

    public Invocador atualizarInvocador(Invocador invocador) {


        Invocador umInvocador = recuperarInvocadorPorId(invocador.getId());
        if (!(invocador.getId().equals(umInvocador.getId()))) {
            invocadorRepository.findById(invocador.getId())
                    .orElseThrow(()-> new EntidadeNaoEncontradaException(
                            "Invocador número " + invocador.getId() + " não encontrada."));
        }
        return invocadorRepository.save(invocador);
    }
}
