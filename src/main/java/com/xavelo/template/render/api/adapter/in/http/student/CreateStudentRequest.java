package com.xavelo.template.render.api.adapter.in.http.student;

import java.util.List;
import java.util.UUID;

public record CreateStudentRequest(String name, List<UUID> guardianIds) {}
