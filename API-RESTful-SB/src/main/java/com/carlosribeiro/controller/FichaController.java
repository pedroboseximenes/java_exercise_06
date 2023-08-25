package com.carlosribeiro.controller;

import com.carlosribeiro.model.Ficha;
import com.carlosribeiro.service.FichaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path ="fichas")    // http://localhost:8080/fichas
public class FichaController {

    @Autowired
    private FichaService fichaService;

    @GetMapping
    public List<Ficha> recuperarFichas() {
        return fichaService.recuperarFichas();
    }

    @PostMapping
    public Ficha cadastrarFicha(@RequestBody Ficha ficha) {
        return fichaService.cadastrarFicha(ficha);
    }

//    @GetMapping("{idFicha}")                // http://localhost:8080/fichas/1
//    public ResponseEntity<?> recuperarFichaPorId(@PathVariable("idFicha") Long id) {
//        try {
//            Ficha ficha = fichaService.recuperarFichaPorId(id);
//            return new ResponseEntity<>(ficha, HttpStatus.OK);
//        }
//        catch(EntidadeNaoEncontradaException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//        }
//    }

    @GetMapping("{idFicha}")     // http://localhost:8080/fichas/5
    public Ficha recuperarFichaPorId(@PathVariable("idFicha") Long id) {
        return fichaService.recuperarFichaPorId(id);
    }

    @PutMapping
    public Ficha atualizarFicha(@RequestBody Ficha ficha) {
        return fichaService.atualizarFicha(ficha);
    }

    @DeleteMapping("{idFicha}")    // http://localhost:8080/fichas/5
    public void removerFicha(@PathVariable("idFicha") Long id) {
        fichaService.removerFicha(id);
    }

    @GetMapping("invocador/{idInvocador}")// http://localhost:8080/fichas/invocador/1
    public List<Ficha> recuperarFichasPorInvocadorV1(@PathVariable("idInvocador") Long id) {
        return fichaService.recuperarFichasPorInvocador(id);
    }

    @GetMapping("invocador")// http://localhost:8080/fichas/invocador?idInvocador=1
    public List<Ficha> recuperarFichasPorInvocadorV2(@RequestParam("idInvocador") Long id) {
        return fichaService.recuperarFichasPorInvocador(id);
    }
}
