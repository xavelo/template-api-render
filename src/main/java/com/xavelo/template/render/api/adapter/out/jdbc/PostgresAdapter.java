package com.xavelo.template.render.api.adapter.out.jdbc;

import com.xavelo.template.render.api.application.port.out.CreateAuthorizationPort;
import com.xavelo.template.render.api.application.port.out.CreateGuardianPort;
import com.xavelo.template.render.api.application.port.out.CreateStudentPort;
import com.xavelo.template.render.api.application.port.out.CreateUserPort;
import com.xavelo.template.render.api.application.port.out.GetGuardianPort;
import com.xavelo.template.render.api.application.port.out.GetUserPort;
import com.xavelo.template.render.api.application.port.out.ListUsersPort;
import com.xavelo.template.render.api.domain.Authorization;
import com.xavelo.template.render.api.domain.Student;
import com.xavelo.template.render.api.domain.User;
import com.xavelo.template.render.api.domain.Guardian;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class PostgresAdapter implements ListUsersPort, GetUserPort, CreateUserPort, CreateAuthorizationPort, CreateStudentPort, CreateGuardianPort, GetGuardianPort {

    private static final Logger logger = LoggerFactory.getLogger(PostgresAdapter.class);

    private final UserRepository userRepository;
    private final AuthorizationRepository authorizationRepository;
    private final StudentRepository studentRepository;
    private final GuardianRepository guardianRepository;

    public PostgresAdapter(UserRepository userRepository,
                           AuthorizationRepository authorizationRepository,
                           StudentRepository studentRepository,
                           GuardianRepository guardianRepository) {
        this.userRepository = userRepository;
        this.authorizationRepository = authorizationRepository;
        this.studentRepository = studentRepository;
        this.guardianRepository = guardianRepository;
    }

    @Override
    public List<User> listUsers() {
        logger.debug("postgress query...");

        return userRepository.findAll().stream()
                .map(user -> new User(user.getId(), user.getName()))
                .toList();
    }

    @Override
    public User createUser(User user) {
        logger.debug("postgress insert...");

        com.xavelo.template.render.api.adapter.out.jdbc.User userEntity =
                new com.xavelo.template.render.api.adapter.out.jdbc.User();
        userEntity.setName(user.name());

        com.xavelo.template.render.api.adapter.out.jdbc.User savedUser = userRepository.save(userEntity);

        return new User(savedUser.getId(), savedUser.getName());
    }

    @Override
    public Authorization createAuthorization(Authorization authorization) {
        logger.debug("postgress insert authorization...");

        com.xavelo.template.render.api.adapter.out.jdbc.Authorization entity =
                new com.xavelo.template.render.api.adapter.out.jdbc.Authorization();
        entity.setTitle(authorization.title());
        entity.setText(authorization.text());
        entity.setStatus(authorization.status());
        entity.setCreatedBy(authorization.createdBy());
        entity.setSentAt(authorization.sentAt());
        entity.setSentBy(authorization.sentBy());
        entity.setApprovedAt(authorization.approvedAt());
        entity.setApprovedBy(authorization.approvedBy());

        com.xavelo.template.render.api.adapter.out.jdbc.Authorization saved = authorizationRepository.save(entity);

        return new Authorization(saved.getId(), saved.getTitle(), saved.getText(), saved.getStatus(), saved.getCreatedAt(),
                saved.getCreatedBy(), saved.getSentAt(), saved.getSentBy(), saved.getApprovedAt(), saved.getApprovedBy());
    }

    @Override
    public Student createStudent(Student student) {
        logger.debug("postgress insert student...");

        com.xavelo.template.render.api.adapter.out.jdbc.Student entity =
                new com.xavelo.template.render.api.adapter.out.jdbc.Student();
        entity.setName(student.name());

        if (student.guardianIds() != null && !student.guardianIds().isEmpty()) {
            List<com.xavelo.template.render.api.adapter.out.jdbc.Guardian> guardians =
                    guardianRepository.findAllById(student.guardianIds());
            entity.setGuardians(new HashSet<>(guardians));
        }

        com.xavelo.template.render.api.adapter.out.jdbc.Student saved = studentRepository.save(entity);

        List<UUID> guardianIds = saved.getGuardians().stream()
                .map(com.xavelo.template.render.api.adapter.out.jdbc.Guardian::getId)
                .toList();
        return new Student(saved.getId(), saved.getName(), guardianIds);
    }

    @Override
    public Guardian createGuardian(Guardian guardian) {
        logger.debug("postgress insert guardian...");

        com.xavelo.template.render.api.adapter.out.jdbc.Guardian entity =
                new com.xavelo.template.render.api.adapter.out.jdbc.Guardian();
        entity.setName(guardian.name());

        com.xavelo.template.render.api.adapter.out.jdbc.Guardian saved = guardianRepository.save(entity);

        return new Guardian(saved.getId(), saved.getName());
    }

    public Optional<User> getUser(UUID id) {
        return userRepository.findById(id)
                .map(user -> new User(user.getId(), user.getName()));
    }

    @Override
    public Optional<Guardian> getGuardian(UUID id) {
        return guardianRepository.findById(id)
                .map(g -> new Guardian(g.getId(), g.getName()));
    }

}
