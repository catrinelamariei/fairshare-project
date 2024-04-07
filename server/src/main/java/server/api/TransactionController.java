package server.api;
import commons.DTOs.TransactionDTO;
import commons.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import server.Services.DTOtoEntity;
import server.database.TransactionRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;


@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    private final TransactionRepository repo;
    private final DTOtoEntity d2e;
    private Map<Object, Consumer<TransactionDTO>> listeners = new HashMap<>();
    private Map<Object, Consumer<UUID>> deletionListeners = new ConcurrentHashMap<>();



    public TransactionController(TransactionRepository repo, DTOtoEntity dtoToEntity) {
        this.repo = repo;
        this.d2e = dtoToEntity;
    }

    @PostMapping(path = {"" , "/"})
    public ResponseEntity<TransactionDTO> createTransaction(
            @RequestBody TransactionDTO ts) {
        if(ts == null || !ts.validate()) return ResponseEntity.badRequest().build();

        TransactionDTO result = new TransactionDTO(d2e.create(ts));
        listeners.forEach((k, l)->l.accept(result));

        return ResponseEntity.ok(result);
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
        if (id == null) return ResponseEntity.badRequest().build();
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        Transaction transaction = repo.getReferenceById(id);
        repo.delete(transaction);
        deletionListeners.values().forEach(listener -> listener.accept(id));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/updates")
    public DeferredResult<ResponseEntity<TransactionDTO>> getUpdates() {
        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        var res = new DeferredResult<ResponseEntity<TransactionDTO>>(5000L, noContent);
        var key = new Object();
        listeners.put(key, t ->{
            res.setResult(ResponseEntity.ok(t));
        });
        res.onCompletion(()->{
            listeners.remove(key);
        });
        return res;
    }

    @GetMapping("/deletion/updates")
    public DeferredResult<ResponseEntity<UUID>> registerForTransactionDeletionUpdates() {
        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        var deferredResult = new DeferredResult<ResponseEntity<UUID>>(5000L, noContent);
        var key = new Object();

        // Register a listener for deletion updates
        deletionListeners.put(key, deletedTransactionId -> {
            deferredResult.setResult(ResponseEntity.ok(deletedTransactionId));
        });

        // Remove the listener when the long polling request is completed
        deferredResult.onCompletion(() -> {
            deletionListeners.remove(key);
        });

        return deferredResult;
    }

}