package ragna.proposta.application.rest.v1;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ragna.proposta.domain.model.Proposta;
import ragna.proposta.domain.service.PropostaCommand;
import ragna.proposta.domain.service.PropostaService;

@RestController
@RequestMapping("/v1")
@Slf4j
public class PropostaServiceController {
  private PropostaService propostaService;

  @Autowired
  public PropostaServiceController(PropostaService propostaService) {
    this.propostaService = propostaService;
  }

  @PostMapping(value = "proposta", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Proposta> novaProposta(@RequestBody PropostaCommand propostaCommand) {
    return new ResponseEntity<>(propostaService.iniciarProposta(propostaCommand), HttpStatus.OK);
  }

  @GetMapping(value = "proposta", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<Proposta>> listar() {
    return new ResponseEntity<>(propostaService.listarPropostas(), HttpStatus.OK);
  }
}
