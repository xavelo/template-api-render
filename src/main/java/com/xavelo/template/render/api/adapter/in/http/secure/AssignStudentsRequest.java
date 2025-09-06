package com.xavelo.template.render.api.adapter.in.http.secure;

import java.util.List;
import java.util.UUID;

public record AssignStudentsRequest(List<UUID> studentIds) {}

