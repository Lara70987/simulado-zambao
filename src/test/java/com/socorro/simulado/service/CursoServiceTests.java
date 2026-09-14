package com.socorro.simulado.service;

import com.socorro.simulado.entity.Curso;
import com.socorro.simulado.exception.CursoNaoEncontradoException;
import com.socorro.simulado.repository.CursoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CursoServiceTests {

    @InjectMocks
    private CursoService cursoService;

    @Mock
    private CursoRepository cursoRepository;

    @Test
    public void test_shouldReturnAllNonDeletedWhenListarWithoutFilter() {
        List<Curso> cursos = new ArrayList<>();
        cursos.add(new Curso());
        cursos.add(new Curso());

        Mockito.when(cursoRepository.findByDeletedFalse()).thenReturn(cursos);

        List<Curso> response = cursoService.listar(null);

        Assertions.assertEquals(2, response.size());
    }

    @Test
    public void test_shouldReturnFilteredWhenListarWithNome() {
        List<Curso> cursos = new ArrayList<>();
        cursos.add(new Curso());

        Mockito.when(cursoRepository.findByNomeStartingWithIgnoreCaseAndDeletedFalse("Java"))
                .thenReturn(cursos);

        List<Curso> response = cursoService.listar("Java");

        Assertions.assertEquals(1, response.size());
    }

    @Test
    public void test_shouldReturnAllNonDeletedWhenListarWithBlankNome() {
        List<Curso> cursos = new ArrayList<>();
        cursos.add(new Curso());
        cursos.add(new Curso());

        Mockito.when(cursoRepository.findByDeletedFalse()).thenReturn(cursos);

        List<Curso> response = cursoService.listar("   ");

        Assertions.assertEquals(2, response.size());
    }

    @Test
    public void test_shouldCreateCursoWhenNomeIsValid() {
        Curso curso = new Curso();
        curso.setNome("Spring Boot");
        curso.setDescricao("Curso de Spring");

        Mockito.when(cursoRepository.save(Mockito.any())).thenReturn(curso);

        Curso response = cursoService.criar(curso);

        Assertions.assertEquals("Spring Boot", response.getNome());
        Assertions.assertFalse(response.isDeleted());
    }

    @Test
    public void test_shouldThrowExceptionWhenNomeIsNull() {
        Curso curso = new Curso();
        curso.setNome(null);

        Assertions.assertThrows(RuntimeException.class, () -> cursoService.criar(curso));
    }

    @Test
    public void test_shouldLogicalDeleteWhenCursoExists() {
        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNome("Java");
        curso.setDeleted(false);

        Mockito.when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        Mockito.when(cursoRepository.save(Mockito.any())).thenReturn(curso);

        cursoService.deletar(1L);

        Assertions.assertTrue(curso.isDeleted());
        Mockito.verify(cursoRepository).save(curso);
    }

    @Test
    public void test_shouldThrowExceptionWhenCursoNotFoundOnDelete() {
        Mockito.when(cursoRepository.findById(99L)).thenReturn(Optional.empty());

        Assertions.assertThrows(CursoNaoEncontradoException.class, () -> cursoService.deletar(99L));
    }
}