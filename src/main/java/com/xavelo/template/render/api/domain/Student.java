package com.xavelo.template.render.api.domain;

import java.util.List;
import java.util.UUID;

public record Student(UUID id, String name, List<UUID> guardianIds) {}
