package server.database;


import org.springframework.data.jpa.repository.JpaRepository;

import commons.Transaction;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

}