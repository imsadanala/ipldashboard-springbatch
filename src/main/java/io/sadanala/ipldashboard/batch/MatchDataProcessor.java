package io.sadanala.ipldashboard.batch;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import io.sadanala.ipldashboard.data.MatchInput;
import io.sadanala.ipldashboard.model.Match;

public class MatchDataProcessor implements ItemProcessor<MatchInput, Match> {

    public static Logger logger = LoggerFactory.getLogger(MatchDataProcessor.class);

    @Override
    public Match process(final MatchInput input) throws Exception {
        Match match = new Match();
        match.setId(Long.parseLong(input.getId()));
        match.setCity(input.getCity());
        match.setDate(LocalDate.parse(input.getDate()));
        match.setPlayerOfMatch(input.getPlayer_of_match());
        match.setVenue(input.getVenue());
        String firstInningsTeam, secondInningsTeam;
        if ("bat".equals(input.getToss_winner())) {
            firstInningsTeam = input.getToss_winner();
            secondInningsTeam = input.getToss_winner().equals(input.getTeam1()) ? input.getTeam2() : input.getTeam1();
        } else {
            secondInningsTeam = input.getToss_winner();
            firstInningsTeam = input.getToss_winner().equals(input.getTeam1()) ? input.getTeam2() : input.getTeam1();
        }
        match.setTeam1(firstInningsTeam);
        match.setTeam2(secondInningsTeam);
        match.setTossWinner(input.getToss_winner());
        match.setTossDecision(input.getToss_decision());
        match.setResult(input.getResult());
        match.setUmpire1(input.getUmpire1());
        match.setUmpire2(input.getUmpire2());
        return match;
    }
}
