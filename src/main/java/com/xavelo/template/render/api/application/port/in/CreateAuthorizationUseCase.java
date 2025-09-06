package com.xavelo.template.render.api.application.port.in;

import com.xavelo.template.render.api.domain.Authorization;

public interface CreateAuthorizationUseCase {
    Authorization createAuthorization(String title, String text, String status, String createdBy, String sentBy, String approvedBy);
}
