//package com.thelocalmusicfinder.thelocalmusicfinderbackend.services;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest
//public class EventServiceTest {
//
//  @MockitoBean
//  private BandService bandService;
//
//  @Autowired
//  private EventService eventService;
//
//  @Test
//  void testTest() {
//    when(bandService.test()).thenReturn("new test");
//
//    String result = eventService.test("test");
//
//    assertEquals("new test", result);
//  }
//}
