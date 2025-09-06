package com.xavelo.template.render.api.application.port.in;

import com.xavelo.template.render.api.domain.Authorization;

import java.util.List;

public interface ListAuthorizationsUseCase {
    List<Authorization> listAuthorizations();
}

