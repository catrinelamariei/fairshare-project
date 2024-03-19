package server.api;
import commons.DTOs.TransactionDTO;
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
            @RequestBody TransactionDTO transactionDTO) {
        if(transactionDTO == null){ // TODO: better validate
            return ResponseEntity.badRequest().build();
        }
        Transaction transaction = d2e.create(transactionDTO);

        return ResponseEntity.ok(new TransactionDTO(transaction));
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
            @RequestBody TransactionDTO transaction) {
        //validation
        if(transaction == null || transaction.id == null
                || transaction.getAmount() == null
                || transaction.getCurrencyCode() == null
                || transaction.getDate() == null
                || transaction.subject == null
                || transaction.subject.isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        //updating
        Transaction ts = d2e.update(transaction);

        //finalising
        return ResponseEntity.ok(new TransactionDTO(ts));
    }

    //id is already included in transactionDTO
    @Transactional
    @PutMapping("")
    public ResponseEntity<TransactionDTO> updateTransaction(@RequestBody TransactionDTO ts) {
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