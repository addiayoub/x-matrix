package com.xmatrix.backend.service;

import com.xmatrix.backend.DTOs.TeamRequestDTO;
import com.xmatrix.backend.DTOs.TeamResponseDTO;
import com.xmatrix.backend.entity.Team;
import com.xmatrix.backend.mappers.TeamMapper;
import com.xmatrix.backend.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService {
    @Autowired
    private TeamRepository teamRepository;

    public List<TeamResponseDTO> getAllTeams() {
        List<Team> teams = teamRepository.findAllActiveAndOrderByCreatedAtDesc();
        return teams.stream().map(TeamMapper.INSTANCE::toDto).toList();
    }

    public Optional<TeamResponseDTO> getTeamById(Long id) {
        return teamRepository.findById(id)
                .filter(team -> !team.getDeleted())
                .map(TeamMapper.INSTANCE::toDto);
    }

    public TeamResponseDTO createTeam(TeamRequestDTO requestDTO) {
        Team team = TeamMapper.INSTANCE.toEntity(requestDTO);
        team.setDeleted(false); // Ensure new teams are active
        team = teamRepository.save(team);
        return TeamMapper.INSTANCE.toDto(team);
    }

    public TeamResponseDTO updateTeam(Long id, TeamRequestDTO requestDTO) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        team.setName(requestDTO.getName());
        team = teamRepository.save(team);
        return TeamMapper.INSTANCE.toDto(team);
    }

    public void softDeleteTeam(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        team.setDeleted(true);
        teamRepository.save(team);
    }

    public void restoreTeam(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        team.setDeleted(false);
        teamRepository.save(team);
    }
}
