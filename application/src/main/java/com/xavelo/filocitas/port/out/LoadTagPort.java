package com.xavelo.filocitas.port.out;

import com.xavelo.filocitas.application.domain.Tag;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public interface LoadTagPort {

    Map<UUID, Tag> findAllByIds(Collection<UUID> ids);

    Map<String, Tag> findAllByNames(Collection<String> names);

}
