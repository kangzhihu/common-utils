//状态映射mapper，方便一个entity的A,B,C 状态映射到另外一个entity的  V,M状态
public class OrderStatusMapper<T> {

    private final ImmutableMap<T, OrderExecStatusEnum> statusMap;

    private OrderStatusMapper(Map<T, OrderExecStatusEnum> statusMap) {
        if (statusMap == null) {
            this.statusMap = ImmutableMap.of();
        } else {
            this.statusMap = ImmutableMap.copyOf(statusMap);
        }
    }

    public OrderExecStatusEnum mapTo(T status) {
        if (!statusMap.containsKey(status)) {
            return OrderExecStatusEnum.UNKNOWN;
        }

        return statusMap.get(status);
    }

    public static <T> Builder<T> newBuilder(Class<T> clz) {
        return new Builder<T>();
    }

    public static <T> Builder<T> newBuilder() {
        return new Builder<T>();
    }

    public final static class Builder<T> {

        private final Map<T, OrderExecStatusEnum> map = Maps.newHashMap();

        public Builder<T> addMapping(OrderExecStatusEnum orderExecStatus, T... statuses) {
            for (T status : statuses) {
                if (map.containsKey(status)) {
                    throw new IllegalArgumentException(
                            "Duplicate order status in OrderStatus=>ExecStatus's mapping");
                }

                map.put(status, orderExecStatus);
            }

            return this;
        }

        public OrderStatusMapper<T> build() {
            return new OrderStatusMapper<T>(map);
        }
    }
    
    public static void main(String[] args) {
        OrderStatusMapper<FinfundOrderStatusEnum> statusMapper = 
        OrderStatusMapper.<FinfundOrderStatusEnum>newBuilder()
                .addMapping(ToStatusEnum.PROGRESS, FromStatusEnum.ACCEPTED)
                .addMapping(ToStatusEnum.SUCCESS, FromStatusEnum.SUCCESS)
                .addMapping(ToStatusEnum.FAIL, FromStatusEnum.CLOSED,
                        FromStatusEnum.FAILURE, FromStatusEnum.CANCELED).build();
    }
    
    
}
