package server.api;
import commons.DTOs.TransactionDTO;
import commons.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.TransactionRepository;

import java.util.UUID;

import static commons.Transaction.validate;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    private final TransactionRepository repo;

    public TransactionController(TransactionRepository repo) {
        this.repo = repo;
    }

    @PostMapping(path = {"" , "/"})
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        if(!validate(transaction)){
            return ResponseEntity.badRequest().build();
        }
        repo.save(transaction);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable("id") UUID id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new TransactionDTO(repo.findById(id).get()));
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<TransactionDTO> updateTransactionById(@PathVariable("id") UUID id,
                                                            @RequestBody Transaction transaction) {
        if(!validate(transaction)){
            return ResponseEntity.badRequest().build();
        }
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Transaction t = repo.findById(id).get();
        t.setDate(transaction.getDate());
        t.setCurrencyCode(transaction.getCurrencyCode());
        t.setAmount(transaction.getAmount());
        repo.save(t);
        return ResponseEntity.ok(new TransactionDTO(repo.findById(id).get()));
    }

    //id is already included in transactionDTO
    @Transactional
    @PutMapping("")
    public ResponseEntity<TransactionDTO> updateTransaction(@RequestBody Transaction ts) {
        return updateTransactionById(ts.id, ts);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<TransactionDTO> deleteTransactionById(@PathVariable("id") UUID id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        TransactionDTO out = new TransactionDTO(repo.findById(id).get());
        repo.deleteById(id);
        return ResponseEntity.ok(out);
    }
}