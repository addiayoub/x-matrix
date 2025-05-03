package com.xmatrix.backend.web;

import com.xmatrix.backend.DTOs.TeamRequestDTO;
import com.xmatrix.backend.DTOs.TeamResponseDTO;
import com.xmatrix.backend.service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/teams")
public class TeamController {
    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping
    public List<TeamResponseDTO> getAllTeams() {
        return teamService.getAllTeams();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamResponseDTO> getTeamById(@PathVariable Long id) {
        Optional<TeamResponseDTO> team = teamService.getTeamById(id);
        return team.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public TeamResponseDTO createTeam(@RequestBody TeamRequestDTO requestDTO) {
        return teamService.createTeam(requestDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamResponseDTO> updateTeam(@PathVariable Long id, @RequestBody TeamRequestDTO requestDTO) {
        return ResponseEntity.ok(teamService.updateTeam(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteTeam(@PathVariable Long id) {
        teamService.softDeleteTeam(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<Void> restoreTeam(@PathVariable Long id) {
        teamService.restoreTeam(id);
        return ResponseEntity.noContent().build();
    }
}
