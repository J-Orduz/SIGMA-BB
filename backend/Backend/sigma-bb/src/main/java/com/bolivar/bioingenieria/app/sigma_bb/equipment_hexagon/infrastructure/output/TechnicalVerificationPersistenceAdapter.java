package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output;

import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.application.ports.output.TechnicalVerificationPersistencePort;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.technical_verification.TechnicalVerification;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.entities.TechnicalVerificationEntity;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.errors.TechnicalVerificationNotFoundException;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.mapper.TechnicalVerificationPersistenceMapper;
import com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.infrastructure.output.repository.SpringTechnicalVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class TechnicalVerificationPersistenceAdapter implements TechnicalVerificationPersistencePort {
    private final SpringTechnicalVerificationRepository repository;
    private final TechnicalVerificationPersistenceMapper mapper;

    @Autowired
    public TechnicalVerificationPersistenceAdapter(SpringTechnicalVerificationRepository repository,
                                                   TechnicalVerificationPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<TechnicalVerification> findAll() {
        return repository.findAll().stream().map(mapper::toTechnicalVerification).toList();
    }

    @Override
    public Optional<TechnicalVerification> findById(String id) {
        return repository.findById(UUID.fromString(id)).map(mapper::toTechnicalVerification);
    }

    @Override
    public TechnicalVerification save(TechnicalVerification tv) {
        return mapper.toTechnicalVerification(repository.save(mapper.toTechnicalVerificationEntity(tv)));
    }

    @Override
    public TechnicalVerification update(String id, TechnicalVerification tv) {
        TechnicalVerificationEntity existing = repository.findById(UUID.fromString(id))
                .orElseThrow(() -> new TechnicalVerificationNotFoundException(id));
        mapper.updateEntityFromDomain(tv, existing);
        return mapper.toTechnicalVerification(repository.save(existing));
    }

    @Override
    public void delete(String id) {
        UUID uuid = UUID.fromString(id);
        if (!repository.existsById(uuid)) throw new TechnicalVerificationNotFoundException(id);
        repository.deleteById(uuid);
    }

    @Override
    public List<TechnicalVerification> findAllById(List<String> ids) {
        List<UUID> uuidList = ids.stream()
                .map(UUID::fromString)
                .toList();
        return mapper.toTechnicalVerificationList(repository.findAllByIdIn(uuidList));
    }
}
