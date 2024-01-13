package com.teams.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teams.entity.models.*;
import com.teams.repository.*;
import org.elasticsearch.action.index.IndexRequest;
import com.teams.entity.*;
import com.teams.util.ElasticSearchUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.teams.entity.models.OrdersModel.FoodStatus.IN_PROGRESS;

@Service
@Slf4j
public class OrderService {

    @Autowired
    FoodItemsOrdersRepository foodItemsOrdersRepository;

    @Value("${elasticsearch.enabled:false}")
    private Boolean elasticEnabled;

    @Autowired
    FoodItemRepository foodItemRepository;

    @Autowired
    OrdersRepository ordersRepository;
    @Autowired
    ManagementUserRepository managementUserRepository;

    @Autowired
    RestHighLevelClient elasticSearchClient;
    @Autowired
    ElasticSearchUtil elasticSearchUtil;
    @Autowired
    TableRepository tableRepository;

    public ResponseMessage saveOrderDetails(OrdersModel ordersModel) {
        UUID uuid = UUID.randomUUID();
        Orders order = new Orders();
        try{
            order.setOrderId(uuid);
            log.info("Added sub-user details to the order");
            ordersModel.getFoodItemOrderModels().stream().forEach(foodItemOrderModel -> {
                FoodItem foodItem = foodItemRepository.findById(foodItemOrderModel.getFoodItemId()).get();
                order.getFoodItemSet().add(foodItem);
                foodItem.getOrderSet().add(order);
            });

            DinningTable dinningTable = tableRepository.findById(ordersModel.getTableId()).get();
            order.setDinningTable(dinningTable);
            ordersRepository.save(order);
            log.info("Updating all the quantity values into the table");
            List<FoodItemOrders> foodItemOrders = updateQuantityInFoodItemOrders(ordersModel, uuid);

            if(elasticEnabled){
                saveOrUpdateOrderDetailsIntoElastic(order,foodItemOrders);
            }
            log.info("Saved the order details for OrderId {}",uuid);

        } catch (Exception e){
            log.error("Error occurred while saving the order details {}",uuid);
            throw new RuntimeException("Error occurred while saving the order details");
        }
        return new ResponseMessage("Order saved !!!");
    }

    public Orders updateOrderDetails(OrdersModel ordersModel){
        try{
            Optional<Orders> ordersOptional = ordersRepository.findById(ordersModel.getOrderId());
            if(ordersOptional.isPresent()){
                Orders orders = ordersOptional.get();
//                if(orders.getSubUser()!= null && !orders.getSubUser().getSubUserId().toString().equalsIgnoreCase(ordersModel.getSubUserId())){
//                    SubUser subUser = managementUserRepository.findSubUserBySubUserId(UUID.fromString(ordersModel.getSubUserId()));
//                    orders.setSubUser(subUser);
//                    log.info("Added sub-user details to the order with sub-userId {}",subUser.getSubUserId());
//                }
//                if(!orders.getFoodItemSet().isEmpty()){
//                    Set<FoodItem> foodItemSet = orders.getFoodItemSet();
//                    ordersModel.getOrderDetails().entrySet().stream().forEach(map ->{
//                        FoodItem foodItem = foodItemRepository.findById(map.getKey()).get();
//                        Optional<FoodItem> savedFoodItem = foodItemSet.stream()
//                                .filter(foodItem1 -> foodItem1.getFoodItemId() == foodItem.getFoodItemId())
//                                .findFirst();
//                        if(!savedFoodItem.isPresent()){
//                            orders.getFoodItemSet().add(foodItem);
//                            foodItem.getOrderSet().add(orders);
//                        }
//                    });
//                }
                ordersRepository.save(orders);
                log.info("Updating all the quantity values into the table");
                List<FoodItemOrders> foodItemOrders = updateQuantityInFoodItemOrders(ordersModel, ordersModel.getOrderId());
                if(elasticEnabled){
                    saveOrUpdateOrderDetailsIntoElastic(orders,foodItemOrders);
                }
                log.info("Updated the order details for OrderId {}",orders.getOrderId());
                return orders;
            }
        } catch (Exception e){
            log.error("Error occurred while updating the order details {}",e.getMessage());
            throw new RuntimeException("Error occurred while saving the order details");
        }
        return null;
    }
    private void saveOrUpdateOrderDetailsIntoElastic(Orders order,List<FoodItemOrders> foodItemOrdersList) throws IOException {
        try{
            ElasticOrderModel elasticOrderModel;
            String orderId = getElasticOrderId(order);
            String indexName = getOrderIndexName();
            IndexRequest indexRequest = new IndexRequest();
            indexRequest.id(getElasticOrderId(order));
            indexRequest.index(indexName);

            if(elasticSearchUtil.isIdExists(orderId,indexName)){
                Map<String, Object> orderMap = elasticSearchUtil.getDocumentById(orderId, indexName);
                elasticOrderModel = convertMapObjectIntoElasticOrderModel(orderMap);
//                elasticOrderModel.setSubUser(order.getSubUser().getSubUserId());
                elasticOrderModel.setFoodItemList(order.getFoodItemSet()
                                .stream()
                                .map(foodItem -> setFoodItem(foodItem,foodItemOrdersList))
                                .collect(Collectors.toList()));
            }
            else {
                elasticOrderModel = ElasticOrderModel.builder()
                        .orderId(order.getOrderId())
//                        .subUser(order.getSubUser().getSubUserId())
                        .foodItemList(order.getFoodItemSet()
                                .stream()
                                .map(foodItem -> setFoodItem(foodItem,foodItemOrdersList))
                                .collect(Collectors.toList())).build();
            }
            // Convert Java object to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(elasticOrderModel);

            indexRequest.source(jsonString, XContentType.JSON);
            IndexResponse index = elasticSearchClient.index(indexRequest, RequestOptions.DEFAULT);
            log.info("Added data to elastic with id {}",index.getId());
        } catch (Exception e){
            log.error("Error occurred saving order details into elastic for orderId {}",order.getOrderId());
            throw new RuntimeException("Error occurred saving order details into elastic",e);
        }
    }

