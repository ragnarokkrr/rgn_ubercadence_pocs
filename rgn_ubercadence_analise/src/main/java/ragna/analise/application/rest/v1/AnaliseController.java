package ragna.analise.application.rest.v1;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ragna.analise.domain.model.Analise;
import ragna.analise.domain.service.AnaliseCommand;
import ragna.analise.domain.service.AnaliseService;
import ragna.analise.domain.service.ParecerAnaliseCommand;

@RestController
@RequestMapping("/v1")
@Slf4j
public class AnaliseController {

  private AnaliseService analiseService;

  @Autowired
  public AnaliseController(AnaliseService analiseService) {
    this.analiseService = analiseService;
  }

  @PostMapping(value = "analise", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Analise> novaAnalise(@RequestBody AnaliseCommand analiseCommand) {
    return new ResponseEntity<>(analiseService.novaAnalise(analiseCommand), HttpStatus.OK);
  }

  @PostMapping(value = "analise/{analiseId}/aprovar")
  public ResponseEntity<Analise> aprovar(
      @PathVariable("analiseId") UUID id, @RequestBody ParecerAnaliseCommand parecerAnalise) {
    return ResponseEntity.ok()
        .cacheControl(CacheControl.maxAge(30, TimeUnit.DAYS))
        .eTag(id.toString())
        .body(analiseService.aprovar(id, parecerAnalise));
  }

  @PostMapping(value = "analise/{analiseId}/reprovar")
  public ResponseEntity<Analise> reprovar(
      @PathVariable("analiseId") UUID id, @RequestBody ParecerAnaliseCommand parecerAnalise) {
    return new ResponseEntity<>(analiseService.reprovar(id, parecerAnalise), HttpStatus.OK);
  }

  @GetMapping(value = "analise", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<Analise>> list() {
    return new ResponseEntity<>(analiseService.list(), HttpStatus.OK);
  }
}
