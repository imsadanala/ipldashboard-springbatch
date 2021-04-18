package io.sadanala.ipldashboard.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import io.sadanala.ipldashboard.data.MatchInput;
import io.sadanala.ipldashboard.model.Match;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private final String[] FIELD_NAMES = new String[] { "id", "city", "date", "player_of_match", "venue",
            "neutral_venue", "team1", "team2", "toss_winner", "toss_decision", "winner", "result", "result_margin",
            "eliminator", "method", "umpire1", "umpire2" };

    public JobBuilderFactory jobBuilderFactory;
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public FlatFileItemReader<MatchInput> reader() {
        return new FlatFileItemReaderBuilder<MatchInput>().name("MatchItemReader")
                .resource(new ClassPathResource("sample-data.csv")).delimited().names(FIELD_NAMES)
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

    @Bean
    public JdbcBatchItemWriter<Match> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Match>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO match (id, city, date, playerOfMatch, venue, neutralVenue, team1, team2, tossWinner, tossDecision, matchWinner, result, resultMargin, umpire1, umpire2) VALUES (id,:city,:ate,:playerOfMatch,:venue,:neutralVenue,:team1,:team2,:tossWinner,:tossDecision,:matchWinner,:result,:resultMargin,:umpire1,:umpire2)")
                .dataSource(dataSource).build();
    }

}
