package br.com.alura.forum.controller;

import br.com.alura.forum.controller.DTO.DetalheTopicoDTO;
import br.com.alura.forum.controller.DTO.TopicoDTO;
import br.com.alura.forum.controller.Form.AtualizarTopicoForm;
import br.com.alura.forum.controller.Form.TopicoForm;
import br.com.alura.forum.model.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping({"/topicos"})
public class TopicoController {

    @Autowired
    private TopicoRepository repository;

    @Autowired
    private CursoRepository cursoRepository;
    private TopicoForm form;

    @GetMapping
    @Transactional
    public List<TopicoDTO> lista(String nomeCurso) {
        if (nomeCurso == null) {
            List<Topico> topicos = repository.findAll();
            return TopicoDTO.converter(topicos);
        } else {
            List<Topico> topicos = repository.findByCursoNome(nomeCurso);
            return TopicoDTO.converter(topicos);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalheTopicoDTO> buscarPorId(@PathVariable Long id) {
        Optional<Topico> topico = repository.findById(id);
        return topico.map(value -> ResponseEntity.ok().body(new DetalheTopicoDTO(value))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Transactional
    public ResponseEntity<TopicoDTO> cadastrar(@RequestBody @Valid TopicoForm form,
                                               UriComponentsBuilder uriBuilder) {
        this.form = form;
        Topico topico = this.form.converter(cursoRepository);
        repository.save(topico);
        URI uri = uriBuilder.path("topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDTO(topico));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<TopicoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizarTopicoForm form){
        Optional<Topico> optional = repository.findById(id);
        if (optional.isPresent()) {
            Topico topico = form.atualizar(id, repository);
            return ResponseEntity.ok(new TopicoDTO(topico));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        Optional<Topico> topico = repository.findById(id);
        if (topico.isPresent()) {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
