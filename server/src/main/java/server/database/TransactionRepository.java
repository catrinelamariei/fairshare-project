package server.database;


import org.springframework.data.jpa.repository.JpaRepository;

import commons.Transaction;
public interface TransactionRepository extends JpaRepository<Transaction, Long> {}