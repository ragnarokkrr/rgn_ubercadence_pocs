package ragna.proposta.infrastructure.exception;

public class PropostaException extends RuntimeException {

  public PropostaException() {}

  public PropostaException(String message) {
    super(message);
  }

  public PropostaException(String message, Throwable cause) {
    super(message, cause);
  }
}
