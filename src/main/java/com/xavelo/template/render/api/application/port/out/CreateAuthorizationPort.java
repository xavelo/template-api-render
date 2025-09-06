package com.xavelo.template.render.api.application.port.out;

import com.xavelo.template.render.api.domain.Authorization;

public interface CreateAuthorizationPort {
    Authorization createAuthorization(Authorization authorization);
}
