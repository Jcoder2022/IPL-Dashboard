package io.javabrains.ipldashboard.data;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import io.javabrains.ipldashboard.model.Match;
import io.javabrains.ipldashboard.repository.MatchRepository;
import jakarta.persistence.EntityManager;

@Configuration
public class BatchConfig {

    private final JobLauncher jobLauncher;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager batchTransactionManager;
    private static final int BATCH_SIZE = 10;

    public BatchConfig(JobLauncher jobLauncher, JobRepository jobRepository, PlatformTransactionManager batchTransactionManager) {
        this.jobLauncher = jobLauncher;
        this.jobRepository = jobRepository;
        this.batchTransactionManager = batchTransactionManager;
    }


    private final String[] FIELD_NAMES = new String[] { "id", "city", "date", "player_of_match", "venue",
            "neutral_venue", "team1", "team2", "toss_winner", "toss_decision", "winner", "result", "result_margin",
            "eliminator", "method", "umpire1", "umpire2" };
        public static final Logger logger = LoggerFactory.getLogger(BatchConfig.class);

   

    @Bean
    public FlatFileItemReader<MatchInput> reader() {
        return new FlatFileItemReaderBuilder<MatchInput>().name("MatchItemReader")
                .resource(new ClassPathResource("match-data.csv")).delimited().names(FIELD_NAMES)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<MatchInput>() {
                    {
                        setTargetType(MatchInput.class);
                    }
                }).build();
    }

    @Bean
    public MatchDataProcessor processor() {
        return new MatchDataProcessor();
    }


    /*
     * 
     * iddue with writer data
     */


     @Autowired     
     private MatchRepository matchRepository;
    
 
    @Bean
    public RepositoryItemWriter<Match> writer() {
        RepositoryItemWriter<Match> writer = new RepositoryItemWriter<>();
        writer.setRepository(matchRepository);
        writer.setMethodName("save");
        return writer;
    }
    @Bean
    public Job importUserJob( JobCompletionNotificationListener listener, Step step1) {
        return new JobBuilder("importUserJob",this.jobRepository)
            .incrementer(new RunIdIncrementer())
            .listener(listener)
            .flow(step1)
            .end()
            .build();
    }


    @Bean
    public Step step1( ) {
        return new StepBuilder("step1", jobRepository)
                .<MatchInput, Match>chunk(BATCH_SIZE, batchTransactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

   
}
