package com.xavelo.filocitas.port.out;

import com.xavelo.filocitas.application.domain.Tag;

public interface SaveTagPort {

    Tag saveTag(String name);
}
