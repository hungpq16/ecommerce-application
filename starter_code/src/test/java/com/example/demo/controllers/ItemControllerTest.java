package com.example.demo.controllers;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void getAllItems() {
        List<Item> items = new ArrayList<>();
        Item item1 = new Item(1L, "Udacity 01", BigDecimal.valueOf(5.9), "The course of Java");
        Item item2 = new Item(2L, "Udacity 02", BigDecimal.valueOf(7.9), "The course of Javasctipt");
        items.add(item1);
        items.add(item2);

        when(itemRepository.findAll()).thenReturn(items);
        ResponseEntity<List<Item>> responseEntity = itemController.getItems();
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(items, responseEntity.getBody());
    }

    @Test
    public void findItemSuccess() {
        Item item1 = new Item(1L, "Udacity 01", BigDecimal.valueOf(5.9), "The course of Java");
        Item item2 = new Item(2L, "Udacity 02", BigDecimal.valueOf(7.9), "The course of Javasctipt");

        // find by itemId = 2
        when(itemRepository.findById(2L)).thenReturn(Optional.of(item2));
        ResponseEntity<Item> responseID = itemController.getItemById(2L);
        assertNotNull(responseID);
        assertEquals(200, responseID.getStatusCodeValue());
        assertEquals(item2, responseID.getBody());

        // find by itemName exist
        when(itemRepository.findByName("Udacity 01")).thenReturn(Arrays.asList(item1));
        ResponseEntity<List<Item>> responseByName = itemController.getItemsByName("Udacity 01");
        assertNotNull(responseByName);
        assertEquals(200, responseByName.getStatusCodeValue());
        assertEquals(Arrays.asList(item1), responseByName.getBody());

    }

    @Test
    public void findItemFail() {
        // find by itemId = 5
        ResponseEntity<Item> responseID = itemController.getItemById(5L);
        assertNotNull(responseID);
        assertEquals(404, responseID.getStatusCodeValue());
        assertNull(responseID.getBody());

        // find by itemName exist
        ResponseEntity<List<Item>> responseByName = itemController.getItemsByName("Udacity 05");
        assertNotNull(responseByName);
        assertEquals(404, responseByName.getStatusCodeValue());
        assertNull(responseByName.getBody());
    }

}
