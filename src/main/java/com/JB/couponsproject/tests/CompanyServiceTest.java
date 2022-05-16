package com.JB.couponsproject.tests;

import com.JB.couponsproject.dto.CouponDto;
import com.JB.couponsproject.entities.CouponEntity;
import com.JB.couponsproject.enums.Category;
import com.JB.couponsproject.exceptions.ApplicationException;
import com.JB.couponsproject.repositories.CouponRepository;
import com.JB.couponsproject.services.CompanyService;
import com.JB.couponsproject.util.ObjectMappingUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@RequiredArgsConstructor
@Component
@Order(3)
public class CompanyServiceTest implements CommandLineRunner {
    private final CompanyService companyService;
    private final CouponRepository couponRepository;
    private final Logger logger = LoggerFactory.getLogger(CompanyServiceTest.class);

    @Override
    public void run(String... args) {
        try {
            //Tests
            //Login test
            try {
                logger.info("Login failed test (throws exception)");
                companyService.login("company2@email.com", "WRONG PASSWORD");
            } catch (ApplicationException e) {
                e.printStackTrace();
            }
            logger.info("Login succeed test");
            companyService.login("company2@email.com", "123456");
            // consider another way
            //logger.info("WELCOME " + companyService.getLoggedInCompany().getName().toUpperCase());
            //Add coupon test
            logger.info("Add coupon test");
            final CouponDto testCouponDto = CouponDto.builder()
                    .category(Category.ELECTRICITY)
                    .title("test title")
                    .description("test desc")
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now())
                    .amount(10)
                    .price(10)
                    .image("http://image.url.jpg")
                    .build();
            Long newCouponId = companyService.addCoupon(testCouponDto, 1L);
            logger.info("Coupon added (id:" + newCouponId + ")");
            logger.info("Add coupon with same title test (throws exception)");
            try {
                companyService.addCoupon(testCouponDto, 1L);
            } catch (ApplicationException e) {
                e.printStackTrace();
            }
            CouponEntity newCoupon = couponRepository.findById(newCouponId).get();
            //Update coupon test
            logger.info("Update coupon test");
            newCoupon.setDescription("updated desc");
            final CouponDto newCouponDto = ObjectMappingUtil.couponEntityToCouponDto(newCoupon);
            companyService.updateCoupon(newCouponDto, 1L);
            logger.info("Updated coupon description:");
            logger.info(couponRepository.findById(newCouponId).get().toString());
            logger.info("Update coupon id test (throws exception)");
            try {
                CouponDto couponToUpdate = CouponDto.builder()
                        .id(1L).companyId(newCouponDto.getCompanyId()).build();
                companyService.updateCoupon(couponToUpdate, 1L);
            } catch (ApplicationException e) {
                e.printStackTrace();
            }
            // Delete coupon test
            logger.info("Deleting coupon...");
            companyService.deleteCoupon(2L, 1L);
            logger.info("Coupon deleted");
            //Get company coupons test
            logger.info("All company coupons");
            logger.info(companyService.getCompanyCoupons(1L).toString());
            logger.info("All company coupons from category (ELECTRICITY)");
            logger.info(companyService.getCompanyCoupons(Category.ELECTRICITY, 1L).toString());
            logger.info("All company coupons up to 500 (max price)");
            logger.info(companyService.getCompanyCoupons(500d, 1L).toString());
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
    }
}
