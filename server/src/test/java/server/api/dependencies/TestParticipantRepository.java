package server.api.dependencies;

import commons.Participant;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.ParticipantRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public class TestParticipantRepository implements ParticipantRepository {

    public final List<Participant> participants = new ArrayList<>();
    public final List<String> calledMethods = new ArrayList<>();

    private void call(String name) {
        calledMethods.add(name);
    }

    private Optional<Participant> find(UUID id) {
        return participants.stream().filter(q -> q.id == id).findFirst();
    }

    @Override
    public void flush() {
    }

    @Override
    public <S extends Participant> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Participant> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Participant> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<UUID> uuids) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Participant getOne(UUID uuid) {
        return null;
    }

    @Override
    public Participant getById(UUID uuid){
        call("getById");
        return find(uuid).get();
    }

    @Override
    public Participant getReferenceById(UUID uuid) {
        call("getReferenceById");
        return find(uuid).get();
    }

    @Override
    public <S extends Participant> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Participant> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Participant> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Participant> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Participant> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Participant> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Participant,
            R> R findBy(Example<S> example,
                        Function<FluentQuery.FetchableFluentQuery<S>,
                                R> queryFunction) {
        return null;
    }


    @Override
    public <S extends Participant> S save(S entity) {
        call("save");
        participants.add(entity);
        return entity;
    }

    @Override
    public Optional<Participant> findById(UUID uuid) {
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
        participants.remove(find(uuid));
    }

    @Override
    public void delete(Participant entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends UUID> uuids) {

    }

    @Override
    public void deleteAll(Iterable<? extends Participant> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Participant> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Participant> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Participant> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public List<Participant> findAll() {
        return null;
    }

    @Override
    public List<Participant> findAllById(Iterable<UUID> uuids) {
        return null;
    }
}
