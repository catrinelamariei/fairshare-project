package server.api;
import commons.DTOs.TransactionDTO;
import commons.Participant;
import commons.Tag;
import commons.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.Services.DTOtoEntity;
import server.database.TransactionRepository;

import java.util.UUID;


@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    private final TransactionRepository repo;
    private final DTOtoEntity d2e;

    public TransactionController(TransactionRepository repo, DTOtoEntity dtoToEntity) {
        this.repo = repo;
        this.d2e = dtoToEntity;
    }

    @PostMapping(path = {"" , "/"})
    public ResponseEntity<TransactionDTO> createTransaction(
            @RequestBody TransactionDTO ts) {
        if(ts == null || !ts.validate()) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(new TransactionDTO(d2e.create(ts)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransaction(@PathVariable("id") UUID id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(new TransactionDTO(repo.getReferenceById(id)));
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<TransactionDTO> updateTransaction(@PathVariable("id") UUID id,
                                                            @RequestBody TransactionDTO ts) {
        if(ts == null || !ts.validate()) return ResponseEntity.badRequest().build();
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        ts.id = id;
        return ResponseEntity.ok(new TransactionDTO(d2e.update(ts)));
    }

    //id is already included in transactionDTO
    @Transactional
    @PutMapping("")
    public ResponseEntity<TransactionDTO> updateTransaction(@RequestBody TransactionDTO ts) {
        return updateTransaction(ts.id, ts);
    }

    // TODO: manage dependencies
    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<TransactionDTO> deleteTransactionById(@PathVariable("id") UUID id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        Transaction transaction = repo.getReferenceById(id);

        for (Participant participant : transaction.getParticipants()) {
            participant.participatedTransactions.remove(transaction);
        }

        for (Tag tag : transaction.getTags()) {
            tag.transactions.remove(transaction);
        }
        // Delete the transaction
        repo.delete(transaction);
        return ResponseEntity.ok(new TransactionDTO(transaction));
    }
}