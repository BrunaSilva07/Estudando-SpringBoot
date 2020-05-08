package br.com.alura.forum.controller;

import br.com.alura.forum.controller.DTO.DetalheTopicoDTO;
import br.com.alura.forum.controller.DTO.TopicoDTO;
import br.com.alura.forum.controller.Form.AtualizarTopicoForm;
import br.com.alura.forum.controller.Form.TopicoForm;
import br.com.alura.forum.model.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;
import javassist.runtime.Desc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
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
    @Cacheable(value = "listaDeTopicos")
    public Page<TopicoDTO> lista(@RequestParam(required = false) String nomeCurso,
                                 @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable paginacao) {

        if (nomeCurso == null) {
            Page<Topico> topicos = repository.findAll(paginacao);
            return TopicoDTO.converter(topicos);
        } else {
            Page<Topico> topicos = repository.findByCursoNome(nomeCurso, paginacao);
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
    @CacheEvict(value = "listaDeTopicos", allEntries = true)
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
    @CacheEvict(value = "listaDeTopicos", allEntries = true)
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
    @CacheEvict(value = "listaDeTopicos", allEntries = true)
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        Optional<Topico> topico = repository.findById(id);
        if (topico.isPresent()) {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
