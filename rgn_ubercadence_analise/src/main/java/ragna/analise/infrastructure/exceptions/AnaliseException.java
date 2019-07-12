package ragna.analise.infrastructure.exceptions;

public class AnaliseException extends RuntimeException {

  public AnaliseException(String message) {
    super(message);
  }

  public AnaliseException(String message, Throwable cause) {
    super(message, cause);
  }
}
