package io.javabrains.ipldashboard.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collector;
import java.util.stream.Collectors;

//import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import io.javabrains.ipldashboard.model.Match;
import io.javabrains.ipldashboard.model.Team;
import io.javabrains.ipldashboard.repository.MatchRepository;
import jakarta.persistence.EntityManager;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

  private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

  // private final JdbcTemplate jdbcTemplate;

  // public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
  // this.jdbcTemplate = jdbcTemplate;
  // }

  private final EntityManager em;

  @Autowired
  public JobCompletionNotificationListener(EntityManager em) {
    this.em = em;
  }
        

  // @Autowired
  //   private MatchRepository matchRepository;

  @Override
  public void afterJob(JobExecution jobExecution) {
    if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
      log.info("!!! JOB FINISHED! Time to verify the results");

      Map<String, Team> teamData =  new HashMap<String,Team >();
      em.createQuery("select distinct m.team1, count(*) from Match m group by m.team1",Object[].class)
        .getResultList()
        .stream()
        .map(e->new Team((String)e[0],(long)e[1]))
        .forEach(team -> teamData.put(team.getTeamName(),team));
      

      // team1.forEach(result-> {
      //   Team team = new Team(String.valueOf(result[0]), Long.parseLong(result[1].toString()));
      //   teams.add(team);
      // });

      // List<Object[]> team2= em.createQuery("select distinct m.team2, count(*) from Match m group by m.team2").getResultList();
      
      // List<String> teamsNames = teams.stream().map(t->(t.getTeamName())).collect(Collectors.toList());
        

      // team2.forEach(result-> {
        
      //     Optional<Team> team = teams.stream().filter(t->t.getTeamName().equalsIgnoreCase(String.valueOf(result[0]))).findAny();
      //     if(team.isPresent()){
      //       team.get().setTotalMatches (team.get().getTotalMatches()+Long.parseLong(result[1].toString()));
      //     }
      //   else{
      //     Team newtTeam = new Team(String.valueOf(result[0]),Long.parseLong(result[1].toString()) );
      //     teams.add(newtTeam);
      

      // List<Object[]> team1= em.createQuery("select distinct m.team1, count(*) from Match m group by m.team1").getResultList();
      

      // team1.forEach(result-> {
      //   Team team = new Team(String.valueOf(result[0]), Long.parseLong(result[1].toString()));
      //   teams.add(team);
      // });

      // List<Object[]> team2= em.createQuery("select distinct m.team2, count(*) from Match m group by m.team2").getResultList();
      
      // List<String> teamsNames = teams.stream().map(t->(t.getTeamName())).collect(Collectors.toList());
        

      // team2.forEach(result-> {
        
      //     Optional<Team> team = teams.stream().filter(t->t.getTeamName().equalsIgnoreCase(String.valueOf(result[0]))).findAny();
      //     if(team.isPresent()){
      //       team.get().setTotalMatches (team.get().getTotalMatches()+Long.parseLong(result[1].toString()));
      //     }
      //   else{
      //     Team newtTeam = new Team(String.valueOf(result[0]),Long.parseLong(result[1].toString()) );
      //     teams.add(newtTeam);
      // }});

      
   //   teams.stream().forEach(t-> System.out.println(t.getTeamName() + "  "+t.getTotalMatches()));
      



/*  With Jdbctemplate


      Set<Team> teams = new HashSet<Team>();
  
      matchRepository.findAll().forEach(m->System.out.println(m.getTeam1()));

      SqlRowSet sqlRowTeam1 = jdbcTemplate
      .queryForRowSet("SELECT team1, count(team1) FROM MATCH GROUP BY team1");

      SqlRowSet sqlRowTeam2 = jdbcTemplate
      .queryForRowSet("SELECT team2, count(team2) FROM MATCH GROUP BY team2");

      while (sqlRowTeam1.next()) {
      String teamName = sqlRowTeam1.getString(1);
      String totalMatch = sqlRowTeam1.getString(2);
      // System.out.println(totalMatches);
      Team team = new Team();
      team.setTeamName(teamName);
      team.setTotalMatches(Long.parseLong(totalMatch));
      teams.add(team);
      }
      System.out.println("teams size = " + teams.size());

      Set<String> team2Set = new HashSet<String>();
      Set<Team> teams2 = new HashSet<Team>();

      while (sqlRowTeam2.next()) {
      String teamName = sqlRowTeam2.getString(1);
      String totalMatch = sqlRowTeam2.getString(2);

      Team t = new Team();
      t.setTeamName(teamName);
      t.setTotalMatches(Long.parseLong(totalMatch));
      teams2.add(t);
      team2Set.add(teamName);
      }

      //team2Set =
      teams2.stream().map(t->t.getTeamName()).collect(Collectors.toSet());

      if (team2Set.size() > 0) {
      teams.stream().forEach(t -> {
      if (team2Set.contains(t.getTeamName())) {
      team2Set.remove(t.getTeamName());
      }
      });
      }

      if (team2Set.size() > 0) {
      team2Set.stream().forEach(teamName -> {
      Team team = new Team();
      team.setTeamName(teamName);
      teams.add(team);
      });
      }

      if(teams2.size()>0){
      teams2.stream().forEach(teamInSet2 -> {
      teams.stream().forEach(team -> {
      if (teamInSet2.getTeamName().equalsIgnoreCase(team.getTeamName()))
      team.setTotalMatches(team.getTotalMatches()+teamInSet2.getTotalMatches());
      });
      } );
      }

      SqlRowSet sqlRowMatchWinner = jdbcTemplate
      .queryForRowSet("SELECT match_winner,count(match_winner) FROM MATCH GROUP BY
      match_winner");

      while (sqlRowMatchWinner.next()) {
      String winner = sqlRowMatchWinner.getString(1);

      if (sqlRowMatchWinner.getString(2) != null)
      numberOfTimesWon = Long.parseLong(sqlRowMatchWinner.getString(2));

      teams.stream().forEach(t -> {
      if (t.getTeamName().equalsIgnoreCase(winner)) {
      t.setTotalWins(numberOfTimesWon);
      //t.setTotalMatches(t.getTotalMatches());
      }
      });
      }

      teams.stream().forEach(t -> System.out.println("id " + t.getId() + " team
      Name " + t.getTeamName()
      + " total matches = " + t.getTotalMatches() + " winner = " +
      t.getTotalWins()));
      teams.stream().forEach(team-> System.out.println(team.getTeamName() + " " +
      team.getTotalWins()));

      jdbcTemplate
      .query("SELECT team1,team2, date FROM MATCH",
      (rs,row) -> "Team 1 "+ rs.getString(1) + ", Team 2 " +rs.getString(2) + ",
      Date " +rs.getString(3)
      )
      .forEach(str -> System.out.println(str));*/
    }
  }
}
