package com.xavelo.template.render.api.adapter.in.http.secure;

public record CreateAuthorizationRequest(
        String title,
        String text,
        String status,
        String createdBy,
        String sentBy,
        String approvedBy
) {}
