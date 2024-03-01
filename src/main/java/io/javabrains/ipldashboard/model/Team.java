package io.javabrains.ipldashboard.model;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;

@Entity
public class Team {
    
    @Id
    private long id;

    private String teamName;
    private long totalWins;
    private long totalMatches;
    
    public long getId() {
        return id;
    }
    public Team(String teamName, long totalMatches) {
        this.teamName = teamName;
        this.totalMatches = totalMatches;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getTeamName() {
        return teamName;
    }
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    public long getTotalWins() {
        return totalWins;
    }
    public void setTotalWins(long totalWins) {
        this.totalWins = totalWins;
    }
    public long getTotalMatches() {
        return totalMatches;
    }
    public void setTotalMatches(long totalMatches) {
        this.totalMatches = totalMatches;
    }

    

}
