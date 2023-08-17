package helper

import (
	"tenflix/lintang/order-aggregator-service/internal/entity"
	"tenflix/lintang/order-aggregator-service/pb"
)

func PlanDtoProtoToPlan(planDto *pb.PlanDto) entity.Plan {
	return entity.Plan{
		PlanId:        int32(planDto.PlanId),
		Name:          planDto.Name,
		Price:         int32(planDto.Price),
		Description:   planDto.Description,
		ActivePeriod:  int32(planDto.ActivePeriod),
		DiscountPrice: int32(planDto.DiscountPrice),
	}
}
