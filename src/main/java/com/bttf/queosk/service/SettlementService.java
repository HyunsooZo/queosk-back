package com.bttf.queosk.service;

import com.bttf.queosk.dto.SettlementDto;
import com.bttf.queosk.entity.Settlement;
import com.bttf.queosk.repository.OrderQueryQueryRepository;
import com.bttf.queosk.repository.SettlementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SettlementService {

    private final SettlementRepository settlementRepository;

    private final OrderQueryQueryRepository queryRepository;

    public SettlementDto todaySettlementGet(Long restaurantId) {

        List<SettlementDto.OrderdMenu> settlement = queryRepository.getTodaySales(restaurantId);

        Long sum = settlement.stream().mapToLong(SettlementDto.OrderdMenu::sumOfPrice).sum();


        return SettlementDto.of(settlement, sum);
    }

    public SettlementDto periodSettlementGet(Long restaurantId,
                                             LocalDateTime to,
                                             LocalDateTime from) {

        List<SettlementDto.OrderdMenu> settlement = queryRepository.getPeriodSales(restaurantId, to, from);

        Long sum = settlement.stream().mapToLong(SettlementDto.OrderdMenu::sumOfPrice).sum();


        return SettlementDto.of(settlement, sum);
    }

    public Long periodSettlementPriceGet(Long restaurantId,
                                         LocalDateTime to,
                                         LocalDateTime from) {
        List<Settlement> settlementList =
                settlementRepository.findSettlementsByRestaurantInDateRange(restaurantId, to, from);

        long totalPrice = settlementList.stream()
                .mapToLong(Settlement::getPrice)
                .sum();

        return totalPrice;
    }

}
