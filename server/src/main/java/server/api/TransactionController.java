package server.api;
import commons.DTOs.TransactionDTO;
import commons.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;
import server.database.ParticipantRepository;
import server.database.TagRepository;
import server.database.TransactionRepository;

import java.util.UUID;

import static commons.Transaction.validate;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    private final TransactionRepository repo;
    private final EventRepository eventRepository;
    private final TagRepository tagRepository;
    private final ParticipantRepository participantRepository;

    public TransactionController(TransactionRepository repo, EventRepository eventRepository,
                                 TagRepository tagRepository,
                                 ParticipantRepository participantRepository) {
        this.repo = repo;
        this.eventRepository = eventRepository;
        this.tagRepository = tagRepository;
        this.participantRepository = participantRepository;
    }

    @PostMapping(path = {"" , "/"})
    public ResponseEntity<TransactionDTO> createTransaction(
            @RequestBody TransactionDTO transactionDTO) {
        if(transactionDTO == null){ // TODO: better validate
            return ResponseEntity.badRequest().build();
        }
        //create transaction entity
        Transaction transaction = new Transaction(transactionDTO);
        transaction.event = eventRepository.getReferenceById(transactionDTO.eventId);
        transaction.author = participantRepository.getReferenceById(transactionDTO.author.id);
        transaction.participants.addAll(
            transactionDTO.participants.stream()
                .map(p -> participantRepository.getReferenceById(p.id)).toList());
        transaction.tags.addAll(
            transactionDTO.tags.stream()
                .map(t -> tagRepository.getReferenceById(t.id)).toList());
        transaction = repo.save(transaction); //idk if these extra safes are actually necessary

        //update event
        transaction.event.addTransaction(transaction);
        eventRepository.save(transaction.event); //idk if these extra safes are actually necessary

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
                                                            @RequestBody Transaction transaction) {
        //validation
        if(!validate(transaction)){
            return ResponseEntity.badRequest().build();
        }
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        //updating
        Transaction t = repo.findById(id).get();
        t.setDate(transaction.getDate());
        t.setCurrencyCode(transaction.getCurrencyCode());
        t.setAmount(transaction.getAmount());
        t.subject = transaction.subject;
        repo.save(t);

        //finalising
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