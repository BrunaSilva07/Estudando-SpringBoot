package br.com.alura.forum.controller;

import br.com.alura.forum.controller.DTO.TopicoDTO;
import br.com.alura.forum.controller.Form.TopicoForm;
import br.com.alura.forum.model.Curso;
import br.com.alura.forum.model.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.Id;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping({"/topicos"})
public class TopicoController {

    @Autowired
    private TopicoRepository repository;

    @Autowired
    private CursoRepository cursoRepository;
    private TopicoForm form;

    @GetMapping
    public List<TopicoDTO> lista(String nomeCurso) {
        if (nomeCurso == null) {
            List<Topico> topicos = repository.findAll();
            return TopicoDTO.converter(topicos);
        } else {
            List<Topico> topicos = repository.findByCursoNome(nomeCurso);
            return TopicoDTO.converter(topicos);
        }
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<List<Topico>> listarId(@PathVariable Long id) {
        return repository.findByCursoId(id)
                .map(c -> ResponseEntity.ok().body(c))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<TopicoDTO> cadastrar(TopicoForm form,
                                               UriComponentsBuilder uriBuilder) {
        this.form = form;
        Topico topico = this.form.converter(cursoRepository);
        repository.save(topico);
        URI uri = uriBuilder.path("topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDTO(topico));
    }

}
