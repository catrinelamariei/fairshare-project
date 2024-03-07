package server.api;

import commons.Transaction;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.TransactionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public class TestTransactionRepository implements TransactionRepository {

    public final List<Transaction> transactions = new ArrayList<>();
    public final List<String> calledMethods = new ArrayList<>();

    private void call(String name) {
        calledMethods.add(name);
    }
    private Optional<Transaction> find(UUID id) {
        return transactions.stream().filter(q -> q.id == id).findFirst();
    }
    @Override
    public void flush() {

    }

    @Override
    public <S extends Transaction> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Transaction> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Transaction> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<UUID> uuids) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Transaction getOne(UUID uuid) {
        return null;
    }

    @Override
    public Transaction getById(UUID uuid) {
        return null;
    }

    @Override
    public Transaction getReferenceById(UUID uuid) {
        return null;
    }

    @Override
    public <S extends Transaction> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Transaction> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Transaction> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public List<Transaction> findAll() {
        return null;
    }

    @Override
    public List<Transaction> findAllById(Iterable<UUID> uuids) {
        return null;
    }

    @Override
    public <S extends Transaction> S save(S entity) {
        call("save");
        transactions.add(entity);
        return entity;
    }

    @Override
    public Optional<Transaction> findById(UUID uuid) {
        call("findById");
        return find(uuid);
    }

    @Override
    public boolean existsById(UUID uuid) {
        call("existById");
        return find(uuid).isPresent();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(UUID uuid) {
        call("deleteById");
        transactions.remove(find(uuid));
    }

    @Override
    public void delete(Transaction entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends UUID> uuids) {

    }

    @Override
    public void deleteAll(Iterable<? extends Transaction> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Transaction> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Transaction> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Transaction> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Transaction> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Transaction> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Transaction> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Transaction, R> R findBy(Example<S> example,
                                               Function<FluentQuery.FetchableFluentQuery<S>,
                                               R> queryFunction) {
        return null;
    }
}