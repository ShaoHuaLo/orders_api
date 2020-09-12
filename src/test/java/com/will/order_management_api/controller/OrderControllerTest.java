package com.will.order_management_api.controller;

import com.google.common.collect.ImmutableMap;
import com.will.order_management_api.dto.JsonDto;
import com.will.order_management_api.dto.Response;
import com.will.order_management_api.entities.Order;
import com.will.order_management_api.entities.Product;
import com.will.order_management_api.exception.InvalidDateFormatException;
import com.will.order_management_api.exception.InvalidItemNameException;
import com.will.order_management_api.exception.IdNotFoundException;
import com.will.order_management_api.service.OrderService;
import com.will.order_management_api.service.ProductService;
import com.will.order_management_api.util.HelperMethods;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit test for {@OrderController}
 */
public class OrderControllerTest {

    @InjectMocks
    private OrderController testClass;

    @Mock
    private OrderService orderService;

    @Mock
    private ProductService productService;

    @Mock
    private JsonDto jsonDto;

    @Mock
    private Response response;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
//        testClass = new OrderController(orderService, productService, jsonDto);
    }

    @SneakyThrows
    @Test
    public void testNewOrder_success() {

        final String inputDateString = "2021-10-10";
        final Date inputDate = HelperMethods.StringToDate(inputDateString).get();
        final Map<String, Integer> purchasedItems = ImmutableMap.of("Apple", 2,
                "Banana", 3,
                "Lemon", 1);

        when(productService.getByName("Apple")).thenReturn(Optional.of(Product.builder()
                .id(1L)
                .name("apple")
                .price(10)
                .build()));
        when(productService.getByName("Banana")).thenReturn(Optional.of(Product.builder()
                .id(2L)
                .name("banana")
                .price(3)
                .build()));
        when(productService.getByName("Lemon")).thenReturn(Optional.of(Product.builder()
                .id(3L)
                .name("lemon")
                .price(6)
                .build()));
        final Order expectedOrder = Order.builder()
                .date(inputDate)
                .subtotal(35)
                .items(purchasedItems)
                .build();
//        expectedOrder.setItems(purchasedItems);
        final Order actualOrder = testClass.newOrder(inputDate, purchasedItems);
        assertThat(actualOrder).isEqualTo(expectedOrder);
    }
    @Test
    @SneakyThrows
    public void testNewOrder_invalidItem() {
        final String inputDateString = "2021-10-10";
        final Optional<Date> inputDate = HelperMethods.StringToDate(inputDateString);
        final Map<String, Integer> invalidItems = ImmutableMap.of("pineapple", 3);

        when(productService.getByName("pineapple")).thenReturn(Optional.empty());

        try {
            testClass.newOrder(inputDate.get(), invalidItems);
        } catch (final InvalidItemNameException e) {
            assertThat(e).hasMessageContaining("there is no such item: ");
        }

    }

    @Test
    public void testCreateOrder_success() {
        final ImmutableMap<String, Integer> purchasedItems = ImmutableMap.of("apple", 1, "banana", 2, "lemon", 3);

        final JsonDto dto = JsonDto.builder()
                .id(1)
                .items(purchasedItems)
                .dateString("2020-12-31")
                .build();


        when(productService.getByName("apple")).thenReturn(Optional.of(Product.builder()
                .name("apple")
                .price(2)
                .build()));
        when(productService.getByName("banana")).thenReturn(Optional.of(Product.builder()
                .name("banana")
                .price(3)
                .build()));
        when(productService.getByName("lemon")).thenReturn(Optional.of(Product.builder()
                .name("lemon")
                .price(1)
                .build()));

        final Order expected = Order.builder()
                .date(HelperMethods.StringToDate("2020-12-31").get())
                .subtotal(11)
                .items(purchasedItems).build();

        when(orderService.post(expected)).thenReturn(expected);

        final Order actual = testClass.createNewOrder(dto);

        assertThat(actual).isEqualTo(expected);
    }


    @Test
    public void testGetAll_success() {
        testClass.getAll();
        verify(orderService, times(1)).getAll();
    }


    @Test
    public void testGetById_success() {
        Optional<Date> optionalDate = HelperMethods.StringToDate("2020-09-11");
        Date date = optionalDate.get();

        Map<String, Integer> items = ImmutableMap.of("Apple", 1, "Lemmon", 1);


        when(orderService.getById(1)).thenReturn(Optional.of(Order.builder()
                .id(1)
                .subtotal(3)
                .date(date)
                .items(items)
                .build()));

        Order actual = testClass.getById(1);
        Order expected = Order.builder()
                .id(1)
                .subtotal(3)
                .date(date)
                .items(items)
                .build();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testGetById_InvalidIdThenErrorThrow() {
        final int invalidId = 0;
        when(orderService.getById(invalidId)).thenReturn(Optional.empty());

        try{
            testClass.getById(invalidId);
        } catch(IdNotFoundException e) {
            assertThat(e).hasMessageContaining("there is no corresponding order record with this id in database");
        }
    }

    //following several testing are testing against OrderController.getByDate()
    //we split this method test into several minor tests
    @Test
    public void testGetByDate_BySpecificDay() throws Exception {

        //given single argument: one specific date
        String dateString = "2020-09-13";
        Optional<Date> optionalDate = HelperMethods.StringToDate(dateString);
        Date date = optionalDate.get();

        Order order_1 = Order.builder()
                .id(1)
                .subtotal(3)
                .date(date)
                .items(ImmutableMap.of("Apple", 1, "Lemmon", 1))
                .build();

        Order order_2 = Order.builder()
                .id(2)
                .subtotal(6)
                .date(date)
                .items(ImmutableMap.of("Apple", 1, "Lemmon", 1, "Banana", 1))
                .build();

        when(orderService.getByDate(date)).thenReturn(List.of(order_1, order_2));

        Response actual_1 = testClass.getByDate(dateString, null, null);
        Response expected = Response.of(List.of(order_1, order_2));

        assertThat(actual_1).isEqualTo(expected);


        //given more than 1 argument, method should still carry out the same way as above
        String startString = "2020-08-14";
        String endString = "2020-10-10";

        //give 2 argument case:
        Response actual_2args_start = testClass.getByDate(dateString, startString, null);
        assertThat(actual_2args_start).isEqualTo(expected);

        Response actual_2args_end= testClass.getByDate(dateString, null, endString);
        assertThat(actual_2args_end).isEqualTo(expected);

        //give 3 argument case:
        Response actual_3 = testClass.getByDate(dateString, startString, endString);
        assertThat(actual_3).isEqualTo(expected);

    }


    @Test
    public void testGetByDate_ByDateRange()  {
        String startString = "2020-09-11";
        String endString = "2020-10-10";

        Optional<Date> startOptional = HelperMethods.StringToDate(startString);
        Optional<Date> endOptional = HelperMethods.StringToDate(endString);

        Date start = startOptional.get();
        Date end = endOptional.get();

        Order order_1 = Order.builder()
                .id(1)
                .subtotal(3)
                .date(start)
                .items(ImmutableMap.of("Apple", 1, "Lemmon", 1))
                .build();

        Order order_2 = Order.builder()
                .id(2)
                .subtotal(6)
                .date(end)
                .items(ImmutableMap.of("Apple", 1, "Lemmon", 1, "Banana", 1))
                .build();



        when(orderService.getByDateRange(start, end)).thenReturn(List.of(order_1, order_2));

        Response actual = testClass.getByDate(null, startString, endString);
        Response expected = Response.of(List.of(order_1, order_2));

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testGetByDate_GivenInvalidNullArgument_ThenThrowError() {

        //case 1:
        try{
            testClass.getByDate(null, null, null);
        } catch (IllegalArgumentException e) {
            assertThat(e).hasMessageContaining("!!!!");
        }


        //case 2:
        try{
            testClass.getByDate(null, "2020-11-1", null);
        } catch (IllegalArgumentException e2) {
            assertThat(e2).hasMessageContaining("!!!!");
        }

        //case 3:
        try{
            testClass.getByDate(null, null, "2020-12-1");
        } catch (IllegalArgumentException e2) {
            assertThat(e2).hasMessageContaining("!!!!");
        }
    }


    @Test
    public void testGetByDate_GivenWrongDateFormat_ThenThrowError() {
        //case 1:
        String invalidDateString_1 = "123123123-12-01298301820397";
        try{
            testClass.getByDate(invalidDateString_1, null, null);
        } catch(InvalidDateFormatException invalidDateFormatException) {
            assertThat(invalidDateFormatException).hasMessageContaining("!!!!!");
        }


        //case 2:
        String invalidDateString_2 = "123123123-12-01298301-123-12-123123";
        try{
            testClass.getByDate(invalidDateString_2, null, null);
        } catch(InvalidDateFormatException invalidDateFormatException) {
            assertThat(invalidDateFormatException).hasMessageContaining("!!!!!");
        }


        //case 3:
        String invalidDateString_3 = "addd-12-1";
        try{
            testClass.getByDate(invalidDateString_3, null, null);
        } catch(InvalidDateFormatException invalidDateFormatException) {
            assertThat(invalidDateFormatException).hasMessageContaining("!!!!!");
        }

    }

    @Test
    public void testPut_WhenValidDateIsProvided() {
        //input new order 1 apple and 3 lemmon
        JsonDto dto = JsonDto.builder()
                        .dateString("2020-10-20")
                        .id(1)
                        .items(ImmutableMap.of("Apple", 1, "Lemmon", 3))
                        .build();

        //old order to be replaced: 1 lemmon
        when(orderService.getById(1)).thenReturn(Optional.of(Order.builder()
                .id(1)
                .subtotal(1)
                .date(HelperMethods.StringToDate("2020-09-11").get())
                .items(ImmutableMap.of("Lemmon", 1))
                .build())
        );

        //for OrderController.newOrder() method
        //product list: apple 1 dolloar per
        when(productService.getByName("Apple")).thenReturn(Optional.of(
                Product.builder()
                        .price(1)
                        .name("Apple")
                        .build()));
        //product list: lemmon 1 dollar per
        when(productService.getByName("Lemmon")).thenReturn(Optional.of(
                Product.builder()
                .price(1)
                .name("Lemmon")
                .build()));

        //new order object
        Order orderUpdated = Order.builder()
                .id(1)
                .subtotal(4)
                .items(ImmutableMap.of("Apple", 1, "Lemmon", 3))
                .date(HelperMethods.StringToDate("2020-10-20").get())
                .build();

        //
        when(orderService.post(orderUpdated)).thenReturn(orderUpdated);

        Order actual = testClass.put(dto);
        Order expected = orderUpdated;

        assertThat(actual).isEqualTo(expected);
    }


    @Test
    public void testPut_WhenDateIsNull()  {
        //input new order 1 apple and 3 lemmon
        JsonDto dto = JsonDto.builder()
                .id(1)
                .items(ImmutableMap.of("Apple", 1, "Lemmon", 3))
                .build();

        //old order to be replaced: 1 lemmon
        when(orderService.getById(1)).thenReturn(Optional.of(Order.builder()
                .id(1)
                .subtotal(10)
                .date(HelperMethods.StringToDate("2020-09-11").get())
                .items(ImmutableMap.of("Lemmon", 1))
                .build())
        );

        //for OrderController.newOrder() method
        //product list: apple 1 dolloar per
        when(productService.getByName("Apple")).thenReturn(Optional.of(
                Product.builder()
                        .price(1)
                        .name("Apple")
                        .build()));
        //product list: lemmon 1 dollar per
        when(productService.getByName("Lemmon")).thenReturn(Optional.of(
                Product.builder()
                        .price(1)
                        .name("Lemmon")
                        .build()));

        //new order object
        Order orderUpdated = Order.builder()
                .id(1)
                .subtotal(4)
                .items(ImmutableMap.of("Apple", 1, "Lemmon", 3))
                .date(HelperMethods.StringToDate("2020-09-11").get())
                .build();

        //
        when(orderService.post(orderUpdated)).thenReturn(orderUpdated);

        Order actual = testClass.put(dto);
        Order expected = orderUpdated;

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testPut_WhenItemsIsNotProvided() {
        JsonDto dto = JsonDto.builder()
                    .id(1)
                    .dateString("2020-10-21")
                    .build();


        //old order to be replaced: 1 lemmon
        when(orderService.getById(1)).thenReturn(Optional.of(Order.builder()
                .id(1)
                .subtotal(1)
                .date(HelperMethods.StringToDate("2020-09-11").get())
                .items(ImmutableMap.of("Lemmon", 1))
                .build())
        );

        //for OrderController.newOrder() method
        //product list: apple 1 dolloar per
        when(productService.getByName("Apple")).thenReturn(Optional.of(
                Product.builder()
                        .price(1)
                        .name("Apple")
                        .build()));
        //product list: lemmon 1 dollar per
        when(productService.getByName("Lemmon")).thenReturn(Optional.of(
                Product.builder()
                        .price(1)
                        .name("Lemmon")
                        .build()));

        //new order object
        Order orderUpdated = Order.builder()
                .id(1)
                .subtotal(1)
                .items(ImmutableMap.of("Lemmon", 1))
                .date(HelperMethods.StringToDate("2020-10-21").get())
                .build();

        //

        when(orderService.post(orderUpdated)).thenReturn(orderUpdated);

        Order actual = testClass.put(dto);
        Order expected = orderUpdated;

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testPut_WhenIdIsNotValid_ThenThrowResultNotFoundException() {
        JsonDto dto = JsonDto.builder()
                .id(2)
                .items(ImmutableMap.of("Apple", 5, "Banana", 2))
                .dateString("2020-09-11")
                .build();

        when(orderService.getById(2)).thenThrow(new IdNotFoundException("there is no corresponding order record with this id in database"));

        try{
            testClass.put(dto);
        } catch (IdNotFoundException e) {
            assertThat(e).hasMessageContaining("corresponding");
        }
    }

    @Test
    public void testPut_WhenInvalidDateStringIsGiven_ThenThrowInvalidDateFormatException() {
        JsonDto dto = JsonDto.builder()
                .id(1)
                .items(ImmutableMap.of("Apple", 5, "Banana", 2))
                .dateString("12312-aa-ds")
                .build();

        when(orderService.getById(1)).thenReturn(Optional.of(Order.builder()
                .id(1)
                .subtotal(1)
                .date(HelperMethods.StringToDate("2020-09-11").get())
                .items(ImmutableMap.of("Lemmon", 1))
                .build()));

        try{
            testClass.put(dto);
        } catch (InvalidDateFormatException e) {
            assertThat(e).hasMessageContaining("!!!!!");
        }
    }

    @Test
    public void testDelete_GivenValidId() {

        //order we want to delete
        Order theOrder = Order.builder()
                .id(1)
                .subtotal(2)
                .date(HelperMethods.StringToDate("2020-09-11").get())
                .items(ImmutableMap.of("Apple", 1, "Lemmon", 1))
                .build();

        when(orderService.getById(1)).thenReturn(Optional.of(theOrder));

        testClass.delete(1);

        //check the order of call if follow orderService.getById() -> orderService.delete();
        InOrder inOrder = inOrder(orderService);
        inOrder.verify(orderService).getById(1);
        inOrder.verify(orderService).delete(theOrder);
    }

    @Test
    public void testDelete_GivenInvalidId_ThenThrowResultNotFounddException() {
        when(orderService.getById(0)).thenReturn(Optional.empty());

        try{
            testClass.delete(0);
        } catch (IdNotFoundException e) {
            assertThat(e).hasMessageContaining("there is no corresponding order record with this id in database");
        }
    }





    private Date createInputDate(final String dateString) throws Exception {
        final String dateFormat = "yyyy-MM-dd";
        Date inputDate;
        try {
            inputDate = new SimpleDateFormat(dateFormat).parse(dateString);
        } catch (final Exception e) {
            throw new Exception(String.format("Error parsing date: %s", dateString), e);
        }
        return inputDate;
    }
}
