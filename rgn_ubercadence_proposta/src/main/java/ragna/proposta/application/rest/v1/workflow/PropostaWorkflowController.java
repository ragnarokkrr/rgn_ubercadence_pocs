package ragna.proposta.application.rest.v1.workflow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
public class PropostaWorkflowController {
  private PropostaService propostaService;

  @Autowired
  public PropostaWorkflowController(PropostaService propostaService) {
    this.propostaService = propostaService;
  }

  @PostMapping(value = "proposta/wofklow", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Proposta> novaProposta(@RequestBody PropostaCommand propostaCommand) {
    Proposta proposta = propostaService.iniciarProposta(propostaCommand);

    return ResponseEntity.ok(proposta);
  }
}
