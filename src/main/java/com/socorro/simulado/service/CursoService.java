package com.socorro.simulado.service;

import com.socorro.simulado.entity.Curso;
import com.socorro.simulado.exception.CursoNaoEncontradoException;
import com.socorro.simulado.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    public List<Curso> listar(String nome) {
        if (nome != null && !nome.isBlank()) {
            return cursoRepository.findByNomeStartingWithIgnoreCaseAndDeletedFalse(nome);
        }
        return cursoRepository.findByDeletedFalse();
    }

    public Curso criar(Curso curso) {
        if (curso.getNome() == null || curso.getNome().isBlank()) {
            throw new RuntimeException("Nome do curso é obrigatório");
        }
        curso.setDeleted(false);
        return cursoRepository.save(curso);
    }

    public void deletar(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new CursoNaoEncontradoException("Curso não encontrado"));
        curso.setDeleted(true);
        cursoRepository.save(curso);
    }
}