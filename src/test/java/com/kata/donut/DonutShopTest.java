package com.kata.donut;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.kata.donut.constants.DonutType;
import com.kata.donut.customer.Customer;
import com.kata.donut.customer.Customers;
import com.kata.donut.shop.DonutShop;
import org.javatuples.Pair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DonutShopTest {

        private final Clock clock = Clock.fixed(Instant.now(), ZoneOffset.UTC);
        private final LocalDate today = LocalDate.now(this.clock);
        private final LocalDate tomorrow = this.today.plusDays(1);
        private final LocalDate yesterday = this.today.minusDays(1);
        private DonutShop donutShop;

        @BeforeEach
        public void setup()
        {
            this.donutShop = new DonutShop();
            this.donutShop.makeDonuts(DonutType.BOSTON_CREAM, 10);
            this.donutShop.makeDonuts(DonutType.BAVARIAN_CREAM, 10);
            this.donutShop.makeDonuts(DonutType.BLUEBERRY, 10);
            this.donutShop.makeDonuts(DonutType.GLAZED, 10);
            this.donutShop.makeDonuts(DonutType.OLD_FASHIONED, 10);
            this.donutShop.makeDonuts(DonutType.PUMPKIN, 10);
            this.donutShop.makeDonuts(DonutType.JELLY, 10);
            this.donutShop.makeDonuts(DonutType.VANILLA_FROSTED, 10);

            var delivery1 =
                    this.donutShop.deliverOrder("Ted Smith", this.today, "BC:2,BA:1,B:2");
            Assertions.assertEquals(6.75d, delivery1.getTotalPrice(), 0.001);
            var delivery2 =
                    this.donutShop.deliverOrder("Mary Williams", this.today, "BC:1,G:1");
            Assertions.assertEquals(2.70d, delivery2.getTotalPrice(), 0.001);
            var delivery3 =
                    this.donutShop.deliverOrder("Sally Prince", this.tomorrow, "BC:6,P:2,B:2,OF:2");
            Assertions.assertEquals(12.0d, delivery3.getTotalPrice(), 0.001);
            var delivery4 =
                    this.donutShop.deliverOrder("Donnie Dapper", this.yesterday, "BC:6,P:2,B:2,OF:2,G:10");
            Assertions.assertEquals(20.9d, delivery4.getTotalPrice(), 0.001);

            System.out.println(this.donutShop);
        }

        @AfterEach
        public void tearDown()
        {
            this.donutShop = null;
        }

        @Test
        public void getTop2Donuts()
        {
            var expected = new ArrayList<Pair<DonutType,Integer>>();
            expected.add(Pair.with(DonutType.BOSTON_CREAM, 15));
            expected.add(Pair.with(DonutType.GLAZED, 11));
            Assertions.assertEquals(expected, this.donutShop.getTopDonuts(2));
        }

        @Test
        public void totalDeliveryValueByDate()
        {
            Assertions.assertEquals(
                    9.45d,
                    this.donutShop.getTotalDeliveryValueFor(this.today),
                    0.001);
            Assertions.assertEquals(
                    12.0d,
                    this.donutShop.getTotalDeliveryValueFor(this.tomorrow),
                    0.001);
            Assertions.assertEquals(
                    20.9d,
                    this.donutShop.getTotalDeliveryValueFor(this.yesterday),
                    0.001);
        }

        @Test
        public void getTopCustomer()
        {
            Assertions.assertEquals("Donnie Dapper", this.donutShop.getTopCustomer().name());
        }

        @Test
        public void getCustomersByDonutTypesOrdered()
        {
            Map<DonutType, List<Customer>> typeCustomerMap = this.donutShop.getCustomersByDonutTypesOrdered();
            Assertions.assertEquals(6, typeCustomerMap.keySet().size());
            Assertions.assertEquals(1, typeCustomerMap.get(DonutType.BAVARIAN_CREAM).size());
            Assertions.assertTrue(new Customers(typeCustomerMap.get(DonutType.BAVARIAN_CREAM)).named("Ted Smith"));
        }

        @Test
        public void getDonutPriceStatistics()
        {
            var stats1 = this.donutShop.getDonutPriceStatistics(this.today, this.today);
            Assertions.assertEquals(9.45d, stats1.getSum(), 0.01);
            Assertions.assertEquals(1.35d, stats1.getAverage(), 0.01);
            Assertions.assertEquals(7, stats1.getCount(), 0.01);

            var stats2 = this.donutShop.getDonutPriceStatistics(this.tomorrow, this.tomorrow);
            Assertions.assertEquals(12.0d, stats2.getSum(), 0.01);
            Assertions.assertEquals(1.0d, stats2.getAverage(), 0.01);
            Assertions.assertEquals(12, stats2.getCount(), 0.01);

            var stats3 = this.donutShop.getDonutPriceStatistics(this.yesterday, this.yesterday);
            Assertions.assertEquals(20.9d, stats3.getSum(), 0.001);
            Assertions.assertEquals(0.95d, stats3.getAverage(), 0.01);
            Assertions.assertEquals(22, stats3.getCount(), 0.01);

            var statsTotal = this.donutShop.getDonutPriceStatistics(this.yesterday, this.tomorrow);
            Assertions.assertEquals(42.35d, statsTotal.getSum(), 0.01);
            Assertions.assertEquals(1.03d, statsTotal.getAverage(), 0.01);
            Assertions.assertEquals(41, statsTotal.getCount(), 0.01);
            Assertions.assertEquals(0.95, statsTotal.getMin(), 0.01);
            Assertions.assertEquals(1.35, statsTotal.getMax(), 0.01);
        }
}
