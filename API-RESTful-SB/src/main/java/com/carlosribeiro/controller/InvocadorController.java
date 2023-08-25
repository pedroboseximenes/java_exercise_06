package com.carlosribeiro.controller;

import com.carlosribeiro.model.Ficha;
import com.carlosribeiro.model.Invocador;
import com.carlosribeiro.model.Invocador;
import com.carlosribeiro.service.InvocadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("invocadores")
public class InvocadorController {
    @Autowired
    private InvocadorService invocadorService;

    @GetMapping
    public List<Invocador> recuperarInvocadores() {
        return invocadorService.recuperarInvocadores();
    }

    @GetMapping("{idInvocador}")
    private Invocador recuperarInvocadorPorId(@PathVariable("idInvocador") Long id) {
        return invocadorService.recuperarInvocadorPorId(id);
    }
    @PostMapping
    public Invocador cadastrarInvocador(@RequestBody Invocador invocador) {
        return invocadorService.cadastrarInvocador(invocador);
    }

    @DeleteMapping("{idInvocador}")    // http://localhost:8080/invocadores/5
    public void removerInvocador(@PathVariable("idInvocador") Long id) {
        invocadorService.removerInvocador(id);
    }
    @PutMapping
    public Invocador atualizarInvocador(@RequestBody Invocador invocador) {
        return invocadorService.atualizarInvocador(invocador);
    }
}
