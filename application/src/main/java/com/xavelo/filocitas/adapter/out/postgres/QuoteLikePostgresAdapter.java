package com.xavelo.filocitas.adapter.out.postgres;

import com.xavelo.filocitas.adapter.out.postgres.repository.QuoteLikeRepository;
import com.xavelo.filocitas.adapter.out.postgres.repository.entity.QuoteLikeEntity;
import com.xavelo.filocitas.port.out.IncrementQuoteLikePort;
import com.xavelo.filocitas.port.out.LoadQuoteLikePort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public class QuoteLikePostgresAdapter implements IncrementQuoteLikePort, LoadQuoteLikePort {

    private final QuoteLikeRepository quoteLikeRepository;

    public QuoteLikePostgresAdapter(QuoteLikeRepository quoteLikeRepository) {
        this.quoteLikeRepository = quoteLikeRepository;
    }

    @Override
    @Transactional
    public long incrementQuoteLike(UUID quoteId) {
        return quoteLikeRepository.findById(quoteId)
                .map(entity -> {
                    entity.setLikes(entity.getLikes() + 1);
                    return quoteLikeRepository.save(entity).getLikes();
                })
                .orElseGet(() -> quoteLikeRepository.save(QuoteLikeEntity.newInstance(quoteId, 1L)).getLikes());
    }

    @Override
    @Transactional(readOnly = true)
    public long getQuoteLikes(UUID quoteId) {
        return quoteLikeRepository.findById(quoteId)
                .map(QuoteLikeEntity::getLikes)
                .orElse(0L);
    }
}
