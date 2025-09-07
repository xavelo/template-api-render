package com.xavelo.template.render.api.adapter.in.http.guardian;

import jakarta.validation.constraints.NotBlank;

public record UpdateGuardianEmailRequest(@NotBlank String email) {}

