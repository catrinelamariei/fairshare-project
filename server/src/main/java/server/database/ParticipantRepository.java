package server.database;

import org.springframework.data.jpa.repository.JpaRepository;
import commons.Participant;

import java.util.UUID;

public interface ParticipantRepository extends JpaRepository<Participant, UUID> {}
