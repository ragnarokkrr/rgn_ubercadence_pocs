package ragna.proposta.infrastructure.config;

import com.google.common.util.concurrent.MoreExecutors;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ragna.proposta.infrastructure.workflow.PropostaActivities;
import ragna.proposta.infrastructure.workflow.PropostaWorkflowImpl;

@Configuration
@EnableCaching
public class UberCadenceConfig {

  @Autowired private ApplicationContext context;

  @Autowired private ExecutorService executorService;

  @Bean
  public ExecutorService executorService() {
    ThreadPoolExecutor threadPoolExecutor =
        (ThreadPoolExecutor) Executors.newFixedThreadPool(1, this::threadFactory);

    return MoreExecutors.getExitingExecutorService(threadPoolExecutor, 100, TimeUnit.MILLISECONDS);
  }

  private Thread threadFactory(Runnable r) {
    return new Thread(r, "uber-worker-pool");
  }

  @Bean
  public UberCadenceWorker uberCadenceWorker() {
    return new UberCadenceWorker(
        UberCadenceConstants.DOMAIN,
        UberCadenceConstants.TASK_LIST,
        workflowImplementationClasses(),
        workflowActivities(),
        executorService);
  }

  List<Class<?>> workflowImplementationClasses() {

    return Collections.singletonList(PropostaWorkflowImpl.class);
  }

  List<?> workflowActivities() {

    return Collections.singletonList(context.getBean(PropostaActivities.class));
  }
}