    public FoodItemModel setFoodItem(FoodItem foodItem, List<FoodItemOrders> foodItemOrdersList){
        FoodItemModel foodItemModel = new FoodItemModel();
        foodItemModel.setFoodItemName(foodItem.getName());
        foodItemModel.setFoodItemId(foodItem.getFoodItemId());
        foodItemModel.setFoodItemPrice(foodItem.getPrice());
        foodItemModel.setCategoryId(foodItem.getCategory().getCategoryId());
        foodItemModel.setQuantity(foodItemOrdersList.stream()
                .filter(foodItemOrder -> foodItemOrder.getFoodItemId() == foodItem.getFoodItemId())
                .findFirst().get().getQuantity());
        return foodItemModel;
    }

    private ElasticOrderModel convertMapObjectIntoElasticOrderModel(Map<String, Object> orderMap) {
        ObjectMapper objectMapper = new ObjectMapper();
        ElasticOrderModel elasticOrderModel = objectMapper.convertValue(orderMap, ElasticOrderModel.class);
        return elasticOrderModel;
    }

    private List<FoodItemOrders> updateQuantityInFoodItemOrders(OrdersModel ordersModel, UUID uuid) {
        List<FoodItemOrders> foodItemOrdersList = ordersModel.getFoodItemOrderModels().stream().map(foodItemOrderModel -> {
            FoodItemsOrderPrimaryKey foodItemsOrderPrimaryKey = new FoodItemsOrderPrimaryKey();
            foodItemsOrderPrimaryKey.setOrderId(uuid);
            foodItemsOrderPrimaryKey.setFoodItemId(foodItemOrderModel.getFoodItemId());
            FoodItemOrders foodItemOrders = foodItemsOrdersRepository.findById(foodItemsOrderPrimaryKey).get();
            SubUser subUser = managementUserRepository.findById(foodItemOrderModel.getCookId()).get();
            foodItemOrders.setSubUser(subUser);
            foodItemOrders.setQuantity(foodItemOrderModel.getQuantity());
            foodItemOrders.setStatus(IN_PROGRESS.toString());
            return foodItemsOrdersRepository.save(foodItemOrders);
        }).collect(Collectors.toList());
        return foodItemOrdersList;
    }

    public String getOrderIndexName(){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        return "restaurants_orders_"+month+"-"+year;
    }

    public String getElasticOrderId(Orders order) {
        //add customerId here
        return "restaurant_order_"+order.getOrderId();
    }

    public Object getOrder(UUID orderId) {
        try {
            Orders order = ordersRepository.findById(orderId).get();
            List<FoodItemOrders> foodItemOrders = foodItemsOrdersRepository.findByOrderId(orderId);
            OrderModel orderModel = new OrderModel();
            orderModel.setSubUserId(order.getDinningTable().getSubUser().getSubUserId());
            orderModel.setTableId(order.getDinningTable().getTableId());
            orderModel.setOrderId(orderId);
            List<OrderModel.FoodItemsOrderModel> foodItemsOrderModelList = foodItemOrders.stream()
                    .map(foodItemOrders1 -> {
                        OrderModel.FoodItemsOrderModel foodItemsOrderModel = new OrderModel.FoodItemsOrderModel();
                        foodItemsOrderModel.setFoodItemId(foodItemOrders1.getFoodItemId());
                        foodItemsOrderModel.setQuantity(foodItemOrders1.getQuantity());
                        return foodItemsOrderModel;
                    }).collect(Collectors.toList());
            orderModel.setFoodItemsOrderModelList(foodItemsOrderModelList);
            return orderModel;
        } catch (Exception e) {
            log.error("Error occurred saving fetching order details for order id {}",orderId);
            throw new RuntimeException("Error occurred while featcing order id",e);
        }
    }
}
//https://stackoverflow.com/questions/39185977/failed-to-convert-request-element-in-entity-with-idclass
//https://stackoverflow.com/questions/4179166/hibernate-how-to-fix-identifier-of-an-instance-altered-from-x-to-y