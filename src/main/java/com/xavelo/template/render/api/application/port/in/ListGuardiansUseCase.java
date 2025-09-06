package com.xavelo.template.render.api.application.port.in;

import com.xavelo.template.render.api.domain.Guardian;

import java.util.List;

public interface ListGuardiansUseCase {
    List<Guardian> listGuardians();
}
