package com.JB.couponsproject.util;

import com.JB.couponsproject.entities.CompanyEntity;
import com.JB.couponsproject.entities.CouponEntity;
import com.JB.couponsproject.entities.CustomerEntity;
import com.JB.couponsproject.enums.Category;
import com.JB.couponsproject.exceptions.ApplicationException;
import com.JB.couponsproject.repositories.CompanyRepository;
import com.JB.couponsproject.repositories.CouponRepository;
import com.JB.couponsproject.repositories.CustomerRepository;
import com.JB.couponsproject.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class MockDataInserter {
    final CompanyRepository companyRepository;
    final CouponRepository couponRepository;
    final CustomerRepository customerRepository;
    final CustomerService customerService;
//    final CouponVsCustomersRepository couponVsCustomersRepository;

    public void insert() throws ApplicationException {
        for (int i = 1; i <= 10; i++) {
            final CompanyEntity newCompany = companyRepository.save(new CompanyEntity(
                    "company" + i,
                    "company" + i + "@email.com",
                    "123456"
            ));
            final CouponEntity newCoupon = couponRepository.save(
                    new CouponEntity(
                            newCompany.getId(),
                            Category.ELECTRICITY,
                            "title",
                            "desc",
                            LocalDate.of(2022, 2, 2),
                            LocalDate.of(2022, 4, 10),
                            300,
                            100,
                            "https://company/image.jpg"
                    )
            );
            final CustomerEntity newCustomer = customerRepository.save(new CustomerEntity(
                    "customer" + i,
                    "last name",
                    "customer" + i + "@email.com",
                    "123456"
            ));
            customerService.purchaseCoupon(newCoupon.getId(),newCustomer.getId());
            customerRepository.save(newCustomer);
        }
    }
}
