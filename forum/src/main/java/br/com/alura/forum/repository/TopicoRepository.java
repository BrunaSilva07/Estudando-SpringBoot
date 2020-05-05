package br.com.alura.forum.repository;

import br.com.alura.forum.controller.DTO.DetalaheTopicoDTO;
import br.com.alura.forum.controller.DTO.TopicoDTO;
import br.com.alura.forum.model.Topico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

    List<Topico> findByCursoNome(String nomeCurso);
}
