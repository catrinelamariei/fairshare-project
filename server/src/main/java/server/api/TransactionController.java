package server.api;
import commons.DTOs.TransactionDTO;
import commons.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.TransactionRepository;
import java.math.BigDecimal;
import java.util.UUID;
@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    private final TransactionRepository repo;

    public TransactionController(TransactionRepository repo) {
        this.repo = repo;
    }

    @PostMapping(path = {"" , "/"})
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        if(transaction==null ||transaction.getAmount()==null
                || transaction.getAmount().compareTo(BigDecimal.ZERO)==0){
            return ResponseEntity.badRequest().build();
        }
        repo.save(transaction);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable("id") UUID id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new TransactionDTO(repo.findById(id).get()));
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<TransactionDTO> updateById(@PathVariable("id") UUID id, @RequestBody Transaction transaction) {
        if(transaction==null ||transaction.getAmount()==null
                || transaction.getAmount().compareTo(BigDecimal.ZERO)==0){
            return ResponseEntity.badRequest().build();
        }
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        Transaction t = repo.findById(id).get();
        t.setAmount(transaction.getAmount());
        repo.save(t);
        return ResponseEntity.ok(new TransactionDTO(repo.findById(id).get()));
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable("id") UUID id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        repo.deleteById(id);
        return ResponseEntity.ok().build();
    }
}