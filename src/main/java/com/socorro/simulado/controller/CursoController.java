package com.socorro.simulado.controller;

import com.socorro.simulado.entity.Curso;
import com.socorro.simulado.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @GetMapping
    public List<Curso> listar(@RequestParam(required = false) String nome) {
        return cursoService.listar(nome);
    }

    @PostMapping
    public Curso criar(@RequestBody Curso curso) {
        return cursoService.criar(curso);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        cursoService.deletar(id);
    }
}